package com.travel.role.domain.travelessential.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travel.role.domain.travelessential.entity.EssentialCategory;
import com.travel.role.domain.travelessential.dto.request.TravelEssentialCheckReqDTO;
import com.travel.role.domain.travelessential.dto.request.TravelEssentialDeleteReqDTO;
import com.travel.role.domain.travelessential.dto.request.TravelEssentialReqDTO;
import com.travel.role.domain.travelessential.dto.response.TravelEssentialResDTO;
import com.travel.role.domain.travelessential.service.TravelEssentialService;
import com.travel.role.global.auth.token.UserPrincipal;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/room/{room_id}/essentials")
@RequiredArgsConstructor
public class TravelEssentialController {

	private final TravelEssentialService travelEssentialService;

	@PostMapping
	public void createTravelEssentials(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable("room_id") Long roomId,
		@RequestBody @Valid TravelEssentialReqDTO reqDTO) {

		travelEssentialService.createTravelEssentials(userPrincipal.getEmail(), roomId, reqDTO);
	}

	@GetMapping
	public ResponseEntity<Map<EssentialCategory, List<TravelEssentialResDTO>>> readTravelEssentials(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable("room_id") Long roomId
	) {

		Map<EssentialCategory, List<TravelEssentialResDTO>> resultMap = travelEssentialService.readAllGroupByCategory(
			userPrincipal.getEmail(), roomId);

		return ResponseEntity.ok(resultMap);
	}

	@DeleteMapping
	public void deleteTravelEssentials(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable("room_id") Long roomId,
		@RequestBody TravelEssentialDeleteReqDTO reqDTO
	) {

		travelEssentialService.deleteTravelEssentials(userPrincipal.getEmail(), roomId, reqDTO);
	}

	@PatchMapping("/check")
	public void checkTravelEssentials(@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable("room_id") Long roomId,
		@RequestBody TravelEssentialCheckReqDTO reqDTO) {

		travelEssentialService.updateCheckTravelEssentials(userPrincipal.getEmail(), roomId, reqDTO);
	}
}
