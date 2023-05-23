package com.travel.role.domain.room.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.room.entity.RoomParticipant;
import com.travel.role.domain.room.repository.querydsl.RoomParticipantQuerydsl;
import com.travel.role.domain.user.entity.User;

public interface RoomParticipantRepository extends JpaRepository<RoomParticipant, Long>, RoomParticipantQuerydsl {


	boolean existsByUserAndRoom(User user, Room room);

	@Modifying
	@Transactional
	@Query("DELETE FROM RoomParticipant WHERE room.id = :roomId")
	void deleteAllByRoomId(@Param("roomId") Long roomId);

	@Query("SELECT rp FROM RoomParticipant rp WHERE rp.room.id = :roomId")
	List<RoomParticipant> findByRoomId(@Param("roomId") Long roomId);

}
