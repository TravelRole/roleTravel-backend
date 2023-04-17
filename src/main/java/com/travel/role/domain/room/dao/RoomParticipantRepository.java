package com.travel.role.domain.room.dao;

import com.travel.role.domain.room.domain.RoomParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomParticipantRepository extends JpaRepository<RoomParticipant, Long> {
}
