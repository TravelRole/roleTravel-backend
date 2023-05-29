package com.travel.role.domain.room.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.room.repository.querydsl.RoomQuerydsl;

public interface RoomRepository extends JpaRepository<Room, Long>, RoomQuerydsl {
    boolean existsByRoomInviteCode(String inviteCode);
    Optional<Room> findByRoomInviteCode(String inviteCode);

    @Modifying
    @Transactional
    @Query("DELETE FROM Room WHERE id = :roomId")
    void deleteById(@Param("roomId") Long roomId);
}


