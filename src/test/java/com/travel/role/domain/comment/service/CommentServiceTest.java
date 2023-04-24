package com.travel.role.domain.comment.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

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
import com.travel.role.global.exception.ExceptionMessage;
import com.travel.role.global.exception.common.ResourceOperationAccessDeniedException;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

	@InjectMocks
	private CommentService commentService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private RoomRepository roomRepository;

	@Mock
	private CommentRepository commentRepository;

	@Mock
	private RoomParticipantRepository roomParticipantRepository;

	@Test
	void depth가_0인_댓글_생성_성공() {
		// given
		User user = makeUser(1L);
		Room room = makeRoom();
		CommentReqDTO commentReqDTO = new CommentReqDTO("content");
		Comment comment = makeCommentAfterSaved(user, 1L, 1L, 0);
		given(userRepository.findByEmail(anyString()))
			.willReturn(Optional.of(user));
		given(roomRepository.findById(anyLong()))
			.willReturn(Optional.of(room));
		given(roomParticipantRepository.existsByUserAndRoom(any(User.class), any(Room.class)))
			.willReturn(true);
		given(commentRepository.save(any(Comment.class)))
			.willReturn(comment);

		// when
		commentService.createComment("kkk@naver.com", 1L, null, commentReqDTO);

		// then
		then(commentRepository).should(times(2)).save(any(Comment.class));
	}

	@Test
	void depth가_N인_댓글_생성_성공() {
		// given
		User user = makeUser(1L);
		Room room = makeRoom();
		CommentReqDTO commentReqDTO = new CommentReqDTO("content");
		Comment comment = makeCommentAfterSaved(user, 2L, 1L, 1);
		given(commentRepository.findById(anyLong()))
			.willReturn(Optional.of(comment));
		given(userRepository.findByEmail(anyString()))
			.willReturn(Optional.of(user));
		given(roomRepository.findById(anyLong()))
			.willReturn(Optional.of(room));
		given(roomParticipantRepository.existsByUserAndRoom(any(User.class), any(Room.class)))
			.willReturn(true);

		// when
		commentService.createComment("kkk@naver.com", 1L, 1L, commentReqDTO);

		// then
		then(commentRepository).should(times(1)).save(any(Comment.class));
	}

	@Test
	void 댓글_생성_실패_존재하지_않는_댓글에_대댓글을_작성할때() {
		// given
		User user = makeUser(1L);
		Room room = makeRoom();
		CommentReqDTO commentReqDTO = new CommentReqDTO("content");
		given(userRepository.findByEmail(anyString()))
			.willReturn(Optional.of(user));
		given(roomRepository.findById(anyLong()))
			.willReturn(Optional.of(room));
		given(roomParticipantRepository.existsByUserAndRoom(any(User.class), any(Room.class)))
			.willReturn(true);
		given(commentRepository.findById(anyLong()))
			.willReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> {
			commentService.createComment("kkk@naver.com", 1L, 1L, commentReqDTO);
		}).isInstanceOf(CommentInfoNotFoundException.class)
			.hasMessage(ExceptionMessage.COMMENT_NOT_FOUND);
	}

	@Test
	void 댓글_생성_실패_존재하지_않는_회원일때() {
		// given
		given(userRepository.findByEmail(anyString()))
			.willReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> {
			commentService.createComment("kkk@naver.com", 1L, null, null);
		}).isInstanceOf(UserInfoNotFoundException.class)
			.hasMessage(ExceptionMessage.USERNAME_NOT_FOUND);
	}

	@Test
	void 댓글_생성_실패_존재하지_않는_방일때() {
		// given
		User user = makeUser(1L);
		given(userRepository.findByEmail(anyString()))
			.willReturn(Optional.of(user));
		given(roomRepository.findById(anyLong()))
			.willReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> {
			commentService.createComment("kkk@naver.com", 1L, null, null);
		}).isInstanceOf(RoomInfoNotFoundException.class)
			.hasMessage(ExceptionMessage.ROOM_NOT_FOUND);
	}

	@Test
	void 댓글_생성_실패_방에_참가하지_않은_회원일때() {
		// given
		User user = makeUser(1L);
		Room room = makeRoom();
		given(userRepository.findByEmail(anyString()))
			.willReturn(Optional.of(user));
		given(roomRepository.findById(anyLong()))
			.willReturn(Optional.of(room));
		given(roomParticipantRepository.existsByUserAndRoom(any(User.class), any(Room.class)))
			.willReturn(false);

		// when & then
		assertThatThrownBy(() -> {
			commentService.createComment("kkk@naver.com", 1L, null, null);
		}).isInstanceOf(UserNotParticipateRoomException.class)
			.hasMessage(ExceptionMessage.USER_NOT_PARTICIPATE_ROOM);
	}

	@Test
	void depth가_0인_댓글_조회_성공() {

		//given
		User user = makeUser(1L);
		Room room = makeRoom();
		List<Comment> findZeroDepthComments = List.of(
			makeCommentAfterSaved(user, 1L, 1L, 0),
			makeCommentAfterSaved(user, 2L, 2L, 0)
		);
		given(userRepository.findByEmail(anyString()))
			.willReturn(Optional.of(user));
		given(roomRepository.findById(anyLong()))
			.willReturn(Optional.of(room));
		given(roomParticipantRepository.existsByUserAndRoom(any(User.class), any(Room.class)))
			.willReturn(true);
		given(commentRepository.findAllFirstDepthComments())
			.willReturn(findZeroDepthComments);

		//when
		CommentListResDTO resDTO = commentService.getComments("kkk@naver.com", 1L, null);

		//then
		List<CommentResDTO> commentResDTOS = resDTO.getComments();
		assertThat(commentResDTOS).hasSize(findZeroDepthComments.size());
		assertThat(commentResDTOS).extracting(CommentResDTO::getCommentId)
			.containsOnly(1L, 2L);
		assertThat(commentResDTOS).extracting(CommentResDTO::getContent)
			.containsOnly("content");
		assertThat(commentResDTOS).extracting(CommentResDTO::getUsername)
			.containsOnly("kkk");
		assertThat(commentResDTOS).extracting(CommentResDTO::getCreatedDate)
			.allMatch(createdDate -> LocalDateTime.now().isAfter(createdDate));
	}

	@Test
	void depth가_N인_댓글_조회_성공() {

		//given
		User user = makeUser(1L);
		Room room = makeRoom();
		Long parentId = 1L;
		List<Comment> findFirstDepthComments = List.of(
			makeCommentAfterSaved(user, 2L, 1L, 1),
			makeCommentAfterSaved(user, 3L, 1L, 1)
		);
		given(userRepository.findByEmail(anyString()))
			.willReturn(Optional.of(user));
		given(roomRepository.findById(anyLong()))
			.willReturn(Optional.of(room));
		given(roomParticipantRepository.existsByUserAndRoom(any(User.class), any(Room.class)))
			.willReturn(true);
		given(commentRepository.findAllChildCommentsByParentId(parentId))
			.willReturn(findFirstDepthComments);

		//when
		CommentListResDTO resDTO = commentService.getComments("kkk@naver.com", 1L, parentId);

		//then
		List<CommentResDTO> commentResDTOS = resDTO.getComments();
		assertThat(commentResDTOS).hasSize(findFirstDepthComments.size());
		assertThat(commentResDTOS).extracting(CommentResDTO::getCommentId)
			.containsOnly(2L, 3L);
		assertThat(commentResDTOS).extracting(CommentResDTO::getContent)
			.containsOnly("content");
		assertThat(commentResDTOS).extracting(CommentResDTO::getUsername)
			.containsOnly("kkk");
		assertThat(commentResDTOS).extracting(CommentResDTO::getCreatedDate)
			.allMatch(createdDate -> LocalDateTime.now().isAfter(createdDate));
	}

	@Test
	void 댓글_수정_성공() {
		//given
		User user = makeUser(1L);
		Room room = makeRoom();
		Comment comment = makeCommentAfterSaved(user, 1L, 1L, 0);
		CommentReqDTO commentReqDTO = new CommentReqDTO("content");
		given(userRepository.findByEmail(anyString()))
			.willReturn(Optional.of(user));
		given(roomRepository.findById(anyLong()))
			.willReturn(Optional.of(room));
		given(commentRepository.findById(anyLong()))
			.willReturn(Optional.of(comment));
		given(roomParticipantRepository.existsByUserAndRoom(any(User.class), any(Room.class)))
			.willReturn(true);

		//when & then
		assertThatNoException().isThrownBy(() ->
			commentService.modifyComment("kkk@naver.com", 1L, 1L, commentReqDTO));
	}

	@Test
	void 댓글_수정_실패_존재하지_않는_회원일때() {
		//given
		CommentReqDTO commentReqDTO = new CommentReqDTO("content");
		given(userRepository.findByEmail(anyString()))
			.willReturn(Optional.empty());

		//when & then
		assertThatThrownBy(() -> {
			commentService.modifyComment("kkk@naver.com", 1L, 1L, commentReqDTO);
		}).isInstanceOf(UserInfoNotFoundException.class)
			.hasMessage(ExceptionMessage.USERNAME_NOT_FOUND);
	}

	@Test
	void 댓글_수정_실패_존재하지_않는_방일때() {
		//given
		User user = makeUser(1L);
		CommentReqDTO commentReqDTO = new CommentReqDTO("content");
		given(userRepository.findByEmail(anyString()))
			.willReturn(Optional.of(user));
		given(roomRepository.findById(anyLong()))
			.willReturn(Optional.empty());

		//when & then
		assertThatThrownBy(() -> {
			commentService.modifyComment("kkk@naver.com", 1L, 1L, commentReqDTO);
		}).isInstanceOf(RoomInfoNotFoundException.class)
			.hasMessage(ExceptionMessage.ROOM_NOT_FOUND);
	}

	@Test
	void 댓글_수정_실패_존재하지_않는_댓글일때() {
		//given
		User user = makeUser(1L);
		Room room = makeRoom();
		CommentReqDTO commentReqDTO = new CommentReqDTO("content");
		given(userRepository.findByEmail(anyString()))
			.willReturn(Optional.of(user));
		given(roomRepository.findById(anyLong()))
			.willReturn(Optional.of(room));
		given(commentRepository.findById(anyLong()))
			.willReturn(Optional.empty());

		//when & then
		assertThatThrownBy(() -> {
			commentService.modifyComment("kkk@naver.com", 1L, 1L, commentReqDTO);
		}).isInstanceOf(CommentInfoNotFoundException.class)
			.hasMessage(ExceptionMessage.COMMENT_NOT_FOUND);
	}

	@Test
	void 댓글_수정_실패_방에_참가하지_않은_회원일때() {
		//given
		User user = makeUser(1L);
		Room room = makeRoom();
		Comment comment = makeCommentAfterSaved(user, 1L, 1L, 0);
		CommentReqDTO commentReqDTO = new CommentReqDTO("content");
		given(userRepository.findByEmail(anyString()))
			.willReturn(Optional.of(user));
		given(roomRepository.findById(anyLong()))
			.willReturn(Optional.of(room));
		given(commentRepository.findById(anyLong()))
			.willReturn(Optional.of(comment));
		given(roomParticipantRepository.existsByUserAndRoom(any(User.class), any(Room.class)))
			.willReturn(false);

		//when & then
		assertThatThrownBy(() -> {
			commentService.modifyComment("kkk@naver.com", 1L, 1L, commentReqDTO);
		}).isInstanceOf(UserNotParticipateRoomException.class)
			.hasMessage(ExceptionMessage.USER_NOT_PARTICIPATE_ROOM);
	}

	@Test
	void 댓글_수정_실패_자신이_작성한_댓글이_아닐때() {
		//given
		User loginUser = makeUser(1L);
		User author = makeUser(2L);
		Room room = makeRoom();
		Comment comment = makeCommentAfterSaved(author, 1L, 1L, 0);
		CommentReqDTO commentReqDTO = new CommentReqDTO("content");
		given(userRepository.findByEmail(anyString()))
			.willReturn(Optional.of(loginUser));
		given(roomRepository.findById(anyLong()))
			.willReturn(Optional.of(room));
		given(commentRepository.findById(anyLong()))
			.willReturn(Optional.of(comment));
		given(roomParticipantRepository.existsByUserAndRoom(any(User.class), any(Room.class)))
			.willReturn(true);

		//when & then
		assertThatThrownBy(() -> {
			commentService.modifyComment("kkk@naver.com", 1L, 1L, commentReqDTO);
		}).isInstanceOf(ResourceOperationAccessDeniedException.class)
			.hasMessage(String.format(ExceptionMessage.RESOURCE_OPERATION_ACCESS_DENIED, "댓글", "수정"));
	}

	@Test
	void 댓글_삭제_성공() {
		//given
		User user = makeUser(1L);
		Room room = makeRoom();
		Comment comment = makeCommentAfterSaved(user, 1L, 1L, 0);
		given(userRepository.findByEmail(anyString()))
			.willReturn(Optional.of(user));
		given(roomRepository.findById(anyLong()))
			.willReturn(Optional.of(room));
		given(commentRepository.findById(anyLong()))
			.willReturn(Optional.of(comment));
		given(roomParticipantRepository.existsByUserAndRoom(any(User.class), any(Room.class)))
			.willReturn(true);

		//when & then
		assertThatNoException().isThrownBy(() ->
			commentService.deleteComment("kkk@naver.com", 1L, 1L));

		then(commentRepository).should(times(1)).deleteAllByGroupIdAndDepth(comment.getGroupId(), comment.getDepth());
	}

	@Test
	void 댓글_삭제_실패_존재하지_않는_회원일때() {
		//given
		given(userRepository.findByEmail(anyString()))
			.willReturn(Optional.empty());

		//when & then
		assertThatThrownBy(() -> {
			commentService.deleteComment("kkk@naver.com", 1L, 1L);
		}).isInstanceOf(UserInfoNotFoundException.class)
			.hasMessage(ExceptionMessage.USERNAME_NOT_FOUND);
	}

	@Test
	void 댓글_삭제_실패_존재하지_않는_방일때() {
		//given
		User user = makeUser(1L);
		given(userRepository.findByEmail(anyString()))
			.willReturn(Optional.of(user));
		given(roomRepository.findById(anyLong()))
			.willReturn(Optional.empty());

		//when & then
		assertThatThrownBy(() -> {
			commentService.deleteComment("kkk@naver.com", 1L, 1L);
		}).isInstanceOf(RoomInfoNotFoundException.class)
			.hasMessage(ExceptionMessage.ROOM_NOT_FOUND);
	}

	@Test
	void 댓글_삭제_실패_존재하지_않는_댓글일때() {
		//given
		User user = makeUser(1L);
		Room room = makeRoom();
		given(userRepository.findByEmail(anyString()))
			.willReturn(Optional.of(user));
		given(roomRepository.findById(anyLong()))
			.willReturn(Optional.of(room));
		given(commentRepository.findById(anyLong()))
			.willReturn(Optional.empty());

		//when & then
		assertThatThrownBy(() -> {
			commentService.deleteComment("kkk@naver.com", 1L, 1L);
		}).isInstanceOf(CommentInfoNotFoundException.class)
			.hasMessage(ExceptionMessage.COMMENT_NOT_FOUND);
	}

	@Test
	void 댓글_삭제_실패_방에_참가하지_않은_회원일때() {
		//given
		User user = makeUser(1L);
		Room room = makeRoom();
		Comment comment = makeCommentAfterSaved(user, 1L, 1L, 0);
		given(userRepository.findByEmail(anyString()))
			.willReturn(Optional.of(user));
		given(roomRepository.findById(anyLong()))
			.willReturn(Optional.of(room));
		given(commentRepository.findById(anyLong()))
			.willReturn(Optional.of(comment));
		given(roomParticipantRepository.existsByUserAndRoom(any(User.class), any(Room.class)))
			.willReturn(false);

		//when & then
		assertThatThrownBy(() -> {
			commentService.deleteComment("kkk@naver.com", 1L, 1L);
		}).isInstanceOf(UserNotParticipateRoomException.class)
			.hasMessage(ExceptionMessage.USER_NOT_PARTICIPATE_ROOM);
	}

	@Test
	void 댓글_삭제_실패_자신이_작성한_댓글이_아닐때() {
		//given
		User loginUser = makeUser(1L);
		User author = makeUser(2L);
		Room room = makeRoom();
		Comment comment = makeCommentAfterSaved(author, 1L, 1L, 0);
		given(userRepository.findByEmail(anyString()))
			.willReturn(Optional.of(loginUser));
		given(roomRepository.findById(anyLong()))
			.willReturn(Optional.of(room));
		given(commentRepository.findById(anyLong()))
			.willReturn(Optional.of(comment));
		given(roomParticipantRepository.existsByUserAndRoom(any(User.class), any(Room.class)))
			.willReturn(true);

		//when & then
		assertThatThrownBy(() -> {
			commentService.deleteComment("kkk@naver.com", 1L, 1L);
		}).isInstanceOf(ResourceOperationAccessDeniedException.class)
			.hasMessage(String.format(ExceptionMessage.RESOURCE_OPERATION_ACCESS_DENIED, "댓글", "삭제"));
	}

	private User makeUser(Long id) {
		return User.builder()
			.id(id)
			.email("kkk@naver.com")
			.name("kkk")
			.build();
	}

	private Room makeRoom() {
		return Room.builder()
			.build();
	}

	private Comment makeCommentAfterSaved(User user, Long id, Long groupId, Integer depth) {

		Comment comment = Comment.builder()
			.user(user)
			.depth(depth)
			.groupId(groupId)
			.content("content")
			.build();
		ReflectionTestUtils.setField(comment, "id", id);
		ReflectionTestUtils.setField(comment, "createDate", LocalDateTime.now());
		return comment;
	}

}
