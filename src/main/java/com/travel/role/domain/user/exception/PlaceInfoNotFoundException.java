package com.travel.role.domain.user.exception;

import com.travel.role.global.exception.ExceptionMessage;


public class PlaceInfoNotFoundException extends RuntimeException{
    public PlaceInfoNotFoundException() {super(ExceptionMessage.PLACE_NOT_FOUND);}
}
