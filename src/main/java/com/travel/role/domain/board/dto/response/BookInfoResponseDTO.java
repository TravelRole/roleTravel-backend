package com.travel.role.domain.board.dto.response;

import com.travel.role.domain.board.entity.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class BookInfoResponseDTO {
    private String placeName;
    private LocalTime time;
    private String link;
    private String scheduleEtc;
    private Integer price;
    private String paymentMethod;
    private Category category;
    private String accountingEtc;
    private Boolean isBooked;

    private BookInfoResponseDTO(Board board, ScheduleInfo scheduleInfo, AccountingInfo accountingInfo, BookInfo bookInfo) {
        this.placeName = scheduleInfo.getPlaceName();
        this.time = board.getScheduleDate().toLocalTime();
        this.link = board.getLink();
        this.scheduleEtc = scheduleInfo.getScheduleEtc();
        this.price = accountingInfo.getPrice();
        this.paymentMethod = accountingInfo.getPaymentMethod();
        this.category = board.getCategory();
        this.accountingEtc = accountingInfo.getAccountingEtc();
        this.isBooked = bookInfo.getIsBooked();
    }

    public static BookInfoResponseDTO of(Board board, ScheduleInfo scheduleInfo,AccountingInfo accountingInfo, BookInfo bookInfo) {
        return new BookInfoResponseDTO(board, scheduleInfo, accountingInfo, bookInfo);
    }

}
