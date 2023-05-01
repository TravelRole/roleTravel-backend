package com.travel.role.global.auth.service;

import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.user.entity.Provider;
import com.travel.role.domain.user.entity.User;
import com.travel.role.domain.user.repository.UserRepository;
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
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		Provider provider = getProvider(registrationId);

		OAuthAttributes newAttributes = OAuthAttributes.of(provider, userNameAttributeName, attributes);

		User user = checkAndSaveUser(newAttributes, provider);

		user.updateProviderToken(userRequest.getAccessToken().getTokenValue());


		return new UserPrincipal(user.getId(), user.getEmail(), user.getProviderId(),
			Collections.singleton(new SimpleGrantedAuthority(user.getRole().getRoleValue())));
	}

	private User checkAndSaveUser(OAuthAttributes attributes, Provider provider) {
		return userRepository.findByProviderAndProviderId(provider,
				attributes.getOAuth2UserInfo().getId())
			.orElseGet(() -> {
				if (!userRepository.existsByEmail(attributes.getOAuth2UserInfo().getEmail()))
					return saveUser(provider, attributes.getOAuth2UserInfo());
				throw new AlreadyExistUserException();
			});
	}

	private User saveUser(Provider provider, OAuth2UserInfo userInfo) {
		User newUser = User.of(provider, userInfo);
		return userRepository.save(newUser);
	}

	private Provider getProvider(String registrationId) {
		if (Provider.google.name().equals(registrationId)) {
			return Provider.google;
		}
		return Provider.kakao;
	}
}
