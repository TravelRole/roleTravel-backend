package com.travel.role.domain.user.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class SignUpRequestDTO {
	private String name;
	private String email;
	private String password;
	private Integer year;
	private Integer month;
	private Integer day;

	private String profile;
	private String expiration;
	private UserCheckDTO check;
}
