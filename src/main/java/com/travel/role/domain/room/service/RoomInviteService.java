package com.travel.role.domain.room.service;

import static com.travel.role.global.util.Constants.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.room.dto.response.InviteResponseDTO;
import com.travel.role.domain.room.entity.ParticipantRole;
import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.room.entity.RoomParticipant;
import com.travel.role.domain.room.entity.RoomRole;
import com.travel.role.domain.room.repository.ParticipantRoleRepository;
import com.travel.role.domain.room.repository.RoomParticipantRepository;
import com.travel.role.domain.user.entity.User;
import com.travel.role.domain.user.service.UserReadService;
import com.travel.role.global.exception.room.AlreadyExistInRoomException;
import com.travel.role.global.exception.room.InvalidInviteCode;
import com.travel.role.global.exception.room.UserHaveNotPrivilegeException;
import com.travel.role.global.util.PasswordGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomInviteService {

	private final UserReadService userReadService;
	private final RoomReadService roomReadService;
	private final ParticipantRoleReadService participantRoleReadService;

	private final RoomParticipantRepository roomParticipantRepository;
	private final ParticipantRoleRepository participantRoleRepository;

	private final PasswordGenerator passwordGenerator;

	@Transactional
	public String makeInviteCode(String email, Long roomId) {
		User user = userReadService.findUserByEmailOrElseThrow(email);
		Room room = roomReadService.findRoomByIdOrElseThrow(roomId);

		participantRoleReadService.validIsAdmin(user, room);

		String inviteCode = passwordGenerator.generateRandomPassword(MAX_PASSWORD_LENGTH);
		if (validateInviteCode(room)) {
			inviteCode = passwordGenerator.generateRandomPassword(MAX_PASSWORD_LENGTH);
			room.updateInviteCode(inviteCode, LocalDateTime.now());
		}

		return inviteCode;
	}

	@Transactional
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

	public void checkRoomInviteCode(String email, String inviteCode) {
		Room room = roomReadService.getRoomUsingInviteCode(inviteCode);

		validateInviteRoom(email, room);
	}

	private boolean validateInviteCode(Room room) {
		return room.getRoomInviteCode() == null || room.getRoomExpiredTime().plusDays(1L).isAfter(LocalDateTime.now());
	}

	private void validateInviteRoom(String email, Room room) {
		if (!validateInviteCode(room)) {
			throw new InvalidInviteCode();
		}

		if (roomParticipantRepository.existsUserInRoom(email, room.getId())) {
			throw new AlreadyExistInRoomException();
		}
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
