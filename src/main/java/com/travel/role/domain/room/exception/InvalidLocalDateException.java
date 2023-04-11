package com.travel.role.domain.room.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidLocalDateException extends RuntimeException{
	public InvalidLocalDateException(String msg) {
		super(msg);
	}
}
