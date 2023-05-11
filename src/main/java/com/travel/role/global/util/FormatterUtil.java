package com.travel.role.global.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.travel.role.global.exception.room.InvalidLocalDateException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class FormatterUtil {

	public static LocalDate stringToLocalDate(String str, String pattern, String exceptionMessage) {

		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
			return LocalDate.parse(str, formatter);
		} catch (DateTimeParseException e) {
			throw new InvalidLocalDateException(exceptionMessage);
		}
	}

	public static LocalDateTime stringToLocalDateTime(String str, String pattern, String exceptionMessage) {

		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
			return LocalDateTime.parse(str, formatter);
		} catch (DateTimeParseException e) {
			throw new InvalidLocalDateException(exceptionMessage);
		}
	}
}
