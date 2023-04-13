package com.travel.role.domain.user.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.travel.role.domain.room.domain.TravelEssentials;
import com.travel.role.domain.room.domain.RoomParticipant;
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

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "refresh_token")
    private String refreshToken;

    private String profile;

    private LocalDate birth;
    @Enumerated(EnumType.STRING)
    private Provider provider;
    private String providerId;

    @Column(name = "provider_token")
    private String providerToken;

    public void updateRefreshToken(final String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void deleteRefreshToken() {
        if (refreshToken != null) {
            refreshToken = null;
        }
    }

    public void updatePassword(String password) {
        if (!password.isEmpty())
            this.password = password;
    }

    public void updateProviderToken(final String providerToken) {
        this.providerToken = providerToken;
    }

    public static User of(SignUpRequestDTO signUpRequestDTO, String password) {
        return User.builder()
                .name(signUpRequestDTO.getName())
                .email(signUpRequestDTO.getEmail())
                .password(password)
                .birth(signUpRequestDTO.getBirth())
                .provider(Provider.local)
                .role(Role.USER)
                .build();
    }

    public static User of(Provider provider, OAuth2UserInfo oAuth2UserInfo) {
        return User.builder()
                .name(oAuth2UserInfo.getName())
                .email(oAuth2UserInfo.getEmail())
                .role(Role.USER)
                .profile(oAuth2UserInfo.getImageUrl())
                .providerId(oAuth2UserInfo.getId())
                .password(oAuth2UserInfo.getId())
                .provider(provider)
                .build();
    }
}
