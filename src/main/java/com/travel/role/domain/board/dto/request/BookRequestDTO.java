package com.travel.role.domain.board.dto.request;

import com.travel.role.domain.board.entity.PaymentMethod;
import com.travel.role.global.exception.dto.ExceptionMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class BookRequestDTO {
    @NotNull(message = ExceptionMessage.ROOM_ID_VALUE_NOT_EMPTY)
    private Long roomId;

    @NotNull(message = ExceptionMessage.BOOK_INFO_ID_VALUE_NOT_EMPTY)
    private Long bookInfoId;

    @NotNull(message = ExceptionMessage.ACCOUNTING_INFO_ID_VALUE_NOT_EMPTY)
    private Long accountingInfoId;

    private PaymentMethod paymentMethod;

    @Min(value = 0, message = ExceptionMessage.EXPENSE_MUST_GREATER_THAN_OR_EQUAL_TO_ZERO)
    private Integer price;

    private String bookEtc;
}
