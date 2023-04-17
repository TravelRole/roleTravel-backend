package com.travel.role.domain.room.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserIsNotPrivilegeException extends RuntimeException{
	public UserIsNotPrivilegeException(String msg) {super(msg);}
}
