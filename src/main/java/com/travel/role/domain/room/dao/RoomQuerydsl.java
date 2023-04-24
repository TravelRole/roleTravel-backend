package com.travel.role.domain.room.dao;

import java.util.List;

import com.querydsl.core.Tuple;

public interface RoomQuerydsl {

	List<Tuple> getMemberInRoom(String email);
}
