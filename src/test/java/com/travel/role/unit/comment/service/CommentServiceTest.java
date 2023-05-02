package com.travel.role.unit.comment.service;

import static com.travel.role.global.exception.common.ResourceOperationAccessDeniedException.Operation.*;
import static com.travel.role.global.exception.common.ResourceOperationAccessDeniedException.Resource.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.travel.role.domain.comment.dto.request.CommentReqDTO;
import com.travel.role.domain.comment.entity.Comment;
import com.travel.role.domain.comment.repository.CommentRepository;
import com.travel.role.domain.comment.service.CommentReadService;
import com.travel.role.domain.comment.service.CommentService;
import com.travel.role.domain.room.entity.Room;
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
	private CommentReadService commentReadService;

	@Mock
	private UserReadService userReadService;

	@Mock
	private RoomReadService roomReadService;

	@Mock
	private RoomParticipantReadService roomParticipantReadService;

	@Mock
	private CommentRepository commentRepository;

	@Test
	void 방에_참여한_회원이_최상단_댓글을_작성하면_댓글이_생성된다(){

	    //Given
		String email = "zzz@naver.com";
		Long roomId = 1L;
		CommentReqDTO reqDTO = makeCommentReqDTO();
		User user = makeUser(1L);
		Room room = makeRoom();

		given(userReadService.findUserByEmailOrElseThrow(email))
			.willReturn(user);
		given(roomReadService.findRoomByIdOrElseThrow(roomId))
			.willReturn(room);
		doNothing().when(roomParticipantReadService).checkParticipant(user, room);

	    //When
	    commentService.createComment(email, roomId, null, reqDTO);

		//Then
		then(commentReadService).should(never()).findCommentWithFromUserByIdOrElseThrow(any(Long.class));
		then(commentRepository).should(times(2)).save(any(Comment.class));
	}

	@Test
	void 방에_참여한_회원이_답글을_작성하면_답글이_생성된다(){

		//Given
		String email = "zzz@naver.com";
		Long roomId = 1L;
		Long parentId = 1L;
		CommentReqDTO reqDTO = makeCommentReqDTO();
		Comment parentComment = makeComment(parentId, null);
		User user = makeUser(1L);
		Room room = makeRoom();

		given(userReadService.findUserByEmailOrElseThrow(email))
			.willReturn(user);
		given(roomReadService.findRoomByIdOrElseThrow(roomId))
			.willReturn(room);
		given(commentReadService.findCommentWithFromUserByIdOrElseThrow(parentId))
			.willReturn(parentComment);
		doNothing().when(roomParticipantReadService).checkParticipant(user, room);

		//When
		commentService.createComment(email, roomId, parentId, reqDTO);

		//Then
		then(commentReadService).should(times(1)).findCommentWithFromUserByIdOrElseThrow(any(Long.class));
		then(commentRepository).should(times(1)).save(any(Comment.class));
	}

	@Test
	void 댓글_주인이_댓글을_수정하면_댓글이_수정된다(){

	    //Given
		String email = "zzz@naver.com";
		Long roomId = 1L;
		Long commentId = 1L;
		User user = makeUser(1L);
		Room room = makeRoom();
		CommentReqDTO reqDTO = makeCommentReqDTO();
		given(userReadService.findUserByEmailOrElseThrow(email))
			.willReturn(user);
		given(roomReadService.findRoomByIdOrElseThrow(roomId))
			.willReturn(room);
		given(commentReadService.findCommentByIdOrElseThrow(commentId))
			.willReturn(makeComment(commentId, user));
		doNothing().when(roomParticipantReadService).checkParticipant(user, room);

	    //When
		commentService.modifyComment(email, roomId, commentId, reqDTO);

	    //Then
		then(commentRepository).should(times(1)).save(any(Comment.class));
	}

	@Test
	void 댓글_주인이_아닌_사람이_댓글을_수정하면_예외를_발생시킨다(){

		//Given
		String email = "zzz@naver.com";
		Long roomId = 1L;
		Long commentId = 1L;
		User requestUser = makeUser(1L);
		User fromUser = makeUser(2L);
		Room room = makeRoom();
		CommentReqDTO reqDTO = makeCommentReqDTO();
		given(userReadService.findUserByEmailOrElseThrow(email))
			.willReturn(requestUser);
		given(roomReadService.findRoomByIdOrElseThrow(roomId))
			.willReturn(room);
		given(commentReadService.findCommentByIdOrElseThrow(commentId))
			.willReturn(makeComment(commentId, fromUser));
		doNothing().when(roomParticipantReadService).checkParticipant(requestUser, room);

		//When
		assertThatThrownBy(() -> commentService.modifyComment(email, roomId, commentId, reqDTO))
			.isInstanceOf(ResourceOperationAccessDeniedException.class)
				.hasMessage(ExceptionMessage.RESOURCE_OPERATION_ACCESS_DENIED, COMMENT.getResourceName(), MODIFY.getOperationName());

		//Then
		then(commentRepository).should(never()).save(any(Comment.class));
	}

	@Test
	void 댓글_주인이_댓글을_삭제하면_댓글이_삭제된다(){

		//Given
		String email = "zzz@naver.com";
		Long roomId = 1L;
		Long commentId = 1L;
		User user = makeUser(1L);
		Room room = makeRoom();
		given(userReadService.findUserByEmailOrElseThrow(email))
			.willReturn(user);
		given(roomReadService.findRoomByIdOrElseThrow(roomId))
			.willReturn(room);
		given(commentReadService.findCommentByIdOrElseThrow(commentId))
			.willReturn(makeComment(commentId, user));
		doNothing().when(roomParticipantReadService).checkParticipant(user, room);

		//When
		commentService.deleteComment(email, roomId, commentId);

		//Then
		then(commentRepository).should(times(1)).dynamicDeleteById(commentId);
	}

	@Test
	void 댓글_주인이_아닌_사람이_댓글을_삭제하면_예외를_발생시킨다(){

		//Given
		String email = "zzz@naver.com";
		Long roomId = 1L;
		Long commentId = 1L;
		User requestUser = makeUser(1L);
		User fromUser = makeUser(2L);
		Room room = makeRoom();
		given(userReadService.findUserByEmailOrElseThrow(email))
			.willReturn(requestUser);
		given(roomReadService.findRoomByIdOrElseThrow(roomId))
			.willReturn(room);
		given(commentReadService.findCommentByIdOrElseThrow(commentId))
			.willReturn(makeComment(commentId, fromUser));
		doNothing().when(roomParticipantReadService).checkParticipant(requestUser, room);

		//When
		assertThatThrownBy(() -> commentService.deleteComment(email, roomId, commentId))
			.isInstanceOf(ResourceOperationAccessDeniedException.class)
			.hasMessage(ExceptionMessage.RESOURCE_OPERATION_ACCESS_DENIED, COMMENT.getResourceName(), DELETE.getOperationName());

		//Then
		then(commentRepository).should(never()).dynamicDeleteById(any(Long.class));
	}

	private CommentReqDTO makeCommentReqDTO(){

		return new CommentReqDTO("content");
	}

	private User makeUser(Long id){
		return User.builder()
			.id(id)
			.build();
	}

	private Room makeRoom(){
		return Room.builder()
			.id(1L)
			.build();
	}

	private Comment makeComment(Long id, User fromUser){

		Comment comment = Comment.builder()
			.fromUser(fromUser)
			.content("content")
			.build();

		ReflectionTestUtils.setField(comment, "id", id, Long.class);

		return comment;
	}
}
