package com.travel.role.domain.room.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoomRole {
	ADMIN,
	SCHEDULE,
	ACCOUNTING,
	RESERVATION,
	NONE;
}
