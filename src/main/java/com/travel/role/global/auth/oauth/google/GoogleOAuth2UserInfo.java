package com.travel.role.global.auth.oauth.google;

import java.util.Map;

import com.travel.role.global.auth.oauth.OAuth2UserInfo;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

	public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
		super(attributes);
	}

	@Override
	public String getId() {
		return (String)attributes.get("sub");
	}

	@Override
	public String getEmail() {
		return (String)attributes.get("email");
	}

	@Override
	public String getName() {
		return (String)attributes.get("name");
	}

	@Override
	public String getImageUrl() {
		return (String)attributes.get("picture");
	}
}
