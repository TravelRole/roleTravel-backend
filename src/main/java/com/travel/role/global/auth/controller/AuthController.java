package com.travel.role.global.auth.controller;

import static com.travel.role.global.auth.service.RefreshTokenCookieProvider.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travel.role.domain.user.dto.SignUpResponseDTO;
import com.travel.role.global.auth.dto.AccessTokenRequestDTO;
import com.travel.role.global.auth.dto.TokenResponse;
import com.travel.role.domain.user.dto.LoginRequestDTO;
import com.travel.role.domain.user.dto.SignUpRequestDTO;
import com.travel.role.global.auth.dto.TokenMapping;
import com.travel.role.global.auth.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;


	@PostMapping("/signup")
	public SignUpResponseDTO signUp(@RequestBody SignUpRequestDTO signUpRequestDTO) {
		return authService.signUp(signUpRequestDTO);
	}

	@PostMapping("/login")
	public ResponseEntity<TokenResponse> signIn(@RequestBody LoginRequestDTO loginRequestDTO) {
		TokenMapping tokenResult = authService.signIn(loginRequestDTO);

		TokenResponse authResponse = new TokenResponse(tokenResult.getAccessToken(), tokenResult.getRefreshToken());

		return ResponseEntity.ok()
			.body(authResponse);
	}

	@PostMapping("/refresh")
	public ResponseEntity<TokenResponse> refresh(
		@CookieValue(value = REFRESH_TOKEN, required = false) String refreshToken,
		@RequestBody AccessTokenRequestDTO token) {
		TokenResponse result = authService.refresh(refreshToken, token.getAccessToken());
		return ResponseEntity.ok()
			.body(result);
	}
}
