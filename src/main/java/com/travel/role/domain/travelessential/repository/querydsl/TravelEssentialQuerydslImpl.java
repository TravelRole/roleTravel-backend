package com.travel.role.domain.travelessential.repository.querydsl;

import static com.querydsl.core.group.GroupBy.*;
import static com.travel.role.domain.travelessential.entity.QTravelEssential.*;

import java.util.List;
import java.util.Map;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travel.role.domain.travelessential.entity.EssentialCategory;
import com.travel.role.domain.travelessential.dto.response.TravelEssentialResDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TravelEssentialQuerydslImpl implements TravelEssentialQuerydsl {

	private final JPAQueryFactory queryFactory;

	@Override
	public Map<EssentialCategory, List<TravelEssentialResDTO>> readAllGroupByEssentialCategory(Long userId,
		Long roomId) {

		return queryFactory.from(travelEssential)
			.where(travelEssential.user.id.eq(userId)
				.and(travelEssential.room.id.eq(roomId)))
			.transform(groupBy(travelEssential.category).as(
				list(Projections.bean(TravelEssentialResDTO.class, travelEssential.id, travelEssential.itemName, travelEssential.isChecked))));
	}
}
