package com.travel.role.domain.user.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AlreadyExistUserException extends RuntimeException{
	public AlreadyExistUserException(String msg) {
		super(msg);
	}
}
