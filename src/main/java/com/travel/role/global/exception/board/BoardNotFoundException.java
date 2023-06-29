package com.travel.role.global.exception.board;

import com.travel.role.global.exception.dto.ExceptionMessage;

public class BoardNotFoundException extends RuntimeException {
	public BoardNotFoundException() {
		super(ExceptionMessage.BOARD_NOT_FOUND);
	}
}
