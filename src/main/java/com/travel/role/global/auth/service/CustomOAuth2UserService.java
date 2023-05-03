package com.travel.role.global.auth.service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

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
import com.travel.role.domain.user.service.UserReadService;
import com.travel.role.global.auth.entity.AuthInfo;
import com.travel.role.global.auth.oauth.OAuth2UserInfo;
import com.travel.role.global.auth.oauth.OAuthAttributes;
import com.travel.role.global.auth.repository.AuthRepository;
import com.travel.role.global.auth.token.UserPrincipal;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final UserRepository userRepository;
	private final AuthRepository authRepository;
	private final UserReadService userReadService;

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

		AuthInfo authInfo = getLoginUser(newAttributes, provider);

		authInfo.updateProviderToken(userRequest.getAccessToken().getTokenValue());

		User user = authInfo.getUser();
		return new UserPrincipal(user.getId(), user.getEmail(), authInfo.getProviderId(),
			Collections.singleton(new SimpleGrantedAuthority(authInfo.getRole().getRoleValue())));
	}

	private AuthInfo getLoginUser(OAuthAttributes attributes, Provider provider) {
		String providerId = attributes.getOAuth2UserInfo().getId();
		String email = attributes.getOAuth2UserInfo().getEmail();
		Optional<AuthInfo> authInfo = authRepository.findUserByProviderAndProviderId(provider,
			providerId);

		return authInfo.orElseGet(
			() -> {
				userReadService.validateUserExistByEmail(email);

				return saveUserAndAuthInfo(provider, attributes.getOAuth2UserInfo());
			}
		);
	}

	private AuthInfo saveUserAndAuthInfo(Provider provider, OAuth2UserInfo userInfo) {
		User user = User.of(provider, userInfo);
		user = userRepository.save(user);

		AuthInfo authInfo = AuthInfo.of(provider, user);
		return authRepository.save(authInfo);
	}

	private Provider getProvider(String registrationId) {
		if (Provider.google.name().equals(registrationId)) {
			return Provider.google;
		}
		return Provider.kakao;
	}
}
