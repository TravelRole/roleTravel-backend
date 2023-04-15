package com.travel.role.domain.room.dto;

import com.travel.role.global.exception.ExceptionMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WantPlaceRequestDTO {
    @NotNull(message = ExceptionMessage.VALUE_NOT_EMPTY)
    private Long roomId;

    @NotBlank(message = ExceptionMessage.VALUE_NOT_EMPTY)
    private String placeName;

    @NotBlank(message = ExceptionMessage.VALUE_NOT_EMPTY)
    private String placeAddress;

    private String phoneNumber;

    @NotNull(message = ExceptionMessage.VALUE_NOT_EMPTY)
    private Double latitude;

    @NotNull(message = ExceptionMessage.VALUE_NOT_EMPTY)
    private Double longitude;
}
