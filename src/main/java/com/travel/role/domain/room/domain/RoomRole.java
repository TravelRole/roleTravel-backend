package com.travel.role.domain.room.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoomRole {
	ADMIN("ADMIN"),
	SCHEDULE("SCHEDULE"),
	ACCOUNTING("ACCOUNTING"),
	RESERVATION("RESERVATION"),
	NONE("NONE");

	private final String value;
}