package com.travel.role.domain.room.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travel.role.domain.room.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Long>, RoomQuerydsl {
    boolean existsByRoomInviteCode(String inviteCode);
    Optional<Room> findByRoomInviteCode(String inviteCode);
}


