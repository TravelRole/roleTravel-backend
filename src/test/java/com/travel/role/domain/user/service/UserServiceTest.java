package com.travel.role.domain.user.service;

import static com.travel.role.global.exception.ExceptionMessage.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.travel.role.domain.user.dao.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserService userService;

	@Test
	void 회원가입_하지않은_경우에_정보를_요청() {
		// given
		given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());

		// when, then
		assertThatThrownBy(() -> {
			userService.getBasicProfile("haechan@naver.com");
		}).isInstanceOf(UsernameNotFoundException.class)
			.hasMessageContaining(USERNAME_NOT_FOUND);
	}
}