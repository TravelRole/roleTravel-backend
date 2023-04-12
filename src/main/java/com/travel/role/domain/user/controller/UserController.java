package com.travel.role.domain.user.controller;

import com.travel.role.domain.user.dto.UserProfileDetailResDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travel.role.domain.user.dto.UserProfileResponseDTO;
import com.travel.role.domain.user.service.UserService;
import com.travel.role.global.auth.token.UserPrincipal;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @GetMapping("/basic-profile")
    public UserProfileResponseDTO basicProfile(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return userService.getBasicProfile(userPrincipal.getEmail());
    }

    @GetMapping("/users")
    public ResponseEntity<UserProfileDetailResDTO> getUserProfile(@AuthenticationPrincipal UserPrincipal userPrincipal) {

        UserProfileDetailResDTO resDTO = userService.getUserProfile(userPrincipal.getEmail());

        return ResponseEntity.ok(resDTO);
    }
}
