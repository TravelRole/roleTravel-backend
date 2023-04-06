package com.travel.role.domain.user.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserInfoNotFoundException extends RuntimeException{
	public UserInfoNotFoundException(String msg) {super(msg);}
}
