package com.travel.role.unit.travelessential.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
import com.travel.role.domain.travelessential.dto.response.TravelEssentialResDTO;
import com.travel.role.domain.travelessential.entity.EssentialCategory;
import com.travel.role.domain.travelessential.entity.TravelEssential;
import com.travel.role.domain.travelessential.repository.TravelEssentialRepository;
import com.travel.role.domain.travelessential.service.TravelEssentialReadService;
import com.travel.role.domain.user.entity.User;
import com.travel.role.domain.user.repository.UserRepository;
import com.travel.role.domain.user.service.UserReadService;
import com.travel.role.global.exception.user.RoomInfoNotFoundException;
import com.travel.role.global.exception.user.UserNotParticipateRoomException;
import com.travel.role.unit.ServiceTestSupport;

public class TravelEssentialReadServiceTest extends ServiceTestSupport {

	@Autowired
	private TravelEssentialReadService travelEssentialReadService;

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

	@DisplayName("여행 준비물을 그룹별로 조회시")
	@Nested
	class ReadAllGroupByCategory {

		private User loginUser;
		private Room targetRoom;

		List<TravelEssential> travelEssentialsWithClothesCategory;
		List<TravelEssential> travelEssentialsWithAboardCategory;
		List<TravelEssential> travelEssentialsWithEtcCategory;

		@BeforeEach
		void setUp() {

			loginUser = createUser();
			targetRoom = createRoom();

			userRepository.save(loginUser);
			roomRepository.save(targetRoom);

			travelEssentialsWithClothesCategory = createTravelEssentials(loginUser, targetRoom,
				EssentialCategory.CLOTHES, 5);

			travelEssentialsWithAboardCategory = createTravelEssentials(loginUser, targetRoom,
				EssentialCategory.ABOARD, 5);

			travelEssentialsWithEtcCategory = createTravelEssentials(loginUser, targetRoom,
				EssentialCategory.ETC, 5);

			travelEssentialRepository.saveAll(travelEssentialsWithClothesCategory);
			travelEssentialRepository.saveAll(travelEssentialsWithAboardCategory);
			travelEssentialRepository.saveAll(travelEssentialsWithEtcCategory);
		}

		@AfterEach
		void tearDown() {
			travelEssentialRepository.deleteAllInBatch();
			roomRepository.deleteAllInBatch();
			userRepository.deleteAllInBatch();
		}

		@DisplayName("방에 참여한 회원이 요청한 경우, 준비물들이 카테고리별로 조회된다.")
		@Test
		void withParticipantUser() {

			// Given
			String email = loginUser.getEmail();
			Long roomId = targetRoom.getId();

			// Stub
			given(userReadService.findUserByEmailOrElseThrow(anyString()))
				.willReturn(loginUser);
			given(roomReadService.findRoomByIdOrElseThrow(anyLong()))
				.willReturn(targetRoom);
			willDoNothing()
				.given(roomParticipantReadService)
				.checkParticipant(any(User.class), any(Room.class));

			// When
			Map<EssentialCategory, List<TravelEssentialResDTO>> resultMap = travelEssentialReadService.readAllGroupByCategory(
				email, roomId);

			// Then
			List<TravelEssentialResDTO> resDTOSWithClothesCategory = resultMap.get(EssentialCategory.CLOTHES);
			List<TravelEssentialResDTO> resDTOSWithAboardCategory = resultMap.get(EssentialCategory.ABOARD);
			List<TravelEssentialResDTO> resDTOSWithEtcCategory = resultMap.get(EssentialCategory.ETC);

			assertThat(resDTOSWithClothesCategory)
				.hasSize(travelEssentialsWithClothesCategory.size())
				.extracting("itemName")
				.asList()
				.isEqualTo(
					travelEssentialsWithClothesCategory.stream()
						.map(TravelEssential::getItemName)
						.collect(Collectors.toList())
				);

			assertThat(resDTOSWithAboardCategory)
				.hasSize(travelEssentialsWithAboardCategory.size())
				.extracting("itemName")
				.asList()
				.isEqualTo(
					travelEssentialsWithAboardCategory.stream()
						.map(TravelEssential::getItemName)
						.collect(Collectors.toList())
				);

			assertThat(resDTOSWithEtcCategory)
				.hasSize(travelEssentialsWithEtcCategory.size())
				.extracting("itemName")
				.asList()
				.isEqualTo(
					travelEssentialsWithEtcCategory.stream()
						.map(TravelEssential::getItemName)
						.collect(Collectors.toList())
				);
		}

		@DisplayName("회원이 존재하지 않는 경우, 예외가 발생한다.")
		@Test
		void withNotExistsUser() {

			// Given
			String email = "asd@naver.com";
			Long roomId = targetRoom.getId();

			// Stub
			RoomInfoNotFoundException expectedException = new RoomInfoNotFoundException();
			given(userReadService.findUserByEmailOrElseThrow(anyString()))
				.willThrow(expectedException);

			// When & Then
			assertThatThrownBy(
				() -> travelEssentialReadService.readAllGroupByCategory(email, roomId))
				.isInstanceOf(expectedException.getClass())
				.hasMessage(expectedException.getMessage());

			then(roomParticipantReadService).shouldHaveNoInteractions();
			then(travelEssentialRepository)
				.should(never())
				.readAllGroupByEssentialCategory(anyLong(), anyLong());
		}

		@DisplayName("방이 존재하지 않는 경우, 예외가 발생한다.")
		@Test
		void withNotExistsRoom() {

			// Given
			String email = loginUser.getEmail();
			Long roomId = targetRoom.getId() + 100;

			// Stub
			given(userReadService.findUserByEmailOrElseThrow(anyString()))
				.willReturn(loginUser);

			RoomInfoNotFoundException expectedException = new RoomInfoNotFoundException();
			given(roomReadService.findRoomByIdOrElseThrow(anyLong()))
				.willThrow(expectedException);

			// When & Then
			assertThatThrownBy(
				() -> travelEssentialReadService.readAllGroupByCategory(email, roomId))
				.isInstanceOf(expectedException.getClass())
				.hasMessage(expectedException.getMessage());

			then(roomParticipantReadService).shouldHaveNoInteractions();
			then(travelEssentialRepository)
				.should(never())
				.readAllGroupByEssentialCategory(anyLong(), anyLong());
		}

		@DisplayName("방에 참여하지 않은 회원인 경우, 예외가 발생한다.")
		@Test
		void withNotParticipantUser() {

			// Given
			String email = loginUser.getEmail();
			Long roomId = targetRoom.getId();

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
				() -> travelEssentialReadService.readAllGroupByCategory(email, roomId))
				.isInstanceOf(expectedException.getClass())
				.hasMessage(expectedException.getMessage());

			then(travelEssentialRepository)
				.should(never())
				.readAllGroupByEssentialCategory(anyLong(), anyLong());
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

	private List<TravelEssential> createTravelEssentials(User user, Room room, EssentialCategory category, int size) {

		List<TravelEssential> travelEssentials = new ArrayList<>();
		Random random = new Random();

		for (int i = 0; i < size; i++) {
			travelEssentials.add(
				createTravelEssential(user, room, category, "item" + random.nextInt(9999999), false)
			);
		}

		return travelEssentials;
	}
}
