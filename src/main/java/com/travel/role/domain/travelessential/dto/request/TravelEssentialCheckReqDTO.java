package com.travel.role.domain.travelessential.dto.request;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.travel.role.global.exception.dto.ExceptionMessage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TravelEssentialCheckReqDTO {

	@NotNull(message = ExceptionMessage.ESSENTIAL_CHECK_STATUS_NOT_EMPTY)
	private Boolean check;
	private List<Long> ids = new ArrayList<>();
}
