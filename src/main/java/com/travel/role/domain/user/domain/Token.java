package com.travel.role.domain.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "token")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Token extends BaseTime {
	@Id
	@Column(name = "user_email")
	private String userEmail;

	@Column(name = "refresh_token")
	private String refreshToken;
}
