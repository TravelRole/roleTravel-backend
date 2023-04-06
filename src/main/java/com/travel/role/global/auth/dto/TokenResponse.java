package com.travel.role.global.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {
	private String accessToken;
	private String refreshToken;
	private final String tokenType = "Bearer";
}
