package com.travel.role.global.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenMapping {
	private String userEmail;
	private String accessToken;
	private String refreshToken;
}
