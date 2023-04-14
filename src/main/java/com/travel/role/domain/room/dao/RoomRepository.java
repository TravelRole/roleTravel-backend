package com.travel.role.domain.room.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travel.role.domain.room.domain.Room;

public interface RoomRepository extends JpaRepository<Room, Long>, RoomQuerydsl {
}
