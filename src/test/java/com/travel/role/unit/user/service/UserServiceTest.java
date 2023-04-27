package com.travel.role.unit.user.service;

import static com.travel.role.global.exception.dto.ExceptionMessage.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.travel.role.domain.user.dto.UserPasswordModifyReqDTO;
import com.travel.role.domain.user.dto.UserProfileDetailResDTO;
import com.travel.role.domain.user.dto.UserProfileModifyReqDTO;
import com.travel.role.domain.user.entity.Provider;
import com.travel.role.domain.user.entity.User;
import com.travel.role.domain.user.service.UserReadService;
import com.travel.role.domain.user.service.UserService;
import com.travel.role.global.exception.user.InputValueNotMatchException;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@InjectMocks
	private UserService userService;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private UserReadService userReadService;

	@Test
	void 회원정보_상세조회_성공() {
		// given
		String email = "email@naver.com";
		User user = User.builder()
			.id(1L)
			.name("name")
			.email(email)
			.birth(LocalDate.of(2000, 10, 10))
			.profile("imageUrl")
			.provider(Provider.local)
			.build();
		given(userReadService.findUserByEmailOrElseThrow(email))
			.willReturn(user);

		// when
		UserProfileDetailResDTO resDTO = userService.getUserProfile(email);

		// then
		assertThat(resDTO.getUserId()).isEqualTo(user.getId());
		assertThat(resDTO.getName()).isEqualTo(user.getName());
		assertThat(resDTO.getEmail()).isEqualTo(user.getEmail());
		assertThat(resDTO.getBirth()).isEqualTo(user.getBirth());
		assertThat(resDTO.getProfile()).isEqualTo(user.getProfile());
		assertThat(resDTO.getProvider()).isEqualTo(user.getProvider().name());
	}

	@Test
	void 회원_프로필_수정_성공() {
		// given
		String email = "aaaa@naver.com";
		User user = User.builder()
			.name("name")
			.birth(LocalDate.of(2000, 10, 10))
			.provider(Provider.local)
			.build();

		UserProfileModifyReqDTO reqDTO = new UserProfileModifyReqDTO(
			"modified", "2000/10/10"
		);
		given(userReadService.findUserByEmailOrElseThrow(email)).willReturn(user);

		// when
		UserProfileDetailResDTO resDTO = userService.modifyUserProfile(email, reqDTO);

		// then
		assertThat(resDTO.getName()).isEqualTo(reqDTO.getName());
		assertThat(resDTO.getBirth()).isEqualTo(reqDTO.getBirth());
	}

	@Test
	void 회원_비밀번호_수정시_기존_비밀번호를_잘못_입력() {
		// given
		String email = "aaaa@naver.com";
		String password = "password";
		String inputPassword = "inputPassword";
		User user = User.builder()
			.email(email)
			.password(password)
			.build();

		UserPasswordModifyReqDTO reqDTO =
			new UserPasswordModifyReqDTO(inputPassword, "pwd", "pwd");
		given(userReadService.findUserByEmailOrElseThrow(email)).willReturn(user);
		given(passwordEncoder.matches(inputPassword, password)).willThrow(
			new BadCredentialsException(INVALID_PASSWORD)
		);

		// when & then
		assertThatThrownBy(() -> {
			userService.modifyPassword(email, reqDTO);
		}).isInstanceOf(BadCredentialsException.class)
			.hasMessage(INVALID_PASSWORD);
	}

	@Test
	void 회원_비밀번호_수정시_새로운_비밀번호_입력과_비밀번호_확인이_일치하지_않을때() {
		// given
		String email = "aaaa@naver.com";
		String password = "password";
		String inputPassword = "inputPassword";
		User user = User.builder()
			.email(email)
			.password(password)
			.build();

		UserPasswordModifyReqDTO reqDTO =
			new UserPasswordModifyReqDTO(inputPassword, "pwd", "pwdd");
		given(userReadService.findUserByEmailOrElseThrow(email)).willReturn(user);

		// when & then
		assertThatThrownBy(() -> {
			userService.modifyPassword(email, reqDTO);
		}).isInstanceOf(InputValueNotMatchException.class)
			.hasMessage(String.format(INPUT_VALUE_NOT_MATCH, "비밀번호", "비밀번호 확인"));
	}
}
