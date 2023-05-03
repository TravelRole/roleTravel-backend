package com.travel.role.global.auth.token;

import static com.travel.role.global.util.Constants.*;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.travel.role.global.auth.service.TokenProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static String REFRESH_API = "/api/refresh";

	@Autowired
	private TokenProvider tokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		String jwt = getJwtFromRequest(request);
		String servletPath = request.getServletPath();

		if (StringUtils.hasText(jwt) && !Objects.equals(servletPath, REFRESH_API)) {
			tokenProvider.validateToken(jwt);
			UsernamePasswordAuthenticationToken authentication = tokenProvider.getAuthenticationById(jwt);
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(request, response);
	}

	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_NAME)) {
			log.info("bearer toke = {}", bearerToken);
			return bearerToken.substring(TOKEN_NAME.length() + 1);
		}
		return null;
	}
}
