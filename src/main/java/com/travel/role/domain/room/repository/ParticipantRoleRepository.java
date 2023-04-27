package com.travel.role.domain.room.repository;

import com.travel.role.domain.room.entity.ParticipantRole;
import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.room.entity.RoomRole;
import com.travel.role.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantRoleRepository extends JpaRepository<ParticipantRole, Long> {
    boolean existsByUserAndRoomAndRoomRoleIn(User user, Room room, List<RoomRole> roomRoles);
}
