package com.travel.role.domain.room.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travel.role.domain.room.domain.Room;
import com.travel.role.domain.room.domain.RoomParticipant;
import com.travel.role.domain.user.domain.User;

public interface RoomParticipantRepository extends JpaRepository<RoomParticipant, Long> {


	boolean existsByUserAndRoom(User user, Room room);
}
