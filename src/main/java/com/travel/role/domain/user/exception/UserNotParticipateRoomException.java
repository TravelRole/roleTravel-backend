package com.travel.role.domain.user.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserNotParticipateRoomException extends RuntimeException{
    public UserNotParticipateRoomException(String msg) {super(msg);}
}
