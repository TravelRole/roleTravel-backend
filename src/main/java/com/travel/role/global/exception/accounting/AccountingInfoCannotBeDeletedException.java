package com.travel.role.global.exception.accounting;

import com.travel.role.global.exception.dto.ExceptionMessage;

public class AccountingInfoCannotBeDeletedException extends RuntimeException {

	public AccountingInfoCannotBeDeletedException() {
		super(ExceptionMessage.ACCOUNTING_INFO_CANNOT_BE_DELETED);
	}
}
