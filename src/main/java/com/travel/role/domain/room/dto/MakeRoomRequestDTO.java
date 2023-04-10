package com.travel.role.domain.room.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MakeRoomRequestDTO {
	private String roomName;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate travelStartDate;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate travelEndDate;

	private Integer totalParticipants;

	private String location;
}
