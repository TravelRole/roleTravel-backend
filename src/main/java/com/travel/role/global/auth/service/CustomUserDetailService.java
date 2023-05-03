package com.travel.role.global.auth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.user.entity.User;
import com.travel.role.domain.user.service.UserReadService;
import com.travel.role.global.auth.token.UserPrincipal;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CustomUserDetailService implements UserDetailsService {

	private final UserReadService userReadService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		User user = userReadService.findUserByEmailOrElseThrow(email);

		return UserPrincipal.create(user);
	}

	public UserDetails loadUserById(Long id) {
		User user = userReadService.findUserByIdOrElseThrow(id);

		return UserPrincipal.create(user);
	}
}
