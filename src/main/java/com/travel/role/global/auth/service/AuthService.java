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

import com.nimbusds.oauth2.sdk.token.RefreshToken;
import com.travel.role.domain.user.dao.UserRepository;
import com.travel.role.domain.user.domain.UserEntity;
import com.travel.role.domain.user.dto.SignUpResponseDTO;
import com.travel.role.global.auth.dto.TokenResponse;
import com.travel.role.domain.user.dto.LoginRequestDTO;
import com.travel.role.domain.user.dto.SignUpRequestDTO;
import com.travel.role.global.auth.dto.TokenMapping;
import com.travel.role.global.auth.exception.InvalidTokenException;
import com.travel.role.global.auth.exception.NotExistTokenException;
import com.travel.role.global.auth.token.UserPrincipal;
import com.travel.role.global.dto.ApiResponse;
import com.travel.role.global.exception.user.AlreadyExistUserException;

import io.jsonwebtoken.ExpiredJwtException;
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

		UserEntity newUser = UserEntity.toEntity(signUpRequestDTO,
			passwordEncoder.encode(signUpRequestDTO.getPassword()));
		userRepository.save(newUser);

		return new SignUpResponseDTO(newUser.getEmail(), newUser.getName(), SUCCESS_SIGN_UP, LocalDateTime.now());
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

		Long refreshTokenExpirationTime = 0L;
		try {
			refreshTokenExpirationTime = tokenProvider.getTokenExpiration(refreshToken);
		} catch (Exception e) {
			findUser.get().deleteRefreshToken();
			throw new InvalidTokenException(INVALID_TOKEN);
		}

		if (refreshTokenExpirationTime < 0) {
			findUser.get().deleteRefreshToken();
			throw new InvalidTokenException(INVALID_TOKEN);
		}

		try {
			tokenProvider.getTokenExpiration(accessToken);
		} catch (ExpiredJwtException ignored) {
		} catch (Exception e) {
			findUser.get().deleteRefreshToken();
			throw new InvalidTokenException(e.getMessage());
		}
	}

	@Transactional
	public ApiResponse logout(String refreshToken, UserPrincipal userPrincipal) {
		UserEntity user = validateLogout(refreshToken, userPrincipal);
		user.deleteRefreshToken();

		return new ApiResponse("로그아웃에 성공하셨습니다.", LocalDateTime.now());
	}

	private UserEntity validateLogout(String refreshToken, UserPrincipal userPrincipal) {
		try {
			tokenProvider.validateToken(refreshToken);
		} catch (Exception e) {
			throw new InvalidTokenException(INVALID_USER);
		}

		UserEntity findTokenUser = userRepository.findByRefreshToken(refreshToken)
			.orElseThrow(() -> new InvalidTokenException(INVALID_USER));

		UserEntity findEmailUser = userRepository.findByEmail(userPrincipal.getEmail())
			.orElseThrow(() -> new InvalidTokenException(INVALID_USER));

		if (!findTokenUser.getEmail().equals(findEmailUser.getEmail())) {
			throw new InvalidTokenException(INVALID_USER);
		}
		return findTokenUser;
	}
}
