package com.travel.role.domain.room.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserHaveNotPrivilegeException extends RuntimeException{
	public UserHaveNotPrivilegeException(String msg) {super(msg);}
}
