package com.travel.role.domain.room.service;

import static com.travel.role.global.exception.ExceptionMessage.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.travel.role.domain.room.dao.ParticipantRoleRepository;
import com.travel.role.domain.room.dao.RoomRepository;
import com.travel.role.domain.room.domain.Room;
import com.travel.role.domain.room.dto.InviteRequestDTO;
import com.travel.role.domain.room.dto.MakeRoomRequestDTO;
import com.travel.role.domain.room.exception.AlreadyExistInRoomException;
import com.travel.role.domain.room.exception.InvalidInviteCode;
import com.travel.role.domain.room.exception.InvalidLocalDateException;
import com.travel.role.domain.room.exception.UserHaveNotPrivilegeException;
import com.travel.role.domain.user.dao.UserRepository;
import com.travel.role.domain.user.domain.Provider;
import com.travel.role.domain.user.domain.Role;
import com.travel.role.domain.user.domain.User;
import com.travel.role.domain.user.exception.UserInfoNotFoundException;
import com.travel.role.global.auth.token.UserPrincipal;
import com.travel.role.global.exception.ExceptionMessage;
import com.travel.role.global.util.PasswordGenerator;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

	@InjectMocks
	private RoomService roomService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private RoomRepository roomRepository;

	@Mock
	private PasswordGenerator passwordGenerator;

	@Mock
	private ParticipantRoleRepository participantRoleRepository;

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
	void 해당하는_유저가_존재하지_않는_경우() {
		// given
		given(userRepository.findByEmail(anyString()))
			.willReturn(Optional.empty());

		// when, then
		assertThatThrownBy(() -> roomService.makeRoom(makeUserPrincipal(), getMakeRoomRequestDTO()))
			.isInstanceOf(UserInfoNotFoundException.class)
			.hasMessageContaining(USERNAME_NOT_FOUND);
	}

	@Test
	void 해당_유저의_코드가_유효기간이_지나_재생성_해야하는_경우() {
		//given
		given(passwordGenerator.generateRandomPassword(20))
			.willReturn("1234");
		given(participantRoleRepository.existsByUserAndRoomAndRoomRoleIn(any(User.class), any(Room.class), anyList()))
			.willReturn(true);
		given(userRepository.findByEmail(anyString()))
			.willReturn(Optional.of(User.builder().build()));
		given(roomRepository.findById(anyLong()))
			.willReturn(Optional.of(Room.builder().build()));
		given(roomRepository.findById(anyLong()))
			.willReturn(Optional.of(new Room(1L, "강릉으로떠나요", LocalDate.now(), LocalDate.now().plusDays(1L),
				null, "강릉", "12", LocalDateTime.now().minusDays(1L).plusSeconds(1L)
				, null)));

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
		given(userRepository.findByEmail(anyString()))
			.willReturn(Optional.of(User.builder().build()));
		given(roomRepository.findById(anyLong()))
			.willReturn(Optional.of(Room.builder().build()));
		given(roomRepository.findById(anyLong()))
			.willReturn(Optional.of(makeRoom()));

		//when
		String inviteCode = roomService.makeInviteCode(makeUserPrincipal(), 1L);

		//then
		assertThat(inviteCode).isEqualTo("1234");
	}

	@Test
	void 방의_초대코드가_유효하지_않은_경우() {
		//given
		given(roomRepository.findByRoomInviteCode(anyString()))
			.willReturn(Optional.empty());

		//when,then
		assertThatThrownBy(() -> {roomService.checkRoomInviteCode(makeUserPrincipal(), "1234");})
			.isInstanceOf(InvalidInviteCode.class);
	}

	@Test
	void 방에_들어갔는데_코드가_만료된_경우() {
		//given
		given(roomRepository.findByRoomInviteCode(anyString()))
			.willReturn(Optional.of(makeInvalidInviteDateRoom()));

		//when,then
		assertThatThrownBy(() -> {roomService.checkRoomInviteCode(makeUserPrincipal(), "1234");})
			.isInstanceOf(InvalidInviteCode.class);
	}

	@Test
	void 방에_이미_들어가있는_유저인_경우() {
		//given
		given(roomRepository.findByRoomInviteCode(anyString()))
			.willReturn(Optional.of(makeRoom()));
		given(roomRepository.existsUserInRoom(anyString(), anyLong()))
			.willReturn(true);

		//when,then
		assertThatThrownBy(() -> {roomService.checkRoomInviteCode(makeUserPrincipal(), "1234");})
			.isInstanceOf(AlreadyExistInRoomException.class);
	}

	@Test
	void 초대한_방_링크로_접속했을때_총무_역할을_선택한_경우() {
		//given
		given(roomRepository.findByRoomInviteCode(anyString()))
			.willReturn(Optional.of(makeRoom()));
		given(userRepository.findByEmail(anyString()))
			.willReturn(Optional.of(makeUser()));
		given(roomRepository.existsUserInRoom(anyString(), anyLong()))
			.willReturn(false);
		List<String> selectRole = List.of("ADMIN");

		//when, then
		assertThatThrownBy(() -> {roomService.inviteUser(makeUserPrincipal(), "1234", new InviteRequestDTO(selectRole));})
			.isInstanceOf(UserHaveNotPrivilegeException.class);
	}

	private static MakeRoomRequestDTO getMakeRoomRequestDTO() {
		return new MakeRoomRequestDTO("여행 가자~", LocalDate.of(2023, 1, 1),
			LocalDate.of(2023, 1, 3), "강원도 춘천", 1L);
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