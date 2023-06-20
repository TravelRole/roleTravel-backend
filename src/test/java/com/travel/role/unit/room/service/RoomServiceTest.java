package com.travel.role.unit.room.service;

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
import com.travel.role.domain.room.dto.request.RoomModifiedRequestDTO;
import com.travel.role.domain.room.dto.request.RoomRoleDTO;
import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.room.entity.RoomParticipant;
import com.travel.role.domain.room.entity.RoomRole;
import com.travel.role.domain.room.repository.ParticipantRoleRepository;
import com.travel.role.domain.room.service.RoomParticipantReadService;
import com.travel.role.domain.room.service.RoomReadService;
import com.travel.role.domain.room.service.RoomService;
import com.travel.role.domain.user.entity.User;
import com.travel.role.domain.user.service.UserReadService;
import com.travel.role.domain.wantplace.dto.request.WantPlaceRequestDTO;
import com.travel.role.domain.wantplace.entity.WantPlace;
import com.travel.role.domain.wantplace.repository.WantPlaceRepository;
import com.travel.role.domain.wantplace.service.WantPlaceService;
import com.travel.role.global.exception.dto.ExceptionMessage;
import com.travel.role.global.exception.room.InvalidLocalDateException;
import com.travel.role.global.exception.room.UserHaveNotPrivilegeException;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

	@InjectMocks
	private RoomService roomService;
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
	void 방_수정시_어드민_유저가_아닐경우() {
		// given
		given(participantRoleRepository.existsByUserEmailAndRoomIdAndRole(anyString(), anyLong(), any(RoomRole.class)))
			.willReturn(false);

		// when, then
		assertThatThrownBy(() -> roomService.modifyRoomInfo("haechan@naver.com",
			createWrongRoomModifiedDTO(LocalDate.now(), LocalDate.now().plusDays(1)), 1L))
			.isInstanceOf(UserHaveNotPrivilegeException.class);
	}

	private static RoomModifiedRequestDTO createWrongRoomModifiedDTO(LocalDate startDate, LocalDate endDate) {
		RoomRoleDTO roomRoleDTO1 = new RoomRoleDTO("haechan@naver.com", List.of(RoomRole.ADMIN));
		RoomRoleDTO roomRoleDTO2 = new RoomRoleDTO("chan@naver.com", List.of(RoomRole.ADMIN));
		List<RoomRoleDTO> roomRoleDTOS = List.of(roomRoleDTO1, roomRoleDTO2);
		return new RoomModifiedRequestDTO("경주로 고고", "경주", startDate, endDate, roomRoleDTOS);
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
			User user1 = new User(1L, "kh", "asd@gmail.com", "1234", null, LocalDate.now());
			User user2 = new User(2L, "hk", "asdd@gmail.com", "1234", null, LocalDate.now());

			Room room2 = new Room(1L, "1번 방", LocalDate.now(), LocalDate.now(), null, "제주", "1234",
				LocalDateTime.now());

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
				123.0, 456.0, "섬", "색당로2314", 12345L, "www.naver.com");
		}
	}
}