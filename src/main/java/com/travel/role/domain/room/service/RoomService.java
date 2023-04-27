package com.travel.role.domain.room.service;

import static com.travel.role.global.exception.dto.ExceptionMessage.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.Tuple;
import com.travel.role.domain.room.repository.ParticipantRoleRepository;
import com.travel.role.domain.room.repository.RoomParticipantRepository;
import com.travel.role.domain.room.repository.RoomRepository;
import com.travel.role.domain.room.entity.ParticipantRole;
import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.room.entity.RoomParticipant;
import com.travel.role.domain.room.entity.RoomRole;
import com.travel.role.domain.room.dto.InviteResponseDTO;
import com.travel.role.domain.room.dto.MakeRoomRequestDTO;
import com.travel.role.domain.room.dto.MemberDTO;
import com.travel.role.domain.room.dto.RoomResponseDTO;
import com.travel.role.global.exception.room.AlreadyExistInRoomException;
import com.travel.role.global.exception.room.InvalidInviteCode;
import com.travel.role.global.exception.room.InvalidLocalDateException;
import com.travel.role.global.exception.room.UserHaveNotPrivilegeException;
import com.travel.role.domain.user.repository.UserRepository;
import com.travel.role.domain.user.entity.User;
import com.travel.role.global.exception.user.RoomInfoNotFoundException;
import com.travel.role.global.exception.user.UserInfoNotFoundException;
import com.travel.role.global.auth.token.UserPrincipal;
import com.travel.role.global.util.PasswordGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomService {

    private static final int MAX_PASSWORD_LENGTH = 20;

	private final RoomRepository roomRepository;
	private final UserRepository userRepository;
	private final RoomParticipantRepository roomParticipantRepository;
	private final ParticipantRoleRepository participantRoleRepository;
	private final PasswordGenerator passwordGenerator;

    public List<RoomResponseDTO> getRoomList(UserPrincipal userPrincipal) {
        List<Tuple> findRoomInfo = roomRepository.getMemberInRoom(userPrincipal.getEmail());

        Map<Long, RoomResponseDTO> hash = new HashMap<>();
		for (Tuple tuple : findRoomInfo) {
			Room room = tuple.get(0, Room.class);
			User user = tuple.get(1, User.class);

			if (Objects.equals(user.getEmail(), userPrincipal.getEmail())) {
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

	public void makeRoom(UserPrincipal userPrincipal, MakeRoomRequestDTO makeRoomRequestDTO) {
		validateDate(makeRoomRequestDTO);
		User user = findUser(userPrincipal);
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

    private User findUser(UserPrincipal userPrincipal) {
        return userRepository.findByEmail(userPrincipal.getEmail())
                .orElseThrow(UserInfoNotFoundException::new);
    }

    private void validateDate(MakeRoomRequestDTO makeRoomRequestDTO) {
        LocalDate start = makeRoomRequestDTO.getTravelStartDate();
        LocalDate end = makeRoomRequestDTO.getTravelEndDate();

		if (start.isAfter(end))
			throw new InvalidLocalDateException(INVALID_DATE_ERROR);
	}

	public String makeInviteCode(UserPrincipal userPrincipal, Long roomId) {
		User user = findUser(userPrincipal);
		Room room = findRoom(roomId);

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

	private Room findRoom(Long id) {
		return roomRepository.findById(id)
			.orElseThrow(RoomInfoNotFoundException::new);
	}

	@Transactional(readOnly = true)
	public void checkRoomInviteCode(UserPrincipal userPrincipal, String inviteCode) {
		Room room = getRoomUsingInviteCode(inviteCode);

		validateInviteRoom(userPrincipal, room);
	}

	private void validateInviteRoom(UserPrincipal userPrincipal, Room room) {
		if (!validateInviteCode(room)) {
			throw new InvalidInviteCode();
		}

		if (roomRepository.existsUserInRoom(userPrincipal.getEmail(), room.getId())) {
			throw new AlreadyExistInRoomException();
		}
	}

	private Room getRoomUsingInviteCode(String inviteCode) {
		return roomRepository.findByRoomInviteCode(inviteCode)
			.orElseThrow(InvalidInviteCode::new);
	}

	public InviteResponseDTO inviteUser(UserPrincipal userPrincipal, String inviteCode, List<String> roles) {
		Room room = getRoomUsingInviteCode(inviteCode);
		User user = findUser(userPrincipal);

		validateInviteRoom(userPrincipal, room);
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
}
