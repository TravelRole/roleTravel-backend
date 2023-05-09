package com.travel.role.domain.room.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.room.entity.ParticipantRole;
import com.travel.role.domain.room.repository.ParticipantRoleRepository;
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
}
