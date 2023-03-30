package com.travel.role.global.auth.controller;

import static com.travel.role.global.auth.service.RefreshTokenCookieProvider.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.travel.role.global.auth.service.RefreshTokenCookieProvider;
import com.travel.role.global.auth.token.UserPrincipal;
import com.travel.role.global.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;
	private final RefreshTokenCookieProvider refreshTokenCookieProvider;


	@PostMapping("/signup")
	public SignUpResponseDTO signUp(@RequestBody SignUpRequestDTO signUpRequestDTO) {
		return authService.signUp(signUpRequestDTO);
	}

	@PostMapping("/login")
	public ResponseEntity<AccessTokenRequestDTO> signIn(@RequestBody LoginRequestDTO loginRequestDTO) {
		TokenMapping tokenResult = authService.signIn(loginRequestDTO);
		ResponseCookie cookie = refreshTokenCookieProvider.createCookie(tokenResult.getRefreshToken());

		return ResponseEntity.ok()
			.header(HttpHeaders.SET_COOKIE, cookie.toString())
			.body(new AccessTokenRequestDTO(tokenResult.getAccessToken()));
	}

	@PostMapping("/refresh")
	public TokenResponse refresh(
		@CookieValue(value = REFRESH_TOKEN, required = false) String refreshToken,
		@RequestBody AccessTokenRequestDTO token) {
		return authService.refresh(refreshToken, token.getAccessToken());
	}

	@GetMapping("/logout")
	public ResponseEntity<ApiResponse> logout(@AuthenticationPrincipal UserPrincipal userPrincipal,
		@CookieValue(value = REFRESH_TOKEN, required = false) String token) {
		ApiResponse response = authService.logout(token, userPrincipal);
		ResponseCookie cookie = refreshTokenCookieProvider.logout();
		return ResponseEntity.ok()
			.header(HttpHeaders.SET_COOKIE, cookie.toString())
			.body(response);
	}
}
