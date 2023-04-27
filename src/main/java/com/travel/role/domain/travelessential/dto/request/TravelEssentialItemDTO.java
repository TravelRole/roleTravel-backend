package com.travel.role.domain.travelessential.dto.request;

import static com.travel.role.global.exception.dto.ExceptionMessage.*;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TravelEssentialItemDTO {

	@Size(min = 1, max = 15, message = INVALID_ESSENTIAL_ITEM_LENGTH)
	private String item;
}
