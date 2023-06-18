package com.travel.role.unit.room.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.room.repository.RoomParticipantRepository;
import com.travel.role.domain.room.service.ParticipantRoleReadService;
import com.travel.role.domain.room.service.RoomInviteService;
import com.travel.role.domain.room.service.RoomReadService;
import com.travel.role.domain.user.entity.User;
import com.travel.role.domain.user.service.UserReadService;
import com.travel.role.global.exception.room.InvalidInviteCode;
import com.travel.role.global.exception.room.UserHaveNotPrivilegeException;
import com.travel.role.global.util.PasswordGenerator;

@ExtendWith(MockitoExtension.class)
class RoomInviteServiceTest {

	@InjectMocks
	private RoomInviteService roomInviteService;

	@Mock
	private UserReadService userReadService;

	@Mock
	private RoomReadService roomReadService;

	@Mock
	private ParticipantRoleReadService participantRoleReadService;

	@Mock
	private RoomParticipantRepository roomParticipantRepository;

	@Mock
	private PasswordGenerator passwordGenerator;

	@Test
	void 해당_유저의_코드가_유효기간이_지나_재생성_해야하는_경우() {
		//given
		given(passwordGenerator.generateRandomPassword(20))
			.willReturn("1234");
		given(userReadService.findUserByEmailOrElseThrow(anyString()))
			.willReturn(User.builder().build());
		given(roomReadService.findRoomByIdOrElseThrow(anyLong()))
			.willReturn(new Room(1L, "강릉으로떠나요", LocalDate.now(), LocalDate.now().plusDays(1L),
				null, "강릉", "12", LocalDateTime.now().minusDays(1L).plusSeconds(1L)));
		doNothing().when(participantRoleReadService)
			.validIsAdmin(any(User.class), any(Room.class));

		//when
		String inviteCode = roomInviteService.makeInviteCode("haechan@naver.com", 1L);

		//then
		assertThat(inviteCode).isEqualTo("1234");
	}

	@Test
	void 해당_유저의_코드를_처음_생성해_초대코드를_생성해야_하는_경우() {
		//given
		given(passwordGenerator.generateRandomPassword(20))
			.willReturn("1234");
		given(userReadService.findUserByEmailOrElseThrow(anyString()))
			.willReturn(User.builder().build());
		given(roomReadService.findRoomByIdOrElseThrow(anyLong()))
			.willReturn(makeRoom());
		doNothing().when(participantRoleReadService)
			.validIsAdmin(any(User.class), any(Room.class));

		//when
		String inviteCode = roomInviteService.makeInviteCode("haechan@naver.com", 1L);

		//then
		assertThat(inviteCode).isEqualTo("1234");
	}

	@Test
	void 방에_들어갔는데_코드가_만료된_경우() {
		//given
		given(roomReadService.getRoomUsingInviteCode(anyString()))
			.willReturn(makeInvalidInviteDateRoom());

		//when,then
		assertThatThrownBy(() -> {
			roomInviteService.checkRoomInviteCode("haechan@naver.com", "1234");
		})
			.isInstanceOf(InvalidInviteCode.class);
	}

	@Test
	void 초대한_방_링크로_접속했을때_총무_역할을_선택한_경우() {
		//given
		given(roomReadService.getRoomUsingInviteCode(anyString()))
			.willReturn(makeRoom());
		given(userReadService.findUserByEmailOrElseThrow(anyString()))
			.willReturn(makeUser());
		given(roomParticipantRepository.existsUserInRoom(anyString(), anyLong()))
			.willReturn(false);

		//when, then
		assertThatThrownBy(() -> {
			roomInviteService.inviteUser("haechan@naver.com", "1234", List.of("ADMIN"));
		})
			.isInstanceOf(UserHaveNotPrivilegeException.class);
	}

	@Test
	void 초대한_방_링크로_접속했을때_존재하지_않은_역할을_선택한_경우() {
		//given
		given(roomReadService.getRoomUsingInviteCode(anyString()))
			.willReturn(makeRoom());
		given(userReadService.findUserByEmailOrElseThrow(anyString()))
			.willReturn(makeUser());
		given(roomParticipantRepository.existsUserInRoom(anyString(), anyLong()))
			.willReturn(false);

		//when, then
		assertThatThrownBy(() -> {
			roomInviteService.inviteUser("haechan@naver.com", "1234", List.of("HAECHAN"));
		})
			.isInstanceOf(UserHaveNotPrivilegeException.class);
	}

	private static Room makeRoom() {
		return new Room(1L, "강릉으로떠나요", LocalDate.now(), LocalDate.now().plusDays(1L),
			1L, "강릉", null, null);
	}

	private static Room makeInvalidInviteDateRoom() {
		return new Room(1L, "강릉으로떠나요", LocalDate.now(), LocalDate.now().plusDays(1L),
			1L, "강릉", "1234", LocalDateTime.now().minusDays(2L));
	}

	private static User makeUser() {
		return new User(1L, "해찬", "haechan@naver.com", "1234", null, LocalDate.now());
	}

}
