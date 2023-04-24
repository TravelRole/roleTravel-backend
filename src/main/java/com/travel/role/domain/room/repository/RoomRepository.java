package com.travel.role.domain.room.repository;

import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.room.repository.querydsl.RoomQuerydsl;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long>, RoomQuerydsl {
}


