package com.travel.role.global.exception.room;

import static com.travel.role.global.exception.ExceptionMessage.*;

public class UserHaveNotPrivilegeException extends RuntimeException{
	public UserHaveNotPrivilegeException() {super(USER_HAVE_NOT_PRIVILEGE);}
}
