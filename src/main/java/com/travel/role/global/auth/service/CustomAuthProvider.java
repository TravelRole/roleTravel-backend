package com.travel.role.global.auth.service;

import static com.travel.role.global.exception.dto.ExceptionMessage.*;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomAuthProvider implements AuthenticationProvider {

	private final CustomUserDetailService customUserDetailService;
	private final PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String email = authentication.getName();
		String password = (String) authentication.getCredentials();

		UserDetails userDetails = customUserDetailService.loadUserByUsername(email);

		if (!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException(BAD_CREDENTIAL_USER);
		}

		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
