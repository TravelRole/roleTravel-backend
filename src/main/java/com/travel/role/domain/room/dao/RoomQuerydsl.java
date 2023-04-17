package com.travel.role.domain.room.dao;

import java.util.List;

import com.querydsl.core.Tuple;
import com.travel.role.domain.room.domain.RoomRole;

public interface RoomQuerydsl {

	List<Tuple> getMemberInRoom(String email);
	List<RoomRole> getRoomRole(String email, Long roomId);
}
