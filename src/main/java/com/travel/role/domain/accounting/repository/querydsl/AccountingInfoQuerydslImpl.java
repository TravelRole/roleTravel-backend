package com.travel.role.domain.accounting.repository.querydsl;

import static com.travel.role.domain.accounting.entity.QAccountingInfo.*;

import java.time.LocalDate;
import java.util.List;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travel.role.domain.accounting.entity.AccountingInfo;
import com.travel.role.domain.accounting.entity.PaymentMethod;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountingInfoQuerydslImpl implements AccountingInfoQuerydsl {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<AccountingInfo> findAllByRoomIdAndDateAndPaymentMethod(Long roomId, LocalDate date,
		PaymentMethod paymentMethod) {

		List<AccountingInfo> accountingInfos = queryFactory.selectFrom(accountingInfo)
			.leftJoin(accountingInfo.bookInfo)
			.fetchJoin()
			.where(accountingInfo.room.id.eq(roomId), accountingInfo.paymentTime.eq(date),
				eqPaymentMethod(paymentMethod))
			.fetch();

		return accountingInfos;
	}

	private BooleanExpression eqPaymentMethod(PaymentMethod paymentMethod) {

		if (paymentMethod == null)
			return null;

		return accountingInfo.paymentMethod.eq(paymentMethod);
	}
}
