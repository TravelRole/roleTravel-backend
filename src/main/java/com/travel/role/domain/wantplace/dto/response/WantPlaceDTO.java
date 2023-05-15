package com.travel.role.domain.wantplace.dto.response;

import com.travel.role.domain.wantplace.entity.WantPlace;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WantPlaceDTO {
	private Long placeId;
	private String placeName;
	private String placeAddress;
	private String phoneNumber;
	private Double latitude;
	private Double longitude;
	private String category;
	private String lotNumberAddress;
	private Long mapPlaceId;

	private WantPlaceDTO(WantPlace wantPlace) {
		this.placeId = wantPlace.getId();
		this.placeName = wantPlace.getPlaceName();
		this.placeAddress = wantPlace.getPlaceAddress();
		this.phoneNumber = wantPlace.getPhoneNumber();
		this.latitude = wantPlace.getLatitude();
		this.longitude = wantPlace.getLongitude();
		this.category = wantPlace.getCategory();
		this.lotNumberAddress = wantPlace.getLotNumberAddress();
		this.mapPlaceId = wantPlace.getMapPlaceId();
	}

	public static WantPlaceDTO from(WantPlace wantPlace) {
		return new WantPlaceDTO(wantPlace);
	}
}
