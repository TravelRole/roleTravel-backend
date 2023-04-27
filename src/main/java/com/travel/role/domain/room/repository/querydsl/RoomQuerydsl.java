package com.travel.role.domain.room.repository.querydsl;

import java.util.List;

import com.querydsl.core.Tuple;

public interface RoomQuerydsl {

	List<Tuple> getMemberInRoom(String email);
	boolean existsUserInRoom(String email, Long roomId);
}
