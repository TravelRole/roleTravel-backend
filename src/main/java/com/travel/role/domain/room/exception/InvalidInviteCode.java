package com.travel.role.domain.room.exception;

import static com.travel.role.global.exception.ExceptionMessage.*;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidInviteCode extends RuntimeException{
	public InvalidInviteCode(String msg) {
		super(INVALID_INVITE_CODE);
	}
}
