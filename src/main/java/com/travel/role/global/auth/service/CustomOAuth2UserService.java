package com.travel.role.global.auth.service;

import static com.travel.role.global.exception.ExceptionMessage.*;

import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.user.dao.UserRepository;
import com.travel.role.domain.user.domain.Provider;
import com.travel.role.domain.user.domain.UserEntity;
import com.travel.role.global.auth.oauth.OAuth2UserInfo;
import com.travel.role.global.auth.oauth.OAuthAttributes;
import com.travel.role.global.auth.token.UserPrincipal;
import com.travel.role.global.exception.user.AlreadyExistUserException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final UserRepository userRepository;

	@Override
	@Transactional
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		DefaultOAuth2UserService defaultOAuth2UserService = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(userRequest);

		String userNameAttributeName = userRequest.getClientRegistration()
			.getProviderDetails()
			.getUserInfoEndpoint()
			.getUserNameAttributeName();
		Map<String, Object> attributes = oAuth2User.getAttributes();

		OAuthAttributes newAttributes = OAuthAttributes.of(Provider.google, userNameAttributeName, attributes);

		UserEntity user = checkAndSaveUser(newAttributes, userRequest.getAccessToken().getTokenValue());

		return new UserPrincipal(user.getId(), user.getEmail(), user.getProviderId(),
			Collections.singleton(new SimpleGrantedAuthority(user.getRole().getRoleValue())));
	}

	private UserEntity checkAndSaveUser(OAuthAttributes attributes, String token) {
		return userRepository.findByProviderAndProviderId(Provider.google,
			attributes.getOAuth2UserInfo().getId())
			.orElseGet(() -> {
				if (!userRepository.existsByEmail(attributes.getOAuth2UserInfo().getEmail()))
					return saveUser(Provider.google, attributes.getOAuth2UserInfo(), token);
				throw new AlreadyExistUserException(ALREADY_EXIST_USER);
			});
	}

	private UserEntity saveUser(Provider provider, OAuth2UserInfo userInfo, String token) {
		UserEntity newUser = UserEntity.toEntity(provider, userInfo, token);
		return userRepository.save(newUser);
	}
}
