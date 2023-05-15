package com.travel.role.domain.board.dto.response;

import java.time.LocalTime;

import com.travel.role.domain.accounting.entity.Category;
import com.travel.role.domain.accounting.entity.PaymentMethod;
import com.travel.role.domain.board.entity.Board;

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

	private BookInfoResponseDTO(Board board) {
		this.placeName = board.getScheduleInfo().getPlaceName();
		this.time = board.getScheduleDate().toLocalTime();
		this.link = board.getLink();
		this.bookEtc = board.getAccountingInfo().getBookInfo().getBookEtc();
		this.price = board.getAccountingInfo().getPrice();
		this.paymentMethod = board.getAccountingInfo().getPaymentMethod();
		this.category = board.getCategory();
		this.accountingEtc = board.getAccountingInfo().getAccountingEtc();
		this.isBooked = board.getAccountingInfo().getBookInfo().getIsBooked();
		this.bookInfoId = board.getAccountingInfo().getBookInfo().getId();
		this.accountingId = board.getAccountingInfo().getId();
	}

	public static BookInfoResponseDTO from(Board board) {
		return new BookInfoResponseDTO(board);
	}

}
