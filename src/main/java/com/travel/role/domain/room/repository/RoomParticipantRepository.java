package com.travel.role.domain.room.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.room.entity.RoomParticipant;
import com.travel.role.domain.room.repository.querydsl.RoomParticipantQuerydsl;
import com.travel.role.domain.user.entity.User;

public interface RoomParticipantRepository extends JpaRepository<RoomParticipant, Long>, RoomParticipantQuerydsl {


	boolean existsByUserAndRoom(User user, Room room);
}
