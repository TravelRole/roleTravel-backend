package com.travel.role.global.auth.service;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.user.dao.UserRepository;
import com.travel.role.domain.user.domain.Role;
import com.travel.role.domain.user.domain.UserEntity;
import com.travel.role.global.auth.dto.AccessTokenResponse;
import com.travel.role.global.auth.dto.SignInRequestDTO;
import com.travel.role.global.auth.dto.SignUpRequestDTO;
import com.travel.role.global.auth.dto.TokenMapping;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
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

	public void updateToken(TokenMapping tokenMapping) {
		UserEntity findUser = userRepository.findByEmail(tokenMapping.getUserEmail()).orElseThrow(
			() -> new RuntimeException("존재하지 않는 유저의 정보입니다.")
		);
		findUser.updateRefreshToken(tokenMapping.getRefreshToken());
	}

	public AccessTokenResponse refresh(final String refreshToken) {
		validateRefreshToken(refreshToken);

		UserEntity findUser = userRepository.findByRefreshToken(refreshToken)
			.orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));
		UsernamePasswordAuthenticationToken authentication = tokenProvider.getAuthenticationByEmail(
			findUser.getEmail());

		return tokenProvider.refreshAccessToken(authentication);
	}

	public void validateRefreshToken(final String refreshToken) {
		if (refreshToken == null) {
			throw new RuntimeException("Refresh Token이 존재하지 않습니다.");
		}

		Optional<UserEntity> findUser = userRepository.findByRefreshToken(refreshToken);

		if (findUser.isEmpty()) {
			throw new RuntimeException("유효하지 않은 Refresh Token 입니다.");
		}

		Long expirationTime = tokenProvider.getTokenExpiration(refreshToken);
		if (expirationTime < 0) {
			findUser.get().deleteRefreshToken();
			throw new RuntimeException("만료시간이 지난 Token 입니다.");
		}

		if (!tokenProvider.validateToken(refreshToken)) {
			throw new RuntimeException("유효하지 않은 Refresh Token 입니다.");
		}
	}
}
