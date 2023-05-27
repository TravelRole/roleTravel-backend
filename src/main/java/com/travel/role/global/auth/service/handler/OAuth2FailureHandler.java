package com.travel.role.global.auth.service.handler;

<<<<<<< HEAD

=======
>>>>>>> master
import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

<<<<<<< HEAD
import com.travel.role.global.exception.dto.ExceptionFilterResponse;
=======
>>>>>>> master

@Component
public class OAuth2FailureHandler implements AuthenticationFailureHandler {

<<<<<<< HEAD
=======
	private static final String OAUTH_LOGIN_FAILURE = "소셜 로그인에 실패하였습니다";

>>>>>>> master
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException exception) throws IOException, ServletException {
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		new ExceptionFilterResponse(OAUTH_LOGIN_FAILURE, HttpStatus.BAD_REQUEST, LocalDateTime.now().toString());
	}
}
