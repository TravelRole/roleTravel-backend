package com.travel.role.domain.user.dto.auth;

import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.travel.role.global.exception.ExceptionMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDTO {

	@NotBlank(message = ExceptionMessage.USERNAME_NOT_EMPTY)
	private String name;

	@NotBlank(message = "이메일 값이 비어있으면 안됩니다.")
	@Email(message = "이메일 형식이 아닙니다.")
	private String email;

	@NotBlank(message = ExceptionMessage.PASSWORD_NOT_EMPTY)
	private String password;
	@JsonFormat(pattern = "yyyy/MM/dd")
	private LocalDate birth;
}
