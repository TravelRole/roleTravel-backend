package com.travel.role.domain.room.repository.querydsl;

import com.travel.role.domain.room.entity.RoomRole;

public interface ParticipantRoleQuerydsl {
	boolean existsByUserEmailAndRoomIdAndRole(String email, Long roomId, RoomRole role);
}
