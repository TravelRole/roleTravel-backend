package com.travel.role.domain.comment.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.comment.dto.response.CommentResDTO;
import com.travel.role.domain.comment.entity.Comment;
import com.travel.role.domain.comment.repository.CommentRepository;
import com.travel.role.global.exception.comment.CommentInfoNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentReadService {

	private final CommentRepository commentRepository;

	public List<CommentResDTO> getAllCommentsByRoomId(Long roomId) {

		return commentRepository.findAllOrderByGroupIdAndCreateDate(roomId);
	}

	public Comment findCommentByIdOrElseThrow(Long commentId) {

		return commentRepository.findById(commentId)
			.orElseThrow(CommentInfoNotFoundException::new);
	}

	public Comment findCommentWithFromUserByIdOrElseThrow(Long commentId) {

		return commentRepository.findByIdWithFromUser(commentId)
			.orElseThrow(CommentInfoNotFoundException::new);
	}
}
