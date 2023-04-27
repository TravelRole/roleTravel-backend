package com.travel.role.global.exception.comment;

import com.travel.role.global.exception.dto.ExceptionMessage;

public class CommentInfoNotFoundException extends RuntimeException{

	public CommentInfoNotFoundException() {
		super(ExceptionMessage.COMMENT_NOT_FOUND);
	}
}
