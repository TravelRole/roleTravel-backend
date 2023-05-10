package com.travel.role.domain.accounting.dto.request;

import static com.travel.role.global.exception.dto.ExceptionMessage.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.travel.role.domain.accounting.entity.Category;
import com.travel.role.domain.accounting.entity.PaymentMethod;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExpenseDetailsRequestDTO {

	@Size(min = 1, max = 20, message = INVALID_PAYMENT_NAME_SIZE)
	private String paymentName;

	@Min(value = 1, message = INVALID_PAYMENT_PRICE)
	private Integer price;

	@NotNull(message = INVALID_PAYMENT_METHOD)
	private PaymentMethod paymentMethod;

	@NotNull(message = INVALID_CATEGORY)
	private Category category;

	private String accountEtc;
}
