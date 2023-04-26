package com.travel.role.domain.room.exception;

import static com.travel.role.global.exception.ExceptionMessage.*;

public class UserHaveNotPrivilegeException extends RuntimeException{
	public UserHaveNotPrivilegeException() {super(USER_HAVE_NOT_PRIVILEGE);}
}
