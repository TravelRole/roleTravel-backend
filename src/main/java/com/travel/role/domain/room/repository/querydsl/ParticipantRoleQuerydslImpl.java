package com.travel.role.domain.room.repository.querydsl;

import static com.travel.role.domain.room.entity.QParticipantRole.*;

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
			.join(participantRole.room).fetchJoin()
			.join(participantRole.user).fetchJoin()
			.where(participantRole.user.email.eq(email),
				participantRole.room.id.eq(roomId),
				participantRole.roomRole.eq(role))
			.fetchFirst();

		return fetchOne != null;
	}
}
