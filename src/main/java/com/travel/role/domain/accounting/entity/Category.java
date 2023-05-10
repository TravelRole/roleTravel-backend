package com.travel.role.domain.accounting.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Category {
	TRAFFIC,
	ACCOMMODATION,
	FOOD,
	TOUR,
	SHOPPING,
	ETC;

	@JsonCreator
	public static Category from(String category){
		try{
			return Category.valueOf(category.toUpperCase());
		}catch (NullPointerException | IllegalArgumentException e){
			return null;
		}
	}
}