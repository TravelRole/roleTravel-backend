package com.travel.role.domain.room.dto.response;

import java.time.LocalTime;

import com.travel.role.domain.accounting.entity.AccountingInfo;
import com.travel.role.domain.board.entity.ScheduleInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleDTO {

	private String placeName;
	private LocalTime time;
	private Boolean isBooked;
	private String scheduleEtc;

	public static ScheduleDTO from(ScheduleInfo scheduleInfo, AccountingInfo accountingInfo, LocalTime time) {
		Boolean isBooked = accountingInfo != null ?
			accountingInfo.getBookInfo() != null ?
				accountingInfo.getBookInfo().getIsBooked() : null : null;

		return ScheduleDTO.builder()
			.placeName(scheduleInfo.getPlaceName())
			.time(time)
			.scheduleEtc(scheduleInfo.getScheduleEtc())
			.isBooked(isBooked)
			.build();
	}
}
