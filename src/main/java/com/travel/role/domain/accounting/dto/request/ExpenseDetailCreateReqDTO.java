package com.travel.role.domain.accounting.dto.request;

import static com.travel.role.global.exception.dto.ExceptionMessage.*;

import java.time.LocalDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.travel.role.domain.accounting.entity.AccountingInfo;
import com.travel.role.domain.accounting.entity.Category;
import com.travel.role.domain.accounting.entity.PaymentMethod;
import com.travel.role.domain.room.entity.Room;
import com.travel.role.global.util.FormatterUtil;

import lombok.Getter;

@Getter
public class ExpenseDetailCreateReqDTO {

	@Size(min = 1, max = 20, message = INVALID_PAYMENT_NAME_SIZE)
	private String paymentName;

	@Min(value = 1, message = INVALID_PAYMENT_PRICE)
	private Integer price;

	@NotNull(message = INVALID_PAYMENT_METHOD)
	private PaymentMethod paymentMethod;

	@NotNull(message = INVALID_CATEGORY)
	private Category category;

	private String accountEtc;

	private LocalDate paymentTime;

	@JsonCreator
	public ExpenseDetailCreateReqDTO(
		@JsonProperty("paymentName") String paymentName,
		@JsonProperty("price") Integer price,
		@JsonProperty("paymentMethod") PaymentMethod paymentMethod,
		@JsonProperty("category") Category category,
		@JsonProperty("accountEtc") String accountEtc,
		@JsonProperty("paymentTime") String paymentTimeStr) {

		LocalDate paymentTime = FormatterUtil.stringToLocalDate(paymentTimeStr, "yyyy-MM-dd",
			INVALID_PAYMENT_TIME);

		this.paymentName = paymentName;
		this.price = price;
		this.paymentMethod = paymentMethod;
		this.category = category;
		this.accountEtc = accountEtc;
		this.paymentTime = paymentTime;
	}

	public AccountingInfo toAccountingInfo(Room room) {

		return AccountingInfo.builder()
			.room(room)
			.board(null)
			.bookInfo(null)
			.category(this.category)
			.accountingEtc(this.accountEtc)
			.paymentMethod(this.paymentMethod)
			.paymentName(this.paymentName)
			.price(this.price)
			.paymentTime(this.paymentTime)
			.build();
	}
}
