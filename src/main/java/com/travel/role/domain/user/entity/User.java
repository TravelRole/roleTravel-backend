package com.travel.role.domain.user.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.travel.role.domain.user.dto.auth.SignUpRequestDTO;
import com.travel.role.global.auth.oauth.OAuth2UserInfo;
import com.travel.role.global.domain.BaseTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "USER_INFO", uniqueConstraints = {@UniqueConstraint(name = "UNIQUE_EMAIL", columnNames = {"email"})})
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends BaseTime {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(nullable = false, length = 20)
	private String name;

	@Column(nullable = false)
	private String email;

	private String password;

	private String profile;

	private LocalDate birth;

	public void updatePassword(String password) {
		if (!password.isEmpty())
			this.password = password;
	}

	public static User of(SignUpRequestDTO signUpRequestDTO, String password) {
		return User.builder()
			.name(signUpRequestDTO.getName())
			.email(signUpRequestDTO.getEmail())
			.password(password)
			.birth(signUpRequestDTO.getBirth())
			.build();
	}

	public static User of(OAuth2UserInfo oAuth2UserInfo) {
		return User.builder()
			.name(oAuth2UserInfo.getName())
			.email(oAuth2UserInfo.getEmail())
			.profile(oAuth2UserInfo.getImageUrl())
			.password(oAuth2UserInfo.getId())
			.build();
	}

	public void update(String name, LocalDate birth) {

		this.name = name;
		this.birth = birth;
	}

	public void updateProfileImageUrl(String profileUrl) {

		this.profile = profileUrl;
	}
}
