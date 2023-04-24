package com.travel.role.domain.room.dto;

import com.travel.role.domain.room.entity.WantPlace;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WantPlaceDTO {
    private Long idx;
    private Long placeId;
    private String placeName;
    private String placeAddress;
    private String phoneNumber;
    private Double latitude;
    private Double longitude;

    private WantPlaceDTO(long idx, WantPlace wantPlace) {
        this.idx = idx;
        this.placeId = wantPlace.getId();
        this.placeName = wantPlace.getPlaceName();
        this.placeAddress = wantPlace.getPlaceAddress();
        this.phoneNumber = wantPlace.getPhoneNumber();
        this.latitude = wantPlace.getLatitude();
        this.longitude = wantPlace.getLongitude();
    }

    public static WantPlaceDTO of(long idx, WantPlace wantPlace) {
        return new WantPlaceDTO(idx, wantPlace);
    }
}
