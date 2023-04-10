package com.travel.role.domain.room.service;

import static com.travel.role.global.exception.ExceptionMessage.*;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.room.dao.ParticipantRoleRepository;
import com.travel.role.domain.room.dao.RoomParticipantRepository;
import com.travel.role.domain.room.dao.RoomRepository;
import com.travel.role.domain.room.domain.ParticipantRole;
import com.travel.role.domain.room.domain.Room;
import com.travel.role.domain.room.domain.RoomParticipant;
import com.travel.role.domain.room.domain.RoomRole;
import com.travel.role.domain.room.dto.MakeRoomRequestDTO;
import com.travel.role.domain.user.dao.UserRepository;
import com.travel.role.domain.user.domain.User;
import com.travel.role.domain.user.exception.UserInfoNotFoundException;
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

	public void makeRoom(UserPrincipal userPrincipal, MakeRoomRequestDTO makeRoomRequestDTO) {
		String randomPassword = PasswordGenerator.generateRandomPassword(MAX_PASSWORD_LENGTH);

		User user = findUser(userPrincipal);
		Room room = roomRepository.save(Room.toEntity(makeRoomRequestDTO, randomPassword));
		RoomParticipant roomParticipant = saveNewRoomParticipant(user, room);
		ParticipantRole participantRole = saveNewParticipantRole(roomParticipant);
	}

	private RoomParticipant saveNewRoomParticipant(User user, Room room) {
		RoomParticipant roomParticipant = RoomParticipant.builder()
			.isPaid(false)
			.joinedAt(LocalDateTime.now())
			.room(room)
			.user(user)
			.isPaid(false)
			.build();

		return roomParticipantRepository.save(roomParticipant);
	}

	private ParticipantRole saveNewParticipantRole(RoomParticipant participant) {
		ParticipantRole newParticipantRole = ParticipantRole.builder()
			.roomRole(RoomRole.ADMIN)
			.roomParticipant(participant)
			.build();

		return participantRoleRepository.save(newParticipantRole);
	}

	private User findUser(UserPrincipal userPrincipal) {
		return userRepository.findByEmail(userPrincipal.getName())
			.orElseThrow(() -> new UserInfoNotFoundException(USERNAME_NOT_FOUND));
	}
}
