package com.travel.role.domain.room.service;

import static com.travel.role.global.exception.dto.ExceptionMessage.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.Tuple;
import com.travel.role.domain.accounting.entity.AccountingInfo;
import com.travel.role.domain.accounting.repository.AccountingInfoRepository;
import com.travel.role.domain.accounting.service.AccountingInfoReadService;
import com.travel.role.domain.book.repository.BookInfoRepository;
import com.travel.role.domain.comment.service.CommentService;
import com.travel.role.domain.room.dto.request.ExitRoomRequestDTO;
import com.travel.role.domain.room.dto.request.ExpensesRequestDTO;
import com.travel.role.domain.room.dto.request.MakeRoomRequestDTO;
import com.travel.role.domain.room.dto.request.RoomModifiedRequestDTO;
import com.travel.role.domain.room.dto.request.RoomRoleDTO;
import com.travel.role.domain.room.dto.response.AllPlanDTO;
import com.travel.role.domain.room.dto.response.AllPlanResponseDTO;
import com.travel.role.domain.room.dto.response.ExpenseResponseDTO;
import com.travel.role.domain.room.dto.response.InviteResponseDTO;
import com.travel.role.domain.room.dto.response.MemberDTO;
import com.travel.role.domain.room.dto.response.RoomInfoResponseDTO;
import com.travel.role.domain.room.dto.response.RoomResponseDTO;
import com.travel.role.domain.room.dto.response.RoomRoleInfoDTO;
import com.travel.role.domain.room.dto.response.ScheduleDTO;
import com.travel.role.domain.room.dto.response.SidebarResponseDTO;
import com.travel.role.domain.room.dto.response.TimeResponseDTO;
import com.travel.role.domain.room.entity.ParticipantRole;
import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.room.entity.RoomParticipant;
import com.travel.role.domain.room.entity.RoomRole;
import com.travel.role.domain.room.repository.ParticipantRoleRepository;
import com.travel.role.domain.room.repository.RoomParticipantRepository;
import com.travel.role.domain.room.repository.RoomRepository;
import com.travel.role.domain.schedule.entity.Board;
import com.travel.role.domain.schedule.repository.BoardRepository;
import com.travel.role.domain.schedule.repository.ScheduleInfoRepository;
import com.travel.role.domain.travelessential.service.TravelEssentialService;
import com.travel.role.domain.user.entity.User;
import com.travel.role.domain.user.service.UserReadService;
import com.travel.role.global.exception.room.AdminIsOnlyOneException;
import com.travel.role.global.exception.room.AlreadyExistInRoomException;
import com.travel.role.global.exception.room.InvalidInviteCode;
import com.travel.role.global.exception.room.InvalidLocalDateException;
import com.travel.role.global.exception.room.RoomNotUpdateAdminException;
import com.travel.role.global.exception.room.UserHaveNotPrivilegeException;
import com.travel.role.global.util.PasswordGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomService {

	private static final int MAX_PASSWORD_LENGTH = 20;

	private final RoomRepository roomRepository;
	private final UserReadService userReadService;
	private final RoomParticipantRepository roomParticipantRepository;
	private final ParticipantRoleRepository participantRoleRepository;
	private final ParticipantRoleReadService participantRoleReadService;
	private final RoomParticipantReadService roomParticipantReadService;
	private final AccountingInfoReadService accountingInfoReadService;
	private final PasswordGenerator passwordGenerator;
	private final RoomReadService roomReadService;
	private final BoardRepository boardRepository;
	private final CommentService commentService;
	private final TravelEssentialService travelEssentialService;
	private final BookInfoRepository bookInfoRepository;
	private final AccountingInfoRepository accountingInfoRepository;
	private final ScheduleInfoRepository scheduleInfoRepository;

	public List<RoomResponseDTO> getRoomList(String email) {
		List<Tuple> findRoomInfo = roomRepository.getMemberInRoom(email);

		Map<Long, RoomResponseDTO> hash = new HashMap<>();
		for (Tuple tuple : findRoomInfo) {
			Room room = tuple.get(0, Room.class);
			User user = tuple.get(1, User.class);

			if (Objects.equals(user.getEmail(), email)) {
				List<MemberDTO> members = new ArrayList<>();
				hash.put(room.getId(), RoomResponseDTO.of(room, members));
			}
		}

		for (Tuple tuple : findRoomInfo) {
			Room room = tuple.get(0, Room.class);
			User user = tuple.get(1, User.class);

			if (hash.containsKey(room.getId())) {
				RoomResponseDTO roomResponseDTO = hash.get(room.getId());
				roomResponseDTO.getMembers().add(new MemberDTO(user.getName(), user.getProfile()));
				hash.put(room.getId(), roomResponseDTO);
			}
		}

		return new ArrayList<>(hash.values());
	}

	public void makeRoom(String email, MakeRoomRequestDTO makeRoomRequestDTO) {
		validateDate(makeRoomRequestDTO.getTravelStartDate(), makeRoomRequestDTO.getTravelEndDate());
		User user = userReadService.findUserByEmailOrElseThrow(email);
		Room room = roomRepository.save(Room.of(makeRoomRequestDTO));
		saveNewRoomParticipant(user, room);
		saveNewParticipantRole(user, room);
	}

	private void saveNewRoomParticipant(User user, Room room) {
		RoomParticipant roomParticipant = RoomParticipant.builder()
			.isPaid(false)
			.joinedAt(LocalDateTime.now())
			.room(room)
			.user(user)
			.build();
		roomParticipantRepository.save(roomParticipant);
	}

	private void saveNewParticipantRole(User user, Room room) {
		ParticipantRole newParticipantRole = ParticipantRole.builder()
			.roomRole(RoomRole.ADMIN)
			.room(room)
			.user(user)
			.build();
		participantRoleRepository.save(newParticipantRole);
	}

	private void validateDate(LocalDate startDate, LocalDate endDate) {
		if (startDate.isAfter(endDate))
			throw new InvalidLocalDateException(INVALID_DATE_ERROR);
	}

	public String makeInviteCode(String email, Long roomId) {
		User user = userReadService.findUserByEmailOrElseThrow(email);
		Room room = roomReadService.findRoomByIdOrElseThrow(roomId);

		validRoomRole(user, room, RoomRole.ADMIN);

		String inviteCode = room.getRoomInviteCode();
		if (validateInviteCode(room)) {
			inviteCode = generateInviteCode();
			room.updateInviteCode(inviteCode, LocalDateTime.now());
		}

		return inviteCode;
	}

	private boolean validateInviteCode(Room room) {
		return room.getRoomInviteCode() == null || room.getRoomExpiredTime().plusDays(1L).isAfter(LocalDateTime.now());
	}

	private void validRoomRole(User user, Room room, RoomRole... checkRooms) {
		boolean isExist = participantRoleRepository.existsByUserAndRoomAndRoomRoleIn(user, room,
			Arrays.asList(checkRooms));
		if (!isExist)
			throw new UserHaveNotPrivilegeException();
	}

	private String generateInviteCode() {
		String inviteCode = passwordGenerator.generateRandomPassword(MAX_PASSWORD_LENGTH);

		while (roomRepository.existsByRoomInviteCode(inviteCode)) {
			inviteCode = passwordGenerator.generateRandomPassword(MAX_PASSWORD_LENGTH);
		}

		return inviteCode;
	}

	@Transactional(readOnly = true)
	public void checkRoomInviteCode(String email, String inviteCode) {
		Room room = roomReadService.getRoomUsingInviteCode(inviteCode);

		validateInviteRoom(email, room);
	}

	private void validateInviteRoom(String email, Room room) {
		if (!validateInviteCode(room)) {
			throw new InvalidInviteCode();
		}

		if (roomParticipantRepository.existsUserInRoom(email, room.getId())) {
			throw new AlreadyExistInRoomException();
		}
	}

	public InviteResponseDTO inviteUser(String email, String inviteCode, List<String> roles) {
		Room room = roomReadService.getRoomUsingInviteCode(inviteCode);
		User user = userReadService.findUserByEmailOrElseThrow(email);

		validateInviteRoom(email, room);
		validateSelectRole(roles);

		for (String role : roles) {
			ParticipantRole participantRole = new ParticipantRole(null, RoomRole.valueOf(role), user, room);
			participantRoleRepository.save(participantRole);
		}

		RoomParticipant roomParticipant = new RoomParticipant(null, LocalDateTime.now(), false, user, room);
		roomParticipantRepository.save(roomParticipant);

		return new InviteResponseDTO(room.getId());
	}

	private void validateSelectRole(List<String> roles) {
		for (String role : roles) {
			try {
				if (RoomRole.valueOf(role) == RoomRole.ADMIN) {
					throw new UserHaveNotPrivilegeException();
				}
			} catch (IllegalArgumentException e) {
				throw new UserHaveNotPrivilegeException();
			}
		}
	}

	public List<TimeResponseDTO> getTime(Long roomId) {
		Room room = roomReadService.findRoomByIdOrElseThrow(roomId);
		LocalDate starDate = room.getTravelStartDate();
		LocalDate endDate = room.getTravelEndDate();
		Long day = ChronoUnit.DAYS.between(starDate, endDate);
		List<TimeResponseDTO> result = new ArrayList<>();

		for (Long i = 1L; i <= day + 1; i++) {
			result.add(TimeResponseDTO.from(i, starDate,
				convertToKoreanDayOfWeek(starDate)));
			starDate = starDate.plusDays(1);
		}
		return result;
	}

	public ExpenseResponseDTO getExpenses(String email, Long roomId) {

		User loginUser = userReadService.findUserByEmailOrElseThrow(email);
		Room room = roomReadService.findRoomByIdOrElseThrow(roomId);
		roomParticipantReadService.checkParticipant(loginUser, room);

		return ExpenseResponseDTO.from(room);
	}

	public void modifyExpenses(String email, Long roomId, ExpensesRequestDTO requestDTO) {

		User loginUser = userReadService.findUserByEmailOrElseThrow(email);
		Room room = roomReadService.findRoomByIdOrElseThrow(roomId);

		validRoomRole(loginUser, room, RoomRole.ACCOUNTING, RoomRole.ADMIN);

		room.updateTravelExpense(requestDTO.getExpenses());
	}

	public void modifyRoomInfo(String email, RoomModifiedRequestDTO dto, Long roomId) {
		validRoomRoles(email, roomId, RoomRole.ADMIN);
		validAdmin(dto, email);

		List<ParticipantRole> participantRoles = participantRoleReadService.findUserByRoomId(roomId);

		Map<String, List<ParticipantRole>> participantMap = convertToParticipantRoleList(participantRoles);

		modifyRoomNameAndDateAndLocation(participantRoles.get(0).getRoom(), dto);
		modifyRoles(participantMap, dto.getUserRoles());
	}

	private void validAdmin(RoomModifiedRequestDTO dto, String email) {
		for (RoomRoleDTO userRole : dto.getUserRoles()) {
			if (!userRole.getEmail().equals(email) && userRole.getRoles().contains(RoomRole.ADMIN)) {
				throw new RoomNotUpdateAdminException();
			}
		}
	}

	private void validRoomRoles(String email, Long roomId, RoomRole roomRole) {
		boolean isExists = participantRoleRepository.existsByUserEmailAndRoomIdAndRole(email, roomId, roomRole);
		if (!isExists) {
			throw new UserHaveNotPrivilegeException();
		}
	}

	private void modifyRoomNameAndDateAndLocation(Room room, RoomModifiedRequestDTO dto) {
		validateDate(dto.getStartDate(), dto.getEndDate());
		room.updateRoomNameAndDate(dto.getRoomName(), dto.getLocation(), room.getTravelStartDate(),
			room.getTravelEndDate());
	}

	private void modifyRoles(Map<String, List<ParticipantRole>> participantMap, List<RoomRoleDTO> userRoles) {
		for (RoomRoleDTO userRole : userRoles) {
			List<ParticipantRole> dbParticipant = participantMap.get(userRole.getEmail());
			deleteRole(dbParticipant, userRole.getRoles().size());
			modifyRole(dbParticipant, userRole.getRoles());
		}
	}

	private void deleteRole(List<ParticipantRole> participantRoles, int roomRoleSize) {
		int participantRoleSize = participantRoles.size();

		if (participantRoleSize > roomRoleSize) {
			List<Long> ids = new ArrayList<>();
			for (int i = 0 ; i < participantRoleSize - roomRoleSize; i++) {
				ids.add(participantRoles.get(i).getId());
				participantRoles.remove(participantRoles.get(i));
			}

			if (!ids.isEmpty()) {
				participantRoleRepository.deleteAllByIdInQuery(ids);
			}
		}
	}

	private void modifyRole(List<ParticipantRole> participantRoles, List<RoomRole> roomRoles) {
		for (int i = 0; i < roomRoles.size(); i++) {
			if (i > participantRoles.size() - 1) {
				ParticipantRole participantRole = participantRoles.get(0);
				ParticipantRole newParticipantRole = new ParticipantRole(null, roomRoles.get(i),
					participantRole.getUser(), participantRole.getRoom());
				participantRoleRepository.save(newParticipantRole);
			} else {
				ParticipantRole participantRole = participantRoles.get(i);
				participantRole.updateRole(roomRoles.get(i));
			}
		}
	}

	private Map<String, List<ParticipantRole>> convertToParticipantRoleList(List<ParticipantRole> participantRoles) {
		Map<String, List<ParticipantRole>> result = new HashMap<>();

		for (ParticipantRole participantRole : participantRoles) {
			String email = participantRole.getUser().getEmail();
			List<ParticipantRole> data;
			if (!result.containsKey(email)) {
				data = new ArrayList<>();

			} else {
				data = result.get(email);

			}
			data.add(participantRole);
			result.put(email, data);
		}
		return result;
	}

	private List<String> validateUserRoleAndEmail(List<RoomRoleDTO> userRoles) {
		List<String> admins = new ArrayList<>();

		for (RoomRoleDTO userRole : userRoles) {
			if (userRole.getRoles().contains(RoomRole.ADMIN))
				admins.add(userRole.getEmail());
		}

		if (admins.size() >= 2) {
			throw new AdminIsOnlyOneException();
		}

		return admins;
	}

	@Transactional(readOnly = true)
	public RoomInfoResponseDTO getRoomInfo(String email, Long roomId) {
		User user = userReadService.findUserByEmailOrElseThrow(email);
		Room room = roomReadService.findRoomByIdOrElseThrow(roomId);
		roomParticipantReadService.checkParticipant(user, room);

		List<ParticipantRole> participantRoles = participantRoleReadService.findUserByRoomId(roomId);

		Map<User, List<RoomRole>> map = getUserAndRolesMap(
			participantRoles);

		List<RoomRoleInfoDTO> roomRoleDTOS = convertToRoomRoleDTOS(map);

		return RoomInfoResponseDTO.of(room, roomRoleDTOS);
	}

	@Transactional(readOnly = true)
	public AllPlanResponseDTO getAllPlan(String email, Long roomId) {
		User user = userReadService.findUserByEmailOrElseThrow(email);
		Room room = roomReadService.findRoomByIdOrElseThrow(roomId);
		roomParticipantReadService.checkParticipant(user, room);

		List<Board> boards = boardRepository.findScheduleAndAccountByRoomOrderByAsc(room.getId());

		Map<LocalDate, AllPlanDTO> resultMap = new TreeMap<>();
		for (Board board : boards) {
			LocalDateTime scheduleDate = board.getScheduleDate();
			addToResultMap(resultMap, board, scheduleDate);
		}

		Integer totalExpense = 0;
		List<AllPlanDTO> result = new ArrayList<>();
		for (LocalDate localDate : resultMap.keySet()) {
			AllPlanDTO allPlanDTO = resultMap.get(localDate);
			result.add(allPlanDTO);
			totalExpense += allPlanDTO.getTravelExpense();
		}

		return new AllPlanResponseDTO(totalExpense, result);
	}

	private void addToResultMap(Map<LocalDate, AllPlanDTO> resultMap, Board board, LocalDateTime scheduleDate) {
		if (!resultMap.containsKey(scheduleDate.toLocalDate())) {
			resultMap.put(scheduleDate.toLocalDate(), makeNewAllPlan(board, scheduleDate));
			return;
		}

		AllPlanDTO currentData = resultMap.get(scheduleDate.toLocalDate());
		List<ScheduleDTO> schedules = currentData.getSchedules();
		schedules.add(
			ScheduleDTO.from(board.getScheduleInfo(), board.getAccountingInfo(), scheduleDate.toLocalTime()));
		addExpense(board, currentData);
	}

	private AllPlanDTO makeNewAllPlan(Board board, LocalDateTime scheduleDate) {
		List<ScheduleDTO> schedules = new ArrayList<>();
		schedules.add(ScheduleDTO.from(board.getScheduleInfo(), board.getAccountingInfo(), scheduleDate.toLocalTime()));

		AllPlanDTO allPlanResponseDTO = new AllPlanDTO(scheduleDate.toLocalDate(),
			convertToKoreanDayOfWeek(scheduleDate.toLocalDate()), 0, schedules);
		addExpense(board, allPlanResponseDTO);
		return allPlanResponseDTO;
	}

	private void addExpense(Board board, AllPlanDTO allPlanResponseDTO) {
		if (board.getAccountingInfo() != null) {
			allPlanResponseDTO.addTravelExpense(board.getAccountingInfo().getPrice());
		}
	}

	private String convertToKoreanDayOfWeek(LocalDate localDate) {
		return localDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);
	}

	private static List<RoomRoleInfoDTO> convertToRoomRoleDTOS(Map<User, List<RoomRole>> map) {
		List<RoomRoleInfoDTO> roomRoleDTOS = new ArrayList<>();
		for (User key : map.keySet()) {
			List<RoomRole> roles = map.get(key);
			RoomRoleInfoDTO newRoomRoleInfo = new RoomRoleInfoDTO(key.getName(), key.getEmail(), key.getProfile(),
				roles);

			if (roles.contains(RoomRole.ADMIN)) {
				roomRoleDTOS.add(0, newRoomRoleInfo);
			} else {
				roomRoleDTOS.add(newRoomRoleInfo);
			}

		}
		return roomRoleDTOS;
	}

	private static Map<User, List<RoomRole>> getUserAndRolesMap(List<ParticipantRole> participantRoles) {
		Map<User, List<RoomRole>> map = new HashMap<>();
		for (ParticipantRole participantRole : participantRoles) {
			User user = participantRole.getUser();
			if (!map.containsKey(user)) {
				List<RoomRole> roles = new ArrayList<>();
				roles.add(participantRole.getRoomRole());
				map.put(user, roles);
			} else {
				List<RoomRole> roles = map.get(user);
				roles.add(participantRole.getRoomRole());
			}
		}
		return map;
	}

	@Transactional(readOnly = true)
	public SidebarResponseDTO getSidebar(String email, Long roomId) {
		User user = userReadService.findUserByEmailOrElseThrow(email);
		Room room = roomReadService.findRoomByIdOrElseThrow(roomId);
		roomParticipantReadService.checkParticipant(user, room);

		List<RoomRole> roles = participantRoleReadService.findRoomRolesByUserAndRoom(user, room);

		return SidebarResponseDTO.of(room, roles);
	}

	public List<RoomRole> getUserRoles(String email, Long roomId) {
		User user = userReadService.findUserByEmailOrElseThrow(email);
		Room room = roomReadService.findRoomByIdOrElseThrow(roomId);
		roomParticipantReadService.checkParticipant(user, room);

		return participantRoleReadService.findRoomRolesByUserAndRoom(user, room);
	}

	public void exitRoom(String email, Long roomId, ExitRoomRequestDTO dto) {
		User user = userReadService.findUserByEmailOrElseThrow(email);
		Room room = roomReadService.findRoomByIdOrElseThrow(roomId);
		roomParticipantReadService.checkParticipant(user, room);

		List<ParticipantRole> participantRoles = participantRoleReadService.findUserByRoomId(roomId);
		List<RoomParticipant> roomParticipants = roomParticipantReadService.findByRoomId(roomId);

		if (checkUserIsAdmin(email, participantRoles) && roomParticipants.size() == 1) {
			// 방에 나 혼자 있을 경우
			deleteAllData(roomId);
			return;
		}

		if (checkUserIsAdmin(email, participantRoles)) {
			// 총무가 나가서, 총무를 위임해야하는 경우
			changeAdmin(dto.getEmail(), roomId);
		}

		commentService.deleteCommentsByRoomIdAndUserId(roomId, user.getId());
		travelEssentialService.deleteAllByRoomIdAndUserId(roomId, user.getId());
		participantRoleRepository.deleteByRoomIdAndEmail(roomId, email);
		roomParticipantRepository.deleteByRoomIdAndEmail(roomId, email);
	}

	private boolean checkUserIsAdmin(String email, List<ParticipantRole> participantRoles) {
		return participantRoles.stream()
			.filter(participantRole -> Objects.equals(participantRole.getUser().getEmail(), email))
			.anyMatch(participantRole -> RoomRole.isAdmin(participantRole.getRoomRole()));
	}

	private void deleteAllData(Long roomId) {
		List<Long> boardIds = roomRepository.findBoardIdsByRoomId(roomId);
		List<AccountingInfo> accountingInfos = accountingInfoReadService.findAccountingInfoByRoomIdAndBoardIds(roomId, boardIds);
		List<Long> accountIds = getAccountIds(accountingInfos);
		List<Long> bookIds = getBookIds(accountingInfos);

		commentService.deleteAllByRoomId(roomId);
		travelEssentialService.deleteAllByRoomId(roomId);
		bookInfoRepository.deleteAllByIds(bookIds);
		accountingInfoRepository.deleteAllByIds(accountIds);
		scheduleInfoRepository.deleteAllByIds(boardIds);
		boardRepository.deleteAllByRoomId(roomId);
		participantRoleRepository.deleteAllByRoomId(roomId);
		roomParticipantRepository.deleteAllByRoomId(roomId);
		roomRepository.deleteById(roomId);
	}

	private List<Long> getBookIds(List<AccountingInfo> accountingInfos) {
		return accountingInfos.stream().map(a -> a.getBoard().getId())
			.collect(Collectors.toList());
	}

	private List<Long> getAccountIds(List<AccountingInfo> accountingInfos) {
		return accountingInfos.stream().map(a -> a.getId())
			.collect(Collectors.toList());
	}

	private void changeAdmin(String newAdminEmail, Long roomId) {
		List<ParticipantRole> participantRoles = participantRoleReadService.findByRoomIdAndEmail(roomId, newAdminEmail);
		for (int i = participantRoles.size() - 1; i >= 1; i--) {
			participantRoleRepository.delete(participantRoles.get(i));
			participantRoles.remove(participantRoles.get(i));
		}

		ParticipantRole participantRole = participantRoles.get(0);
		participantRole.updateRole(RoomRole.ADMIN);
	}
}
