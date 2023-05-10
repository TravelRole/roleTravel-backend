package com.travel.role.global.exception.board;

import com.travel.role.global.exception.dto.ExceptionMessage;

public class BookInfoNotFoundException extends RuntimeException {
	public BookInfoNotFoundException() {
		super(ExceptionMessage.BOOK_INFO_NOT_FOUND);
	}
}
