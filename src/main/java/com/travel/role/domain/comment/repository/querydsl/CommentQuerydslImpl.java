package com.travel.role.domain.comment.repository.querydsl;

import static com.querydsl.core.group.GroupBy.*;
import static com.travel.role.domain.comment.entity.QComment.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travel.role.domain.comment.dto.response.CommentResDTO;
import com.travel.role.domain.comment.entity.QComment;
import com.travel.role.domain.room.entity.QParticipantRole;
import com.travel.role.domain.room.entity.RoomRole;
import com.travel.role.domain.user.dto.SimpleUserInfoResDTO;
import com.travel.role.domain.user.entity.QUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentQuerydslImpl implements CommentQuerydsl {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<CommentResDTO> findAllOrderByGroupIdAndCreateDate(Long roomId) {

		QComment c = new QComment("c");
		QParticipantRole p = new QParticipantRole("p");
		QUser fu = new QUser("fu");
		QUser tu = new QUser("tu");

		List<Tuple> tuples = queryFactory.select(
				c.id,
				c.content,
				Projections.bean(SimpleUserInfoResDTO.class, fu.id, fu.name, fu.profile),
				tu.name,
				c.createDate,
				c.deleted
			).from(c)
			.innerJoin(c.fromUser, fu)
			.leftJoin(c.toUser, tu)
			.where(c.room.id.eq(roomId))
			.orderBy(c.groupId.asc())
			.orderBy(c.createDate.asc())
			.fetch();

		Map<Long, List<RoomRole>> roleMap = queryFactory
			.from(p)
			.where(p.room.id.eq(roomId))
			.transform(groupBy(p.user.id).as(list(p.roomRole)));

		return tuples.stream().
			map(
				tuple -> {
					SimpleUserInfoResDTO simpleUserInfoResDTO = tuple.get(2, SimpleUserInfoResDTO.class);
					setRoles(roleMap, simpleUserInfoResDTO);

					return new CommentResDTO(tuple.get(c.id)
						, tuple.get(c.content),
						simpleUserInfoResDTO,
						tuple.get(tu.name),
						tuple.get(c.createDate),
						Boolean.TRUE.equals(tuple.get(c.deleted)));
				}
			).collect(Collectors.toList());
	}

	@Override
	public void dynamicDeleteById(Long commentId) {

		Long toUserId = queryFactory.select(comment.toUser.id)
			.from(comment)
			.where(comment.id.eq(commentId))
			.fetchOne();

		if (toUserId == null) {
			// 최상단 댓글이면 update
			queryFactory.update(comment)
				.set(comment.content, "")
				.set(comment.deleted, true)
				.where(comment.id.eq(commentId))
				.execute();
		} else {
			// 자식 댓글이면 delete
			queryFactory.delete(comment)
				.where(comment.id.eq(commentId))
				.execute();
		}
	}

	private void setRoles(Map<Long, List<RoomRole>> roleMap, SimpleUserInfoResDTO resDTO) {

		Long userId = resDTO.getId();
		resDTO.setRoles(roleMap.getOrDefault(userId, List.of()));
	}
}
