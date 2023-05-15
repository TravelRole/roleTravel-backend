package com.travel.role.domain.board.controller;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.travel.role.domain.board.dto.request.BoardRequestDTO;
import com.travel.role.domain.board.dto.request.BookInfoRequestDTO;
import com.travel.role.domain.board.dto.request.BookedRequestDTO;
import com.travel.role.domain.board.dto.response.BookInfoResponseDTO;
import com.travel.role.domain.board.service.BoardService;
import com.travel.role.global.auth.token.UserPrincipal;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/room/{roomId}/board")
public class BoardController {
	private final BoardService boardService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void addSchedule(@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable("roomId") Long roomId,
		@RequestBody @Valid BoardRequestDTO boardRequestDTO) {
		boardService.addSchedule(userPrincipal.getEmail(), roomId, boardRequestDTO);
	}

	@GetMapping("/book")
	public List<BookInfoResponseDTO> getBookInfo(@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable("roomId") Long roomId,
		@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
		return boardService.getBookInfo(userPrincipal.getEmail(), roomId, date);
	}

	@PatchMapping("/book")
	public void updateBookInfo(@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable("roomId") Long roomId,
		@RequestBody @Valid BookInfoRequestDTO bookInfoRequestDTO) {
		boardService.modifyBookInfo(userPrincipal.getEmail(), roomId, bookInfoRequestDTO);
	}

	@PatchMapping("/booked")
	public void updateIsBook(@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable("roomId") Long roomId,
		@RequestBody @Valid BookedRequestDTO bookedRequestDTO
	) {
		boardService.modifyIsBookedAndPaymentTime(userPrincipal.getEmail(), roomId, bookedRequestDTO);
	}
}
