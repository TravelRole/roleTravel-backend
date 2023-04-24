package com.travel.role.domain.room.repository;

import com.travel.role.domain.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long>, RoomQuerydsl {
}


