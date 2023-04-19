package com.travel.role.domain.comment.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.comment.dao.CommentRepository;
import com.travel.role.domain.comment.dto.CommentListResDTO;
import com.travel.role.domain.comment.dto.CommentReqDTO;
import com.travel.role.domain.comment.dto.CommentResDTO;
import com.travel.role.domain.comment.entity.Comment;
import com.travel.role.domain.comment.exception.CommentInfoNotFoundException;
import com.travel.role.domain.room.dao.RoomParticipantRepository;
import com.travel.role.domain.room.dao.RoomRepository;
import com.travel.role.domain.room.domain.Room;
import com.travel.role.domain.user.dao.UserRepository;
import com.travel.role.domain.user.domain.User;
import com.travel.role.domain.user.exception.RoomInfoNotFoundException;
import com.travel.role.domain.user.exception.UserInfoNotFoundException;
import com.travel.role.domain.user.exception.UserNotParticipateRoomException;
import com.travel.role.global.exception.ResourceOperationAccessDeniedException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

	private final UserRepository userRepository;
	private final RoomRepository roomRepository;
	private final CommentRepository commentRepository;
	private final RoomParticipantRepository roomParticipantRepository;

	public void createComment(String email, Long roomId, Long parentId, CommentReqDTO reqDTO) {

		User loginUser = findUserByEmailOrElseThrow(email);
		Room room = findRoomByIdOrElseThrow(roomId);
		Comment newComment;

		checkUserInRoom(loginUser, room);

		if (parentId == null) {
			newComment = Comment.ofParent(loginUser, room, reqDTO.getContent());
			newComment = commentRepository.save(newComment);
			newComment.setGroupId(newComment.getId());
		} else {
			Comment parentComment = findCommentByIdOrElseThrow(parentId);
			newComment = Comment.ofChild(loginUser, room, parentComment, reqDTO.getContent());
		}

		commentRepository.save(newComment);
	}

	@Transactional(readOnly = true)
	public CommentListResDTO getComments(String email, Long roomId, Long parentId) {

		User loginUser = findUserByEmailOrElseThrow(email);
		Room room = findRoomByIdOrElseThrow(roomId);
		checkUserInRoom(loginUser, room);

		List<Comment> comments;

		if (parentId == null) {
			comments = commentRepository.findAllFirstDepthComments();
		} else {
			comments = commentRepository.findAllChildCommentsByParentId(parentId);
		}

		return CommentListResDTO.from(comments.stream()
			.map(CommentResDTO::fromComment)
			.collect(Collectors.toList()));
	}

	public void modifyComment(String email, Long roomId, Long commentId, CommentReqDTO reqDTO) {

		User loginUser = findUserByEmailOrElseThrow(email);
		Room room = findRoomByIdOrElseThrow(roomId);
		Comment comment = findCommentByIdOrElseThrow(commentId);

		checkUserInRoom(loginUser, room);
		checkAuthorizationForComment(comment, loginUser.getId(), "수정");

		comment.update(reqDTO.getContent());
	}

	public void deleteComment(String email, Long roomId, Long commentId) {

		User loginUser = findUserByEmailOrElseThrow(email);
		Room room = findRoomByIdOrElseThrow(roomId);
		Comment comment = findCommentByIdOrElseThrow(commentId);

		checkUserInRoom(loginUser, room);
		checkAuthorizationForComment(comment, loginUser.getId(), "삭제");

		commentRepository.deleteAllByGroupIdAndDepth(comment.getGroupId(), comment.getDepth());
	}

	private void checkAuthorizationForComment(Comment comment, Long userId, String operation) {

		if (!Objects.equals(comment.getUser().getId(), userId)) {
			throw new ResourceOperationAccessDeniedException("댓글", operation);
		}
	}

	private void checkUserInRoom(User user, Room room) {

		if (!roomParticipantRepository.existsByUserAndRoom(user, room)) {
			throw new UserNotParticipateRoomException();
		}
	}

	private User findUserByEmailOrElseThrow(String email) {

		return userRepository.findByEmail(email)
			.orElseThrow(UserInfoNotFoundException::new);
	}

	private Room findRoomByIdOrElseThrow(Long roomId) {

		return roomRepository.findById(roomId)
			.orElseThrow(RoomInfoNotFoundException::new);
	}

	private Comment findCommentByIdOrElseThrow(Long commentId) {

		return commentRepository.findById(commentId)
			.orElseThrow(CommentInfoNotFoundException::new);
	}
}
