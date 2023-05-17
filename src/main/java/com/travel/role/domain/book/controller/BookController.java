package com.travel.role.domain.book.controller;

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

import com.travel.role.domain.book.dto.request.BookInfoRequestDTO;
import com.travel.role.domain.book.dto.request.BookModifyRequestDTO;
import com.travel.role.domain.book.dto.request.BookedRequestDTO;
import com.travel.role.domain.book.dto.response.BookInfoResponseDTO;
import com.travel.role.domain.book.service.BookService;
import com.travel.role.global.auth.token.UserPrincipal;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/room/{roomId}/board")
public class BookController {
	private final BookService bookService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void addSchedule(@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable("roomId") Long roomId,
		@RequestBody @Valid BookInfoRequestDTO bookInfoRequestDTO) {
		bookService.addSchedule(userPrincipal.getEmail(), roomId, bookInfoRequestDTO);
	}

	@GetMapping("/book")
	public List<BookInfoResponseDTO> getBookInfo(@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable("roomId") Long roomId,
		@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
		return bookService.getBookInfo(userPrincipal.getEmail(), roomId, date);
	}

	@PatchMapping("/book")
	public void updateBookInfo(@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable("roomId") Long roomId,
		@RequestBody @Valid BookModifyRequestDTO bookModifyRequestDTO) {
		bookService.modifyBookInfo(userPrincipal.getEmail(), roomId, bookModifyRequestDTO);
	}

	@PatchMapping("/booked")
	public void updateIsBook(@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable("roomId") Long roomId,
		@RequestBody @Valid BookedRequestDTO bookedRequestDTO
	) {
		bookService.modifyIsBookedAndPaymentTime(userPrincipal.getEmail(), roomId, bookedRequestDTO);
	}
}
