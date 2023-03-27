package com.travel.role.global.auth.exception.auth;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotExistTokenException extends RuntimeException{
	public NotExistTokenException(String msg) {
		super(msg);
	}
}
