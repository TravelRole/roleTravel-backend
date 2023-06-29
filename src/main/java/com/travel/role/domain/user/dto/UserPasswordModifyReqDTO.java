package com.travel.role.domain.user.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.travel.role.global.exception.dto.ExceptionMessage;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserPasswordModifyReqDTO {

	@NotBlank(message = ExceptionMessage.PASSWORD_NOT_EMPTY)
	private String password;
	@Pattern(regexp = "^[a-zA-Z\\d_\\-\\!]{8,16}$", message = ExceptionMessage.PASSWORD_PATTERN)
	private String newPassword;
	@NotBlank(message = "비밀번호 확인은 필수값 입니다.")
	private String newPasswordCheck;

}
