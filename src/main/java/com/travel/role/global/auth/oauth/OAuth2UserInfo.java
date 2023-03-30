package com.travel.role.global.auth.oauth;

import java.util.Map;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class OAuth2UserInfo {

	protected Map<String, Object> attributes;

	public abstract String getId();
	public abstract String getEmail();
	public abstract String getName();
	public abstract String getImageUrl();
}
