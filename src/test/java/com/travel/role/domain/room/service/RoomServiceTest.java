package com.travel.role.domain.room.service;

import static com.travel.role.global.exception.ExceptionMessage.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.travel.role.domain.room.dao.RoomRepository;
import com.travel.role.domain.room.domain.RoomRole;
import com.travel.role.domain.room.dto.MakeRoomRequestDTO;
import com.travel.role.domain.room.exception.InvalidLocalDateException;
import com.travel.role.domain.room.exception.UserHaveNotPrivilegeException;
import com.travel.role.domain.user.dao.UserRepository;
import com.travel.role.domain.user.exception.UserInfoNotFoundException;
import com.travel.role.global.auth.token.UserPrincipal;
import com.travel.role.global.exception.ExceptionMessage;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

	@InjectMocks
	private RoomService roomService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private RoomRepository roomRepository;

	@Test
	void 시작날짜가_종료날짜보다_클_경우() {
		// given
		MakeRoomRequestDTO newDto = new MakeRoomRequestDTO("여행 가자~", LocalDate.of(2023, 1, 3),
			LocalDate.of(2023, 1, 1),  "강원도 춘천");

		// when, then
		Assertions.assertThatThrownBy(() -> roomService.makeRoom(null, newDto))
			.isInstanceOf(InvalidLocalDateException.class)
			.hasMessageContaining(ExceptionMessage.INVALID_DATE_ERROR);
	}

	@Test
	void 해당하는_유저가_존재하지_않는_경우() {
		// given
		given(userRepository.findByEmail(anyString()))
			.willReturn(Optional.empty());

		// when, then
		Assertions.assertThatThrownBy(() -> roomService.makeRoom(makeUserPrincipal(), getMakeRoomRequestDTO()))
			.isInstanceOf(UserInfoNotFoundException.class)
			.hasMessageContaining(USERNAME_NOT_FOUND);
	}

	@Test
	void 해당_정보의_권한이_없거나_쿼리로_가져온_결과가_없을경우() {
		// given
		given(roomRepository.getRoomRole(anyString(), anyLong()))
			.willReturn(new ArrayList<RoomRole>());

		// when
		Assertions.assertThatThrownBy(() -> roomService.makeInviteCode(makeUserPrincipal(), 1L))
			.isInstanceOf(UserHaveNotPrivilegeException.class)
			.hasMessageContaining(USER_HAVE_NOT_PRIVILEGE);
	}

	private static MakeRoomRequestDTO getMakeRoomRequestDTO() {
		return new MakeRoomRequestDTO("여행 가자~", LocalDate.of(2023, 1, 1),
			LocalDate.of(2023, 1, 3), "강원도 춘천");
	}

	private static UserPrincipal makeUserPrincipal() {
		return new UserPrincipal(1L, "haechan@naver.com", "1234", null);
	}
}