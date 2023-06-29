package com.travel.role.domain.travelessential.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum EssentialCategory {

	ESSENTIAL, ABOARD, CLOTHES, TOILETRIES, MEDICINE, SEASONAL, COOKWARE, ETC;

	@JsonCreator
	public static EssentialCategory from(String category) {
		try {
			return EssentialCategory.valueOf(category.toUpperCase());
		} catch (NullPointerException | IllegalArgumentException e) {
			return null;
		}
	}
}
