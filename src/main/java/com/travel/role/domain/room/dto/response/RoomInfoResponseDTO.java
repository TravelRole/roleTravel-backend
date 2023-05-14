package com.travel.role.domain.room.dto.response;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.travel.role.domain.room.entity.Room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomInfoResponseDTO {
	private String location;
	@JsonFormat(pattern = "yyyy/MM/dd")
	private LocalDate startDate;
	@JsonFormat(pattern = "yyyy/MM/dd")
	private LocalDate endDate;
	private List<RoomRoleInfoDTO> roles;

	public static RoomInfoResponseDTO of(Room room, List<RoomRoleInfoDTO> roles) {
		return new RoomInfoResponseDTO(room.getLocation(), room.getTravelStartDate(), room.getTravelEndDate(), roles);
	}
}
