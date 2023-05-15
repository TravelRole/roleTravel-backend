package com.travel.role.domain.schedule.dto.response;

import java.time.LocalTime;

import com.travel.role.domain.accounting.entity.AccountingInfo;
import com.travel.role.domain.accounting.entity.Category;
import com.travel.role.domain.board.entity.Board;
import com.travel.role.domain.board.entity.BookInfo;
import com.travel.role.domain.schedule.entity.ScheduleInfo;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScheduleResponseDTO {
	private Long id;
	private String placeName;
	private LocalTime time;
	private Boolean isBooked;
	private Category category;
	private Integer price;
	private String link;
	private String etc;
	private Double latitude;
	private Double longitude;
	private Long mapPlaceId;

	private ScheduleResponseDTO(Board board, ScheduleInfo scheduleInfo, AccountingInfo accountingInfo,
		BookInfo bookInfo) {
		this.id = scheduleInfo.getId();
		this.placeName = scheduleInfo.getPlaceName();
		this.time = board.getScheduleDate().toLocalTime();
		this.category = board.getCategory();
		this.link = board.getLink();
		this.etc = scheduleInfo.getScheduleEtc();
		this.price = accountingInfo != null ? accountingInfo.getPrice() : 0;
		this.latitude = scheduleInfo.getLatitude();
		this.longitude = scheduleInfo.getLongitude();
		this.mapPlaceId = scheduleInfo.getMapPlaceId();
		this.isBooked = accountingInfo != null ?
			accountingInfo.getBookInfo() != null ?
				accountingInfo.getBookInfo().getIsBooked() : null : null;
	}

	public static ScheduleResponseDTO of(Board board, ScheduleInfo scheduleInfo, AccountingInfo accountingInfo,
		BookInfo bookInfo) {
		return new ScheduleResponseDTO(board, scheduleInfo, accountingInfo, bookInfo);
	}
}
