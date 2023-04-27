package com.travel.role.global.exception.user;

import com.travel.role.global.exception.dto.ExceptionMessage;


public class PlaceInfoNotFoundException extends RuntimeException{
    public PlaceInfoNotFoundException() {super(ExceptionMessage.PLACE_NOT_FOUND);}
}
