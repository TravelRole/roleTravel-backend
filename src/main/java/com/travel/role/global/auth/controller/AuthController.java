package com.travel.role.global.auth.controller;

import static com.travel.role.global.auth.service.RefreshTokenCookieProvider.*;

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

import com.travel.role.domain.user.dao.UserRepository;
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
import com.travel.role.global.auth.token.UserPrincipal;
import com.travel.role.global.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Validated
public class AuthController {

	private final AuthService authService;
	private final RefreshTokenCookieProvider refreshTokenCookieProvider;


	@PostMapping("/signup")
	public SignUpResponseDTO signUp(@RequestBody @Validated SignUpRequestDTO signUpRequestDTO) {
		return authService.signUp(signUpRequestDTO);
	}

	@PostMapping("/login")
	public ResponseEntity<AccessTokenRequestDTO> signIn(@RequestBody @Validated LoginRequestDTO loginRequestDTO) {
		TokenMapping tokenResult = authService.signIn(loginRequestDTO);
		ResponseCookie cookie = refreshTokenCookieProvider.createCookie(tokenResult.getRefreshToken());

		return ResponseEntity.ok()
			.header(HttpHeaders.SET_COOKIE, cookie.toString())
			.body(new AccessTokenRequestDTO(tokenResult.getAccessToken()));
	}

	@PostMapping("/refresh")
	public TokenResponse refresh(
		@CookieValue(value = REFRESH_TOKEN, required = false) String refreshToken,
		@RequestBody @Validated AccessTokenRequestDTO token) {
		return authService.refresh(refreshToken, token.getAccessToken());
	}

	@PostMapping("/find-id")
	public ResponseEntity<ConfirmUserResponseDTO> confirmId(@RequestBody @Validated ConfirmUserRequestDTO confirmUserRequestDTO) {
		//TODO: 이후, PUll 당겼을때, Exception Handler 적용시 코드 변경할것
		try {
			ConfirmUserResponseDTO result = authService.findId(confirmUserRequestDTO);
			return ResponseEntity.ok().body(result);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(new ConfirmUserResponseDTO("실패하셨습니다", HttpStatus.BAD_REQUEST, null));
		}
	}

	@PostMapping("/confirm-id")
	public ResponseEntity<CheckIdResponse> confirmId(@RequestBody @Validated CheckIdRequest checkIdRequest) {
		CheckIdResponse result = authService.confirmId(checkIdRequest);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/new-password")
	public ResponseEntity<?> newPassword(@RequestBody NewPasswordRequestDTO dto) {
		try {
			authService.changePassword(dto);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}
