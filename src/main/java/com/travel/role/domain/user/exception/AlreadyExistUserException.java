package com.travel.role.domain.user.exception;

import com.travel.role.global.exception.ExceptionMessage;

public class AlreadyExistUserException extends RuntimeException {
	public AlreadyExistUserException() {
		super(ExceptionMessage.ALREADY_EXIST_USER);
	}
}
