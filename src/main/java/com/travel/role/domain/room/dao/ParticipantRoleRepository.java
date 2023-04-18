package com.travel.role.domain.room.dao;

import com.travel.role.domain.room.domain.ParticipantRole;
import com.travel.role.domain.room.domain.RoomParticipant;
import com.travel.role.domain.room.domain.RoomRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantRoleRepository extends JpaRepository<ParticipantRole, Long> {
    boolean existsByRoomParticipantAndRoomRoleIn(RoomParticipant roomParticipant, List<RoomRole> roomRoles);
}
