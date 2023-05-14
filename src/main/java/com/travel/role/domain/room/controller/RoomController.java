package com.travel.role.domain.room.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.travel.role.domain.room.dto.request.ExpensesRequestDTO;
import com.travel.role.domain.room.dto.request.MakeRoomRequestDTO;
import com.travel.role.domain.room.dto.request.RoomModifiedRequestDTO;
import com.travel.role.domain.room.dto.response.ExpenseResponseDTO;
import com.travel.role.domain.room.dto.response.InviteResponseDTO;
import com.travel.role.domain.room.dto.response.RoomInfoResponseDTO;
import com.travel.role.domain.room.dto.response.RoomResponseDTO;
import com.travel.role.domain.room.dto.response.TimeResponseDTO;
import com.travel.role.domain.room.service.RoomService;
import com.travel.role.global.auth.token.UserPrincipal;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RoomController {

	private final RoomService roomService;

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/room")
	public void makeRoom(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody MakeRoomRequestDTO dto) {
		roomService.makeRoom(userPrincipal.getEmail(), dto);
	}

	@GetMapping("/room")
	public List<RoomResponseDTO> getRoomList(@AuthenticationPrincipal UserPrincipal userPrincipal) {
		return roomService.getRoomList(userPrincipal.getEmail());
	}

	@GetMapping("/room/invite-code/{room_id}")
	public String getInviteCode(@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable("room_id") Long roomId) {
		return roomService.makeInviteCode(userPrincipal.getEmail(), roomId);
	}

	@GetMapping("/check-room/{invite_code}")
	public void checkRoomInviteCode(@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable("invite_code") String inviteCode) {
		roomService.checkRoomInviteCode(userPrincipal.getEmail(), inviteCode);
	}

	@PostMapping("/room/{invite_code}")
	@ResponseStatus(HttpStatus.CREATED)
	public InviteResponseDTO inviteUser(@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable("invite_code") String inviteCode, @RequestBody List<String> roles) {
		return roomService.inviteUser(userPrincipal.getEmail(), inviteCode, roles);
	}

	@GetMapping("/room/day")
	public List<TimeResponseDTO> getDay(@RequestParam("roomId") Long roomId) {
		return roomService.getTime(roomId);
	}

	@PutMapping("/room/{room_id}")
	public void modifyRoomInfo(@AuthenticationPrincipal UserPrincipal userPrincipal,
		@RequestBody @Valid RoomModifiedRequestDTO roomModifiedRequestDTO, @PathVariable("room_id") Long roomId) {
		roomService.modifyRoomInfo(userPrincipal.getEmail(), roomModifiedRequestDTO, roomId);
	}

	@GetMapping("/room/{room_id}/expenses")
	public ResponseEntity<ExpenseResponseDTO> readExpenses(@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable("room_id") Long roomId) {

		ExpenseResponseDTO responseDTO = roomService.getExpenses(userPrincipal.getEmail(), roomId);

		return ResponseEntity.ok(responseDTO);
	}

	@PutMapping("/room/{room_id}/expenses")
	public void modifyExpenses(@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable("room_id") Long roomId,
		@RequestBody @Valid ExpensesRequestDTO requestDTO) {

		roomService.modifyExpenses(userPrincipal.getEmail(), roomId, requestDTO);
	}

	@GetMapping("/room/{room_id}")
	public RoomInfoResponseDTO roomInfo(@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable("room_id") Long roomId) {
		return roomService.getRoomInfo(userPrincipal.getEmail(), roomId);
	}
}
