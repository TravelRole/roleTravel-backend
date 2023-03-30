package com.travel.role.global.auth.service.handler;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.role.domain.user.dao.UserRepository;
import com.travel.role.domain.user.domain.UserEntity;
import com.travel.role.global.auth.dto.TokenMapping;
import com.travel.role.global.auth.service.TokenProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
	private final TokenProvider tokenProvider;

	private final ObjectMapper mapper = new ObjectMapper();

	private static final String redirectPath = "http://localhost:3000/landing/social";

	private final UserRepository userRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
		Authentication authentication) throws IOException, ServletException {
		AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
	}

	@Override
	@Transactional
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		TokenMapping token = tokenProvider.createToken(authentication);

		saveUser(token);

		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");

		response.sendRedirect(redirectPath + "?accessToken=" + token.getAccessToken());
	}

	private void saveUser(TokenMapping token) {
		Optional<UserEntity> user = userRepository.findByEmail(token.getUserEmail());

		if (user.isEmpty())
			return;

		user.get().updateRefreshToken(token.getRefreshToken());
	}
}
