package com.travel.role.domain.room.repository.querydsl;

import static com.travel.role.domain.room.entity.QRoom.*;
import static com.travel.role.domain.room.entity.QRoomParticipant.*;
import static com.travel.role.domain.user.entity.QUser.*;

import java.util.List;

import javax.persistence.EntityManager;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RoomParticipantQuerydslImpl implements RoomParticipantQuerydsl{

	private final JPAQueryFactory queryFactory;
	private final EntityManager em;

	@Override
	public boolean existsUserInRoom(String email, Long roomId) {
		Integer fetchOne = queryFactory
			.selectOne()
			.from(roomParticipant)
			.where(roomParticipant.user.email.eq(email), roomParticipant.room.id.eq(roomId))
			.fetchFirst();
		return fetchOne != null;
	}

	@Override
	public void deleteByRoomIdAndEmail(Long roomId, String email) {
		queryFactory
			.delete(roomParticipant)
			.where(roomParticipant.id.in(findIdsByRoomIdAndEmail(roomId, email)))
			.execute();

		em.flush();
		em.clear();
	}

	private List<Long> findIdsByRoomIdAndEmail(Long roomId, String email) {
		return queryFactory
			.select(roomParticipant.id)
			.from(roomParticipant, room, user)
			.join(roomParticipant.room, room)
			.join(roomParticipant.user, user)
			.where(roomParticipant.room.id.eq(roomId), roomParticipant.user.email.eq(email))
			.fetch();
	}
}
