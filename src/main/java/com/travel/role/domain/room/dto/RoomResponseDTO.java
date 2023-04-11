package com.travel.role.domain.room.dto;

import java.time.LocalDate;
import java.util.List;

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
	private Integer totalNumber;
	private List<RoomResponseDTO> members;
}
