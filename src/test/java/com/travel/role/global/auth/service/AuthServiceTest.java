package com.travel.role.global.auth.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.travel.role.domain.user.dao.UserRepository;
import com.travel.role.domain.user.domain.Role;
import com.travel.role.domain.user.domain.UserEntity;
import com.travel.role.domain.user.dto.auth.SignUpRequestDTO;

import com.travel.role.global.auth.exception.InvalidTokenException;
import com.travel.role.global.auth.exception.NotExistTokenException;
import com.travel.role.global.exception.ExceptionMessage;
import com.travel.role.domain.user.exception.AlreadyExistUserException;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@InjectMocks
	private AuthService authService;
	@Mock
	private UserRepository userRepository;
	@Mock
	private TokenProvider tokenProvider;

	@Test
	void 이미_존재하는_이메일로_회원가입을_진행할때_예외_발생() {
		// given
		SignUpRequestDTO signUpRequestDTO = createSignUpRequestDTO();
		given(userRepository.existsByEmail(anyString())).willReturn(true);

		// when, then
		assertThatThrownBy(() -> authService.signUp(signUpRequestDTO))
			.isInstanceOf(AlreadyExistUserException.class)
				.hasMessageContaining(ExceptionMessage.ALREADY_EXIST_USER);
	}

	@Test
	void 토큰없이_액세스토큰을_재발급_받으려는_경우_예외_발생() {
		// given,when,then
		assertThatThrownBy(() -> authService.refresh(null, "token"))
			.isInstanceOf(NotExistTokenException.class)
			.hasMessageContaining(ExceptionMessage.NOT_EXISTS_TOKEN);

		assertThatThrownBy(() -> authService.refresh("token", null))
			.isInstanceOf(NotExistTokenException.class)
			.hasMessageContaining(ExceptionMessage.NOT_EXISTS_TOKEN);

		assertThatThrownBy(() -> authService.refresh(null, null))
			.isInstanceOf(NotExistTokenException.class)
			.hasMessageContaining(ExceptionMessage.NOT_EXISTS_TOKEN);
	}

	@Test
	void 리프레시토큰을_가지고있는_유저가_없는경우_예외_발생() {
		//given
		given(userRepository.findByRefreshToken(anyString()))
			.willReturn(Optional.empty());
		//when, then
		assertThatThrownBy(() -> authService.refresh("refreshToken", "accessToken"))
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
		assertThatThrownBy(() -> authService.refresh(refreshToken, accessToken))
			.isInstanceOf(InvalidTokenException.class)
			.hasMessageContaining(ExceptionMessage.INVALID_TOKEN);
	}

	@Test
	void 리프레시토큰의_유효시간이_지났는데_토큰을_재발급받을경우_예외_발생() {
		//given
		final String refreshToken = "refreshToken";
		final String accessToken = "accessToken";

		given(userRepository.findByRefreshToken(anyString()))
			.willReturn(Optional.of(createUser()));

		given(tokenProvider.getTokenExpiration(accessToken))
			.willReturn(-20L);

		given(tokenProvider.getTokenExpiration(refreshToken))
			.willReturn(-20L);
		//when,then
		assertThatThrownBy(() -> authService.refresh(refreshToken, accessToken))
			.isInstanceOf(InvalidTokenException.class)
			.hasMessageContaining(ExceptionMessage.INVALID_TOKEN);
	}

	private SignUpRequestDTO createSignUpRequestDTO() {
		SignUpRequestDTO signUpRequestDTO = new SignUpRequestDTO();
		signUpRequestDTO.setEmail("chan@naver.com");
		signUpRequestDTO.setName("김철수");
		signUpRequestDTO.setPassword("12342");
		return signUpRequestDTO;
	}

	private UserEntity createUser() {
		return UserEntity.builder()
			.id(1L)
			.email("chan@naver.com")
			.name("김철수")
			.password("$2a$10$RmFajfEsgvXwpJLl7GmKR.0OI5GaH6gb1XsZlvBVuruFZj852loyC")
			.role(Role.USER)
			.refreshToken("refreshToken").build();
	}
}