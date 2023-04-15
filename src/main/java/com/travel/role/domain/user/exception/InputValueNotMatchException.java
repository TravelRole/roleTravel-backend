package com.travel.role.domain.user.exception;

import com.travel.role.global.exception.ExceptionMessage;

public class InputValueNotMatchException extends RuntimeException {

	public InputValueNotMatchException(String property1, String property2) {
		super(String.format(ExceptionMessage.INPUT_VALUE_NOT_MATCH, property1, property2));
	}

}
