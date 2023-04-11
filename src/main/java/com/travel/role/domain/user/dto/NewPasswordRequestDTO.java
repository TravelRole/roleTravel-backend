package com.travel.role.domain.user.dto;

import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class NewPasswordRequestDTO {
	@NotBlank(message = "값이 비어있으면 안됩니다.")
	private String name;

	@NotBlank(message = "값이 비어있으면 안됩니다.")
	@Email(message = "이메일 형식이 아닙니다.")
	private String email;

	@NotNull(message = "값이 비어있으면 안됩니다.")
	@JsonFormat(pattern = "yyyy/MM/dd")
	private LocalDate birth;
}
