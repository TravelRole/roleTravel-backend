package com.travel.role.domain.board.controller;

import com.travel.role.domain.board.dto.request.BoardRequestDTO;
import com.travel.role.domain.board.service.BoardService;
import com.travel.role.global.auth.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/board")
    public void addSchedule(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid BoardRequestDTO boardRequestDTO){
        boardService.addSchedule(userPrincipal.getEmail(), boardRequestDTO);
    }
}
