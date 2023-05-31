package com.travel.role.domain.accounting.repository.querydsl;

import static com.travel.role.domain.accounting.entity.QAccountingInfo.*;
import static com.travel.role.domain.book.entity.QBookInfo.*;

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

	@Override
	public int findTotalExpenseByRoomId(Long roomId) {

		Integer totalExpense = queryFactory.select(accountingInfo.price.sum())
			.from(accountingInfo)
			.where(accountingInfo.room.id.eq(roomId), accountingInfo.paymentTime.isNotNull())
			.fetchOne();

		return totalExpense == null ? 0 : totalExpense;
	}

	@Override
	public List<AccountingInfo> findAccountingInfoByRoomIdAndBoardIds(Long roomId, List<Long> boardIds) {
		return queryFactory
			.selectDistinct(accountingInfo)
			.from(accountingInfo)
			.join(accountingInfo.bookInfo, bookInfo).fetchJoin()
			.where(accountingInfo.room.id.eq(roomId).and(accountingInfo.board.id.in(boardIds)))
			.fetch();
	}

	private BooleanExpression eqPaymentMethod(PaymentMethod paymentMethod) {

		if (paymentMethod == null)
			return null;

		return accountingInfo.paymentMethod.eq(paymentMethod);
	}
}
