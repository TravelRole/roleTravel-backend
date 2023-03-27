package com.travel.role.global.auth.exception.auth;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.role.global.exception.ExceptionFilterResponse;
import com.travel.role.global.exception.user.AlreadyExistUserException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TokenExceptionHandlerFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (InvalidTokenException | NotExistTokenException | AlreadyExistUserException e) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json");

			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.writeValue(response.getWriter(),
				new ExceptionFilterResponse(e.getMessage(), HttpStatus.UNAUTHORIZED, LocalDateTime.now().toString()));
		}
	}

}
