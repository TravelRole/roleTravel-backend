package com.travel.role.domain.room.repository.querydsl;

public interface RoomParticipantQuerydsl {
	boolean existsUserInRoom(String email, Long roomId);
}
