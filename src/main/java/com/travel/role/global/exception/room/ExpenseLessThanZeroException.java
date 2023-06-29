package com.travel.role.global.exception.room;

import com.travel.role.global.exception.dto.ExceptionMessage;

public class ExpenseLessThanZeroException extends RuntimeException {

	public ExpenseLessThanZeroException() {
		super(ExceptionMessage.EXPENSE_MUST_GREATER_THAN_OR_EQUAL_TO_ZERO);
	}
}
