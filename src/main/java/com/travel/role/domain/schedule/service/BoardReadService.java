package com.travel.role.domain.schedule.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.schedule.entity.Board;
import com.travel.role.domain.schedule.repository.BoardRepository;
import com.travel.role.global.exception.board.BoardNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardReadService {
	private final BoardRepository boardRepository;

	public List<Board> findBookInfoForDate(Long roomId, LocalDate date) {

		return boardRepository.findBoardByRoomIdAndScheduleDate(roomId, date.atStartOfDay(),
			date.atTime(LocalTime.MAX));
	}

	public List<Board> findScheduleForDate(Long roomId, LocalDate date) {

		return boardRepository.findScheduleByRoomIdAndScheduleDate(roomId, date.atStartOfDay(),
			date.atTime(LocalTime.MAX));
	}

	public Board findBoardByIdOrElseThrow(Long boardId) {

		return boardRepository.findById(boardId)
			.orElseThrow(BoardNotFoundException::new);
	}
}