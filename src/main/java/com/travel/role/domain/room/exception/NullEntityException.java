package com.travel.role.domain.room.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ValidateEntityException extends RuntimeException {
    public ValidateEntityException(String msg){
        super(msg);
    }

}
