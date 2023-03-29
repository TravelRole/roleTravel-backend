package com.travel.role.global.auth.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.travel.role.global.exception.ExceptionResponse;

@RestControllerAdvice
public class AuthExceptionHandler {

	@ExceptionHandler(NotExistTokenException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ExceptionResponse notExistTokenHandler(Exception e) {
		return new ExceptionResponse(e.getMessage(), HttpStatus.UNAUTHORIZED, LocalDateTime.now());
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ExceptionResponse usernameNotFoundHandler(Exception e) {
		return new ExceptionResponse(e.getMessage(), HttpStatus.UNAUTHORIZED, LocalDateTime.now());
	}

	@ExceptionHandler(InvalidTokenException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ExceptionResponse invalidTokenHandler(Exception e) {
		return new ExceptionResponse(e.getMessage(), HttpStatus.UNAUTHORIZED, LocalDateTime.now());
	}

	@ExceptionHandler(BadCredentialsException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ExceptionResponse badCredentialUserHandler(Exception e) {
		return new ExceptionResponse(e.getMessage(), HttpStatus.UNAUTHORIZED, LocalDateTime.now());
	}
}
