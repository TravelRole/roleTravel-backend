package com.travel.role.domain.user.dto.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDTO {

    @NotBlank(message = "유저이름 값이 비어있으면 안됩니다.")
    private String name;

    @NotBlank(message = "이메일 값이 비어있으면 안됩니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호 값이 비어있으면 안됩니다.")
    private String password;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate birth;
}
