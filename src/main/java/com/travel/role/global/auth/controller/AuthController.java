package com.travel.role.global.auth.controller;

import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travel.role.global.auth.dto.AuthResponse;
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
	public ResponseEntity<AuthResponse> signIn(@RequestBody SignInRequestDTO signInRequestDTO) {
		TokenMapping tokenResult = authService.signIn(signInRequestDTO);

		ResponseCookie cookie = refreshTokenCookieProvider.createCookie(tokenResult.getRefreshToken());
		AuthResponse authResponse = new AuthResponse(tokenResult.getAccessToken());

		return ResponseEntity.ok()
			.header(HttpHeaders.SET_COOKIE, cookie.toString())
			.body(authResponse);
	}

	@PostMapping("/test")
	public ResponseEntity<?> test(@AuthenticationPrincipal UserPrincipal userPrincipal) {
		return ResponseEntity.ok().body(userPrincipal);
	}

}
