package com.travel.role.global.exception.user;

import com.travel.role.global.exception.ExceptionMessage;

public class UserInfoNotFoundException extends RuntimeException {
	public UserInfoNotFoundException() {
		super(ExceptionMessage.USERNAME_NOT_FOUND);
	}
}
