package com.travel.role.global.auth.controller;

import static com.travel.role.global.util.Constants.*;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travel.role.domain.user.dto.auth.LoginRequestDTO;
import com.travel.role.global.auth.dto.AccessTokenRequestDTO;
import com.travel.role.global.auth.dto.TokenMapping;
import com.travel.role.global.auth.service.AuthService;
import com.travel.role.global.auth.service.RefreshTokenCookieProvider;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Validated
public class AuthController {

	private final AuthService authService;
	private final RefreshTokenCookieProvider refreshTokenCookieProvider;

	@PostMapping("/login")
	public ResponseEntity<AccessTokenRequestDTO> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
		TokenMapping tokenResult = authService.login(loginRequestDTO);
		ResponseCookie cookie = refreshTokenCookieProvider.createCookie(tokenResult.getRefreshToken());

		return ResponseEntity.ok()
			.header(HttpHeaders.SET_COOKIE, cookie.toString())
			.body(new AccessTokenRequestDTO(tokenResult.getAccessToken()));
	}

	@GetMapping("/refresh")
	public AccessTokenRequestDTO refresh(
		@CookieValue(value = REFRESH_TOKEN_NAME, required = false) String refreshToken) {
		return authService.refresh(refreshToken);
	}
}
