package com.travel.role.global.exception.user;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AlreadyExistUserException extends RuntimeException{
	public AlreadyExistUserException(String msg) {
		super(msg);
	}
}
