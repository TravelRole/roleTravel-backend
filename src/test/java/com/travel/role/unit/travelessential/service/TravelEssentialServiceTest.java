package com.travel.role.unit.travelessential.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.room.repository.RoomRepository;
import com.travel.role.domain.room.service.RoomParticipantReadService;
import com.travel.role.domain.room.service.RoomReadService;
import com.travel.role.domain.travelessential.dto.request.TravelEssentialCheckReqDTO;
import com.travel.role.domain.travelessential.dto.request.TravelEssentialDeleteReqDTO;
import com.travel.role.domain.travelessential.dto.request.TravelEssentialItemDTO;
import com.travel.role.domain.travelessential.dto.request.TravelEssentialReqDTO;
import com.travel.role.domain.travelessential.dto.response.TravelEssentialResDTO;
import com.travel.role.domain.travelessential.entity.EssentialCategory;
import com.travel.role.domain.travelessential.entity.TravelEssential;
import com.travel.role.domain.travelessential.repository.TravelEssentialRepository;
import com.travel.role.domain.travelessential.service.TravelEssentialService;
import com.travel.role.domain.user.entity.User;
import com.travel.role.domain.user.repository.UserRepository;
import com.travel.role.domain.user.service.UserReadService;
import com.travel.role.global.exception.user.RoomInfoNotFoundException;
import com.travel.role.global.exception.user.UserInfoNotFoundException;
import com.travel.role.global.exception.user.UserNotParticipateRoomException;
import com.travel.role.unit.ServiceTestSupport;

public class TravelEssentialServiceTest extends ServiceTestSupport {

	@Autowired
	private TravelEssentialService travelEssentialService;

	@SpyBean
	private TravelEssentialRepository travelEssentialRepository;

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private UserRepository userRepository;

	@MockBean
	private RoomReadService roomReadService;

	@MockBean
	private RoomParticipantReadService roomParticipantReadService;

	@MockBean
	private UserReadService userReadService;

	@DisplayName("여행 준비물 생성시")
	@Nested
	class CreateTravelEssentials {

		private User loginUser;
		private Room targetRoom;

		@BeforeEach
		void setUp() {

			loginUser = createUser();
			targetRoom = createRoom();

			userRepository.save(loginUser);
			roomRepository.save(targetRoom);
		}

		@AfterEach
		void tearDown() {
			travelEssentialRepository.deleteAllInBatch();
			roomRepository.deleteAllInBatch();
			userRepository.deleteAllInBatch();
		}

		@DisplayName("방에 참여한 회원이 요청한 경우, 준비물이 생성된다.")
		@Test
		void withParticipantUser() {

			// Given
			String email = loginUser.getEmail();
			Long roomId = targetRoom.getId();
			EssentialCategory category = EssentialCategory.ESSENTIAL;
			List<String> items = List.of("준비물1", "준비물2", "준비물3");
			TravelEssentialReqDTO travelEssentialReqDTO = createTravelEssentialReqDTO(category,
				items);

			// Stub
			given(userReadService.findUserByEmailOrElseThrow(anyString()))
				.willReturn(loginUser);
			given(roomReadService.findRoomByIdOrElseThrow(anyLong()))
				.willReturn(targetRoom);
			willDoNothing()
				.given(roomParticipantReadService)
				.checkParticipant(any(User.class), any(Room.class));

			// When
			travelEssentialService.createTravelEssentials(email, roomId, travelEssentialReqDTO);

			// Then
			Map<EssentialCategory, List<TravelEssentialResDTO>> resultMap = travelEssentialRepository.readAllGroupByEssentialCategory(
				loginUser.getId(), roomId);

			assertThat(resultMap)
				.containsOnlyKeys(category);
			assertThat(resultMap.get(category))
				.extracting("itemName", "isChecked")
				.containsExactlyInAnyOrder(
					tuple(items.get(0), false),
					tuple(items.get(1), false),
					tuple(items.get(2), false)
				);
		}

		@DisplayName("회원이 존재하지 않는 경우, 예외가 발생한다.")
		@Test
		void withNotExistsUser() {

			// Given
			String email = "asd@naver.com";
			Long roomId = targetRoom.getId();
			EssentialCategory category = EssentialCategory.ESSENTIAL;
			List<String> items = List.of("준비물1", "준비물2", "준비물3");
			TravelEssentialReqDTO travelEssentialReqDTO = createTravelEssentialReqDTO(category,
				items);

			// Stub
			UserInfoNotFoundException expectedException = new UserInfoNotFoundException();
			given(userReadService.findUserByEmailOrElseThrow(anyString()))
				.willThrow(expectedException);

			// When & Then
			assertThatThrownBy(
				() -> travelEssentialService.createTravelEssentials(email, roomId, travelEssentialReqDTO))
				.isInstanceOf(expectedException.getClass())
				.hasMessage(expectedException.getMessage());

			then(roomReadService).shouldHaveNoInteractions();
			then(roomParticipantReadService).shouldHaveNoInteractions();
			then(travelEssentialRepository).shouldHaveNoInteractions();
		}

		@DisplayName("방이 존재하지 않는 경우, 예외가 발생한다.")
		@Test
		void withNotExistsRoom() {

			// Given
			String email = loginUser.getEmail();
			Long roomId = targetRoom.getId() + 100;
			EssentialCategory category = EssentialCategory.ESSENTIAL;
			List<String> items = List.of("준비물1", "준비물2", "준비물3");
			TravelEssentialReqDTO travelEssentialReqDTO = createTravelEssentialReqDTO(category,
				items);

			// Stub
			given(userReadService.findUserByEmailOrElseThrow(anyString()))
				.willReturn(loginUser);

			RoomInfoNotFoundException expectedException = new RoomInfoNotFoundException();
			given(roomReadService.findRoomByIdOrElseThrow(anyLong()))
				.willThrow(expectedException);

			// When & Then
			assertThatThrownBy(
				() -> travelEssentialService.createTravelEssentials(email, roomId, travelEssentialReqDTO))
				.isInstanceOf(expectedException.getClass())
				.hasMessage(expectedException.getMessage());

			then(roomParticipantReadService).shouldHaveNoInteractions();
			then(travelEssentialRepository).shouldHaveNoInteractions();
		}

		@DisplayName("방에 참여하지 않은 회원인 경우, 예외가 발생한다.")
		@Test
		void withNotParticipantUser() {

			// Given
			String email = loginUser.getEmail();
			Long roomId = targetRoom.getId();
			EssentialCategory category = EssentialCategory.ESSENTIAL;
			List<String> items = List.of("준비물1", "준비물2", "준비물3");
			TravelEssentialReqDTO travelEssentialReqDTO = createTravelEssentialReqDTO(category,
				items);

			// Stub
			given(userReadService.findUserByEmailOrElseThrow(anyString()))
				.willReturn(loginUser);
			given(roomReadService.findRoomByIdOrElseThrow(anyLong()))
				.willReturn(targetRoom);

			UserNotParticipateRoomException expectedException = new UserNotParticipateRoomException();
			willThrow(expectedException)
				.given(roomParticipantReadService)
				.checkParticipant(any(User.class), any(Room.class));

			// When & Then
			assertThatThrownBy(
				() -> travelEssentialService.createTravelEssentials(email, roomId, travelEssentialReqDTO))
				.isInstanceOf(expectedException.getClass())
				.hasMessage(expectedException.getMessage());

			then(travelEssentialRepository).shouldHaveNoInteractions();
		}
	}

	@DisplayName("여행 준비물 삭제시")
	@Nested
	class DeleteTravelEssentials {

		private User loginUser;
		private Room targetRoom;
		private TravelEssential savedTravelEssential;

		@BeforeEach
		void setUp() {

			loginUser = createUser();
			targetRoom = createRoom();

			userRepository.save(loginUser);
			roomRepository.save(targetRoom);

			savedTravelEssential = createTravelEssential(loginUser, targetRoom, EssentialCategory.ESSENTIAL, "item1",
				false);
			travelEssentialRepository.save(savedTravelEssential);
		}

		@AfterEach
		void tearDown() {
			travelEssentialRepository.deleteAllInBatch();
			roomRepository.deleteAllInBatch();
			userRepository.deleteAllInBatch();
		}

		@DisplayName("방에 참여한 회원이 존재하는 준비물 id 로 요청한 경우, 준비물이 삭제된다.")
		@Test
		void withParticipantUserAndExistsTravelEssential() {

			// Given
			String email = loginUser.getEmail();
			Long roomId = targetRoom.getId();
			Long savedTravelEssentialId = savedTravelEssential.getId();
			TravelEssentialDeleteReqDTO travelEssentialDeleteReqDTO = createTravelEssentialDeleteReqDTO(
				List.of(savedTravelEssentialId));

			// Stub
			given(userReadService.findUserByEmailOrElseThrow(anyString()))
				.willReturn(loginUser);
			given(roomReadService.findRoomByIdOrElseThrow(anyLong()))
				.willReturn(targetRoom);
			willDoNothing()
				.given(roomParticipantReadService)
				.checkParticipant(any(User.class), any(Room.class));

			// When
			travelEssentialService.deleteTravelEssentials(email, roomId, travelEssentialDeleteReqDTO);

			// Then
			Optional<TravelEssential> findTravelEssential = travelEssentialRepository.findById(savedTravelEssentialId);

			assertThat(findTravelEssential).isEmpty();
		}

		@DisplayName("회원이 존재하지 않는 경우, 예외가 발생한다.")
		@Test
		void withNotExistsUser() {

			// Given
			String email = "asd@naver.com";
			Long roomId = targetRoom.getId();
			Long savedTravelEssentialId = savedTravelEssential.getId();
			TravelEssentialDeleteReqDTO travelEssentialDeleteReqDTO = createTravelEssentialDeleteReqDTO(
				List.of(savedTravelEssentialId));

			// Stub
			RoomInfoNotFoundException expectedException = new RoomInfoNotFoundException();
			given(userReadService.findUserByEmailOrElseThrow(anyString()))
				.willThrow(expectedException);

			// When & Then
			assertThatThrownBy(
				() -> travelEssentialService.deleteTravelEssentials(email, roomId, travelEssentialDeleteReqDTO))
				.isInstanceOf(expectedException.getClass())
				.hasMessage(expectedException.getMessage());

			then(roomParticipantReadService).shouldHaveNoInteractions();
			then(travelEssentialRepository)
				.should(never())
				.deleteAllByRoomIdAndUserId(anyLong(), anyLong());
		}

		@DisplayName("방이 존재하지 않는 경우, 예외가 발생한다.")
		@Test
		void withNotExistsRoom() {

			// Given
			String email = loginUser.getEmail();
			Long roomId = targetRoom.getId() + 100;
			Long savedTravelEssentialId = savedTravelEssential.getId();
			TravelEssentialDeleteReqDTO travelEssentialDeleteReqDTO = createTravelEssentialDeleteReqDTO(
				List.of(savedTravelEssentialId));

			// Stub
			given(userReadService.findUserByEmailOrElseThrow(anyString()))
				.willReturn(loginUser);

			RoomInfoNotFoundException expectedException = new RoomInfoNotFoundException();
			given(roomReadService.findRoomByIdOrElseThrow(anyLong()))
				.willThrow(expectedException);

			// When & Then
			assertThatThrownBy(
				() -> travelEssentialService.deleteTravelEssentials(email, roomId, travelEssentialDeleteReqDTO))
				.isInstanceOf(expectedException.getClass())
				.hasMessage(expectedException.getMessage());

			then(roomParticipantReadService).shouldHaveNoInteractions();
			then(travelEssentialRepository)
				.should(never())
				.deleteAllByRoomIdAndUserId(anyLong(), anyLong());
		}

		@DisplayName("방에 참여하지 않은 회원인 경우, 예외가 발생한다.")
		@Test
		void withNotParticipantUser() {

			// Given
			String email = loginUser.getEmail();
			Long roomId = targetRoom.getId();
			Long savedTravelEssentialId = savedTravelEssential.getId();
			TravelEssentialDeleteReqDTO travelEssentialDeleteReqDTO = createTravelEssentialDeleteReqDTO(
				List.of(savedTravelEssentialId));

			// Stub
			given(userReadService.findUserByEmailOrElseThrow(anyString()))
				.willReturn(loginUser);
			given(roomReadService.findRoomByIdOrElseThrow(anyLong()))
				.willReturn(targetRoom);

			UserNotParticipateRoomException expectedException = new UserNotParticipateRoomException();
			willThrow(expectedException)
				.given(roomParticipantReadService)
				.checkParticipant(any(User.class), any(Room.class));

			// When & Then
			assertThatThrownBy(
				() -> travelEssentialService.deleteTravelEssentials(email, roomId, travelEssentialDeleteReqDTO))
				.isInstanceOf(expectedException.getClass())
				.hasMessage(expectedException.getMessage());

			then(travelEssentialRepository)
				.should(never())
				.deleteAllByRoomIdAndUserId(anyLong(), anyLong());
		}
	}

	@DisplayName("여행 준비물 체크시")
	@Nested
	class UpdateCheckTravelEssentials {

		private User loginUser;
		private Room targetRoom;
		private TravelEssential savedUncheckedTravelEssential;
		private TravelEssential savedCheckedTravelEssential;

		@BeforeEach
		void setUp() {

			loginUser = createUser();
			targetRoom = createRoom();

			userRepository.save(loginUser);
			roomRepository.save(targetRoom);

			savedCheckedTravelEssential = createTravelEssential(loginUser, targetRoom, EssentialCategory.ESSENTIAL,
				"item2",
				true);
			savedUncheckedTravelEssential = createTravelEssential(loginUser, targetRoom, EssentialCategory.ESSENTIAL,
				"item1",
				false);
			travelEssentialRepository.saveAll(List.of(savedCheckedTravelEssential, savedUncheckedTravelEssential));
		}

		@AfterEach
		void tearDown() {
			travelEssentialRepository.deleteAllInBatch();
			roomRepository.deleteAllInBatch();
			userRepository.deleteAllInBatch();
		}

		@DisplayName("방에 참여한 회원이 체크되지 않은 준비물 id 로 요청한 경우, 준비물이 체크 상태로 변경된다.")
		@Test
		void withParticipantUserAndUnCheckedTravelEssential() {

			// Given
			String email = loginUser.getEmail();
			Long roomId = targetRoom.getId();
			Long savedUncheckedTravelEssentialId = savedUncheckedTravelEssential.getId();
			TravelEssentialCheckReqDTO travelEssentialCheckReqDTO = createTravelEssentialCheckReqDTO(true,
				List.of(savedUncheckedTravelEssentialId));

			// Stub
			given(userReadService.findUserByEmailOrElseThrow(anyString()))
				.willReturn(loginUser);
			given(roomReadService.findRoomByIdOrElseThrow(anyLong()))
				.willReturn(targetRoom);
			willDoNothing()
				.given(roomParticipantReadService)
				.checkParticipant(any(User.class), any(Room.class));

			// When
			travelEssentialService.updateCheckTravelEssentials(email, roomId, travelEssentialCheckReqDTO);

			// Then
			TravelEssential changedTravelEssential = travelEssentialRepository.findById(savedUncheckedTravelEssentialId)
				.get();

			assertThat(changedTravelEssential.getIsChecked()).isTrue();
		}

		@DisplayName("방에 참여한 회원이 체크된 준비물 id 로 요청한 경우, 준비물이 체크 해제 상태로 변경된다.")
		@Test
		void withParticipantUserAndCheckedTravelEssential() {

			// Given
			String email = loginUser.getEmail();
			Long roomId = targetRoom.getId();
			Long savedCheckedTravelEssentialId = savedCheckedTravelEssential.getId();
			TravelEssentialCheckReqDTO travelEssentialCheckReqDTO = createTravelEssentialCheckReqDTO(false,
				List.of(savedCheckedTravelEssentialId));

			// Stub
			given(userReadService.findUserByEmailOrElseThrow(anyString()))
				.willReturn(loginUser);
			given(roomReadService.findRoomByIdOrElseThrow(anyLong()))
				.willReturn(targetRoom);
			willDoNothing()
				.given(roomParticipantReadService)
				.checkParticipant(any(User.class), any(Room.class));

			// When
			travelEssentialService.updateCheckTravelEssentials(email, roomId, travelEssentialCheckReqDTO);

			// Then
			TravelEssential changedTravelEssential = travelEssentialRepository.findById(savedCheckedTravelEssentialId)
				.get();

			assertThat(changedTravelEssential.getIsChecked()).isFalse();
		}

		@DisplayName("회원이 존재하지 않는 경우, 예외가 발생한다.")
		@Test
		void withNotExistsUser() {

			// Given
			String email = "asd@naver.com";
			Long roomId = targetRoom.getId();
			Long savedCheckedTravelEssentialId = savedCheckedTravelEssential.getId();
			TravelEssentialCheckReqDTO travelEssentialCheckReqDTO = createTravelEssentialCheckReqDTO(false,
				List.of(savedCheckedTravelEssentialId));

			// Stub
			RoomInfoNotFoundException expectedException = new RoomInfoNotFoundException();
			given(userReadService.findUserByEmailOrElseThrow(anyString()))
				.willThrow(expectedException);

			// When & Then
			assertThatThrownBy(
				() -> travelEssentialService.updateCheckTravelEssentials(email, roomId, travelEssentialCheckReqDTO))
				.isInstanceOf(expectedException.getClass())
				.hasMessage(expectedException.getMessage());

			then(roomParticipantReadService).shouldHaveNoInteractions();
			then(travelEssentialRepository)
				.should(never())
				.updateCheckEssentialsByUserAndRoomAndEssentialIds(anyLong(), anyLong(), anyList(), anyBoolean());
		}

		@DisplayName("방이 존재하지 않는 경우, 예외가 발생한다.")
		@Test
		void withNotExistsRoom() {

			// Given
			String email = loginUser.getEmail();
			Long roomId = targetRoom.getId() + 100;
			Long savedCheckedTravelEssentialId = savedCheckedTravelEssential.getId();
			TravelEssentialCheckReqDTO travelEssentialCheckReqDTO = createTravelEssentialCheckReqDTO(false,
				List.of(savedCheckedTravelEssentialId));

			// Stub
			given(userReadService.findUserByEmailOrElseThrow(anyString()))
				.willReturn(loginUser);

			RoomInfoNotFoundException expectedException = new RoomInfoNotFoundException();
			given(roomReadService.findRoomByIdOrElseThrow(anyLong()))
				.willThrow(expectedException);

			// When & Then
			assertThatThrownBy(
				() -> travelEssentialService.updateCheckTravelEssentials(email, roomId, travelEssentialCheckReqDTO))
				.isInstanceOf(expectedException.getClass())
				.hasMessage(expectedException.getMessage());

			then(roomParticipantReadService).shouldHaveNoInteractions();
			then(travelEssentialRepository)
				.should(never())
				.updateCheckEssentialsByUserAndRoomAndEssentialIds(anyLong(), anyLong(), anyList(), anyBoolean());
		}

		@DisplayName("방에 참여하지 않은 회원인 경우, 예외가 발생한다.")
		@Test
		void withNotParticipantUser() {

			// Given
			String email = loginUser.getEmail();
			Long roomId = targetRoom.getId();
			Long savedCheckedTravelEssentialId = savedCheckedTravelEssential.getId();
			TravelEssentialCheckReqDTO travelEssentialCheckReqDTO = createTravelEssentialCheckReqDTO(false,
				List.of(savedCheckedTravelEssentialId));

			// Stub
			given(userReadService.findUserByEmailOrElseThrow(anyString()))
				.willReturn(loginUser);
			given(roomReadService.findRoomByIdOrElseThrow(anyLong()))
				.willReturn(targetRoom);

			UserNotParticipateRoomException expectedException = new UserNotParticipateRoomException();
			willThrow(expectedException)
				.given(roomParticipantReadService)
				.checkParticipant(any(User.class), any(Room.class));

			// When & Then
			assertThatThrownBy(
				() -> travelEssentialService.updateCheckTravelEssentials(email, roomId, travelEssentialCheckReqDTO))
				.isInstanceOf(expectedException.getClass())
				.hasMessage(expectedException.getMessage());

			then(travelEssentialRepository)
				.should(never())
				.updateCheckEssentialsByUserAndRoomAndEssentialIds(anyLong(), anyLong(), anyList(), anyBoolean());
		}
	}

	private User createUser() {

		return User.builder()
			.email("email@naver.com")
			.name("홍길동")
			.build();
	}

	private Room createRoom() {

		return Room.builder()
			.roomName("room")
			.travelExpense(1000)
			.travelStartDate(LocalDate.of(2023, 1, 1))
			.travelEndDate(LocalDate.of(2023, 2, 1))
			.location("제주")
			.build();
	}

	private TravelEssentialReqDTO createTravelEssentialReqDTO(EssentialCategory category, List<String> items) {

		List<TravelEssentialItemDTO> itemDTOS = items.stream()
			.map(TravelEssentialItemDTO::new)
			.collect(Collectors.toList());

		return new TravelEssentialReqDTO(category, itemDTOS);
	}

	private TravelEssential createTravelEssential(User user, Room room, EssentialCategory category, String itemName,
		boolean isChecked) {

		return TravelEssential.builder()
			.user(user)
			.room(room)
			.category(category)
			.itemName(itemName)
			.isChecked(isChecked)
			.build();
	}

	private List<TravelEssential> createTravelEssentials(User user, Room room) {

		List<TravelEssential> travelEssentials = new ArrayList<>();

		for (int i = 0; i < 5; i++) {
			travelEssentials.add(
				createTravelEssential(user, room, EssentialCategory.ESSENTIAL, "item" + i, false)
			);
		}

		return travelEssentials;
	}

	private TravelEssentialDeleteReqDTO createTravelEssentialDeleteReqDTO(List<Long> ids) {

		return new TravelEssentialDeleteReqDTO(ids);
	}

	private TravelEssentialCheckReqDTO createTravelEssentialCheckReqDTO(boolean check, List<Long> ids) {

		return new TravelEssentialCheckReqDTO(check, ids);
	}
}
