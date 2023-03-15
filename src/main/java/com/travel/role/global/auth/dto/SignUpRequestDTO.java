package com.travel.role.global.auth.dto;

import lombok.Data;

@Data
public class SignUpRequestDTO {
	private String nickname;

	private String email;

	private String password;
}
