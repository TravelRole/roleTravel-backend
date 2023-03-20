package com.travel.role.global.auth.service;

import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.travel.role.domain.user.dao.TokenRepository;
import com.travel.role.domain.user.dao.UserRepository;
import com.travel.role.domain.user.domain.Role;
import com.travel.role.domain.user.domain.Token;
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
	private final TokenRepository tokenRepository;
	private final PasswordEncoder passwordEncoder;

	public ResponseEntity<?> signUp(SignUpRequestDTO signUpRequestDTO) {

		UserEntity newUser = new UserEntity(null, signUpRequestDTO.getNickname(), signUpRequestDTO.getEmail(),
			passwordEncoder.encode(signUpRequestDTO.getPassword()),
			Role.USER);

		userRepository.save(newUser);

		return ResponseEntity.ok().body(newUser);
	}

	public ResponseEntity<?> signIn(SignInRequestDTO signInRequestDTO) {
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(
				signInRequestDTO.getEmail(),
				signInRequestDTO.getPassword()
			)
		);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		TokenMapping tokenMapping = tokenProvider.createToken(authentication);
		Token token = new Token(tokenMapping.getUserEmail(), tokenMapping.getRefreshToken());
		tokenRepository.save(token);

		AuthResponse authResponse = new AuthResponse(tokenMapping.getAccessToken(), tokenMapping.getRefreshToken(),
			null);

		return ResponseEntity.ok(authResponse);
	}
}
