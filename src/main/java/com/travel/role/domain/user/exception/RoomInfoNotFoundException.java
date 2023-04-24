package com.travel.role.domain.user.exception;

import com.travel.role.global.exception.ExceptionMessage;

public class RoomInfoNotFoundException extends RuntimeException {
	public RoomInfoNotFoundException() {
		super(ExceptionMessage.ROOM_NOT_FOUND);
	}
}

