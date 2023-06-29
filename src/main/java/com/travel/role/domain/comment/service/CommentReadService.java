package com.travel.role.domain.comment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	public Page<CommentResDTO> getCommentsByRoomId(Long roomId, Pageable pageable) {

		return commentRepository.findAllOrderByGroupIdAndCreateDate(roomId, pageable);
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
