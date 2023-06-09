package com.travel.role.global.auth.oauth.filter;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.travel.role.domain.user.dao.UserRepository;
import com.travel.role.domain.user.domain.UserEntity;
import com.travel.role.global.auth.service.TokenProvider;
import com.travel.role.global.auth.token.UserPrincipal;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomOAuth2LogoutHandler implements LogoutHandler {

	private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
	private final TokenProvider tokenProvider;
	private final UserRepository userRepository;

	@Override
	@Transactional
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		UsernamePasswordAuthenticationToken auth = getUserAuthentication(getJwtFromRequest(request));
		UserEntity userInfo = getUserInfo(auth);

		if (userInfo == null) {
			response.setStatus(HttpStatus.FORBIDDEN.value());
		}

		userInfo.deleteRefreshToken();

		new SecurityContextLogoutHandler().logout(request, response, auth);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		String encoded = null;
		try {
			encoded = URLEncoder.encode(userInfo.getProviderToken(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		headers.set("Authorization", "Bearer " + encoded);
		HttpEntity<String> entity = new HttpEntity<>("", headers);
		String url = "https://accounts.google.com/o/oauth2/revoke?token=" + encoded;
		ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		String body = exchange.getBody();
	}

	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}

	private UsernamePasswordAuthenticationToken getUserAuthentication(String jwt) {
		return tokenProvider.getAuthenticationById(jwt);
	}
	private UserEntity getUserInfo(UsernamePasswordAuthenticationToken authentication) {
		String email = authentication.getName();

		Optional<UserEntity> userInfo = userRepository.findByEmail(email);
		return userInfo.orElse(null);
	}
}
