package com.travel.role.global.auth.service;

import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.travel.role.domain.user.dao.UserRepository;
import com.travel.role.domain.user.domain.Role;
import com.travel.role.domain.user.domain.UserEntity;
import com.travel.role.global.auth.dto.AuthResponse;
import com.travel.role.global.auth.dto.SignInRequestDTO;
import com.travel.role.global.auth.dto.SignUpRequestDTO;
import com.travel.role.global.auth.dto.TokenMapping;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final AuthenticationManager authenticationManager;
	private final TokenProvider tokenProvider;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public ResponseEntity<?> signUp(SignUpRequestDTO signUpRequestDTO) {

		UserEntity newUser = new UserEntity(null, signUpRequestDTO.getNickname(), signUpRequestDTO.getEmail(),
			passwordEncoder.encode(signUpRequestDTO.getPassword()),
			Role.USER, null);

		userRepository.save(newUser);

		return ResponseEntity.ok().body(newUser);
	}

	public TokenMapping signIn(SignInRequestDTO signInRequestDTO) {
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(
				signInRequestDTO.getEmail(),
				signInRequestDTO.getPassword()
			)
		);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		TokenMapping tokenMapping = tokenProvider.createToken(authentication);
		updateToken(tokenMapping);

		return tokenMapping;
	}

	private void updateToken(TokenMapping tokenMapping) {
		UserEntity findUser = userRepository.findByEmail(tokenMapping.getUserEmail()).orElseThrow(
			() -> new RuntimeException("존재하지 않는 유저의 정보입니다.")
		);
		findUser.updateRefreshToken(tokenMapping.getRefreshToken());

	}
}
