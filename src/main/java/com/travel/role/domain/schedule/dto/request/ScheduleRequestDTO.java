package com.travel.role.domain.schedule.dto.request;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.travel.role.domain.accounting.entity.Category;
import com.travel.role.global.exception.dto.ExceptionMessage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleRequestDTO {

	@NotBlank(message = ExceptionMessage.PLACE_NAME_VALUE_NOT_EMPTY)
	private String placeName;

	@NotBlank(message = ExceptionMessage.PLACE_ADDRESS_VALUE_NOT_EMPTY)
	private String placeAddress;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
	private LocalDateTime scheduleDate;

	private String link;

	private Boolean isBookRequired;

	@NotNull(message = ExceptionMessage.INVALID_CATEGORY)
	private Category category;

	@NotNull(message = ExceptionMessage.LATITUDE_VALUE_NOT_EMPTY)
	private Double latitude;

	@NotNull(message = ExceptionMessage.LONGITUDE_NOT_EMPTY)
	private Double longitude;

	private String etc;

	@NotNull(message = ExceptionMessage.MAP_PLACE_ID_VALUE_NOT_EMPTY)
	private Long mapPlaceId;
}
