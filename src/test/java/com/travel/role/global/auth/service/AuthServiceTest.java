package com.travel.role.global.auth.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.travel.role.domain.user.dao.UserRepository;
import com.travel.role.global.auth.dto.SignUpRequestDTO;
import com.travel.role.global.auth.exception.NotExistTokenException;
import com.travel.role.global.exception.ExceptionMessage;
import com.travel.role.global.exception.user.AlreadyExistUserException;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@InjectMocks
	private AuthService authService;
	@Mock
	private UserRepository userRepository;

	@Test
	void 이미_존재하는_이메일로_회원가입을_진행할때() {
		// given
		SignUpRequestDTO signUpRequestDTO = createSignUpRequestDTO();
		given(userRepository.existsByEmail(anyString())).willReturn(true);

		// when, then
		assertThatThrownBy(() -> authService.signUp(signUpRequestDTO))
			.isInstanceOf(AlreadyExistUserException.class)
				.hasMessageContaining(ExceptionMessage.ALREADY_EXIST_USER);
	}

	@Test
	void 토큰없이_액세스토큰을_재발급_받으려는_경우() {
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

	private SignUpRequestDTO createSignUpRequestDTO() {
		SignUpRequestDTO signUpRequestDTO = new SignUpRequestDTO();
		signUpRequestDTO.setEmail("chan@naver.com");
		signUpRequestDTO.setName("김철수");
		signUpRequestDTO.setPassword("12342");
		return signUpRequestDTO;
	}
}