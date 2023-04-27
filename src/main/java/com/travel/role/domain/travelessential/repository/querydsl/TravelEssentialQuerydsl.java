package com.travel.role.domain.travelessential.repository.querydsl;

import java.util.List;
import java.util.Map;

import com.travel.role.domain.travelessential.entity.EssentialCategory;
import com.travel.role.domain.travelessential.dto.response.TravelEssentialResDTO;

public interface TravelEssentialQuerydsl {

	Map<EssentialCategory, List<TravelEssentialResDTO>> readAllGroupByEssentialCategory(Long userId, Long roomId);
}
