package com.travel.role.domain.room.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class StartdateBiggerException extends RuntimeException {
    public StartdateBiggerException(String msg){
        super(msg);
    }

}
