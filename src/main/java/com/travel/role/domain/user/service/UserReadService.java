package com.travel.role.domain.user.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.user.entity.User;
import com.travel.role.domain.user.repository.UserRepository;
import com.travel.role.global.exception.user.AlreadyExistUserException;
import com.travel.role.global.exception.user.UserInfoNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserReadService {

	private final UserRepository userRepository;

	public User findUserByEmailOrElseThrow(String email) {

		return userRepository.findByEmail(email)
			.orElseThrow(UserInfoNotFoundException::new);
	}

	public User findUserByRefreshTokenOrElseThrow(String refreshToken) {
		return userRepository.findByRefreshToken(refreshToken)
			.orElseThrow(UserInfoNotFoundException::new);
	}

	public User findUserByIdOrElseThrow(Long id) {
		return userRepository.findById(id)
			.orElseThrow(UserInfoNotFoundException::new);
	}

	public User findUserByNameAndBirthAndEmailOrElseThrow(String name, LocalDate birth, String email) {
		return userRepository.findByNameAndBirthAndEmail(name, birth, email)
			.orElseThrow(UserInfoNotFoundException::new);
	}

	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	public void validateUserExistByEmail(String email) {
		if (!userRepository.existsByEmail(email)) {
			throw new AlreadyExistUserException();
		}
	}

	public User findByNameAndBirthOrElseThrow(String name, LocalDate birth) {
		return userRepository.findByNameAndBirth(name, birth)
			.orElseThrow(UserInfoNotFoundException::new);
	}
}
