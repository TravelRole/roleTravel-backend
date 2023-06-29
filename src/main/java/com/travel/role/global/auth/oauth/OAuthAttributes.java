package com.travel.role.global.auth.oauth;

import java.util.Map;

import com.travel.role.domain.user.entity.Provider;
import com.travel.role.global.auth.oauth.google.GoogleOAuth2UserInfo;
import com.travel.role.global.auth.oauth.kakao.KakaoOAuth2UserInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OAuthAttributes {

	private String idAttributeKey;
	private OAuth2UserInfo oAuth2UserInfo;

	public static OAuthAttributes of(Provider provider, String idAttributes, Map<String, Object> attributes) {
		if (provider == Provider.google) {
			return ofGoogle(idAttributes, attributes);
		}
		return ofKakao(idAttributes, attributes);
	}

	private static OAuthAttributes ofGoogle(String idAttributes, Map<String, Object> attributes) {
		return new OAuthAttributes(idAttributes, new GoogleOAuth2UserInfo(attributes));
	}

	private static OAuthAttributes ofKakao(String idAttributeKey, Map<String, Object> attributes) {
		return new OAuthAttributes(idAttributeKey, new KakaoOAuth2UserInfo(attributes));
	}
}
