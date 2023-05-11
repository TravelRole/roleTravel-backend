package com.travel.role.domain.accounting.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PaymentMethod {
	CARD,
	CREDIT;

	@JsonCreator
	public static PaymentMethod from(String paymentMethod) {
		try {
			return PaymentMethod.valueOf(paymentMethod.toUpperCase());
		} catch (NullPointerException | IllegalArgumentException e) {
			return null;
		}
	}
}
