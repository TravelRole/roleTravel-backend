package com.travel.role.global.auth.service;

import static com.travel.role.global.exception.ExceptionMessage.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.user.dao.UserRepository;
import com.travel.role.domain.user.domain.UserEntity;
import com.travel.role.domain.user.dto.SignUpResponseDTO;
import com.travel.role.global.auth.dto.TokenResponse;
import com.travel.role.domain.user.dto.LoginRequestDTO;
import com.travel.role.domain.user.dto.SignUpRequestDTO;
import com.travel.role.global.auth.dto.TokenMapping;
import com.travel.role.global.auth.exception.InvalidTokenException;
import com.travel.role.global.auth.exception.NotExistTokenException;
import com.travel.role.global.exception.user.AlreadyExistUserException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

	private final AuthenticationManager authenticationManager;
	private final TokenProvider tokenProvider;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	private static final String SUCCESS_SIGN_UP = "회원가입에 성공하셨습니다";

	@Transactional
	public SignUpResponseDTO signUp(SignUpRequestDTO signUpRequestDTO) {
		if (userRepository.existsByEmail(signUpRequestDTO.getEmail())) {
			throw new AlreadyExistUserException(ALREADY_EXIST_USER);
		}

		UserEntity newUser = UserEntity.toEntity(signUpRequestDTO, passwordEncoder.encode(signUpRequestDTO.getPassword()));
		userRepository.save(newUser);

		new SignUpResponseDTO(newUser.getEmail(),newUser.getName(), SUCCESS_SIGN_UP, LocalDateTime.now());
	}

	@Transactional
	public TokenMapping signIn(LoginRequestDTO loginRequestDTO) {
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(
				loginRequestDTO.getEmail(),
				loginRequestDTO.getPassword()
			)
		);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		TokenMapping tokenMapping = tokenProvider.createToken(authentication);
		updateToken(tokenMapping);

		return tokenMapping;
	}

	@Transactional
	public void updateToken(TokenMapping tokenMapping) {
		UserEntity findUser = userRepository.findByEmail(tokenMapping.getUserEmail()).orElseThrow(
			() -> new UsernameNotFoundException(USERNAME_NOT_FOUND)
		);
		findUser.updateRefreshToken(tokenMapping.getRefreshToken());
	}

	public TokenResponse refresh(final String refreshToken, String accessToken) {
		validateToken(refreshToken, accessToken);

		UserEntity findUser = userRepository.findByRefreshToken(refreshToken)
			.orElseThrow(() -> new UsernameNotFoundException(USERNAME_NOT_FOUND));
		UsernamePasswordAuthenticationToken authentication = tokenProvider.getAuthenticationByEmail(
			findUser.getEmail());

		return tokenProvider.refreshAccessToken(authentication);
	}

	@Transactional
	public void validateToken(final String refreshToken, String accessToken) {
		if (refreshToken == null || accessToken == null) {
			throw new NotExistTokenException(NOT_EXISTS_TOKEN);
		}

		Optional<UserEntity> findUser = userRepository.findByRefreshToken(refreshToken);

		if (findUser.isEmpty()) {
			throw new InvalidTokenException(INVALID_TOKEN);
		}

		Long accessTokenExpirationTime = 0L;
		Long refreshTokenExpirationTime = 0L;
		try {
			accessTokenExpirationTime = tokenProvider.getTokenExpiration(accessToken);
			refreshTokenExpirationTime = tokenProvider.getTokenExpiration(refreshToken);
		} catch (Exception e) {
			findUser.get().deleteRefreshToken();
			throw new InvalidTokenException(INVALID_TOKEN);
		}

		if (accessTokenExpirationTime > 0) {
			findUser.get().deleteRefreshToken();
			throw new InvalidTokenException(INVALID_TOKEN);
		}

		if (refreshTokenExpirationTime < 0) {
			findUser.get().deleteRefreshToken();
			throw new InvalidTokenException(INVALID_TOKEN);
		}
	}
}
