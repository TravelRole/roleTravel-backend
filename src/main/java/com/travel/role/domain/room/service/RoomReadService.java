package com.travel.role.domain.room.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.room.repository.RoomRepository;
import com.travel.role.global.exception.room.InvalidInviteCode;
import com.travel.role.global.exception.user.RoomInfoNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomReadService {
	private final RoomRepository roomRepository;

	public Room findRoomByIdOrElseThrow(Long roomId) {

		return roomRepository.findById(roomId)
			.orElseThrow(RoomInfoNotFoundException::new);
	}

	public Room getRoomUsingInviteCode(String inviteCode) {
		return roomRepository.findByRoomInviteCode(inviteCode)
			.orElseThrow(InvalidInviteCode::new);
	}
}
