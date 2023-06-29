package com.travel.role.global.exception.accounting;

import com.travel.role.global.exception.dto.ExceptionMessage;

public class AccountingInfoCannotBeModifiedException extends RuntimeException {

	public AccountingInfoCannotBeModifiedException() {
		super(ExceptionMessage.ACCOUNTING_INFO_CANNOT_BE_MODIFIED);
	}
}
