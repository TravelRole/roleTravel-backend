package com.travel.role.unit.comment.service;

import static com.travel.role.global.exception.dto.ExceptionMessage.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.travel.role.domain.comment.dto.response.CommentResDTO;
import com.travel.role.domain.comment.entity.Comment;
import com.travel.role.domain.comment.repository.CommentRepository;
import com.travel.role.domain.comment.service.CommentReadService;
import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.user.dto.SimpleUserInfoResDTO;
import com.travel.role.domain.user.entity.User;
import com.travel.role.global.exception.comment.CommentInfoNotFoundException;

@ExtendWith(MockitoExtension.class)
class CommentReadServiceTest {

	@InjectMocks
	private CommentReadService commentReadService;

	@Mock
	private CommentRepository commentRepository;

	@Test
	void 존재하는_댓글_아이디가_주어졌을때_댓글을_찾으면_댓글을_반환한다() {

		// Given
		Long commentId = 1L;
		Room room = makeRoom();
		Comment expectedComment = Comment.ofParent(null, room, "content");
		given(commentRepository.findById(commentId))
			.willReturn(Optional.of(expectedComment));

		// When
		Comment findComment = commentReadService.findCommentByIdOrElseThrow(commentId);

		// Then
		assertThat(findComment).usingRecursiveComparison().isEqualTo(expectedComment);
	}

	@Test
	void 존재하는_댓글_아이디가_주어졌을때_작성자와_함께_댓글을_찾으면_댓글을_반환한다() {

		// Given
		Long commentId = 1L;
		User fromUser = makeUser();
		Room room = makeRoom();
		Comment expectedComment = Comment.ofParent(fromUser, room, "content");
		given(commentRepository.findByIdWithFromUser(commentId))
			.willReturn(Optional.of(expectedComment));

		// When
		Comment findComment = commentReadService.findCommentWithFromUserByIdOrElseThrow(commentId);

		// Then
		assertThat(findComment).usingRecursiveComparison().isEqualTo(expectedComment);
	}

	@Test
	void 방_아이디와_페이징_정보가_주어졌을때_방에_존재하는_댓글을_찾으면_댓글_목록_페이지를_반환한다() {

		// Given
		Long roomId = 1L;
		PageRequest pageRequest = PageRequest.of(1, 10);
		List<CommentResDTO> expectedResDTOList = makeCommentResDTOList();
		PageImpl<CommentResDTO> page = new PageImpl<>(expectedResDTOList, pageRequest, 20L);
		given(commentRepository.findAllOrderByGroupIdAndCreateDate(roomId, pageRequest))
			.willReturn(page);

		// When
		Page<CommentResDTO> result = commentReadService.getCommentsByRoomId(roomId, pageRequest);

		// Then
		assertThat(result.getTotalElements()).isEqualTo(20);
		assertThat(result.getTotalPages()).isEqualTo(2);
		assertThat(result.getContent()).hasSize(10);
	}

	@Test
	void 존재하지_않는_댓글_아이디가_주어졌을때_댓글을_찾으면_예외를_발생시킨다() {

		//Given
		Long commentId = 1L;
		given(commentRepository.findById(commentId))
			.willReturn(Optional.empty());

		//When & Then
		assertThatThrownBy(() -> commentReadService.findCommentByIdOrElseThrow(commentId))
			.isInstanceOf(CommentInfoNotFoundException.class)
			.hasMessage(COMMENT_NOT_FOUND);
	}

	@Test
	void 존재하지_않는_댓글_아이디가_주어졌을때_작성자와_함께_댓글을_찾으면_예외를_발생시킨다() {

		//Given
		Long commentId = 1L;
		given(commentRepository.findByIdWithFromUser(commentId))
			.willReturn(Optional.empty());

		//When & Then
		assertThatThrownBy(() -> commentReadService.findCommentWithFromUserByIdOrElseThrow(commentId))
			.isInstanceOf(CommentInfoNotFoundException.class)
			.hasMessage(COMMENT_NOT_FOUND);
	}


	private List<CommentResDTO> makeCommentResDTOList() {

		List<CommentResDTO> resDTOList = new ArrayList<>();

		for(long i = 1 ; i <= 10 ; i++){
			resDTOList.add(
				CommentResDTO.builder()
					.commentId(i)
					.content("content" + i)
					.fromUserInfo(new SimpleUserInfoResDTO(10 * i, "fromuser" + i, "profile" + i, List.of()))
					.toUsername("user" + i)
					.deleted(i % 2 == 0)
					.createdDate(LocalDateTime.now())
					.build()
			);
		}

		return resDTOList;
	}

	private User makeUser() {

		return User.builder()
			.id(1L)
			.name("kim")
			.build();
	}

	private Room makeRoom() {

		return Room.builder()
			.id(1L)
			.build();
	}
}
