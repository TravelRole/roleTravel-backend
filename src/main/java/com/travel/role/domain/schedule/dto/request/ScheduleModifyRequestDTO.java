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
public class ScheduleModifyRequestDTO {

	@NotNull(message = ExceptionMessage.BOARD_ID_EMPTY)
	private Long boardId;

	@NotBlank(message = ExceptionMessage.PLACE_NAME_VALUE_NOT_EMPTY)
	private String placeName;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
	private LocalDateTime scheduleDate;

	private Boolean isBookRequired;

	@NotNull(message = ExceptionMessage.INVALID_CATEGORY)
	private Category category;

	private String etc;
}
