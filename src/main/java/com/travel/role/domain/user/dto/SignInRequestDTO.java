package com.travel.role.domain.user.dto;

import lombok.Data;

@Data
public class SignInRequestDTO {
	private String email;
	private String password;
}
