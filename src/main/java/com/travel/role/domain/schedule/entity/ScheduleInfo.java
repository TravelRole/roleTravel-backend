package com.travel.role.domain.schedule.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.travel.role.domain.book.dto.request.BookInfoRequestDTO;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "schedule_info")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleInfo {
	@Id
	private Long id;

	@Column(nullable = false)
	private Double latitude;

	@Column(nullable = false)
	private Double longitude;

	@Column(name = "place_name", length = 100, nullable = false)
	private String placeName;

	@Column(name = "place_address", length = 100, nullable = false)
	private String placeAddress;

	@Column(name = "schedule_etc", length = 100)
	private String scheduleEtc;

	@Column(name = "map_place_id", nullable = false)
	private Long mapPlaceId;

	@MapsId
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id")
	private Board board;

	public static ScheduleInfo of(Board board, BookInfoRequestDTO bookInfoRequestDTO) {
		return new ScheduleInfo(board, bookInfoRequestDTO);
	}

	private ScheduleInfo(Board board, BookInfoRequestDTO bookInfoRequestDTO) {
		this.board = board;
		this.latitude = bookInfoRequestDTO.getLatitude();
		this.longitude = bookInfoRequestDTO.getLongitude();
		this.placeName = bookInfoRequestDTO.getPlaceName();
		this.placeAddress = bookInfoRequestDTO.getPlaceAddress();
		this.scheduleEtc = bookInfoRequestDTO.getEtc();
		this.mapPlaceId = bookInfoRequestDTO.getMapPlaceId();
	}
}
