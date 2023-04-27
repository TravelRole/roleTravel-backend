package com.travel.role.domain.room.dao;

import static com.querydsl.core.group.GroupBy.*;
import static com.travel.role.domain.room.domain.QTravelEssentials.*;

import java.util.List;
import java.util.Map;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travel.role.domain.room.domain.EssentialCategory;
import com.travel.role.domain.room.dto.TravelEssentialResDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TravelEssentialQuerydslImpl implements TravelEssentialQuerydsl {

	private final JPAQueryFactory queryFactory;

	@Override
	public Map<EssentialCategory, List<TravelEssentialResDTO>> readAllGroupByEssentialCategory(Long userId,
		Long roomId) {

		Map<EssentialCategory, List<TravelEssentialResDTO>> result = queryFactory.from(travelEssentials)
			.where(travelEssentials.user.id.eq(userId)
				.and(travelEssentials.room.id.eq(roomId)))
			.transform(groupBy(travelEssentials.category).as(
				list(Projections.bean(TravelEssentialResDTO.class, travelEssentials.id, travelEssentials.itemName, travelEssentials.isChecked))));

		return result;
	}
}
