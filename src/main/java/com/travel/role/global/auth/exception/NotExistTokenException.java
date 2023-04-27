package com.travel.role.global.auth.exception;

import com.travel.role.global.exception.dto.ExceptionMessage;

public class NotExistTokenException extends RuntimeException {
	public NotExistTokenException() {
		super(ExceptionMessage.NOT_EXISTS_TOKEN);
	}
}
