package com.travel.role.global.exception.dto;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionFilterResponse {
	private String message;
	private HttpStatus httpStatus;
	private String time;
}
