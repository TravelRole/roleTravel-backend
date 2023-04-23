package com.travel.role.domain.room.dao;

import com.travel.role.domain.room.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long>, RoomQuerydsl {
}


