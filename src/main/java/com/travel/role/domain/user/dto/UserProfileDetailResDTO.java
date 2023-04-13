package com.travel.role.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.travel.role.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

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

    public static UserProfileDetailResDTO fromUser(User user) {

        return new UserProfileDetailResDTO(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getBirth(),
                user.getProfile(),
                user.getProvider().name()
        );
    }
}
