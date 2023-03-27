package com.travel.role.domain.user.domain;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.travel.role.global.auth.dto.SignUpRequestDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "users", uniqueConstraints = {@UniqueConstraint(name = "UNIQUE_EMAIL", columnNames = {"email"})})
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity extends BaseTime{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 20)
	private String name;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	private Role role;

	@Column(name = "refresh_token")
	private String refreshToken;

	private String profile;

	private LocalDate birth;

	public void updateRefreshToken(final String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public void deleteRefreshToken() {
		if (refreshToken != null) {
			refreshToken = null;
		}
	}

	public static UserEntity toEntity(SignUpRequestDTO signUpRequestDTO, String password) {
		return UserEntity.builder()
			.name(signUpRequestDTO.getName())
			.email(signUpRequestDTO.getEmail())
			.password(password)
			.birth(signUpRequestDTO.getBirth())
			.profile(signUpRequestDTO.getProfile())
			.role(Role.USER)
			.build();
	}
}
