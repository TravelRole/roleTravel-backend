package com.travel.role.domain.schedule.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.schedule.entity.ScheduleInfo;
import com.travel.role.domain.schedule.repository.ScheduleInfoRepository;
import com.travel.role.global.exception.board.BoardNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleReadService {

	private final ScheduleInfoRepository scheduleInfoRepository;

	public ScheduleInfo findScheduleInfoByIdOrElseThrow(Long boardId) {

		return scheduleInfoRepository.findById(boardId)
			.orElseThrow(BoardNotFoundException::new);
	}
}
