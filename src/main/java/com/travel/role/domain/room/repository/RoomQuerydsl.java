package com.travel.role.domain.room.repository;

import java.util.List;

import com.querydsl.core.Tuple;

public interface RoomQuerydsl {

	List<Tuple> getMemberInRoom(String email);
}
