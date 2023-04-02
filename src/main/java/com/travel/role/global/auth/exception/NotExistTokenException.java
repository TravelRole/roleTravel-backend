package com.travel.role.global.auth.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotExistTokenException extends RuntimeException{
	public NotExistTokenException(String msg) {
		super(msg);
	}
}
