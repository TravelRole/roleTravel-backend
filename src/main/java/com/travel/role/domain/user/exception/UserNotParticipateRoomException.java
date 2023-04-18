package com.travel.role.domain.user.exception;

import com.travel.role.global.exception.ExceptionMessage;

public class UserNotParticipateRoomException extends RuntimeException{
    public UserNotParticipateRoomException() {super(ExceptionMessage.USER_NOT_PARTICIPATE_ROOM);}
}
