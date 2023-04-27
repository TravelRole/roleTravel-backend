package com.travel.role.global.exception.auth;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidTokenException extends RuntimeException{
	public InvalidTokenException(String msg) {
		super(msg);
	}
}
