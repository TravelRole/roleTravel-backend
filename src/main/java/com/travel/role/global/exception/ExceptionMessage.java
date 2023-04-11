package com.travel.role.global.exception;

public final class ExceptionMessage {
	public static final String NOT_EXISTS_TOKEN = "토큰 값이 존재하지 않습니다.";
	public static final String USERNAME_NOT_FOUND = "해당하는 유저가 존재하지 않습니다.";
	public static final String INVALID_TOKEN = "유효하지 않은 토큰입니다.";
	public static final String ALREADY_EXIST_USER = "이미 존재하는 회원입니다.";
	public static final String BAD_CREDENTIAL_USER = "인증정보가 다른 회원입니다.";
	public static final String INVALID_USER = "유효하지 않은 회원입니다.";
	public static final String MAIL_SEND_FAILD_ERROR = "서버가 메일을 보내는데 실패하였습니다.";


	// Room
	public static final String INVALID_DATE_ERROR = "시작 날짜의 크기는 종료 날짜의 크기보다 클 수 없습니다.";
}
