package com.travel.role.global.exception.user;

import com.travel.role.global.exception.dto.ExceptionMessage;

public class AlreadyExistUserException extends RuntimeException {
	public AlreadyExistUserException() {
		super(ExceptionMessage.ALREADY_EXIST_USER);
	}
}
