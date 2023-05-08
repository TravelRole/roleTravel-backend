package com.travel.role.domain.room.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.travel.role.domain.room.entity.ParticipantRole;
import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.room.entity.RoomRole;
import com.travel.role.domain.room.repository.querydsl.ParticipantRoleQuerydsl;
import com.travel.role.domain.user.entity.User;

public interface ParticipantRoleRepository extends JpaRepository<ParticipantRole, Long> ,ParticipantRoleQuerydsl {
    boolean existsByUserAndRoomAndRoomRoleIn(User user, Room room, List<RoomRole> roomRoles);

    @Query("SELECT pr FROM ParticipantRole pr"
        + " JOIN FETCH pr.user"
        + " JOIN FETCH pr.room"
        + " WHERE pr.room.id = :roomId")
    List<ParticipantRole> findUserByRoomId(@Param("roomId") Long roomId);
}
