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

	public void validUserRoleInRoom(User user, Room room, List<RoomRole> roomRoles) {

		if (!participantRoleRepository.existsByUserAndRoomAndRoomRoleIn(user, room, roomRoles)) {
			throw new UserHaveNotPrivilegeException();
		}
	}

	public List<RoomRole> findRoomRolesByUserAndRoom(User user, Room room) {

		List<ParticipantRole> participantRoles = participantRoleRepository.findByUserAndRoom(user, room);
		return participantRoles.stream().map(participantRole -> participantRole.getRoomRole())
			.collect(Collectors.toList());
	}
}
