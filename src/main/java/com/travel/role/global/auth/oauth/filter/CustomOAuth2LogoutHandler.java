package com.travel.role.global.auth.oauth.filter;

import static com.travel.role.global.util.Constants.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.travel.role.domain.user.entity.Provider;
import com.travel.role.domain.user.entity.User;
import com.travel.role.domain.user.repository.UserRepository;
import com.travel.role.global.auth.service.TokenProvider;
import com.travel.role.global.exception.user.UserInfoNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2LogoutHandler implements LogoutHandler {
	private final TokenProvider tokenProvider;
	private final UserRepository userRepository;

	@Override
	@Transactional
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		String token = getJwtFromRequest(request);

		tokenProvider.validateToken(token);

		UsernamePasswordAuthenticationToken auth = getUserAuthentication(token);
		User user = getUserInfo(auth);

		if (user != null) {
			user.deleteRefreshToken();

			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();

			String encoded = null;
			try {
				encoded = URLEncoder.encode(user.getProviderToken(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				log.info("AccessToken Encoding에 실패하였습니다");
			}
			headers.set(HttpHeaders.AUTHORIZATION, TOKEN_NAME + " " + encoded);
			HttpEntity<String> entity = new HttpEntity<>("", headers);
			if (user.getProvider() == Provider.google) {
				String url = "https://accounts.google.com/o/oauth2/revoke?token=" + encoded;
				restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
			} else if (user.getProvider() == Provider.kakao) {
				String url = "https://kapi.kakao.com/v1/user/logout";
				ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
				exchange.getStatusCode();
			}
		} else {
			throw new UserInfoNotFoundException();
		}
	}

	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_NAME)) {
			return bearerToken.substring(TOKEN_NAME.length() + 1);
		}
		return null;
	}

	private UsernamePasswordAuthenticationToken getUserAuthentication(String jwt) {
		return tokenProvider.getAuthenticationById(jwt);
	}
	private User getUserInfo(UsernamePasswordAuthenticationToken authentication) {
		String email = authentication.getName();

		Optional<User> userInfo = userRepository.findByEmail(email);
		return userInfo.orElse(null);
	}
}
