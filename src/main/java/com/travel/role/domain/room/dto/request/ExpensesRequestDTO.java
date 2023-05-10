package com.travel.role.domain.room.dto.request;

import javax.validation.constraints.Min;

import com.travel.role.global.exception.dto.ExceptionMessage;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ExpensesRequestDTO {

	@Min(value = 0, message = ExceptionMessage.EXPENSE_MUST_GREATER_THAN_OR_EQUAL_TO_ZERO)
	private Integer expenses;
}
