package com.travel.role.domain.room.repository.querydsl;

import static com.travel.role.domain.room.entity.QParticipantRole.*;
import static com.travel.role.domain.room.entity.QRoom.*;
import static com.travel.role.domain.user.entity.QUser.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travel.role.domain.room.entity.RoomRole;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ParticipantRoleQuerydslImpl implements ParticipantRoleQuerydsl{

	private final JPAQueryFactory queryFactory;

	@Override
	public boolean existsByUserEmailAndRoomIdAndRole(String email, Long roomId, RoomRole role) {
		Integer fetchOne = queryFactory
			.selectOne()
			.from(participantRole)
			.join(participantRole.room, room)
			.join(participantRole.user, user)
			.where(participantRole.user.email.eq(email),
				participantRole.room.id.eq(roomId),
				participantRole.roomRole.eq(role))
			.fetchFirst();

		return fetchOne != null;
	}
}
