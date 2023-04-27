package com.travel.role.unit.commnet.service;

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

import com.travel.role.domain.comment.dto.request.CommentReqDTO;
import com.travel.role.domain.comment.dto.response.CommentListResDTO;
import com.travel.role.domain.comment.dto.response.CommentResDTO;
import com.travel.role.domain.comment.entity.Comment;
import com.travel.role.domain.comment.repository.CommentRepository;
import com.travel.role.domain.comment.service.CommentService;
import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.room.repository.RoomParticipantRepository;
import com.travel.role.domain.room.service.RoomParticipantReadService;
import com.travel.role.domain.room.service.RoomReadService;
import com.travel.role.domain.user.entity.User;
import com.travel.role.domain.user.service.UserReadService;
import com.travel.role.global.exception.common.ResourceOperationAccessDeniedException;
import com.travel.role.global.exception.dto.ExceptionMessage;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

	@InjectMocks
	private CommentService commentService;

	@Mock
	private UserReadService userReadService;
	@Mock
	private RoomReadService roomReadService;

	@Mock
	private CommentRepository commentRepository;

	@Mock
	private RoomParticipantRepository roomParticipantRepository;

	@Mock
	private RoomParticipantReadService roomParticipantReadService;

	@Test
	void depth가_0인_댓글_생성_성공() {
		// given
		User user = makeUser(1L);
		Room room = makeRoom();
		CommentReqDTO commentReqDTO = new CommentReqDTO("content");
		Comment comment = makeCommentAfterSaved(user, 1L, 1L, 0);
		given(userReadService.findUserByEmailOrElseThrow(anyString()))
			.willReturn(user);
		given(roomReadService.findRoomByIdOrElseThrow(anyLong()))
			.willReturn(room);
		doNothing()
			.when(roomParticipantReadService).checkParticipant(any(User.class), any(Room.class));
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
		given(userReadService.findUserByEmailOrElseThrow(anyString()))
			.willReturn(user);
		given(roomReadService.findRoomByIdOrElseThrow(anyLong()))
			.willReturn(room);
		doNothing()
			.when(roomParticipantReadService).checkParticipant(any(User.class), any(Room.class));

		// when
		commentService.createComment("kkk@naver.com", 1L, 1L, commentReqDTO);

		// then
		then(commentRepository).should(times(1)).save(any(Comment.class));
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
		given(userReadService.findUserByEmailOrElseThrow(anyString()))
			.willReturn(user);
		given(roomReadService.findRoomByIdOrElseThrow(anyLong()))
			.willReturn(room);
		doNothing()
			.when(roomParticipantReadService).checkParticipant(any(User.class), any(Room.class));
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
		given(userReadService.findUserByEmailOrElseThrow(anyString()))
			.willReturn(user);
		given(roomReadService.findRoomByIdOrElseThrow(anyLong()))
			.willReturn(room);
		doNothing()
			.when(roomParticipantReadService).checkParticipant(any(User.class), any(Room.class));
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
		given(userReadService.findUserByEmailOrElseThrow(anyString()))
			.willReturn(user);
		given(roomReadService.findRoomByIdOrElseThrow(anyLong()))
			.willReturn(room);
		given(commentRepository.findById(anyLong()))
			.willReturn(Optional.of(comment));
		doNothing()
			.when(roomParticipantReadService).checkParticipant(any(User.class), any(Room.class));

		//when & then
		assertThatNoException().isThrownBy(() ->
			commentService.modifyComment("kkk@naver.com", 1L, 1L, commentReqDTO));
	}

	@Test
	void 댓글_삭제_성공() {
		//given
		User user = makeUser(1L);
		Room room = makeRoom();
		Comment comment = makeCommentAfterSaved(user, 1L, 1L, 0);
		given(userReadService.findUserByEmailOrElseThrow(anyString()))
			.willReturn(user);
		given(roomReadService.findRoomByIdOrElseThrow(anyLong()))
			.willReturn(room);
		given(commentRepository.findById(anyLong()))
			.willReturn(Optional.of(comment));
		doNothing()
			.when(roomParticipantReadService).checkParticipant(any(User.class), any(Room.class));

		//when & then
		assertThatNoException().isThrownBy(() ->
			commentService.deleteComment("kkk@naver.com", 1L, 1L));

		then(commentRepository).should(times(1)).deleteAllByGroupIdAndDepth(comment.getGroupId(), comment.getDepth());
	}


	@Test
	void 댓글_삭제_실패_자신이_작성한_댓글이_아닐때() {
		//given
		User loginUser = makeUser(1L);
		User author = makeUser(2L);
		Room room = makeRoom();
		Comment comment = makeCommentAfterSaved(author, 1L, 1L, 0);
		given(userReadService.findUserByEmailOrElseThrow(anyString()))
			.willReturn(loginUser);
		given(roomReadService.findRoomByIdOrElseThrow(anyLong()))
			.willReturn(room);
		given(commentRepository.findById(anyLong()))
			.willReturn(Optional.of(comment));
		doNothing()
			.when(roomParticipantReadService).checkParticipant(any(User.class), any(Room.class));

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
