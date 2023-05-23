package com.travel.role.domain.room.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.room.entity.RoomParticipant;
import com.travel.role.domain.room.repository.RoomParticipantRepository;
import com.travel.role.domain.user.entity.User;
import com.travel.role.global.exception.user.UserNotParticipateRoomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomParticipantReadService {
	private final RoomParticipantRepository roomParticipantRepository;
	public void checkParticipant(User user, Room room) {

		if (!roomParticipantRepository.existsByUserAndRoom(user, room)) {
			throw new UserNotParticipateRoomException();
		}
	}

	public List<RoomParticipant> findByRoomId(Long roomId) {
		return roomParticipantRepository.findByRoomId(roomId);
	}
}
