package com.travel.role.domain.room.controller;


import com.travel.role.domain.room.dto.WantPlaceRequestDTO;
import com.travel.role.domain.room.dto.WantPlaceResponseDTO;
import com.travel.role.domain.room.service.WantPlaceService;
import com.travel.role.global.auth.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class WantPlaceController {
    private final WantPlaceService wantPlaceService;

    @GetMapping("/want-place")
    public ResponseEntity<WantPlaceResponseDTO> getPlaceList(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam("roomId") Long roomId) {
        WantPlaceResponseDTO wantPlaceResponseDTOS = wantPlaceService.getWantPlaceList(userPrincipal, roomId);
        return ResponseEntity.ok(wantPlaceResponseDTOS);
    }

    @PostMapping("/want-place")
    public void addWantPlace(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid WantPlaceRequestDTO wantPlaceRequestDTO) {
        wantPlaceService.addWantPlace(userPrincipal, wantPlaceRequestDTO);
    }

    @DeleteMapping("/want-place")
    public void deleteWantPlace(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam("roomId") Long roomId, @RequestParam("placeId") Long placeId) {
        wantPlaceService.deleteWantPlace(userPrincipal, roomId, placeId);
    }
}
