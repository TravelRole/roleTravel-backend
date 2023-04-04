package com.travel.role.domain.user.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ConfirmUserRequestDTO {

	@NotBlank(message = "값이 비어있으면 안됩니다.")
	private String name;

	@NotBlank(message = "값이 비어있으면 안됩니다.")
	@JsonFormat(pattern = "yyyy/MM/dd")
	private LocalDate birth;
}
