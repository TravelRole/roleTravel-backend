package com.travel.role.global.auth.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.Cookie;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application-oauth.yml")
public class RefreshTokenCookieProvider {

	public static final String REFRESH_TOKEN = "refreshToken";

	@Value("${cookieExpireTime}")
	private long EXPIRED_TIME;

	@Value("${cookieDomain}")
	private String COOKIE_DOMAIN;

	public ResponseCookie createCookie(final String refreshToken) {
		return ResponseCookie.from(REFRESH_TOKEN, refreshToken)
			.path("/") // 모든 경로일 때
			.domain(COOKIE_DOMAIN)
			.httpOnly(true)
			.maxAge(EXPIRED_TIME)
			.build();
	}
}
