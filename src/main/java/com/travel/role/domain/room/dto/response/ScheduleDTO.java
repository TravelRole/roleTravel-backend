package com.travel.role.domain.room.dto.response;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDTO {

	private String placeName;
	private LocalTime time;
	private Boolean isBooked;
	private String scheduleEtc;
}
