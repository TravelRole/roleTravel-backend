package com.travel.role.domain.room.dao;

import static com.travel.role.domain.room.domain.QParticipantRole.*;
import static com.travel.role.domain.room.domain.QRoom.*;
import static com.travel.role.domain.room.domain.QRoomParticipant.*;
import static com.travel.role.domain.user.domain.QUser.*;

import java.util.List;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travel.role.domain.room.domain.QRoom;
import com.travel.role.domain.room.domain.QRoomParticipant;
import com.travel.role.domain.room.domain.RoomRole;
import com.travel.role.domain.user.domain.QUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RoomQuerydslImpl implements RoomQuerydsl {
	private final JPAQueryFactory queryFactory;

	@Override
	public List<Tuple> getMemberInRoom(String email) {
		QUser findUser = new QUser("findUser");
		QRoomParticipant findParticipant = new QRoomParticipant("findParticipant");
		QRoom findRoom = new QRoom("findRoom");

		return queryFactory
			.selectDistinct(room, user)
			.from(room, roomParticipant, user)
			.join(roomParticipant.user, user)
			.join(roomParticipant.room, room)
			.where(roomParticipant.room.in(
				JPAExpressions
					.select(findRoom)
					.from(findUser, findParticipant, findRoom)
					.join(findParticipant.room, room)
					.join(findParticipant.user, user)
					.where(user.email.eq(email))
			)).orderBy(room.createDate.desc())
			.fetch();
	}

	@Override
	public List<RoomRole> getRoomRole(String email, Long roomId) {
		return queryFactory
			.select(participantRole.roomRole)
			.from(roomParticipant, participantRole)
			.join(participantRole.roomParticipant, roomParticipant)
			.where(roomParticipant.user.email.eq(email), roomParticipant.room.id.eq(roomId))
			.fetch();
	}

}
