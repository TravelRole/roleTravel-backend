package com.travel.role.global.exception;

import com.travel.role.domain.room.exception.InvalidLocalDateException;
import com.travel.role.domain.user.exception.*;
import com.travel.role.global.auth.exception.InvalidTokenException;
import com.travel.role.global.auth.exception.NotExistTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.amazonaws.SdkClientException;
import com.travel.role.domain.room.exception.InvalidLocalDateException;
import com.travel.role.domain.user.exception.AlreadyExistUserException;
import com.travel.role.domain.user.exception.InputValueNotMatchException;
import com.travel.role.domain.user.exception.UserInfoNotFoundException;
import com.travel.role.global.auth.exception.InvalidTokenException;
import com.travel.role.global.auth.exception.NotExistTokenException;
import javax.mail.SendFailedException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

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

	@ExceptionHandler(AlreadyExistUserException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ExceptionResponse alreadyExistUserHandler(Exception e) {
		return new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now());
	}

	@ExceptionHandler(SendFailedException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ExceptionResponse sendMailFailedHandler(Exception e) {
		return new ExceptionResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now());
	}

	@ExceptionHandler(UserInfoNotFoundException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ExceptionResponse userInfoNotFoundHandler(Exception e) {
		return new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ExceptionResponse methodArgumentNotValidHandler(MethodArgumentNotValidException e) {
		return new ExceptionResponse(e.getBindingResult().getAllErrors().get(0).getDefaultMessage()
			, HttpStatus.BAD_REQUEST, LocalDateTime.now());
	}

	@ExceptionHandler(InvalidLocalDateException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ExceptionResponse invalidLocalDateException(Exception e) {
		return new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now());
	}

    @ExceptionHandler(UserNotParticipateRoomException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionResponse userNotParticipateRoomHandler(Exception e) {
        return new ExceptionResponse(e.getMessage(), HttpStatus.UNAUTHORIZED, LocalDateTime.now());
    }

    @ExceptionHandler(InputValueNotMatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse inputValueNotMatchException(InputValueNotMatchException e) {
        return new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now());
    }

	@ExceptionHandler(SdkClientException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ExceptionResponse sdkClientException(SdkClientException e) {
		return new ExceptionResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now());
	}

	@ExceptionHandler(S3ImageNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ExceptionResponse s3ImageNotFoundException(S3ImageNotFoundException e) {
		return new ExceptionResponse(e.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now());
	}
}
