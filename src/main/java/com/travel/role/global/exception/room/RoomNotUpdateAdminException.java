package com.travel.role.global.exception.room;

import static com.travel.role.global.exception.dto.ExceptionMessage.*;

public class RoomNotUpdateAdminException extends RuntimeException {
	public RoomNotUpdateAdminException() {
		super(ROOM_NOT_UPDATE_ADMIN);
	}
}
