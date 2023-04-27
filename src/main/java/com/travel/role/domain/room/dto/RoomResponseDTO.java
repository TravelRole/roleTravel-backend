package com.travel.role.domain.room.dto;

import java.time.LocalDate;
import java.util.List;

import com.travel.role.domain.room.entity.Room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomResponseDTO {
	private Long roomId;
	private String roomName;
	private LocalDate startDate;
	private LocalDate endDate;
	private String location;
	private Long roomImage;
	private List<MemberDTO> members;

	public static RoomResponseDTO of(Room room, List<MemberDTO> members) {
		return new RoomResponseDTO(room.getId(), room.getRoomName(), room.getTravelStartDate(), room.getTravelEndDate(),
			room.getLocation(), room.getRoomImage(), members);
	}
}
