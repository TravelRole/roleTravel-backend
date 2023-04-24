package com.travel.role.global.exception.common;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidLocalDateException extends RuntimeException{
	public InvalidLocalDateException(String msg) {
		super(msg);
	}
}
