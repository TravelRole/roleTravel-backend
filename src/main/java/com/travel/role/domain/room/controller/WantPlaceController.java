package com.travel.role.domain.room.controller;

import com.travel.role.domain.room.dto.WantPlaceRequestDTO;
import com.travel.role.domain.room.service.WantPlaceService;
import com.travel.role.global.auth.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class WantPlaceController {
    private final WantPlaceService wantPlaceService;

    @PostMapping("/want-place")
    @ResponseStatus(HttpStatus.OK)
    public void addWantPlace(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid WantPlaceRequestDTO wantPlaceRequestDTO) {
        wantPlaceService.addWantPlace(userPrincipal, wantPlaceRequestDTO);
    }
}
