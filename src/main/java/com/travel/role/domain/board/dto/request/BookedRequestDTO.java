package com.travel.role.domain.board.dto.request;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.travel.role.global.exception.dto.ExceptionMessage;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookedRequestDTO {

	@NotNull(message = ExceptionMessage.BOOK_INFO_ID_VALUE_NOT_EMPTY)
	private Long bookInfoId;

	@NotNull(message = ExceptionMessage.ACCOUNTING_INFO_ID_VALUE_NOT_EMPTY)
	private Long accountingInfoId;

	@NotNull(message = ExceptionMessage.PAYMENT_TIME_NOT_FOUND)
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate paymentTime;

	@NotNull(message = ExceptionMessage.IS_BOOKED_NOT_FOUND)
	private Boolean isBooked;
}
