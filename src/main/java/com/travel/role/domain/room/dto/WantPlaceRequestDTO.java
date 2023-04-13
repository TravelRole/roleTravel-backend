package com.travel.role.domain.room.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WantPlaceRequestDTO {

    @NotNull(message = "값이 비어있으면 안됩니다")
    private Long roomId;

    @NotBlank(message = "값이 비어있으면 안됩니다")
    private String placeName;

    @NotBlank(message = "값이 비어있으면 안됩니다")
    private String placeAddress;

    private String placeNumber;

    @NotNull(message = "값이 비어있으면 안됩니다")
    private Double latitude;

    @NotNull(message = "값이 비어있으면 안됩니다")
    private Double longitude;
}
