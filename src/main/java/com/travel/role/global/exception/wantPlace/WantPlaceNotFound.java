package com.travel.role.global.exception.wantPlace;

import com.travel.role.global.exception.dto.ExceptionMessage;

public class WantPlaceNotFound extends RuntimeException {
	public WantPlaceNotFound() {
		super(ExceptionMessage.WANT_PLACE_NOT_FOUND);
	}
}
