package com.travel.role.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.user.entity.User;
import com.travel.role.domain.user.repository.UserRepository;
import com.travel.role.global.auth.token.UserPrincipal;
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

	//TODO: 이후 userprincipal이 아닌 email로 변경
	public User findUserByEmailOrElseThrow(UserPrincipal userPrincipal) {
		return userRepository.findByEmail(userPrincipal.getEmail())
			.orElseThrow(UserInfoNotFoundException::new);
	}
}
