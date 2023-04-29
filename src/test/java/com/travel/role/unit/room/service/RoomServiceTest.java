package com.travel.role.unit.room.service;

import com.travel.role.domain.room.dto.request.MakeRoomRequestDTO;
import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.room.entity.RoomParticipant;
import com.travel.role.domain.room.repository.ParticipantRoleRepository;
import com.travel.role.domain.room.repository.RoomRepository;
import com.travel.role.domain.room.service.RoomParticipantReadService;
import com.travel.role.domain.room.service.RoomReadService;
import com.travel.role.domain.room.service.RoomService;
import com.travel.role.domain.user.entity.Provider;
import com.travel.role.domain.user.entity.Role;
import com.travel.role.domain.user.entity.User;
import com.travel.role.domain.user.service.UserReadService;
import com.travel.role.domain.wantplace.dto.request.WantPlaceRequestDTO;
import com.travel.role.domain.wantplace.entity.WantPlace;
import com.travel.role.domain.wantplace.repository.WantPlaceRepository;
import com.travel.role.domain.wantplace.service.WantPlaceService;
import com.travel.role.global.auth.token.UserPrincipal;
import com.travel.role.global.exception.dto.ExceptionMessage;
import com.travel.role.global.exception.room.InvalidInviteCode;
import com.travel.role.global.exception.room.InvalidLocalDateException;
import com.travel.role.global.exception.room.UserHaveNotPrivilegeException;
import com.travel.role.global.util.PasswordGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

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
		given(userReadService.findUserByEmailOrElseThrow(anyString()))
			.willReturn(User.builder().build());
		given(roomReadService.findRoomByIdOrElseThrow(anyLong()))
			.willReturn(new Room(1L, "강릉으로떠나요", LocalDate.now(), LocalDate.now().plusDays(1L),
				null, "강릉", "12", LocalDateTime.now().minusDays(1L).plusSeconds(1L)));

		//when
		String inviteCode = roomService.makeInviteCode("haechan@naver.com", 1L);

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
		given(userReadService.findUserByEmailOrElseThrow(anyString()))
			.willReturn(User.builder().build());
		given(roomReadService.findRoomByIdOrElseThrow(anyLong()))
			.willReturn(makeRoom());

		//when
		String inviteCode = roomService.makeInviteCode("haechan@naver.com", 1L);

		//then
		assertThat(inviteCode).isEqualTo("1234");
	}

	@Test
	void 방에_들어갔는데_코드가_만료된_경우() {
		//given
		given(roomReadService.getRoomUsingInviteCode(anyString()))
			.willReturn(makeInvalidInviteDateRoom());

		//when,then
		assertThatThrownBy(() -> {roomService.checkRoomInviteCode("haechan@naver.com", "1234");})
			.isInstanceOf(InvalidInviteCode.class);
	}

	@Test
	void 초대한_방_링크로_접속했을때_총무_역할을_선택한_경우() {
		//given
		given(roomReadService.getRoomUsingInviteCode(anyString()))
			.willReturn(makeRoom());
		given(userReadService.findUserByEmailOrElseThrow(anyString()))
			.willReturn(makeUser());
		given(roomRepository.existsUserInRoom(anyString(), anyLong()))
			.willReturn(false);

		//when, then
		assertThatThrownBy(() -> {roomService.inviteUser("haechan@naver.com", "1234", List.of("ADMIN"));})
			.isInstanceOf(UserHaveNotPrivilegeException.class);
	}

	@Test
	void 초대한_방_링크로_접속했을때_존재하지_않은_역할을_선택한_경우() {
		//given
		given(roomReadService.getRoomUsingInviteCode(anyString()))
			.willReturn(makeRoom());
		given(userReadService.findUserByEmailOrElseThrow(anyString()))
			.willReturn(makeUser());
		given(roomRepository.existsUserInRoom(anyString(), anyLong()))
			.willReturn(false);

		//when, thend .
		assertThatThrownBy(() -> {roomService.inviteUser("haechan@naver.com", "1234", List.of("HAECHAN"));})
			.isInstanceOf(UserHaveNotPrivilegeException.class);
	}
	private static UserPrincipal makeUserPrincipal() {
		return new UserPrincipal(1L, "haechan@naver.com", "1234", null);
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
		return new User(1L, "해찬", "haechan@naver.com", "1234", Role.USER, "1234", "1234", LocalDate.now(),
			Provider.local, "1234", "1234");
	}

	@ExtendWith(MockitoExtension.class)
	static
	class WantPlaceServiceTest {

		@Mock
		private UserReadService userReadService;

		@Mock
		private WantPlaceRepository wantPlaceRepository;

		@InjectMocks
		private WantPlaceService wantPlaceService;
		@Mock
		private RoomReadService roomReadService;
		@Mock
		private RoomParticipantReadService roomParticipantReadService;

		@Test
		void 가고싶은_장소_추가_성공() {
			// given
			User user1 = new User(1L, "kh", "asd@gmail.com", "1234", null, null, null, LocalDate.now(),
					null, null, null);
			User user2 = new User(2L, "hk", "asdd@gmail.com", "1234", null, null, null, LocalDate.now(),
					null, null, null);


			Room room2 = new Room(1L, "1번 방", LocalDate.now(), LocalDate.now(), null, "제주", "1234", LocalDateTime.now());

			RoomParticipant roomParticipant1 = new RoomParticipant(1L, LocalDateTime.now(), true, user1, room2);
			RoomParticipant roomParticipant2 = new RoomParticipant(1L, LocalDateTime.now(), true, user2, room2);

			Room room = new Room(1L, "1번 방", LocalDate.now(), LocalDate.now(), null, "제주", "1234", LocalDateTime.now());

			WantPlace wantPlace = WantPlace.of(room, getWantPlaceRequestDto());

			given(userReadService.findUserByEmailOrElseThrow(anyString()))
					.willReturn(user2);
			given(roomReadService.findRoomByIdOrElseThrow(anyLong()))
					.willReturn(room);
			doNothing()
				.when(roomParticipantReadService).checkParticipant(any(User.class), any(Room.class));

			// when
			wantPlaceService.addWantPlace("asdd@gmail.com", getWantPlaceRequestDto());

			// then
			assertThat(wantPlace.getPlaceName()).isEqualTo("제주도");
			assertThat(wantPlace.getPhoneNumber()).isEqualTo("1234");
			assertThat(wantPlace.getRoom()).isEqualTo(room);
			assertThat(wantPlace.getLatitude()).isEqualTo(123.0);
			assertThat(wantPlace.getLongitude()).isEqualTo(456.0);
			verify(wantPlaceRepository, times(1)).save(any(WantPlace.class));
		}

		private static WantPlaceRequestDTO getWantPlaceRequestDto() {
			return new WantPlaceRequestDTO(
				1L, "제주도", "제주도", "1234",
				123.0, 456.0);
		}
	}
}