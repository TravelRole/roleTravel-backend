package com.travel.role.global.auth.service;

import java.time.Duration;

import org.springframework.boot.web.server.Cookie;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenCookieProvider {

	public static final String REFRESH_TOKEN = "refreshToken";
	private final long EXPIRED_TIME = 60 * 60 * 24 * 14;

	public ResponseCookie createCookie(final String refreshToken) {
		return ResponseCookie.from(REFRESH_TOKEN, refreshToken)
			.httpOnly(true)
			.secure(true)
			.path("/") // 모든 경로일 때
			.maxAge(EXPIRED_TIME)
			.sameSite(Cookie.SameSite.NONE.attributeValue()) // sameSite 이전 상태로 기술
			.build();
	}
}
