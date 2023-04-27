package com.travel.role.domain.room.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.travel.role.domain.room.dto.request.MakeRoomRequestDTO;
import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.room.repository.ParticipantRoleRepository;
import com.travel.role.domain.room.repository.RoomRepository;
import com.travel.role.domain.user.entity.Provider;
import com.travel.role.domain.user.entity.Role;
import com.travel.role.domain.user.entity.User;
import com.travel.role.domain.user.service.UserReadService;
import com.travel.role.global.auth.token.UserPrincipal;
import com.travel.role.global.exception.dto.ExceptionMessage;
import com.travel.role.global.exception.room.InvalidInviteCode;
import com.travel.role.global.exception.room.InvalidLocalDateException;
import com.travel.role.global.exception.room.UserHaveNotPrivilegeException;
import com.travel.role.global.util.PasswordGenerator;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

	@InjectMocks
	private RoomService roomService;

	@Mock
	private UserReadService userReadService;

	@Mock
	private RoomReadService roomReadService;

	@Mock
	private PasswordGenerator passwordGenerator;

	@Mock
	private ParticipantRoleRepository participantRoleRepository;
	@Mock
	private RoomRepository roomRepository;

	@Test
	void 시작날짜가_종료날짜보다_클_경우() {
		// given
		MakeRoomRequestDTO newDto = new MakeRoomRequestDTO("여행 가자~", LocalDate.of(2023, 1, 3),
			LocalDate.of(2023, 1, 1), "강원도 춘천", 1L);

		// when, then
		assertThatThrownBy(() -> roomService.makeRoom(null, newDto))
			.isInstanceOf(InvalidLocalDateException.class)
			.hasMessageContaining(ExceptionMessage.INVALID_DATE_ERROR);
	}

	@Test
	void 해당_유저의_코드가_유효기간이_지나_재생성_해야하는_경우() {
		//given
		given(passwordGenerator.generateRandomPassword(20))
			.willReturn("1234");
		given(participantRoleRepository.existsByUserAndRoomAndRoomRoleIn(any(User.class), any(Room.class), anyList()))
			.willReturn(true);
		given(userReadService.findUserByEmailOrElseThrow(any(UserPrincipal.class)))
			.willReturn(User.builder().build());
		given(roomReadService.findRoomByIdOrElseThrow(anyLong()))
			.willReturn(new Room(1L, "강릉으로떠나요", LocalDate.now(), LocalDate.now().plusDays(1L),
				null, "강릉", "12", LocalDateTime.now().minusDays(1L).plusSeconds(1L)
				, null));

		//when
		String inviteCode = roomService.makeInviteCode(makeUserPrincipal(), 1L);

		//then
		assertThat(inviteCode).isEqualTo("1234");
	}

	@Test
	void 해당_유저의_코드를_처음_생성해_초대코드를_생성해야_하는_경우() {
		//given
		given(passwordGenerator.generateRandomPassword(20))
			.willReturn("1234");
		given(participantRoleRepository.existsByUserAndRoomAndRoomRoleIn(any(User.class), any(Room.class), anyList()))
			.willReturn(true);
		given(userReadService.findUserByEmailOrElseThrow(any(UserPrincipal.class)))
			.willReturn(User.builder().build());
		given(roomReadService.findRoomByIdOrElseThrow(anyLong()))
			.willReturn(makeRoom());

		//when
		String inviteCode = roomService.makeInviteCode(makeUserPrincipal(), 1L);

		//then
		assertThat(inviteCode).isEqualTo("1234");
	}

	@Test
	void 방에_들어갔는데_코드가_만료된_경우() {
		//given
		given(roomReadService.getRoomUsingInviteCode(anyString()))
			.willReturn(makeInvalidInviteDateRoom());

		//when,then
		assertThatThrownBy(() -> {roomService.checkRoomInviteCode(makeUserPrincipal(), "1234");})
			.isInstanceOf(InvalidInviteCode.class);
	}

	@Test
	void 초대한_방_링크로_접속했을때_총무_역할을_선택한_경우() {
		//given
		given(roomReadService.getRoomUsingInviteCode(anyString()))
			.willReturn(makeRoom());
		given(userReadService.findUserByEmailOrElseThrow(any(UserPrincipal.class)))
			.willReturn(makeUser());
		given(roomRepository.existsUserInRoom(anyString(), anyLong()))
			.willReturn(false);

		//when, then
		assertThatThrownBy(() -> {roomService.inviteUser(makeUserPrincipal(), "1234", List.of("ADMIN"));})
			.isInstanceOf(UserHaveNotPrivilegeException.class);
	}

	@Test
	void 초대한_방_링크로_접속했을때_존재하지_않은_역할을_선택한_경우() {
		//given
		given(roomReadService.getRoomUsingInviteCode(anyString()))
			.willReturn(makeRoom());
		given(userReadService.findUserByEmailOrElseThrow(any(UserPrincipal.class)))
			.willReturn(makeUser());
		given(roomRepository.existsUserInRoom(anyString(), anyLong()))
			.willReturn(false);

		//when, thend .
		assertThatThrownBy(() -> {roomService.inviteUser(makeUserPrincipal(), "1234", List.of("HAECHAN"));})
			.isInstanceOf(UserHaveNotPrivilegeException.class);
	}
	private static UserPrincipal makeUserPrincipal() {
		return new UserPrincipal(1L, "haechan@naver.com", "1234", null);
	}

	private static Room makeRoom() {
		return new Room(1L, "강릉으로떠나요", LocalDate.now(), LocalDate.now().plusDays(1L),
			1L, "강릉", null, null
			, null);
	}

	private static Room makeInvalidInviteDateRoom() {
		return new Room(1L, "강릉으로떠나요", LocalDate.now(), LocalDate.now().plusDays(1L),
			1L, "강릉", "1234", LocalDateTime.now().minusDays(2L)
			, null);
	}

	private static User makeUser() {
		return new User(1L, "해찬", "haechan@naver.com", "1234", Role.USER, "1234", "1234", LocalDate.now(),
			Provider.local, "1234", "1234");
	}
}