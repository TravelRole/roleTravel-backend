package com.travel.role.domain.room.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.travel.role.domain.room.dto.MakeRoomRequestDTO;
import com.travel.role.domain.room.service.RoomService;
import com.travel.role.global.auth.token.UserPrincipal;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RoomController {

	private final RoomService roomService;

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/v1/room")
	public void makeRoom(@AuthenticationPrincipal UserPrincipal userPrincipal,@RequestBody MakeRoomRequestDTO dto) {
		roomService.makeRoom(userPrincipal, dto);
	}
}
