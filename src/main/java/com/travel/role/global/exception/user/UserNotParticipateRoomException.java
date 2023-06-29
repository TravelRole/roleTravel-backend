package com.travel.role.global.exception.user;

import com.travel.role.global.exception.dto.ExceptionMessage;

public class UserNotParticipateRoomException extends RuntimeException{
    public UserNotParticipateRoomException() {super(ExceptionMessage.USER_NOT_PARTICIPATE_ROOM);}
}
