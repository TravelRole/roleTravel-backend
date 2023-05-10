package com.travel.role.domain.accounting.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExpenseDetailsRequestDTO {

	private String paymentName;
	private Integer price;
	private String paymentMethod;
	private String category;
	private String accountEtc;
}
