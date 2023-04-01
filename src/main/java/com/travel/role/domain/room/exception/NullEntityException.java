package com.travel.role.domain.room.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NullEntityException extends RuntimeException {
    public NullEntityException(String msg){
        super(msg);
    }

}
