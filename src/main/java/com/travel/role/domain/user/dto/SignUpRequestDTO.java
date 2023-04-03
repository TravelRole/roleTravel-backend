package com.travel.role.domain.user.dto;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class SignUpRequestDTO {
	private String name;
	private String email;
	private String password;
	@JsonFormat(pattern = "yyyyMMdd")
	private LocalDate birth;
	private String profile;
	private String expiration;
	private UserCheckDTO check;
}
