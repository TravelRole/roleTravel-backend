package com.travel.role.global.auth.oauth;

import java.util.Map;

import com.travel.role.domain.user.domain.Provider;
import com.travel.role.global.auth.oauth.google.GoogleOAuth2UserInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OAuthAttributes {

	private String idAttributeKey;
	private OAuth2UserInfo oAuth2UserInfo;

	public static OAuthAttributes of(Provider provider, String idAttributes, Map<String, Object> attributes) {
		return ofGoogle(idAttributes, attributes);
	}

	private static OAuthAttributes ofGoogle(String idAttributes, Map<String, Object> attributes) {
		return new OAuthAttributes(idAttributes, new GoogleOAuth2UserInfo(attributes));
	}
}
