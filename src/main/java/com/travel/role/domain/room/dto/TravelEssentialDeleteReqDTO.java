package com.travel.role.domain.room.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TravelEssentialDeleteReqDTO {

	private List<Long> ids = new ArrayList<>();
}
