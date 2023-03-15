package com.travel.role.global.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travel.role.global.auth.dto.SignUpRequest;
import com.travel.role.global.auth.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	public ResponseEntity<?> signUp(@RequestBody SignUpRequest signUpRequest) {
		return authService.signUp(signUpRequest);
	}

}
