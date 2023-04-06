package com.travel.role.domain.user.dto.auth;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class LoginRequestDTO {

	@NotBlank(message = "값이 비어있으면 안됩니다.")
	@Email(message = "이메일 형식이 아닙니다.")
	private String email;

	@NotBlank(message = "값이 비어있으면 안됩니다.")
	private String password;
}
