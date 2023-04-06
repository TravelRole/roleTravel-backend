package com.travel.role.domain.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class CheckIdRequest {

	@Email(message = "이메일 형식이 아닙니다.")
	@NotBlank(message = "값이 비어있으면 안됩니다.")
	String email;
}
