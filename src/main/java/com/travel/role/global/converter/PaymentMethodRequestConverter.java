package com.travel.role.global.converter;

import org.springframework.core.convert.converter.Converter;

import com.travel.role.domain.accounting.entity.PaymentMethod;

public class PaymentMethodRequestConverter implements Converter<String, PaymentMethod> {

	@Override
	public PaymentMethod convert(String paymentMethodStr) {
		return PaymentMethod.from(paymentMethodStr);
	}

}
