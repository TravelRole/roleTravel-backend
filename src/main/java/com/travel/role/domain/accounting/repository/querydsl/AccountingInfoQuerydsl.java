package com.travel.role.domain.accounting.repository.querydsl;

import java.time.LocalDate;
import java.util.List;

import com.travel.role.domain.accounting.entity.AccountingInfo;
import com.travel.role.domain.accounting.entity.PaymentMethod;

public interface AccountingInfoQuerydsl {

	List<AccountingInfo> findAllByRoomIdAndDateAndPaymentMethod(Long roomId, LocalDate date, PaymentMethod paymentMethod);
	List<AccountingInfo> findAccountingInfoByRoomIdAndBoardIds(Long roomId, List<Long> boardIds);
}
