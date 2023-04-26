package com.travel.role.domain.room.dao;

import com.travel.role.domain.room.domain.ParticipantRole;
import com.travel.role.domain.room.domain.Room;
import com.travel.role.domain.room.domain.RoomRole;
import com.travel.role.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantRoleRepository extends JpaRepository<ParticipantRole, Long> {
    boolean existsByUserAndRoomAndRoomRoleIn(User user, Room room, List<RoomRole> roomRoles);
}
