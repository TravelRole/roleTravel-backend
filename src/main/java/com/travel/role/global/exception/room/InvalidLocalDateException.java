package com.travel.role.global.exception.room;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidLocalDateException extends RuntimeException{
	public InvalidLocalDateException(String msg) {
		super(msg);
	}
}
