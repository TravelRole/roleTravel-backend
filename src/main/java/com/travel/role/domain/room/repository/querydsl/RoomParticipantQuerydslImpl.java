package com.travel.role.domain.room.repository.querydsl;

import static com.travel.role.domain.room.entity.QRoomParticipant.*;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RoomParticipantQuerydslImpl implements RoomParticipantQuerydsl{

	private final JPAQueryFactory queryFactory;

	@Override
	public boolean existsUserInRoom(String email, Long roomId) {
		Integer fetchOne = queryFactory
			.selectOne()
			.from(roomParticipant)
			.where(roomParticipant.user.email.eq(email), roomParticipant.room.id.eq(roomId))
			.fetchFirst();
		return fetchOne != null;
	}
}
