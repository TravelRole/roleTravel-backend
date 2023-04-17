package com.travel.role.domain.user.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PlaceInfoNotFoundException extends RuntimeException{
    public PlaceInfoNotFoundException(String msg) {super(msg);}
}
