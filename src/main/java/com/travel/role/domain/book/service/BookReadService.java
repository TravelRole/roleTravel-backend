package com.travel.role.domain.book.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.book.entity.BookInfo;
import com.travel.role.domain.book.repository.BookInfoRepository;
import com.travel.role.global.exception.board.BookInfoNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookReadService {
	private final BookInfoRepository bookInfoRepository;

	public BookInfo findBookInfoByIdOrElseThrow(Long bookInfoId) {

		return bookInfoRepository.findById(bookInfoId)
			.orElseThrow(BookInfoNotFoundException::new);
	}
}
