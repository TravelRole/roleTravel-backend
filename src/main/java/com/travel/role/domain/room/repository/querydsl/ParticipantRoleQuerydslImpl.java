package com.travel.role.domain.room.repository.querydsl;

import static com.travel.role.domain.room.entity.QParticipantRole.*;
import static com.travel.role.domain.room.entity.QRoom.*;
import static com.travel.role.domain.user.entity.QUser.*;

import java.util.List;

import javax.persistence.EntityManager;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travel.role.domain.room.entity.RoomRole;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ParticipantRoleQuerydslImpl implements ParticipantRoleQuerydsl{

	private final JPAQueryFactory queryFactory;
	private final EntityManager em;

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

	@Override
	public void deleteByRoomIdAndEmail(Long roomId, String email) {
		queryFactory
			.delete(participantRole)
			.where(participantRole.id.in(findIdsByRoomIdAndEmail(roomId, email)))
			.execute();

		em.flush();
		em.clear();
	}

	private List<Long> findIdsByRoomIdAndEmail(Long roomId, String email) {
		return queryFactory
			.select(participantRole.id)
			.from(participantRole, user, room)
			.join(participantRole.user, user)
			.join(participantRole.room, room)
			.where(participantRole.room.id.eq(roomId), participantRole.user.email.eq(email))
			.fetch();
	}
}
