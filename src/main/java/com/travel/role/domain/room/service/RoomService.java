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

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.Tuple;
import com.travel.role.domain.room.dto.request.MakeRoomRequestDTO;
import com.travel.role.domain.room.dto.request.RoomModifiedRequestDTO;
import com.travel.role.domain.room.dto.response.InviteResponseDTO;
import com.travel.role.domain.room.dto.response.MemberDTO;
import com.travel.role.domain.room.dto.response.RoomResponseDTO;
import com.travel.role.domain.room.dto.response.TimeResponseDTO;
import com.travel.role.domain.room.entity.ParticipantRole;
import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.room.entity.RoomParticipant;
import com.travel.role.domain.room.entity.RoomRole;
import com.travel.role.domain.room.repository.ParticipantRoleRepository;
import com.travel.role.domain.room.repository.RoomParticipantRepository;
import com.travel.role.domain.room.repository.RoomRepository;
import com.travel.role.domain.user.entity.User;
import com.travel.role.domain.user.service.UserReadService;
import com.travel.role.global.exception.room.AlreadyExistInRoomException;
import com.travel.role.global.exception.room.InvalidInviteCode;
import com.travel.role.global.exception.room.InvalidLocalDateException;
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
    private final PasswordGenerator passwordGenerator;
    private final RoomReadService roomReadService;

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
        validateDate(makeRoomRequestDTO);
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

    private void validateDate(MakeRoomRequestDTO makeRoomRequestDTO) {
        LocalDate start = makeRoomRequestDTO.getTravelStartDate();
        LocalDate end = makeRoomRequestDTO.getTravelEndDate();

        if (start.isAfter(end))
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
        boolean isExist = participantRoleRepository.existsByUserAndRoomAndRoomRoleIn(user, room, Arrays.asList(checkRooms));
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
            result.add(TimeResponseDTO.from(i, starDate, starDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN)));
            starDate = starDate.plusDays(1);
        }
        return result;
    }

    public void modifiedRoomInfo(String email, RoomModifiedRequestDTO dto) {

    }
}
