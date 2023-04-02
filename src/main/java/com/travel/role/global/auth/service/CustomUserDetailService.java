package com.travel.role.global.auth.service;

import static com.travel.role.global.exception.ExceptionMessage.*;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.user.dao.UserRepository;
import com.travel.role.domain.user.domain.UserEntity;
import com.travel.role.global.auth.token.UserPrincipal;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		UserEntity user = userRepository.findByEmail(email)
			.orElseThrow(() ->
				new UsernameNotFoundException(USERNAME_NOT_FOUND));

		return UserPrincipal.create(user);
	}

	@Transactional
	public UserDetails loadUserById(Long id) {
		Optional<UserEntity> user = userRepository.findById(id);
		if (user.isEmpty()) {
			throw new UsernameNotFoundException(USERNAME_NOT_FOUND);
		}

		return UserPrincipal.create(user.get());
	}
}
