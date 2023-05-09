package com.travel.role.global.exception.board;

import com.travel.role.global.exception.dto.ExceptionMessage;

public class AccountingInfoNotFoundException extends RuntimeException {
	public AccountingInfoNotFoundException() {
		super(ExceptionMessage.ACCOUNTING_INFO_NOT_FOUND);
	}
}
