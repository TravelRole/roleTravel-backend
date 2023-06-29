package com.travel.role.global.auth.oauth.kakao;

import java.util.Map;

import com.travel.role.global.auth.oauth.OAuth2UserInfo;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

	public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
		super(attributes);
	}

	@Override
	public String getId() {
		return String.valueOf(attributes.get("id"));
	}

	@Override
	public String getEmail() {
		Map<String, Object> account = (Map<String, Object>)attributes.get("kakao_account");

		if (account == null) {
			return null;
		}

		return String.valueOf(account.get("email"));
	}

	@Override
	public String getName() {
		Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
		if (account == null) {
			return null;
		}

		Map<String, Object> profile = (Map<String, Object>) account.get("profile");
		if (profile == null) {
			return null;
		}

		return String.valueOf(profile.get("nickname"));
	}

	@Override
	public String getImageUrl() {
		Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
		if (account == null) {
			return null;
		}

		Map<String, Object> profile = (Map<String, Object>) account.get("profile");
		if (profile == null) {
			return null;
		}

		return String.valueOf(profile.get("thumbnail_image_url"));
	}
}
