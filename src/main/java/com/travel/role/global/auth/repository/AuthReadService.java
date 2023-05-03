package com.travel.role.global.auth.repository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.global.auth.entity.AuthInfo;
import com.travel.role.global.exception.auth.InvalidTokenException;
import com.travel.role.global.exception.user.UserInfoNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthReadService {
	private final AuthRepository authRepository;

	public AuthInfo findUserByEmailOrElseThrow(String email) {
		return authRepository.findUserByEmail(email)
			.orElseThrow(UserInfoNotFoundException::new);
	}

	public AuthInfo findUserByRefreshTokenOrElseThrow(String refreshToken) {
		return authRepository.findUserByRefreshToken(refreshToken)
			.orElseThrow(InvalidTokenException::new);
	}

	public AuthInfo findUserByIdOrElseThrow(Long id) {
		return authRepository.findUserById(id)
			.orElseThrow(UserInfoNotFoundException::new);
	}
}
