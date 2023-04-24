package com.travel.role.global.exception.common;

import com.travel.role.global.exception.ExceptionMessage;

public class InputValueNotMatchException extends RuntimeException {

	public InputValueNotMatchException(String property1, String property2) {
		super(String.format(ExceptionMessage.INPUT_VALUE_NOT_MATCH, property1, property2));
	}

}
