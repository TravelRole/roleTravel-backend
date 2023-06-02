package com.travel.role.domain.wantplace.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.wantplace.dto.request.WantPlaceRequestDTO;
import com.travel.role.global.domain.BaseTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "want_place")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WantPlace extends BaseTime {

	@Id
	@Column(name = "place_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id", nullable = false, updatable = false)
	private Room room;

	@Column(name = "place_name", length = 100, nullable = false)
	private String placeName;

	@Column(name = "place_address", length = 100)
	private String placeAddress;

	@Column(name = "phone_number", length = 20)
	private String phoneNumber;

	@Column(nullable = false)
	private Double latitude;

	@Column(nullable = false)
	private Double longitude;

	@Column(length = 100, nullable = false)
	private String category;

	@Column(name = "lot_number_address", length = 100, nullable = false)
	private String lotNumberAddress;

	@Column(name = "map_place_id", nullable = false)
	private Long mapPlaceId;

	@Column
	private String link;

	private WantPlace(Room room, WantPlaceRequestDTO wantPlaceRequestDTO) {
		this.room = room;
		this.placeName = wantPlaceRequestDTO.getPlaceName();
		this.placeAddress = wantPlaceRequestDTO.getPlaceAddress();
		this.phoneNumber = wantPlaceRequestDTO.getPhoneNumber();
		this.latitude = wantPlaceRequestDTO.getLatitude();
		this.longitude = wantPlaceRequestDTO.getLongitude();
		this.category = wantPlaceRequestDTO.getCategory();
		this.lotNumberAddress = wantPlaceRequestDTO.getLotNumberAddress();
		this.mapPlaceId = wantPlaceRequestDTO.getMapPlaceId();
		this.link = wantPlaceRequestDTO.getLink();
	}

	public static WantPlace of(Room room, WantPlaceRequestDTO wantPlaceRequestDTO) {
		return new WantPlace(room, wantPlaceRequestDTO);
	}
}
