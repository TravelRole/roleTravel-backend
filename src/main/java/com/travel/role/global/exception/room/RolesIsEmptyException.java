package com.travel.role.global.exception.room;

import static com.travel.role.global.exception.dto.ExceptionMessage.*;

public class RolesIsEmptyException extends RuntimeException{
	public RolesIsEmptyException() {
		super(ROLE_IS_EMPTY);
	}
}
