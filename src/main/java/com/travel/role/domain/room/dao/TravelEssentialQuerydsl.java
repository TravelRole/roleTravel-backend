package com.travel.role.domain.room.dao;

import java.util.List;
import java.util.Map;

import com.travel.role.domain.room.domain.EssentialCategory;
import com.travel.role.domain.room.dto.TravelEssentialResDTO;

public interface TravelEssentialQuerydsl {

	Map<EssentialCategory, List<TravelEssentialResDTO>> readAllGroupByEssentialCategory(Long userId, Long roomId);
}
