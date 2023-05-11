package com.travel.role.domain.accounting.dto.response;

import com.travel.role.domain.accounting.entity.AccountingInfo;
import com.travel.role.domain.accounting.entity.Category;
import com.travel.role.domain.accounting.entity.PaymentMethod;
import com.travel.role.domain.board.entity.BookInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ExpenseDetailResDTO {

	private Long id;
	private PaymentMethod paymentMethod;
	private Category category;
	private Integer price;
	private String accountingEtc;
	private String bookEtc;

	public static ExpenseDetailResDTO of(AccountingInfo accountingInfo, BookInfo bookInfo){

		return ExpenseDetailResDTO.builder()
			.id(accountingInfo.getId())
			.paymentMethod(accountingInfo.getPaymentMethod())
			.category(accountingInfo.getCategory())
			.price(accountingInfo.getPrice())
			.accountingEtc(accountingInfo.getAccountingEtc())
			.bookEtc(bookInfo.getBookEtc())
			.build();
	}
}
