package com.travel.role.domain.room.dao;

import java.util.List;

import com.querydsl.core.Tuple;
import com.travel.role.domain.room.domain.Room;
import com.travel.role.domain.room.dto.MemberDTO;

public interface RoomQuerydsl {

	List<Tuple> getMemberInRoom(String email);
}
