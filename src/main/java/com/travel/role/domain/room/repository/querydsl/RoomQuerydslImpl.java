package com.travel.role.domain.room.repository.querydsl;

import static com.travel.role.domain.room.entity.QRoom.*;
import static com.travel.role.domain.room.entity.QRoomParticipant.*;
import static com.travel.role.domain.user.entity.QUser.*;

import java.util.List;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travel.role.domain.room.entity.QRoom;
import com.travel.role.domain.room.entity.QRoomParticipant;
import com.travel.role.domain.room.repository.querydsl.RoomQuerydsl;
import com.travel.role.domain.user.entity.QUser;

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
}
