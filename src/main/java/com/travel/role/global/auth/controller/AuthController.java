package com.travel.role.global.auth.controller;

import static com.travel.role.global.auth.service.RefreshTokenCookieProvider.*;

import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.travel.role.global.auth.dto.AccessTokenResponse;
import com.travel.role.global.auth.dto.SignInRequestDTO;
import com.travel.role.global.auth.dto.SignUpRequestDTO;
import com.travel.role.global.auth.dto.TokenMapping;
import com.travel.role.global.auth.service.AuthService;
import com.travel.role.global.auth.service.RefreshTokenCookieProvider;
import com.travel.role.global.auth.token.UserPrincipal;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;
	private final RefreshTokenCookieProvider refreshTokenCookieProvider;

	@PostMapping("/auth/signup")
	public ResponseEntity<?> signUp(@RequestBody SignUpRequestDTO signUpRequestDTO) {
		return authService.signUp(signUpRequestDTO);
	}

	@PostMapping("/auth/signin")
	public ResponseEntity<AccessTokenResponse> signIn(@RequestBody SignInRequestDTO signInRequestDTO) {
		TokenMapping tokenResult = authService.signIn(signInRequestDTO);

		ResponseCookie cookie = refreshTokenCookieProvider.createCookie(tokenResult.getRefreshToken());
		AccessTokenResponse authResponse = new AccessTokenResponse(tokenResult.getAccessToken());

		return ResponseEntity.ok()
			.header(HttpHeaders.SET_COOKIE, cookie.toString())
			.body(authResponse);
	}

	@PostMapping("/test")
	public ResponseEntity<?> test(@AuthenticationPrincipal UserPrincipal userPrincipal) {
		return ResponseEntity.ok().body(userPrincipal);
	}

	@PostMapping("/auth/refresh")
	public ResponseEntity<AccessTokenResponse> refresh(@CookieValue(value = REFRESH_TOKEN, required = false) String refreshToken) {
		AccessTokenResponse result = authService.refresh(refreshToken);
		return ResponseEntity.ok()
			.body(result);
	}
}
