package com.travel.role.unit.auth.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.travel.role.domain.user.dto.auth.SignUpRequestDTO;
import com.travel.role.domain.user.entity.Role;
import com.travel.role.domain.user.entity.User;
import com.travel.role.domain.user.repository.UserRepository;
import com.travel.role.global.auth.service.AuthService;
import com.travel.role.global.auth.service.TokenProvider;
import com.travel.role.global.exception.auth.InvalidTokenException;
import com.travel.role.global.exception.auth.NotExistTokenException;
import com.travel.role.global.exception.dto.ExceptionMessage;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@InjectMocks
	private AuthService authService;
	@Mock
	private UserRepository userRepository;
	@Mock
	private TokenProvider tokenProvider;

	@Test
	void 토큰없이_액세스토큰을_재발급_받으려는_경우_예외_발생() {
		// given,when,then
		assertThatThrownBy(() -> authService.refresh(null))
			.isInstanceOf(NotExistTokenException.class)
			.hasMessageContaining(ExceptionMessage.NOT_EXISTS_TOKEN);
	}

	@Test
	void 리프레시토큰을_가지고있는_유저가_없는경우_예외_발생() {
		//given
		given(userRepository.findByRefreshToken(anyString()))
			.willReturn(Optional.empty());
		//when, then
		assertThatThrownBy(() -> authService.refresh("refreshToken"))
			.isInstanceOf(InvalidTokenException.class)
			.hasMessageContaining(ExceptionMessage.INVALID_TOKEN);
	}

	@Test
	void 액세스토큰의_유효시간이_지나지않았는데_토큰을_새로발급받을_경우_예외_발생() {
		//given
		final String refreshToken = "refreshToken";
		final String accessToken = "accessToken";

		given(userRepository.findByRefreshToken(anyString()))
			.willReturn(Optional.of(createUser()));

		given(tokenProvider.getTokenExpiration(accessToken))
			.willReturn(20L);

		//when,then
		assertThatThrownBy(() -> authService.refresh(refreshToken))
			.isInstanceOf(InvalidTokenException.class)
			.hasMessageContaining(ExceptionMessage.INVALID_TOKEN);
	}

	@Test
	void 리프레시토큰의_유효시간이_지났는데_토큰을_재발급받을경우_예외_발생() {
		//given
		final String refreshToken = "refreshToken";
		final String accessToken = "accessToken";

		doReturn(Optional.of(createUser())).when(userRepository)
				.findByRefreshToken(refreshToken);

		given(tokenProvider.getTokenExpiration(refreshToken))
			.willReturn(-20L);
		//when,then
		assertThatThrownBy(() -> authService.refresh(refreshToken))
			.isInstanceOf(InvalidTokenException.class)
			.hasMessageContaining(ExceptionMessage.INVALID_TOKEN);
	}

	private SignUpRequestDTO createSignUpRequestDTO() {
		SignUpRequestDTO signUpRequestDTO = new SignUpRequestDTO();
		signUpRequestDTO.setEmail("chan@naver.com");
		signUpRequestDTO.setName("김철수");
		signUpRequestDTO.setPassword("12342");
		signUpRequestDTO.setBirth(LocalDate.now());
		return signUpRequestDTO;
	}

	private User createUser() {
		return User.builder()
			.id(1L)
			.email("chan@naver.com")
			.name("김철수")
			.password("$2a$10$RmFajfEsgvXwpJLl7GmKR.0OI5GaH6gb1XsZlvBVuruFZj852loyC")
			.role(Role.USER)
			.refreshToken("refreshToken").build();
	}
}