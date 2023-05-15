package com.travel.role.domain.board.dto.response;

import java.time.LocalTime;

import com.travel.role.domain.accounting.entity.AccountingInfo;
import com.travel.role.domain.accounting.entity.Category;
import com.travel.role.domain.accounting.entity.PaymentMethod;
import com.travel.role.domain.board.entity.Board;
import com.travel.role.domain.board.entity.BookInfo;
import com.travel.role.domain.schedule.entity.ScheduleInfo;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookInfoResponseDTO {
	private String placeName;
	private LocalTime time;
	private String link;
	private String bookEtc;
	private Integer price;
	private PaymentMethod paymentMethod;
	private Category category;
	private String accountingEtc;
	private Boolean isBooked;
	private Long bookInfoId;
	private Long accountingId;

	private BookInfoResponseDTO(Board board, ScheduleInfo scheduleInfo, AccountingInfo accountingInfo,
		BookInfo bookInfo) {
		this.placeName = scheduleInfo.getPlaceName();
		this.time = board.getScheduleDate().toLocalTime();
		this.link = board.getLink();
		this.bookEtc = bookInfo.getBookEtc();
		this.price = accountingInfo.getPrice();
		this.paymentMethod = accountingInfo.getPaymentMethod();
		this.category = board.getCategory();
		this.accountingEtc = accountingInfo.getAccountingEtc();
		this.isBooked = bookInfo.getIsBooked();
		this.bookInfoId = bookInfo.getId();
		this.accountingId = accountingInfo.getId();
	}

	public static BookInfoResponseDTO of(Board board, ScheduleInfo scheduleInfo, AccountingInfo accountingInfo,
		BookInfo bookInfo) {
		return new BookInfoResponseDTO(board, scheduleInfo, accountingInfo, bookInfo);
	}

}
