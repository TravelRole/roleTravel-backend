package com.travel.role.domain.book.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.book.entity.BookInfo;
import com.travel.role.domain.book.repository.BookInfoRepository;
import com.travel.role.domain.schedule.entity.Board;
import com.travel.role.domain.schedule.repository.BoardRepository;
import com.travel.role.global.exception.board.BookInfoNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookReadService {
	private final BookInfoRepository bookInfoRepository;
	private final BoardRepository boardRepository;

	public List<Board> findBookInfoForDate(Long roomId, LocalDate date) {

		return boardRepository.findBoardByRoomIdAndScheduleDate(roomId, date.atStartOfDay(),
			date.atTime(LocalTime.MAX));
	}

	public BookInfo findBookInfoByIdOrElseThrow(Long bookInfoId) {

		return bookInfoRepository.findById(bookInfoId)
			.orElseThrow(BookInfoNotFoundException::new);
	}
}
