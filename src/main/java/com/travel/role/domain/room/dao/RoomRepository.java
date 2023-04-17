package com.travel.role.domain.room.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.travel.role.domain.room.domain.Room;

public interface RoomRepository extends JpaRepository<Room, Long>, RoomQuerydsl {
    @Query("SELECT r FROM Room r JOIN FETCH r.roomParticipants rp JOIN FETCH rp.user WHERE r.id = :roomId")
    Optional<Room> findByIdWithParticipants(@Param("roomId") Long roomId);

    boolean existsByRoomInviteCode(String inviteCode);
}


