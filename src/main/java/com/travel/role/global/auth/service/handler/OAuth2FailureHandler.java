package com.travel.role.global.auth.service.handler;

import static com.travel.role.global.util.Constants.*;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.travel.role.global.exception.dto.ExceptionFilterResponse;

@Component
public class OAuth2FailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException exception) throws IOException, ServletException {
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		new ExceptionFilterResponse(OAUTH_LOGIN_FAILURE, HttpStatus.BAD_REQUEST, LocalDateTime.now().toString());
	}
}
