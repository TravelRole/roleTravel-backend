package com.travel.role.domain.room.repository.querydsl;

import java.util.List;

import com.querydsl.core.Tuple;

public interface RoomQuerydsl {

	List<Tuple> getMemberInRoom(String email);
	long deleteAllBoard(Long roomId);
	List<Long> findBoardIdsByRoomId(Long roomId);

}
