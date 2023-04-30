package com.travel.role.domain.room.entity;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoomRole {
	ADMIN("ADMIN", "총무"),
	SCHEDULE("SCHEDULE", "일정"),
	ACCOUNTING("ACCOUNTING", "회계"),
	RESERVATION("RESERVATION", "예약"),
	NONE("NONE", "역할없음");

	private final String value;
	private final String responseValue;

	@JsonValue
	public String getResponseValue() {
		return this.responseValue;
	}
}
