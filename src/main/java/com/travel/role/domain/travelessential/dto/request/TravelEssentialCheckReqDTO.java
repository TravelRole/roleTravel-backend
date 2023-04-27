package com.travel.role.domain.travelessential.dto.request;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TravelEssentialCheckReqDTO {

	private Boolean check;
	private List<Long> ids = new ArrayList<>();
}
