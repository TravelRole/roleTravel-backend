package com.travel.role.domain.room.dto.response;

import java.util.List;

import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.room.entity.RoomRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SidebarResponseDTO {

	private String roomName;
	private Long roomImage;
	private List<RoomRole> roles;

	public static SidebarResponseDTO of(Room room, List<RoomRole> roles) {
		return new SidebarResponseDTO(room.getRoomName(), room.getRoomImage(), roles);
	}
}
