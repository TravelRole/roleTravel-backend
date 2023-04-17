package com.travel.role.domain.room.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class WantPlaceResponseDTO {
    private List<WantPlaceDTO> wantPlaces = new ArrayList<>();
    private boolean isScheduler;

    private WantPlaceResponseDTO(List<WantPlaceDTO> wantPlaces, boolean isScheduler) {
        this.wantPlaces = wantPlaces;
        this.isScheduler = isScheduler;
    }

    public static WantPlaceResponseDTO of(List<WantPlaceDTO> wantPlaces, boolean isScheduler) {
        return new WantPlaceResponseDTO(wantPlaces, isScheduler);
    }
}