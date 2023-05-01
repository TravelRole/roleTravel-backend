package com.travel.role.domain.user.controller;

import javax.mail.SendFailedException;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travel.role.domain.user.dto.CheckIdRequest;
import com.travel.role.domain.user.dto.CheckIdResponse;
import com.travel.role.domain.user.dto.ConfirmUserRequestDTO;
import com.travel.role.domain.user.dto.ConfirmUserResponseDTO;
import com.travel.role.domain.user.dto.NewPasswordRequestDTO;
import com.travel.role.domain.user.dto.UserPasswordModifyReqDTO;
import com.travel.role.domain.user.dto.UserProfileDetailResDTO;
import com.travel.role.domain.user.dto.UserProfileModifyReqDTO;
import com.travel.role.domain.user.dto.UserProfileResponseDTO;
import com.travel.role.domain.user.dto.auth.SignUpRequestDTO;
import com.travel.role.domain.user.dto.auth.SignUpResponseDTO;
import com.travel.role.domain.user.service.UserService;
import com.travel.role.global.auth.service.AuthService;
import com.travel.role.global.auth.token.UserPrincipal;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
	private final UserService userService;
	private final AuthService authService;

	@GetMapping("/basic-profile")
	public ResponseEntity<UserProfileResponseDTO> basicProfile(@AuthenticationPrincipal UserPrincipal userPrincipal) {

		UserProfileResponseDTO resDTO = userService.getBasicProfile(userPrincipal.getEmail());

		return ResponseEntity.ok(resDTO);
	}

	@GetMapping("/users")
	public ResponseEntity<UserProfileDetailResDTO> getUserProfile(
		@AuthenticationPrincipal UserPrincipal userPrincipal) {

		UserProfileDetailResDTO resDTO = userService.getUserProfile(userPrincipal.getEmail());

		return ResponseEntity.ok(resDTO);
	}

	@PutMapping("/users")
	public ResponseEntity<UserProfileDetailResDTO> modifyUserProfile(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		@RequestBody @Valid UserProfileModifyReqDTO reqDTO) {

		UserProfileDetailResDTO resDTO = userService.modifyUserProfile(userPrincipal.getEmail(), reqDTO);

		return ResponseEntity.ok(resDTO);
	}

	@PutMapping("/users/password")
	public void modifyPassword(@AuthenticationPrincipal UserPrincipal userPrincipal,
		@RequestBody @Valid UserPasswordModifyReqDTO reqDTO) {

		userService.modifyPassword(userPrincipal.getEmail(), reqDTO);
	}

	@GetMapping("/users/image/presigned-url")
	public ResponseEntity<String> getProfileImagePreSignedUrl(@AuthenticationPrincipal UserPrincipal userPrincipal) {

		String preSignedUrl = userService.getPreSignedUrlForProfileImage(userPrincipal.getEmail());

		return ResponseEntity.ok(preSignedUrl);
	}

	@PutMapping("/users/image")
	public void modifyProfileImageUrl(@AuthenticationPrincipal UserPrincipal userPrincipal) {

		userService.modifyProfileImageUrl(userPrincipal.getEmail());
	}

	@DeleteMapping("/users/image")
	public void deleteProfileImage(@AuthenticationPrincipal UserPrincipal userPrincipal){

		userService.deleteProfileImageUrl(userPrincipal.getEmail());
	}

	@PostMapping("/signup")
	public SignUpResponseDTO signUp(@RequestBody @Valid SignUpRequestDTO signUpRequestDTO) {
		return authService.signUp(signUpRequestDTO);
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
