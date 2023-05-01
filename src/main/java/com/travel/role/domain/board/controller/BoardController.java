package com.travel.role.domain.board.controller;

import com.travel.role.domain.board.dto.request.BoardRequestDTO;
import com.travel.role.domain.board.dto.response.BookInfoResponseDTO;
import com.travel.role.domain.board.service.BoardService;
import com.travel.role.global.auth.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {
    private final BoardService boardService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addSchedule(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid BoardRequestDTO boardRequestDTO) {
        boardService.addSchedule(userPrincipal.getEmail(), boardRequestDTO);
    }

    @GetMapping("/book")
    public List<BookInfoResponseDTO> getScheduleInfo(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam("roomId") Long roomId, @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return boardService.getScheduleInfo(userPrincipal.getEmail(), roomId, date);
    }

}
