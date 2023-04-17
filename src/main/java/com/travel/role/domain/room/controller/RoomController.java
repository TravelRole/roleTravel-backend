package com.travel.role.domain.room.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.travel.role.domain.room.dto.MakeRoomRequestDTO;
import com.travel.role.domain.room.dto.RoomResponseDTO;
import com.travel.role.domain.room.service.RoomService;
import com.travel.role.global.auth.token.UserPrincipal;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class RoomController {

	private final RoomService roomService;

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/room")
	public void makeRoom(@AuthenticationPrincipal UserPrincipal userPrincipal,@RequestBody MakeRoomRequestDTO dto) {
		roomService.makeRoom(userPrincipal, dto);
	}

	@GetMapping("/room")
	public List<RoomResponseDTO> getRoomList(@AuthenticationPrincipal UserPrincipal userPrincipal) {
		return roomService.getRoomList(userPrincipal);
	}

	@GetMapping("/room/invite-code")
	public String makeInviteCode(@AuthenticationPrincipal UserPrincipal userPrincipal) {
		return roomService.makeInviteCode(userPrincipal.getEmail());
	}
}
