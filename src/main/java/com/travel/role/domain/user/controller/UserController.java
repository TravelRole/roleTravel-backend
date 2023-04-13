package com.travel.role.domain.user.controller;

import javax.validation.Valid;

import com.travel.role.domain.user.dto.UserProfileDetailResDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travel.role.domain.user.dto.UserProfileModifyReqDTO;
import com.travel.role.domain.user.dto.UserProfileResponseDTO;
import com.travel.role.domain.user.service.UserService;
import com.travel.role.global.auth.token.UserPrincipal;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
	private final UserService userService;

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
	public ResponseEntity<Void> modifyUserProfile(@AuthenticationPrincipal UserPrincipal userPrincipal,
		@RequestBody @Valid UserProfileModifyReqDTO reqDTO) {

		userService.modifyUserProfile(userPrincipal.getEmail(), reqDTO);

		return ResponseEntity.ok().build();
	}
}
