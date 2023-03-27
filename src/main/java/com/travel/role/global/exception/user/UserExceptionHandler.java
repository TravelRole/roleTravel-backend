package com.travel.role.global.exception.user;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.travel.role.global.exception.ExceptionResponse;
import com.travel.role.global.exception.user.AlreadyExistUserException;

@RestControllerAdvice
public class UserExceptionHandler {

	@ExceptionHandler(AlreadyExistUserException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ExceptionResponse alreadyExistUserHandler(Exception e) {
		return new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now());
	}
}
