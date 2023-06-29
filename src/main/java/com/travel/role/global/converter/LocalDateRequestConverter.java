package com.travel.role.global.converter;

import java.time.LocalDate;

import org.springframework.core.convert.converter.Converter;

import com.travel.role.global.exception.dto.ExceptionMessage;
import com.travel.role.global.util.FormatterUtil;

public class LocalDateRequestConverter implements Converter<String, LocalDate> {

	@Override
	public LocalDate convert(String localDateStr) {

		return FormatterUtil.stringToLocalDate(localDateStr, "yyyy-MM-dd", ExceptionMessage.INVALID_DATE_FORMAT);
	}
}
