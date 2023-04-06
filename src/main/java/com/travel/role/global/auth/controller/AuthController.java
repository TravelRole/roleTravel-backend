package com.travel.role.global.auth.controller;

import static com.travel.role.global.auth.service.RefreshTokenCookieProvider.*;

import javax.mail.SendFailedException;
import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travel.role.domain.user.dto.CheckIdRequest;
import com.travel.role.domain.user.dto.CheckIdResponse;
import com.travel.role.domain.user.dto.ConfirmUserRequestDTO;
import com.travel.role.domain.user.dto.ConfirmUserResponseDTO;
import com.travel.role.domain.user.dto.NewPasswordRequestDTO;
import com.travel.role.domain.user.dto.auth.SignUpResponseDTO;
import com.travel.role.global.auth.dto.AccessTokenRequestDTO;
import com.travel.role.global.auth.dto.TokenResponse;
import com.travel.role.domain.user.dto.auth.LoginRequestDTO;
import com.travel.role.domain.user.dto.auth.SignUpRequestDTO;
import com.travel.role.global.auth.dto.TokenMapping;
import com.travel.role.global.auth.service.AuthService;
import com.travel.role.global.auth.service.RefreshTokenCookieProvider;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Validated
public class AuthController {

	private final AuthService authService;
	private final RefreshTokenCookieProvider refreshTokenCookieProvider;


	@PostMapping("/signup")
	public SignUpResponseDTO signUp(@RequestBody @Valid SignUpRequestDTO signUpRequestDTO) {
		return authService.signUp(signUpRequestDTO);
	}

	@PostMapping("/login")
	public ResponseEntity<AccessTokenRequestDTO> signIn(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
		TokenMapping tokenResult = authService.signIn(loginRequestDTO);
		ResponseCookie cookie = refreshTokenCookieProvider.createCookie(tokenResult.getRefreshToken());

		return ResponseEntity.ok()
			.header(HttpHeaders.SET_COOKIE, cookie.toString())
			.body(new AccessTokenRequestDTO(tokenResult.getAccessToken()));
	}

	@PostMapping("/refresh")
	public TokenResponse refresh(
		@CookieValue(value = REFRESH_TOKEN, required = false) String refreshToken,
		@RequestBody @Valid AccessTokenRequestDTO token) {
		return authService.refresh(refreshToken, token.getAccessToken());
	}

	@PostMapping("/find-id")
	public ResponseEntity<ConfirmUserResponseDTO> confirmId(@RequestBody @Valid ConfirmUserRequestDTO confirmUserRequestDTO) {
		ConfirmUserResponseDTO result = authService.findId(confirmUserRequestDTO);
		return ResponseEntity.ok().body(result);
	}

	@PostMapping("/confirm-id")
	public ResponseEntity<CheckIdResponse> confirmId(@RequestBody @Valid CheckIdRequest checkIdRequest) {
		CheckIdResponse result = authService.confirmId(checkIdRequest);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/new-password")
	public ResponseEntity<?> newPassword(@RequestBody NewPasswordRequestDTO dto) throws SendFailedException {
		authService.changePassword(dto);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
