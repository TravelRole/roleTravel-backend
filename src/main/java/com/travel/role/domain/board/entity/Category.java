package com.travel.role.domain.board.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Category {
    TRAFFIC(),
    ACCOMMODATION(),
    FOOD(),
    TOUR(),
    SHOPPING(),
    ETC();

}