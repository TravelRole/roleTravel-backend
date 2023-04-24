package com.travel.role.domain.comment.service;

import static com.travel.role.global.exception.common.ResourceOperationAccessDeniedException.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.comment.repository.CommentRepository;
import com.travel.role.domain.comment.dto.CommentListResDTO;
import com.travel.role.domain.comment.dto.CommentReqDTO;
import com.travel.role.domain.comment.dto.CommentResDTO;
import com.travel.role.domain.comment.entity.Comment;
import com.travel.role.global.exception.comment.CommentInfoNotFoundException;
import com.travel.role.domain.room.repository.RoomParticipantRepository;
import com.travel.role.domain.room.repository.RoomRepository;
import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.user.repository.UserRepository;
import com.travel.role.domain.user.entity.User;
import com.travel.role.global.exception.room.RoomInfoNotFoundException;
import com.travel.role.global.exception.user.UserInfoNotFoundException;
import com.travel.role.global.exception.user.UserNotParticipateRoomException;
import com.travel.role.global.exception.common.ResourceOperationAccessDeniedException;

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
		checkUserInRoom(loginUser, room);

		Comment newComment = createComment(parentId, loginUser, room, reqDTO.getContent());

		commentRepository.save(newComment);
	}

	@Transactional(readOnly = true)
	public CommentListResDTO getComments(String email, Long roomId, Long parentId) {

		User loginUser = findUserByEmailOrElseThrow(email);
		Room room = findRoomByIdOrElseThrow(roomId);
		checkUserInRoom(loginUser, room);

		List<Comment> comments = getComments(parentId);

		return CommentListResDTO.from(comments.stream()
			.map(CommentResDTO::fromComment)
			.collect(Collectors.toList()));
	}

	public void modifyComment(String email, Long roomId, Long commentId, CommentReqDTO reqDTO) {

		User loginUser = findUserByEmailOrElseThrow(email);
		Room room = findRoomByIdOrElseThrow(roomId);
		Comment comment = findCommentByIdOrElseThrow(commentId);

		checkUserInRoom(loginUser, room);
		checkAuthorizationForComment(comment, loginUser.getId(), Operation.MODIFY);

		comment.update(reqDTO.getContent());
	}

	public void deleteComment(String email, Long roomId, Long commentId) {

		User loginUser = findUserByEmailOrElseThrow(email);
		Room room = findRoomByIdOrElseThrow(roomId);
		Comment comment = findCommentByIdOrElseThrow(commentId);

		checkUserInRoom(loginUser, room);
		checkAuthorizationForComment(comment, loginUser.getId(), Operation.DELETE);

		commentRepository.deleteAllByGroupIdAndDepth(comment.getGroupId(), comment.getDepth());
	}

	private Comment createComment(Long parentId, User loginUser, Room room, String content) {

		Comment newComment;

		if (parentId == null) {
			newComment = Comment.ofParent(loginUser, room, content);
			newComment = commentRepository.save(newComment);
			newComment.setGroupId(newComment.getId());
		} else {
			Comment parentComment = findCommentByIdOrElseThrow(parentId);
			newComment = Comment.ofChild(loginUser, room, parentComment, content);
		}

		return newComment;
	}

	private List<Comment> getComments(Long parentId) {

		List<Comment> comments;

		if (parentId == null) {
			comments = commentRepository.findAllFirstDepthComments();
		} else {
			comments = commentRepository.findAllChildCommentsByParentId(parentId);
		}

		return comments;
	}

	private void checkAuthorizationForComment(Comment comment, Long userId, Operation operation) {

		if (!Objects.equals(comment.getUser().getId(), userId)) {
			throw new ResourceOperationAccessDeniedException(Resource.COMMENT, operation);
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
