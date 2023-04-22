package com.travel.role.domain.room.dao;

import com.travel.role.domain.room.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long>, RoomQuerydsl {
    Optional<Room> findById(Long roomId);
}


