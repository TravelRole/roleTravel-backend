package com.travel.role.global.exception.room;

import static com.travel.role.global.exception.dto.ExceptionMessage.*;

public class AdminIsOnlyOneException extends RuntimeException {
	public AdminIsOnlyOneException() {super(ADMIN_IS_ONLY_ONE);}
}
