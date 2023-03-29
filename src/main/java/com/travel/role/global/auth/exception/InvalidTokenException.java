package com.travel.role.global.auth.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidTokenException extends RuntimeException{
	public InvalidTokenException(String msg) {
		super(msg);
	}
}
