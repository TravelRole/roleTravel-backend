package com.travel.role.global.auth.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.travel.role.domain.user.entity.Provider;
import com.travel.role.domain.user.entity.Role;
import com.travel.role.domain.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "AUTH_INFO")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "auth_id")
	private Long id;

	@Enumerated(EnumType.STRING)
	private Provider provider;

	@Column(name = "provider_id")
	private String providerId;

	@Column(name = "provider_token")
	private String providerToken;

	@Column(name = "refresh_token")
	private String refreshToken;

	@Enumerated(EnumType.STRING)
	private Role role;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	public static AuthInfo of(Provider provider, User user) {
		return new AuthInfo(null, provider, null, null, null, Role.USER, user);
	}

	public static AuthInfo of(Provider provider, String providerId, User user) {
		return new AuthInfo(null, provider, providerId, null, null, Role.USER, user);
	}

	public void updateRefreshToken(final String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public void deleteRefreshToken() {
		if (refreshToken != null)
			refreshToken = null;
	}

	public void updateProviderToken(final String providerToken) {
		this.providerToken = providerToken;
	}
}
