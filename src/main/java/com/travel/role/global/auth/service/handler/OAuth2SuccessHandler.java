package com.travel.role.global.auth.service.handler;

import static com.travel.role.global.util.Constants.*;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.global.auth.dto.TokenMapping;
import com.travel.role.global.auth.entity.AuthInfo;
import com.travel.role.global.auth.repository.AuthReadService;
import com.travel.role.global.auth.repository.AuthRepository;
import com.travel.role.global.auth.service.RefreshTokenCookieProvider;
import com.travel.role.global.auth.service.TokenProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
@PropertySource("classpath:application-oauth.yml")
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
	private final TokenProvider tokenProvider;

	@Value("${redirectPath}")
	private String redirectPath;
	private final AuthReadService authReadService;
	private final AuthRepository authRepository;
	private final RefreshTokenCookieProvider refreshTokenCookieProvider;

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
		saveToken(token);

		ResponseCookie cookie = refreshTokenCookieProvider.createCookie(token.getRefreshToken());
		response.addHeader(COOKIE_HEADER, cookie.toString());
		response.sendRedirect(redirectPath + "?accessToken=" + token.getAccessToken());
	}

	private void saveToken(TokenMapping token) {
		AuthInfo authInfo = authReadService.findUserByEmailOrElseThrow(token.getUserEmail());
		authInfo.updateRefreshToken(token.getRefreshToken());
	}
}
