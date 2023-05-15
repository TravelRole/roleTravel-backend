package com.travel.role.domain.accounting.dto.response;

import java.time.LocalDate;

import com.travel.role.domain.accounting.entity.AccountingInfo;
import com.travel.role.domain.accounting.entity.Category;
import com.travel.role.domain.accounting.entity.PaymentMethod;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ExpenseDetailModifyResDTO {

	private Long id;
	private String paymentName;
	private Integer price;
	private PaymentMethod paymentMethod;
	private Category category;
	private String accountEtc;
	private LocalDate paymentTime;

	public static ExpenseDetailModifyResDTO from(AccountingInfo accountingInfo) {

		return ExpenseDetailModifyResDTO.builder()
			.id(accountingInfo.getId())
			.paymentName(accountingInfo.getPaymentName())
			.price(accountingInfo.getPrice())
			.paymentMethod(accountingInfo.getPaymentMethod())
			.category(accountingInfo.getCategory())
			.accountEtc(accountingInfo.getAccountingEtc())
			.paymentTime(accountingInfo.getPaymentTime())
			.build();
	}
}
