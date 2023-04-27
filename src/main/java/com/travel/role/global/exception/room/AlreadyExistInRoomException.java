package com.travel.role.global.exception.room;

import static com.travel.role.global.exception.ExceptionMessage.*;

public class AlreadyExistInRoomException extends RuntimeException{
	public AlreadyExistInRoomException() {super(ALREADY_EXIST_USER_IN_ROOM);}
}
