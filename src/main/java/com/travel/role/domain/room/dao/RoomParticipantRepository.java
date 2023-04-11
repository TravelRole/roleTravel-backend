package com.travel.role.domain.room.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travel.role.domain.room.domain.RoomParticipant;

public interface RoomParticipantRepository extends JpaRepository<RoomParticipant, Long> {
}
