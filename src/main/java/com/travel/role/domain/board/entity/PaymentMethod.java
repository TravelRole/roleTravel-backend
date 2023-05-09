package com.travel.role.domain.board.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PaymentMethod {
    CARD(),
    CREDIT();
}
