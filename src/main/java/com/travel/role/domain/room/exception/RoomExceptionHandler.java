package com.travel.role.domain.room.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class RoomExceptionHandler  {

    @ExceptionHandler(StartdateBiggerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse StartdateBiggerHandler(Exception e) {
        return new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now());
    }

}
