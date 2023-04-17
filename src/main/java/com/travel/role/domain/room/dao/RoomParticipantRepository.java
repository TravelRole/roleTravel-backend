package com.travel.role.domain.room.dao;

import com.travel.role.domain.room.domain.RoomParticipant;
import com.travel.role.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomParticipantRepository extends JpaRepository<RoomParticipant, Long> {

    boolean existsByUserAndRoomId(User user, Long roomId);
}
