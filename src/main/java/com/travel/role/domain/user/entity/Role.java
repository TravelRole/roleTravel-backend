package com.travel.role.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {

	ADMIN("ROLE_ADMIN"),
	USER("ROLE_USER");

	private final String roleValue;
}
