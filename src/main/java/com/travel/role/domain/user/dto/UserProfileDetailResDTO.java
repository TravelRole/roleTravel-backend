package com.travel.role.domain.user.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.travel.role.domain.user.entity.User;
import com.travel.role.global.auth.entity.AuthInfo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfileDetailResDTO {

    private Long userId;

    private String email;

    private String name;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate birth;

    private String profile;

    private String provider;

    public static UserProfileDetailResDTO fromUser(AuthInfo authInfo) {

        User user = authInfo.getUser();
        return new UserProfileDetailResDTO(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getBirth(),
                user.getProfile(),
                authInfo.getProvider().name()
        );
    }
}
