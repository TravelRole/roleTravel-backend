package com.travel.role.domain.room.dto.response;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllPlanDTO {

	private LocalDate date;
	private String dayOfTheWeek;
	private Integer travelExpense;
	private List<ScheduleDTO> schedules;

	public void addTravelExpense(int expense) {
		this.travelExpense += expense;
	}
}
