package com.travel.role.domain.room.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.room.entity.ParticipantRole;
import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.room.entity.RoomRole;
import com.travel.role.domain.room.repository.ParticipantRoleRepository;
import com.travel.role.domain.user.entity.User;
import com.travel.role.global.exception.room.RolesIsEmptyException;
import com.travel.role.global.exception.room.UserHaveNotPrivilegeException;
import com.travel.role.global.exception.user.RoomInfoNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParticipantRoleReadService {
	private final ParticipantRoleRepository participantRoleRepository;

	public List<ParticipantRole> findUserByRoomId(Long roomId) {
		List<ParticipantRole> participantRoles = participantRoleRepository.findUserAndRoomByRoomId(roomId);
		if (participantRoles.isEmpty()) {
			throw new RoomInfoNotFoundException();
		}
		return participantRoles;
	}

	public void validateUserRoleInRoom(User user, Room room, List<RoomRole> roomRoles) {

		if (!participantRoleRepository.existsByUserAndRoomAndRoomRoleIn(user, room, roomRoles)) {
			throw new UserHaveNotPrivilegeException();
		}
	}

	public boolean getRole(User user, Room room, List<RoomRole> roomRoles) {

		return participantRoleRepository.existsByUserAndRoomAndRoomRoleIn(user, room, roomRoles);
	}

	public List<RoomRole> findRoomRolesByUserAndRoom(User user, Room room) {

		List<ParticipantRole> participantRoles = participantRoleRepository.findByUserAndRoom(user, room);

		if (participantRoles.isEmpty())
			throw new RolesIsEmptyException();

		return participantRoles.stream().map(ParticipantRole::getRoomRole)
			.collect(Collectors.toList());
	}

	public boolean checkUserIsAdmin(User user, Room room) {
		return participantRoleRepository.existsByUserAndRoomAndRoomRoleIn(user, room, List.of(RoomRole.ADMIN));
	}
}
