package com.travel.role.domain.schedule.service;

import static com.travel.role.global.exception.dto.ExceptionMessage.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.accounting.entity.AccountingInfo;
import com.travel.role.domain.accounting.repository.AccountingInfoRepository;
import com.travel.role.domain.accounting.service.AccountingInfoReadService;
import com.travel.role.domain.book.entity.BookInfo;
import com.travel.role.domain.book.repository.BookInfoRepository;
import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.room.entity.RoomRole;
import com.travel.role.domain.room.service.ParticipantRoleReadService;
import com.travel.role.domain.room.service.RoomParticipantReadService;
import com.travel.role.domain.room.service.RoomReadService;
import com.travel.role.domain.room.service.RoomService;
import com.travel.role.domain.schedule.dto.request.ScheduleModifyRequestDTO;
import com.travel.role.domain.schedule.dto.request.ScheduleRequestDTO;
import com.travel.role.domain.schedule.dto.response.ScheduleResponseDTO;
import com.travel.role.domain.schedule.entity.Board;
import com.travel.role.domain.schedule.entity.ScheduleInfo;
import com.travel.role.domain.schedule.repository.BoardRepository;
import com.travel.role.domain.schedule.repository.ScheduleInfoRepository;
import com.travel.role.domain.user.entity.User;
import com.travel.role.domain.user.service.UserReadService;
import com.travel.role.global.exception.room.InvalidLocalDateException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleService {
	private final RoomService roomService;
	private final UserReadService userReadService;
	private final RoomReadService roomReadService;
	private final BoardReadService boardReadService;
	private final ScheduleReadService scheduleReadService;
	private final AccountingInfoReadService accountingInfoReadService;
	private final RoomParticipantReadService roomParticipantReadService;
	private final ParticipantRoleReadService participantRoleReadService;
	private final BoardRepository boardRepository;
	private final BookInfoRepository bookInfoRepository;
	private final ScheduleInfoRepository scheduleInfoRepository;
	private final AccountingInfoRepository accountingInfoRepository;

	public void addSchedule(String email, Long roomId, ScheduleRequestDTO scheduleRequestDTO) {

		User user = userReadService.findUserByEmailOrElseThrow(email);
		Room room = roomReadService.findRoomByIdOrElseThrow(roomId);
		roomParticipantReadService.checkParticipant(user, room);
		validateDate(room.getTravelStartDate(), room.getTravelEndDate(),
			scheduleRequestDTO.getScheduleDate().toLocalDate());

		Board board = boardRepository.save(Board.of(room, scheduleRequestDTO));
		scheduleInfoRepository.save(ScheduleInfo.of(board, scheduleRequestDTO));

		if (scheduleRequestDTO.getIsBookRequired()) {
			BookInfo bookInfo = bookInfoRepository.save(BookInfo.builder()
				.isBooked(false)
				.build());
			accountingInfoRepository.save(AccountingInfo.builder()
				.board(board)
				.bookInfo(bookInfo)
				.category(scheduleRequestDTO.getCategory())
				.paymentName(scheduleRequestDTO.getPlaceName())
				.price(0)
				.room(room)
				.build());
		}
	}

	public void modifySchedule(String email, Long roomId, ScheduleModifyRequestDTO scheduleModifyRequestDTO) {

		User user = userReadService.findUserByEmailOrElseThrow(email);
		Room room = roomReadService.findRoomByIdOrElseThrow(roomId);

		roomParticipantReadService.checkParticipant(user, room);
		participantRoleReadService.validateUserRoleInRoom(user, room, RoomRole.getScheduleRoles());

		Board board = boardReadService.findBoardByIdOrElseThrow(scheduleModifyRequestDTO.getBoardId());
		ScheduleInfo scheduleInfo = scheduleReadService.findScheduleInfoByIdOrElseThrow(
			scheduleModifyRequestDTO.getBoardId());

		board.updateTimeAndCategory(scheduleModifyRequestDTO);
		scheduleInfo.updateEtc(scheduleModifyRequestDTO.getEtc());

		if (scheduleModifyRequestDTO.getIsBookRequired()) {
			BookInfo bookInfo = bookInfoRepository.save(BookInfo.builder()
				.isBooked(false)
				.build());
			accountingInfoRepository.save(AccountingInfo.builder()
				.board(board)
				.bookInfo(bookInfo)
				.category(scheduleModifyRequestDTO.getCategory())
				.paymentName(scheduleModifyRequestDTO.getPlaceName())
				.price(0)
				.room(room)
				.build());
		}
	}

	public void deleteSchedule(String email, Long roomId, List<Long> scheduleIds) {

		User user = userReadService.findUserByEmailOrElseThrow(email);
		Room room = roomReadService.findRoomByIdOrElseThrow(roomId);

		roomParticipantReadService.checkParticipant(user, room);
		participantRoleReadService.validateUserRoleInRoom(user, room, RoomRole.getScheduleRoles());

		deleteScheduleById(roomId, scheduleIds);
	}

	private void deleteScheduleById(Long roomId, List<Long> scheduleIds) {

		List<AccountingInfo> accountingInfos = accountingInfoReadService.findAccountingInfoByRoomIdAndBoardIds(roomId,
			scheduleIds);
		List<Long> accountIds = roomService.getAccountIds(accountingInfos);
		List<Long> bookIds = roomService.getBookIds(accountingInfos);

		accountingInfoRepository.deleteAllByIdsIn(accountIds);
		bookInfoRepository.deleteAllByIds(bookIds);
		scheduleInfoRepository.deleteAllByIds(scheduleIds);
		boardRepository.deleteAllByIds(scheduleIds);
	}

	public List<ScheduleResponseDTO> getSchedule(String email, Long roomId, LocalDate date) {

		User user = userReadService.findUserByEmailOrElseThrow(email);
		Room room = roomReadService.findRoomByIdOrElseThrow(roomId);

		roomParticipantReadService.checkParticipant(user, room);
		validateDate(room.getTravelStartDate(), room.getTravelEndDate(), date);

		return getScheduleInfo(boardReadService.findScheduleForDate(roomId, date));
	}

	private List<ScheduleResponseDTO> getScheduleInfo(List<Board> boardList) {
		return boardList.stream()
			.map(board -> ScheduleResponseDTO.from(board))
			.collect(Collectors.toList());
	}

	public void validateDate(LocalDate startDate, LocalDate endDate, LocalDate date) {

		if (date.isBefore(startDate))
			throw new InvalidLocalDateException(EARLY_DATE_ERROR);
		if (date.isAfter(endDate))
			throw new InvalidLocalDateException(LATE_DATE_ERROR);
	}

}
