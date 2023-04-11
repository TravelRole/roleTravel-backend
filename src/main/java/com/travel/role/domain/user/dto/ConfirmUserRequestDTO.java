package com.travel.role.domain.user.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmUserRequestDTO {

	@NotBlank(message = "값이 비어있으면 안됩니다.")
	private String name;

	@NotNull(message = "값이 비어있으면 안됩니다.")
	@JsonFormat(pattern = "yyyy/MM/dd")
	private LocalDate birth;
}
