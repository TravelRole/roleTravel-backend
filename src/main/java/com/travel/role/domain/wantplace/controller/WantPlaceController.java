package com.travel.role.domain.wantplace.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.travel.role.domain.wantplace.dto.request.WantPlaceRequestDTO;
import com.travel.role.domain.wantplace.dto.response.WantPlaceResponseDTO;
import com.travel.role.domain.wantplace.service.WantPlaceService;
import com.travel.role.global.auth.token.UserPrincipal;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/want-place")
public class WantPlaceController {
	private final WantPlaceService wantPlaceService;

	@GetMapping
	public ResponseEntity<WantPlaceResponseDTO> getPlaceList(@AuthenticationPrincipal UserPrincipal userPrincipal,
		@RequestParam("roomId") Long roomId) {
		WantPlaceResponseDTO wantPlaceResponseDTOS = wantPlaceService.getWantPlaceList(userPrincipal.getEmail(),
			roomId);
		return ResponseEntity.ok(wantPlaceResponseDTOS);
	}

	@PostMapping
	public void addWantPlace(@AuthenticationPrincipal UserPrincipal userPrincipal,
		@RequestBody @Valid WantPlaceRequestDTO wantPlaceRequestDTO) {
		wantPlaceService.addWantPlace(userPrincipal.getEmail(), wantPlaceRequestDTO);
	}

	@DeleteMapping
	public void deleteWantPlace(@AuthenticationPrincipal UserPrincipal userPrincipal,
		@RequestParam("roomId") Long roomId, @RequestParam("placeId") Long placeId) {
		wantPlaceService.deleteWantPlace(userPrincipal.getEmail(), roomId, placeId);
	}
}
