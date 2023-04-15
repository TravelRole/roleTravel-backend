package com.travel.role.domain.user.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RoomInfoNotFoundException extends RuntimeException{
    public RoomInfoNotFoundException(String msg) {super(msg);}
}

