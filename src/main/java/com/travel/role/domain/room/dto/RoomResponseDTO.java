package com.travel.role.domain.room.dto;

import java.time.LocalDate;
import java.util.List;

import com.travel.role.domain.room.domain.Room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomResponseDTO {
	private String roomName;
	private LocalDate startDate;
	private LocalDate endDate;
	private String location;
	private List<MemberDTO> members;

	public static RoomResponseDTO of(Room room, List<MemberDTO> members) {
		return new RoomResponseDTO(room.getRoomName(), room.getTravelStartDate(), room.getTravelEndDate(),
			room.getLocation(), members);
	}
}
