package com.travel.role.domain.comment.repository.querydsl;

import java.util.List;

import com.travel.role.domain.comment.dto.response.CommentResDTO;

public interface CommentQuerydsl {

	List<CommentResDTO> findAllOrderByGroupIdAndCreateDate(Long roomId);
	void dynamicDeleteById(Long commentId);

}
