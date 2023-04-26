package com.travel.role.global.exception;

public final class ExceptionMessage {

	private ExceptionMessage(){}
	public static final String NOT_EXISTS_TOKEN = "토큰 값이 존재하지 않습니다.";
	public static final String USERNAME_NOT_FOUND = "해당하는 유저가 존재하지 않습니다.";
	public static final String INVALID_TOKEN = "유효하지 않은 토큰입니다.";
	public static final String ALREADY_EXIST_USER = "이미 존재하는 회원입니다.";
	public static final String BAD_CREDENTIAL_USER = "인증정보가 다른 회원입니다.";
	public static final String INVALID_PASSWORD = "비밀번호가 일치하지 않습니다.";
	public static final String INVALID_USER = "유효하지 않은 회원입니다.";
	public static final String MAIL_SEND_FAILED_ERROR = "서버가 메일을 보내는데 실패하였습니다.";
	public static final String USERNAME_NOT_EMPTY = "이름은 필수값 입니다.";
	public static final String LATITUDE_VALUE_NOT_EMPTY = "위도는 필수값 입니다.";
	public static final String LONGITUDE_NOT_EMPTY = "경도는 필수값 입니다.";
	public static final String PASSWORD_NOT_EMPTY = "비밀번호는 필수값 입니다.";
	public static final String INPUT_VALUE_NOT_MATCH = "%s 값과 %s 값이 일치하지 않습니다.";
	public static final String BIRTH_PATTERN = "생년월일을 1900 ~ 2099 년 사이의 XXXX/XX/XX 형식으로 입력해 주세요.";
	public static final String PASSWORD_PATTERN = "비밀번호는 영문, 숫자, 특수문자(!, -, _)로 구성된 8-16 자로 입력해 주세요.";
	public static final String RESOURCE_OPERATION_ACCESS_DENIED = "해당 %s에 대한 %s 권한이 없습니다.";
	// Room
	public static final String INVALID_DATE_ERROR = "시작 날짜의 크기는 종료 날짜의 크기보다 클 수 없습니다.";
	public static final String ROOM_NOT_FOUND = "해당하는 방이 존재하지 않습니다.";
	public static final String USER_NOT_PARTICIPATE_ROOM = "사용자는 방에 참여하지 않았습니다";
	public static final String ROOM_ID_VALUE_NOT_EMPTY = "방 아이디는 필수값 입니다.";
	public static final String PLACE_NAME_VALUE_NOT_EMPTY = "장소 이름은 필수값 입니다.";
	public static final String PLACE_ADDRESS_VALUE_NOT_EMPTY = "장소 주소는 필수값 입니다.";
	public static final String PLACE_NOT_FOUND = "해당하는 장소가 존재하지 않습니다.";
	public static final String USER_HAVE_NOT_PRIVILEGE = "유저는 해당 권한이 존재하지 않습니다.";
	public static final String INVALID_INVITE_CODE = "초대코드가 유효하지 않습니다.";
	// comment
	public static final String COMMENT_NOT_EMPTY = "댓글 내용은 필수값 입니다.";
	public static final String COMMENT_NOT_FOUND = "해당하는 댓글이 존재하지 않습니다.";

	// image
	public static final String IMAGE_NOT_FOUND_IN_S3 = "%s 이미지가 s3에 존재하지 않습니다.";

}
