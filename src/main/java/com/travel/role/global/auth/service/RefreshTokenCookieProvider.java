package com.travel.role.global.auth.service;

import static com.travel.role.global.util.Constants.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application-oauth.yml")
public class RefreshTokenCookieProvider {

	@Value("${cookieExpireTime}")
	private long EXPIRED_TIME;

	@Value("${cookieDomain}")
	private String COOKIE_DOMAIN;

	public ResponseCookie createCookie(final String refreshToken) {
		return ResponseCookie.from(REFRESH_TOKEN_NAME, refreshToken)
			.path("/") // 모든 경로일 때
			.domain(COOKIE_DOMAIN)
			.httpOnly(true)
			.maxAge(EXPIRED_TIME)
			.build();
	}
}
