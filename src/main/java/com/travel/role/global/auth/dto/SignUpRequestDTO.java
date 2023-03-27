package com.travel.role.global.auth.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class SignUpRequestDTO {
	private String name;
	private String email;
	private String password;
	private LocalDate birth;
	private String profile;
}
