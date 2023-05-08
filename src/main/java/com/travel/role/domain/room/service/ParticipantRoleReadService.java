package com.travel.role.domain.room.service;

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

	public ParticipantRole findUserByRoomId(Long roomId) {
		return participantRoleRepository.findUserByRoomId(roomId)
			.orElseThrow(RoomInfoNotFoundException::new);
	}
}
