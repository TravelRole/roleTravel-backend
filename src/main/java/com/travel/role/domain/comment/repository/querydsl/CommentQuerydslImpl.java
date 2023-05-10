package com.travel.role.domain.comment.repository.querydsl;

import static com.querydsl.core.group.GroupBy.*;
import static com.travel.role.domain.comment.entity.QComment.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
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

	private static final QComment c = new QComment("c");
	private static final QUser fu = new QUser("fu");
	private static final QUser tu = new QUser("tu");

	@Override
	public Page<CommentResDTO> findAllOrderByGroupIdAndCreateDate(Long roomId, Pageable pageable) {

		// 먼저 부모 댓글을 tuple 타입으로 불러옴
		List<Tuple> parentTuples = findFirstDepthCommentTuples(roomId, pageable);

		// 부모 댓글들의 id 추출
		List<Long> parentGroupIds = parentTuples.stream().map(tuple -> tuple.get(c.id)).collect(Collectors.toList());

		// 각 유저들의 방에서의 역할 추출
		Map<Long, List<RoomRole>> roleMap = findRoleMapInRoom(roomId);

		// 해당 groupId를 가진 자식 댓글들을 tuple 형태로 불러옴
		List<Tuple> childCommentsTuple = findChildCommentTupleInGroupIds(roomId, parentGroupIds);

		// key - groupId, 불러온 댓글들을 그루핑
		Map<Long, List<CommentResDTO>> childMap = groupByGroupId(roleMap, childCommentsTuple);

		List<CommentResDTO> resDTOS = mappingToCommentResDTOList(parentTuples, roleMap, childMap);

		JPAQuery<Long> countQuery = queryFactory
			.select(c.count())
			.from(c)
			.where(c.room.id.eq(roomId), c.toUser.isNull());

		return PageableExecutionUtils.getPage(resDTOS, pageable, countQuery::fetchOne);
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

	private List<CommentResDTO> mappingToCommentResDTOList(List<Tuple> tuples, Map<Long, List<RoomRole>> roleMap,
		Map<Long, List<CommentResDTO>> childMap) {
		return tuples.stream().
			map(
				tuple -> {
					SimpleUserInfoResDTO simpleUserInfoResDTO = tuple.get(2, SimpleUserInfoResDTO.class);
					setRoles(roleMap, simpleUserInfoResDTO);

					Long commentId = tuple.get(c.id);
					List<CommentResDTO> childComments = childMap.getOrDefault(commentId, null);

					return convertToCommentResDTO(tuple, simpleUserInfoResDTO, childComments);
				}
			).collect(Collectors.toList());
	}

	private Map<Long, List<CommentResDTO>> groupByGroupId(Map<Long, List<RoomRole>> roleMap,
		List<Tuple> childCommentsTuple) {
		return childCommentsTuple.stream()
			.collect(Collectors.groupingBy(tuple -> tuple.get(c.groupId), Collectors.mapping(tuple -> {
				SimpleUserInfoResDTO simpleUserInfoResDTO = tuple.get(2, SimpleUserInfoResDTO.class);
				setRoles(roleMap, simpleUserInfoResDTO);

				return convertToCommentResDTO(tuple, simpleUserInfoResDTO, null);
			}, Collectors.toList())));
	}

	private List<Tuple> findChildCommentTupleInGroupIds(Long roomId, List<Long> parentGroupIds) {
		return queryFactory.select(
				c.id,
				c.content,
				Projections.bean(SimpleUserInfoResDTO.class, fu.id, fu.name, fu.profile),
				tu.name,
				c.createDate,
				c.deleted,
				c.groupId
			).from(c)
			.innerJoin(c.fromUser, fu)
			.leftJoin(c.toUser, tu)
			.where(c.room.id.eq(roomId), c.toUser.isNotNull(), c.groupId.in(parentGroupIds))
			.orderBy(c.groupId.asc())
			.orderBy(c.createDate.asc())
			.fetch();
	}

	private List<Tuple> findFirstDepthCommentTuples(Long roomId, Pageable pageable) {
		return queryFactory.select(
				c.id,
				c.content,
				Projections.bean(SimpleUserInfoResDTO.class, fu.id, fu.name, fu.profile),
				tu.name,
				c.createDate,
				c.deleted
			).from(c)
			.innerJoin(c.fromUser, fu)
			.leftJoin(c.toUser, tu)
			.where(c.room.id.eq(roomId), c.toUser.isNull())
			.orderBy(c.groupId.asc())
			.orderBy(c.createDate.asc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
	}

	private void setRoles(Map<Long, List<RoomRole>> roleMap, SimpleUserInfoResDTO resDTO) {

		Long userId = resDTO.getId();
		resDTO.setRoles(roleMap.getOrDefault(userId, List.of()));
	}

	private Map<Long, List<RoomRole>> findRoleMapInRoom(Long roomId) {

		QParticipantRole p = new QParticipantRole("p");

		return queryFactory
			.from(p)
			.where(p.room.id.eq(roomId))
			.transform(groupBy(p.user.id).as(list(p.roomRole)));
	}

	private CommentResDTO convertToCommentResDTO(Tuple tuple, SimpleUserInfoResDTO simpleUserInfoResDTO,
		List<CommentResDTO> childComments) {
		return CommentResDTO.builder()
			.commentId(tuple.get(c.id))
			.content(tuple.get(c.content))
			.fromUserInfo(simpleUserInfoResDTO)
			.toUsername(tuple.get(tu.name))
			.createdDate(tuple.get(c.createDate))
			.deleted(Boolean.TRUE.equals(tuple.get(c.deleted)))
			.childComments(childComments)
			.build();
	}
}
