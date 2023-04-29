package com.travel.role.domain.board.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Category {
    TRAFFIC("TRAFFIC"),
    ACCOMMODATION("ACCOMMODATION"),
    FOOD("FOOD"),
    TOUR("TOUR"),
    SHOPPING("SHOPPING"),
    ETC("ETC");

    private final String value;
}