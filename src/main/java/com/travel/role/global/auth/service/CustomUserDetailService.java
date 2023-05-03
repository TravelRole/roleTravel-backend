package com.travel.role.global.auth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.global.auth.entity.AuthInfo;
import com.travel.role.global.auth.repository.AuthReadService;
import com.travel.role.global.auth.token.UserPrincipal;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CustomUserDetailService implements UserDetailsService {

	private final AuthReadService authReadService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		AuthInfo authInfo = authReadService.findUserByEmailOrElseThrow(email);

		return UserPrincipal.create(authInfo);
	}

	public UserDetails loadUserById(Long id) {
		AuthInfo authInfo = authReadService.findUserByIdOrElseThrow(id);

		return UserPrincipal.create(authInfo);
	}
}
