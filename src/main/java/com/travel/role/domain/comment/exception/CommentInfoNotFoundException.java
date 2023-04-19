package com.travel.role.domain.comment.exception;

import com.travel.role.global.exception.ExceptionMessage;

public class CommentInfoNotFoundException extends RuntimeException{

	public CommentInfoNotFoundException() {
		super(ExceptionMessage.COMMENT_NOT_FOUND);
	}
}
