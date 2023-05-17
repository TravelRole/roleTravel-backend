package com.travel.role.domain.schedule.dto.response;

import java.time.LocalTime;

import com.travel.role.domain.accounting.entity.Category;
import com.travel.role.domain.schedule.entity.Board;

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

	private ScheduleResponseDTO(Board board) {
		this.id = board.getScheduleInfo().getId();
		this.placeName = board.getScheduleInfo().getPlaceName();
		this.time = board.getScheduleDate().toLocalTime();
		this.category = board.getCategory();
		this.link = board.getLink();
		this.etc = board.getScheduleInfo().getScheduleEtc();
		this.price = board.getAccountingInfo() != null ? board.getAccountingInfo().getPrice() : 0;
		this.latitude = board.getScheduleInfo().getLatitude();
		this.longitude = board.getScheduleInfo().getLongitude();
		this.mapPlaceId = board.getScheduleInfo().getMapPlaceId();
		this.isBooked = board.getAccountingInfo() != null ?
			board.getAccountingInfo().getBookInfo() != null ?
				board.getAccountingInfo().getBookInfo().getIsBooked() : null : null;
	}

	public static ScheduleResponseDTO from(Board board) {
		return new ScheduleResponseDTO(board);
	}
}
