package com.travel.role.unit.travelessential.controller;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.travel.role.domain.travelessential.dto.request.TravelEssentialCheckReqDTO;
import com.travel.role.domain.travelessential.dto.request.TravelEssentialDeleteReqDTO;
import com.travel.role.domain.travelessential.dto.request.TravelEssentialItemDTO;
import com.travel.role.domain.travelessential.dto.request.TravelEssentialReqDTO;
import com.travel.role.domain.travelessential.dto.response.TravelEssentialResDTO;
import com.travel.role.domain.travelessential.entity.EssentialCategory;
import com.travel.role.domain.travelessential.service.TravelEssentialReadService;
import com.travel.role.domain.travelessential.service.TravelEssentialService;
import com.travel.role.domain.user.entity.Provider;
import com.travel.role.domain.user.entity.User;
import com.travel.role.domain.user.repository.UserRepository;
import com.travel.role.global.auth.entity.AuthInfo;
import com.travel.role.global.auth.repository.AuthRepository;
import com.travel.role.global.exception.dto.ExceptionMessage;
import com.travel.role.global.exception.user.RoomInfoNotFoundException;
import com.travel.role.global.exception.user.UserNotParticipateRoomException;
import com.travel.role.unit.ControllerTestSupport;

@WithUserDetails(value = "test@email.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
class TravelEssentialControllerTest extends ControllerTestSupport {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthRepository authRepository;

	@MockBean
	private TravelEssentialService travelEssentialService;

	@MockBean
	private TravelEssentialReadService travelEssentialReadService;

	private Long userId;
	private Long authId;

	@BeforeEach
	void setUp() {
		User mockUser = createMockUser();
		userRepository.save(mockUser);
		userId = mockUser.getId();

		AuthInfo authInfo = AuthInfo.of(Provider.local, mockUser);
		authRepository.save(authInfo);
		authId = authInfo.getId();
	}

	@AfterEach
	void tearDown() {
		authRepository.deleteById(authId);
		userRepository.deleteById(userId);
	}

	@DisplayName("회원이 준비물을 입력했을 때")
	@Nested
	class CreateTravelEssentials {

		@DisplayName("입력이 올바르면 준비물이 생성된다.")
		@Test
		void createTravelEssentialsWithValidInput() throws Exception {

			//Given
			TravelEssentialReqDTO request = createTravelEssentialReqDTO(
				EssentialCategory.ESSENTIAL,
				"준비물1", "준비물2");

			//When & Then
			mockMvc.perform(post("/api/room/1/essentials")
					.contentType(MediaType.APPLICATION_JSON)
					.content(om.writeValueAsString(request))
					.characterEncoding("UTF-8"))
				.andExpect(status().isCreated());

			then(travelEssentialService).should(times(1))
				.createTravelEssentials(anyString(), anyLong(), any(TravelEssentialReqDTO.class));
		}

		@DisplayName("존재하지 않는 방이면 예외메시지를 응답한다.")
		@Test
		void createTravelEssentialsWithNotExistsRoom() throws Exception {

			//Given
			TravelEssentialReqDTO request = createTravelEssentialReqDTO(
				EssentialCategory.ESSENTIAL, "item");

			// Stub
			RoomInfoNotFoundException expectedException = new RoomInfoNotFoundException();
			willThrow(expectedException).given(travelEssentialService)
				.createTravelEssentials(anyString(), anyLong(), any(TravelEssentialReqDTO.class));

			//When & Then
			mockMvc.perform(post("/api/room/1/essentials")
					.contentType(MediaType.APPLICATION_JSON)
					.content(om.writeValueAsString(request))
					.characterEncoding("UTF-8"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value(ExceptionMessage.ROOM_NOT_FOUND))
				.andExpect(jsonPath("$.httpStatus").value(HttpStatus.NOT_FOUND.name()))
				.andExpect(jsonPath("$.time").exists());
		}

		@DisplayName("방에 속해 있지 않은 회원이면 예외메시지를 응답한다.")
		@Test
		void createTravelEssentialsWithNotParticipantUser() throws Exception {

			//Given
			TravelEssentialReqDTO request = createTravelEssentialReqDTO(
				EssentialCategory.ESSENTIAL, "item");

			// Stub
			UserNotParticipateRoomException expectedException = new UserNotParticipateRoomException();
			willThrow(expectedException).given(travelEssentialService)
				.createTravelEssentials(anyString(), anyLong(), any(TravelEssentialReqDTO.class));

			//When & Then
			mockMvc.perform(post("/api/room/1/essentials")
					.contentType(MediaType.APPLICATION_JSON)
					.content(om.writeValueAsString(request))
					.characterEncoding("UTF-8"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value(ExceptionMessage.USER_NOT_PARTICIPATE_ROOM))
				.andExpect(jsonPath("$.httpStatus").value(HttpStatus.UNAUTHORIZED.name()))
				.andExpect(jsonPath("$.time").exists());
		}

		@DisplayName("카테고리를 잘못 입력했다면 예외메시지를 응답한다.")
		@NullAndEmptySource
		@CsvSource({"invalid"})
		@ParameterizedTest
		void createTravelEssentialsWithInvalidCategory(String category) throws Exception {

			//Given
			Map<String, Object> request = category != null
				? Map.of("category", category, "items", List.of("aa"))
				: Map.of("items", List.of("aa"));

			//When & Then
			mockMvc.perform(post("/api/room/1/essentials")
					.contentType(MediaType.APPLICATION_JSON)
					.content(om.writeValueAsString(request))
					.characterEncoding("UTF-8"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(ExceptionMessage.INVALID_ESSENTIAL_CATEGORY))
				.andExpect(jsonPath("$.httpStatus").value(HttpStatus.BAD_REQUEST.name()))
				.andExpect(jsonPath("$.time").exists());

			then(travelEssentialService).shouldHaveNoInteractions();
		}

		@DisplayName("준비물 이름을 잘못 입력했다면 예외메시지를 응답한다.")
		@EmptySource
		@CsvSource({"aaaaa.aaaaa.aaaa"})
		@ParameterizedTest
		void createTravelEssentialsWithInvalidItemName(String itemName) throws Exception {

			//Given
			TravelEssentialReqDTO request = createTravelEssentialReqDTO(
				EssentialCategory.ESSENTIAL,
				itemName);

			//When & Then
			mockMvc.perform(post("/api/room/1/essentials")
					.contentType(MediaType.APPLICATION_JSON)
					.content(om.writeValueAsString(request))
					.characterEncoding("UTF-8"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(ExceptionMessage.INVALID_ESSENTIAL_ITEM_LENGTH))
				.andExpect(jsonPath("$.httpStatus").value(HttpStatus.BAD_REQUEST.name()))
				.andExpect(jsonPath("$.time").exists());

			then(travelEssentialService).shouldHaveNoInteractions();
		}

		@DisplayName("준비물 이름을 입력하지 않았다면 예외메시지를 응답한다.")
		@Test
		void createTravelEssentialsWithEmptyItemName() throws Exception {

			//Given
			TravelEssentialReqDTO request = createTravelEssentialReqDTO(
				EssentialCategory.ESSENTIAL);

			//When & Then
			mockMvc.perform(post("/api/room/1/essentials")
					.contentType(MediaType.APPLICATION_JSON)
					.content(om.writeValueAsString(request))
					.characterEncoding("UTF-8"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(ExceptionMessage.ESSENTIAL_NAME_NOT_EMPTY))
				.andExpect(jsonPath("$.httpStatus").value(HttpStatus.BAD_REQUEST.name()))
				.andExpect(jsonPath("$.time").exists());

			then(travelEssentialService).shouldHaveNoInteractions();
		}
	}

	@DisplayName("회원이 준비물을 조회했을 때")
	@Nested
	class ReadTravelEssentials {

		@DisplayName("정상적인 경우라면 준비물 목록을 반환한다.")
		@Test
		void readTravelEssentials() throws Exception {

			//Given
			TravelEssentialResDTO item1 = createTravelEssentialResDTO(1L, "item1", true);
			TravelEssentialResDTO item2 = createTravelEssentialResDTO(2L, "item2", false);
			Map<EssentialCategory, List<TravelEssentialResDTO>> response = createResponseMap(
				EssentialCategory.ESSENTIAL, List.of(item1, item2));

			// Stub
			given(travelEssentialReadService.readAllGroupByCategory(anyString(), anyLong()))
				.willReturn(response);

			//When & Then
			MvcResult result = mockMvc.perform(get("/api/room/1/essentials"))
				.andExpect(status().isOk())
				.andReturn();

			String resultContent = result.getResponse().getContentAsString();
			Map<EssentialCategory, List<TravelEssentialResDTO>> resultMap = om.readValue(resultContent,
				new TypeReference<>() {
				});

			assertThat(resultMap).containsEntry(
				EssentialCategory.ESSENTIAL, List.of(item1, item2)
			);

			then(travelEssentialReadService).should(times(1)).readAllGroupByCategory(anyString(), anyLong());
		}

		@DisplayName("존재하지 않는 방이면 예외메시지를 응답한다.")
		@Test
		void readTravelEssentialsWithNotExistsRoom() throws Exception {

			// Stub
			RoomInfoNotFoundException expectedException = new RoomInfoNotFoundException();
			willThrow(expectedException).given(travelEssentialReadService)
				.readAllGroupByCategory(anyString(), anyLong());

			//When & Then
			mockMvc.perform(get("/api/room/1/essentials"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value(ExceptionMessage.ROOM_NOT_FOUND))
				.andExpect(jsonPath("$.httpStatus").value(HttpStatus.NOT_FOUND.name()))
				.andExpect(jsonPath("$.time").exists());
		}

		@DisplayName("방에 속해 있지 않은 회원이면 예외메시지를 응답한다.")
		@Test
		void readTravelEssentialsWithNotParticipantUser() throws Exception {

			// Stub
			UserNotParticipateRoomException expectedException = new UserNotParticipateRoomException();
			willThrow(expectedException).given(travelEssentialReadService)
				.readAllGroupByCategory(anyString(), anyLong());

			//When & Then
			mockMvc.perform(get("/api/room/1/essentials"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.message").value(ExceptionMessage.USER_NOT_PARTICIPATE_ROOM))
				.andExpect(jsonPath("$.httpStatus").value(HttpStatus.UNAUTHORIZED.name()))
				.andExpect(jsonPath("$.time").exists());
		}

		@DisplayName("회원이 준비물을 삭제했을 때")
		@Nested
		class DeleteTravelEssentials {

			@DisplayName("정상적인 경우라면 준비물이 삭제된다.")
			@Test
			void deleteTravelEssentials() throws Exception {

				// Given
				List<Long> requestIds = List.of(1L, 2L);
				TravelEssentialDeleteReqDTO request = createTravelEssentialDeleteReqDTO(requestIds);

				//When & Then
				mockMvc.perform(delete("/api/room/1/essentials")
						.contentType(MediaType.APPLICATION_JSON)
						.content(om.writeValueAsString(request))
						.characterEncoding("UTF-8"))
					.andExpect(status().isOk());

				then(travelEssentialService).should(times(1))
					.deleteTravelEssentials(anyString(), anyLong(), any(TravelEssentialDeleteReqDTO.class));
			}

			@DisplayName("존재하지 않는 방이면 예외메시지를 응답한다.")
			@Test
			void deleteTravelEssentialsWithNotExistsRoom() throws Exception {

				// Given
				List<Long> requestIds = List.of(1L, 2L);
				TravelEssentialDeleteReqDTO request = createTravelEssentialDeleteReqDTO(requestIds);

				// Stub
				RoomInfoNotFoundException expectedException = new RoomInfoNotFoundException();
				willThrow(expectedException).given(travelEssentialService)
					.deleteTravelEssentials(anyString(), anyLong(), any(TravelEssentialDeleteReqDTO.class));

				//When & Then
				mockMvc.perform(delete("/api/room/1/essentials")
						.contentType(MediaType.APPLICATION_JSON)
						.content(om.writeValueAsString(request))
						.characterEncoding("UTF-8"))
					.andExpect(status().isNotFound())
					.andExpect(jsonPath("$.message").value(ExceptionMessage.ROOM_NOT_FOUND))
					.andExpect(jsonPath("$.httpStatus").value(HttpStatus.NOT_FOUND.name()))
					.andExpect(jsonPath("$.time").exists());
			}

			@DisplayName("방에 속해 있지 않은 회원이면 예외메시지를 응답한다.")
			@Test
			void deleteTravelEssentialsWithNotParticipantUser() throws Exception {

				// Given
				List<Long> requestIds = List.of(1L, 2L);
				TravelEssentialDeleteReqDTO request = createTravelEssentialDeleteReqDTO(requestIds);

				// Stub
				UserNotParticipateRoomException expectedException = new UserNotParticipateRoomException();
				willThrow(expectedException).given(travelEssentialService)
					.deleteTravelEssentials(anyString(), anyLong(), any(TravelEssentialDeleteReqDTO.class));

				//When & Then
				mockMvc.perform(delete("/api/room/1/essentials")
						.contentType(MediaType.APPLICATION_JSON)
						.content(om.writeValueAsString(request))
						.characterEncoding("UTF-8"))
					.andExpect(status().isUnauthorized())
					.andExpect(jsonPath("$.message").value(ExceptionMessage.USER_NOT_PARTICIPATE_ROOM))
					.andExpect(jsonPath("$.httpStatus").value(HttpStatus.UNAUTHORIZED.name()))
					.andExpect(jsonPath("$.time").exists());
			}
		}

		@DisplayName("회원이 준비물을 체크했을 때")
		@Nested
		class CheckTravelEssentials {

			@DisplayName("정상적인 경우라면 준비물이 체크된다.")
			@Test
			void checkTravelEssentials() throws Exception {

				// Given
				List<Long> requestIds = List.of(1L, 2L);
				TravelEssentialCheckReqDTO request = createTravelEssentialCheckReqDTO(true,
					requestIds);

				//When & Then
				mockMvc.perform(patch("/api/room/1/essentials/check")
						.contentType(MediaType.APPLICATION_JSON)
						.content(om.writeValueAsString(request))
						.characterEncoding("UTF-8"))
					.andExpect(status().isOk());

				then(travelEssentialService).should(times(1))
					.updateCheckTravelEssentials(anyString(), anyLong(), any(TravelEssentialCheckReqDTO.class));
			}

			@DisplayName("존재하지 않는 방이면 예외메시지를 응답한다.")
			@Test
			void checkTravelEssentialsWithNotExistsRoom() throws Exception {

				// Given
				List<Long> requestIds = List.of(1L, 2L);
				TravelEssentialCheckReqDTO request = createTravelEssentialCheckReqDTO(true,
					requestIds);

				// Stub
				RoomInfoNotFoundException expectedException = new RoomInfoNotFoundException();
				willThrow(expectedException).given(travelEssentialService)
					.updateCheckTravelEssentials(anyString(), anyLong(), any(TravelEssentialCheckReqDTO.class));

				//When & Then
				mockMvc.perform(patch("/api/room/1/essentials/check")
						.contentType(MediaType.APPLICATION_JSON)
						.content(om.writeValueAsString(request))
						.characterEncoding("UTF-8"))
					.andExpect(status().isNotFound())
					.andExpect(jsonPath("$.message").value(ExceptionMessage.ROOM_NOT_FOUND))
					.andExpect(jsonPath("$.httpStatus").value(HttpStatus.NOT_FOUND.name()))
					.andExpect(jsonPath("$.time").exists());
			}

			@DisplayName("방에 속해 있지 않은 회원이면 예외메시지를 응답한다.")
			@Test
			void checkTravelEssentialsWithNotParticipantUser() throws Exception {

				// Given
				List<Long> requestIds = List.of(1L, 2L);
				TravelEssentialCheckReqDTO request = createTravelEssentialCheckReqDTO(true,
					requestIds);

				// Stub
				UserNotParticipateRoomException expectedException = new UserNotParticipateRoomException();
				willThrow(expectedException).given(travelEssentialService)
					.updateCheckTravelEssentials(anyString(), anyLong(), any(TravelEssentialCheckReqDTO.class));

				//When & Then
				mockMvc.perform(patch("/api/room/1/essentials/check")
						.contentType(MediaType.APPLICATION_JSON)
						.content(om.writeValueAsString(request))
						.characterEncoding("UTF-8"))
					.andExpect(status().isUnauthorized())
					.andExpect(jsonPath("$.message").value(ExceptionMessage.USER_NOT_PARTICIPATE_ROOM))
					.andExpect(jsonPath("$.httpStatus").value(HttpStatus.UNAUTHORIZED.name()))
					.andExpect(jsonPath("$.time").exists());
			}

			@DisplayName("어떤 상태로 변경할 것인지 입력되지 않았다면 예외메시지를 응답한다.")
			@Test
			void checkTravelEssentialsWithEmptyStatus() throws Exception {

				// Given
				List<Long> requestIds = List.of(1L, 2L);
				TravelEssentialCheckReqDTO request = createTravelEssentialCheckReqDTO(null,
					requestIds);

				//When & Then
				mockMvc.perform(patch("/api/room/1/essentials/check")
						.contentType(MediaType.APPLICATION_JSON)
						.content(om.writeValueAsString(request))
						.characterEncoding("UTF-8"))
					.andExpect(status().isBadRequest())
					.andExpect(jsonPath("$.message").value(ExceptionMessage.ESSENTIAL_CHECK_STATUS_NOT_EMPTY))
					.andExpect(jsonPath("$.httpStatus").value(HttpStatus.BAD_REQUEST.name()))
					.andExpect(jsonPath("$.time").exists());

				then(travelEssentialService).shouldHaveNoInteractions();
			}
		}
	}

	private User createMockUser() {

		return User.builder()
			.name("name")
			.email("test@email.com")
			.build();
	}

	private Map<EssentialCategory, List<TravelEssentialResDTO>> createResponseMap(EssentialCategory category,
		List<TravelEssentialResDTO> travelEssentialResDTOS) {

		return Map.of(category, travelEssentialResDTOS);
	}

	private TravelEssentialResDTO createTravelEssentialResDTO(Long id, String itemName, boolean checked) {

		return new TravelEssentialResDTO(id, itemName, checked);
	}

	private TravelEssentialReqDTO createTravelEssentialReqDTO(EssentialCategory category, String... itemNames) {

		List<TravelEssentialItemDTO> travelEssentialItemDTOs = createTravelEssentialItemDTOs(itemNames);

		return TravelEssentialReqDTO.builder()
			.category(category)
			.items(travelEssentialItemDTOs)
			.build();
	}

	private List<TravelEssentialItemDTO> createTravelEssentialItemDTOs(String... itemNames) {

		return Arrays.stream(itemNames).map(TravelEssentialItemDTO::new)
			.collect(Collectors.toList());
	}

	private TravelEssentialDeleteReqDTO createTravelEssentialDeleteReqDTO(List<Long> ids) {

		return new TravelEssentialDeleteReqDTO(ids);
	}

	private TravelEssentialCheckReqDTO createTravelEssentialCheckReqDTO(Boolean check, List<Long> ids) {

		return new TravelEssentialCheckReqDTO(check, ids);
	}
}
