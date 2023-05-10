package com.travel.role.domain.accounting.entity;

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