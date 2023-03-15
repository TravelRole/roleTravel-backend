package com.travel.role.global.auth.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.travel.role.domain.user.dao.UserRepository;
import com.travel.role.domain.user.domain.Role;
import com.travel.role.domain.user.domain.UserEntity;
import com.travel.role.global.auth.dto.SignUpRequestDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;

	public ResponseEntity<?> signUp(SignUpRequestDTO signUpRequestDTO) {

		UserEntity newUser = new UserEntity(null, signUpRequestDTO.getNickname(), signUpRequestDTO.getEmail(),
			signUpRequestDTO.getPassword(),
			Role.USER);

		userRepository.save(newUser);

		return ResponseEntity.ok().body(newUser);
	}
}
