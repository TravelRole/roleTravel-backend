package com.travel.role.domain.user.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {
	private String email;
	private String password;
}
