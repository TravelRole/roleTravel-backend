package com.travel.role.domain.comment.repository.querydsl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.travel.role.domain.comment.dto.response.CommentResDTO;

public interface CommentQuerydsl {

	Page<CommentResDTO> findAllOrderByGroupIdAndCreateDate(Long roomId, Pageable pageable);

	void dynamicDeleteById(Long commentId);

	void dynamicDeleteByUserIdAndRoomId(Long userId, Long roomId);
}
