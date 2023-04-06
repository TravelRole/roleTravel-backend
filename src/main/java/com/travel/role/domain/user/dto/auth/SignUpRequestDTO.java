package com.travel.role.domain.user.dto.auth;

import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class SignUpRequestDTO {

	@NotBlank(message = "값이 비어있으면 안됩니다.")
	private String name;

	@NotBlank(message = "값이 비어있으면 안됩니다.")
	@Email(message = "이메일 형식이 아닙니다.")
	private String email;

	@NotBlank(message = "값이 비어있으면 안됩니다.")
	private String password;
	@JsonFormat(pattern = "yyyy/MM/dd")
	private LocalDate birth;
}
