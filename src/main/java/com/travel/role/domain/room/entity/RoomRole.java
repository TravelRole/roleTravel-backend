package com.travel.role.domain.room.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
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

	public static boolean isAdmin(RoomRole roomRole) {
		return roomRole == RoomRole.ADMIN;
	}

	@JsonValue
	public String getResponseValue() {
		return this.responseValue;
	}

	public static List<RoomRole> getAccountingRoles() {

		return List.of(ACCOUNTING, ADMIN);
	}

	public static List<RoomRole> getScheduleRoles() {

		return List.of(SCHEDULE, ADMIN);
	}

	public static List<RoomRole> getReservationRoles() {

		return List.of(RESERVATION, ADMIN);
	}

	@JsonCreator
	public static RoomRole from(String roomRole) {
		try {
			for (RoomRole role : RoomRole.values()) {
				if (role.responseValue.equals(roomRole)) {
					return role;
				}
			}
			return null;
		} catch (NullPointerException | IllegalArgumentException e) {
			return null;
		}
	}
}
