package com.travel.role.domain.user.service;

import static com.travel.role.global.exception.ExceptionMessage.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.util.Optional;

import com.travel.role.domain.user.domain.User;
import com.travel.role.domain.user.dto.UserProfileDetailResDTO;
import com.travel.role.domain.user.exception.UserInfoNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.travel.role.domain.user.dao.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void 회원가입_하지않은_경우에_정보를_요청() {
        // given
        given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> {
            userService.getBasicProfile("haechan@naver.com");
        }).isInstanceOf(UserInfoNotFoundException.class)
                .hasMessageContaining(USERNAME_NOT_FOUND);
    }

    @Test
    void 회원정보_상세조회_성공() {
        // given
        String email = "email@naver.com";
        User user = User.builder()
                .id(1L)
                .name("name")
                .email(email)
                .birth(LocalDate.of(2000, 10, 10))
                .profile("imageUrl")
                .build();
        given(userRepository.findByEmail(email))
                .willReturn(Optional.of(user));

        // when
        UserProfileDetailResDTO resDTO = userService.getUserProfile(email);

        // then
        assertThat(resDTO).usingRecursiveComparison().comparingOnlyFields("id", "name", "email", "birth", "profile");
    }

    @Test
    void 회원정보_상세조회시_회원이_존재하지_않을때() {
        // given
        String email = "aaaa@naver.com";
        given(userRepository.findByEmail(email)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> {
            userService.getUserProfile(email);
        }).isInstanceOf(UserInfoNotFoundException.class)
                .hasMessage(USERNAME_NOT_FOUND);
    }
}