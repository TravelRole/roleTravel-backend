package com.travel.role.domain.user.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ConfirmUserRequestDTO {
	private String name;

	@JsonFormat(pattern = "yyyyMMdd")
	private LocalDate birth;
}
