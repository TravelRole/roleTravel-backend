package com.travel.role.domain.user.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class NewPasswordRequestDTO {
	private String name;
	private String email;

	@JsonFormat(pattern = "yyyyMMdd")
	private LocalDate birth;
}
