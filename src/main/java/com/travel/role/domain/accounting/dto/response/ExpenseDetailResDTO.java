package com.travel.role.domain.accounting.dto.response;

import com.travel.role.domain.accounting.entity.AccountingInfo;
import com.travel.role.domain.accounting.entity.Category;
import com.travel.role.domain.accounting.entity.PaymentMethod;
import com.travel.role.domain.book.entity.BookInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ExpenseDetailResDTO {

	private Long id;
	private String paymentName;
	private PaymentMethod paymentMethod;
	private Category category;
	private Integer price;
	private String accountingEtc;
	private String bookEtc;
	private boolean fromBook;

	public static ExpenseDetailResDTO from(AccountingInfo accountingInfo) {

		BookInfo bookInfo = accountingInfo.getBookInfo();
		String bookEtc = bookInfo == null ? null : bookInfo.getBookEtc();

		return ExpenseDetailResDTO.builder()
			.id(accountingInfo.getId())
			.paymentName(accountingInfo.getPaymentName())
			.paymentMethod(accountingInfo.getPaymentMethod())
			.category(accountingInfo.getCategory())
			.price(accountingInfo.getPrice())
			.accountingEtc(accountingInfo.getAccountingEtc())
			.bookEtc(bookEtc)
			.fromBook(bookInfo != null)
			.build();
	}
}
