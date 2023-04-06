package com.travel.role.domain.user.dto.auth;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class SignUpRequestDTO {
	private String name;
	private String email;
	private String password;
	@DateTimeFormat(pattern = "yyyyMMdd")
	private LocalDate birth;
	private String profile;
	private String expiration;
	private UserCheckDTO check;
}
