package com.travel.role.global.exception.dto;

public final class ExceptionMessage {

	private ExceptionMessage() {
	}

	// common
	public static final String INVALID_DATE_FORMAT = "날짜 형식은 yyyy-MM-dd 형식으로 올바른 값을 입력해 주세요";

	public static final String NOT_EXISTS_TOKEN = "토큰 값이 존재하지 않습니다.";
	public static final String USERNAME_NOT_FOUND = "해당하는 유저가 존재하지 않습니다.";
	public static final String INVALID_TOKEN = "유효하지 않은 토큰입니다.";
	public static final String ALREADY_EXIST_USER = "이미 존재하는 회원입니다.";
	public static final String BAD_CREDENTIAL_USER = "인증정보가 다른 회원입니다.";
	public static final String INVALID_PASSWORD = "비밀번호가 일치하지 않습니다.";
	public static final String NO_AUTHENTICATION = "인증에 실패하였습니다.";
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
	public static final String ALREADY_EXIST_USER_IN_ROOM = "이미 이 방에 참여하고 있는 유저입니다.";
	public static final String EARLY_DATE_ERROR = "일정보다 이른 날짜입니다.";
	public static final String LATE_DATE_ERROR = "일정보다 늦은 날짜입니다.";
	public static final String ADMIN_IS_ONLY_ONE = "총무는 한명만 가능합니다.";
	public static final String ROOM_NAME_NOT_EMPTY = "방 이름은 필수값 입니다.";
	public static final String ROLE_IS_EMPTY = "이 방에 해당하는 유저의 역할 정보가 존재하지 않습니다.";
	// comment
	public static final String COMMENT_NOT_EMPTY = "댓글 내용은 필수값 입니다.";
	public static final String COMMENT_NOT_FOUND = "해당하는 댓글이 존재하지 않습니다.";
	// image
	public static final String IMAGE_NOT_FOUND_IN_S3 = "%s 이미지가 s3에 존재하지 않습니다.";
	public static final String CREATE_PRESIGNED_URL_FAIL = "PreSigned Url 을 생성하던 중 문제가 발생하였습니다.";
	public static final String DELETE_IMAGE_FAIL_IN_S3 = "이미지를 삭제하던 중 문제가 발생하였습니다.";

	// travel essential
	public static final String INVALID_ESSENTIAL_CATEGORY = "지정한 카테고리 내에서 입력해 주세요.";
	public static final String INVALID_ESSENTIAL_ITEM_LENGTH = "준비물 길이는 2 이상 15 이하여야 합니다.";
	public static final String ESSENTIAL_NAME_NOT_EMPTY = "준비물을 하나 이상 입력해 주세요.";
	public static final String ESSENTIAL_CHECK_STATUS_NOT_EMPTY = "준비물 상태를 어떤 상태로 변경할 것인지 입력해 주세요";

	// expense
	public static final String EXPENSE_MUST_GREATER_THAN_OR_EQUAL_TO_ZERO = "경비는 0원 이상이어야 합니다.";

	//board
	public static final String BOOK_INFO_ID_VALUE_NOT_EMPTY = "예약 정보 아이디는 필수값 입니다.";
	public static final String ACCOUNTING_INFO_ID_VALUE_NOT_EMPTY = "회계 정보 아이디는 필수값 입니다.";
	public static final String BOOK_INFO_NOT_FOUND = "해당하는 예약정보가 존재하지 않습니다.";
	public static final String ACCOUNTING_INFO_NOT_FOUND = "해당하는 회계정보가 존재하지 않습니다.";
	public static final String PAYMENT_TIME_NOT_FOUND = "결제 내역은 필수 값 입니다.";
	public static final String IS_BOOKED_NOT_FOUND = "예약 여부는 필수 값 입니다";

	// accounting
	public static final String INVALID_PAYMENT_NAME_SIZE = "지출 내역은 1자 이상, 20자 이하이어야 합니다.";
	public static final String INVALID_PAYMENT_PRICE = "금액은 1원 이상이어야 합니다.";
	public static final String INVALID_CATEGORY = "지정된 카테고리 내에서 입력해 주세요.";
	public static final String INVALID_PAYMENT_METHOD = "지정된 결제수단 내에서 입력해 주세요";
	public static final String INVALID_PAYMENT_TIME = "결제 시간을 yyyy-MM-dd HH:mm 형식으로 올바른 시간을 입력해 주세요.";
	public static final String ACCOUNTING_INFO_CANNOT_BE_DELETED = "예약 정보와 연동된 지출 내역은 삭제할 수 없습니다.";
	public static final String ACCOUNTING_INFO_CANNOT_BE_MODIFIED = "예약 정보와 연동된 지출 내역은 수정할 수 없습니다.";

	//schedule
	public static final String MAP_PLACE_ID_VALUE_NOT_EMPTY = "카카오맵 장소 아이디는 필수값 입니다.";
	public static final String BOARD_NOT_FOUND = "해당하는 일정이 존재하지 않습니다.";
	public static final String BOARD_ID_EMPTY = "일정 아이디는 필수 값 입니다.";

	//want-place
	public static final String WANT_PLACE_NOT_FOUND = "해당하는 찜 목록이 존재하지 않습니다.";
}
