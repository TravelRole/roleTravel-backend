package com.travel.role.global.exception.user;

import com.travel.role.global.exception.ExceptionMessage;

public class RoomInfoNotFoundException extends RuntimeException {
	public RoomInfoNotFoundException() {
		super(ExceptionMessage.ROOM_NOT_FOUND);
	}
}

