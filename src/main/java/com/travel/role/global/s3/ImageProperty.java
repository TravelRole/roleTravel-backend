package com.travel.role.global.s3;

import lombok.Getter;

@Getter
public enum ImageProperty {
	USER_PROFILE("회원 프로필");

	private final String propertyName;

	ImageProperty(String propertyName) {
		this.propertyName = propertyName;
	}
}
