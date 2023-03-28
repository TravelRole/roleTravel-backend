package com.travel.role.global.auth.controller;

import static com.travel.role.global.auth.service.RefreshTokenCookieProvider.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travel.role.global.auth.dto.AccessTokenRequestDTO;
import com.travel.role.global.auth.dto.AccessTokenResponse;
import com.travel.role.domain.user.dto.SignInRequestDTO;
import com.travel.role.domain.user.dto.SignUpRequestDTO;
import com.travel.role.global.auth.dto.TokenMapping;
import com.travel.role.global.auth.service.AuthService;
import com.travel.role.global.auth.service.RefreshTokenCookieProvider;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;
	private final RefreshTokenCookieProvider refreshTokenCookieProvider;

	@PostMapping("/signup")
	public ResponseEntity<?> signUp(@RequestBody SignUpRequestDTO signUpRequestDTO) {
		return authService.signUp(signUpRequestDTO);
	}

	@PostMapping("/login")
	public ResponseEntity<AccessTokenResponse> signIn(@RequestBody SignInRequestDTO signInRequestDTO) {
		TokenMapping tokenResult = authService.signIn(signInRequestDTO);

		ResponseCookie cookie = refreshTokenCookieProvider.createCookie(tokenResult.getRefreshToken());
		AccessTokenResponse authResponse = new AccessTokenResponse(tokenResult.getAccessToken());

		return ResponseEntity.ok()
			.header(HttpHeaders.SET_COOKIE, cookie.toString())
			.body(authResponse);
	}

	@PostMapping("/refresh")
	public ResponseEntity<AccessTokenResponse> refresh(
		@CookieValue(value = REFRESH_TOKEN, required = false) String refreshToken,
		@RequestBody AccessTokenRequestDTO token) {
		AccessTokenResponse result = authService.refresh(refreshToken, token.getAccessToken());
		return ResponseEntity.ok()
			.body(result);
	}
}
