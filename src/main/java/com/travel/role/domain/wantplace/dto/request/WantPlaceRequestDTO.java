package com.travel.role.domain.wantplace.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.travel.role.global.exception.dto.ExceptionMessage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WantPlaceRequestDTO {
	@NotNull(message = ExceptionMessage.ROOM_ID_VALUE_NOT_EMPTY)
	private Long roomId;

	@NotBlank(message = ExceptionMessage.PLACE_NAME_VALUE_NOT_EMPTY)
	private String placeName;

	@NotBlank(message = ExceptionMessage.PLACE_ADDRESS_VALUE_NOT_EMPTY)
	private String placeAddress;

	private String phoneNumber;

	@NotNull(message = ExceptionMessage.LATITUDE_VALUE_NOT_EMPTY)
	private Double latitude;

	@NotNull(message = ExceptionMessage.LONGITUDE_NOT_EMPTY)
	private Double longitude;

	private String category;

	@NotBlank(message = ExceptionMessage.PLACE_ADDRESS_VALUE_NOT_EMPTY)
	private String lotNumberAddress;

	@NotNull(message = ExceptionMessage.MAP_PLACE_ID_VALUE_NOT_EMPTY)
	private Long mapPlaceId;
}
