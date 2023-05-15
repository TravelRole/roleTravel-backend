package com.travel.role.domain.schedule.service;

import static com.travel.role.global.exception.dto.ExceptionMessage.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.accounting.entity.AccountingInfo;
import com.travel.role.domain.board.entity.Board;
import com.travel.role.domain.board.entity.BookInfo;
import com.travel.role.domain.board.repository.BoardRepository;
import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.room.service.RoomParticipantReadService;
import com.travel.role.domain.room.service.RoomReadService;
import com.travel.role.domain.schedule.dto.response.ScheduleResponseDTO;
import com.travel.role.domain.schedule.entity.ScheduleInfo;
import com.travel.role.domain.user.entity.User;
import com.travel.role.domain.user.service.UserReadService;
import com.travel.role.global.exception.room.InvalidLocalDateException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleService {
	private final UserReadService userReadService;
	private final RoomReadService roomReadService;
	private final BoardRepository boardRepository;
	private final RoomParticipantReadService roomParticipantReadService;

	public List<ScheduleResponseDTO> getSchedule(String email, Long roomId, LocalDate date) {
		User user = userReadService.findUserByEmailOrElseThrow(email);
		Room room = roomReadService.findRoomByIdOrElseThrow(roomId);
		roomParticipantReadService.checkParticipant(user, room);

		validateDate(room.getTravelStartDate(), room.getTravelEndDate(), date);

		return getScheduleInfo(
			boardRepository.findScheduleByRoomIdAndScheduleDate(roomId, date.atStartOfDay(),
				date.atTime(LocalTime.MAX)));
	}

	private List<ScheduleResponseDTO> getScheduleInfo(List<Board> boardList) {
		List<ScheduleResponseDTO> resultList = new ArrayList<>();
		for (Board board : boardList) {
			ScheduleInfo scheduleInfo = board.getScheduleInfo();
			AccountingInfo accountingInfo = board.getAccountingInfo();
			BookInfo bookInfo = accountingInfo != null ? accountingInfo.getBookInfo() : null;
			ScheduleResponseDTO scheduleResponseDTO = ScheduleResponseDTO.of(board, scheduleInfo, accountingInfo,
				bookInfo);
			resultList.add(scheduleResponseDTO);
		}
		return resultList;
	}

	public void validateDate(LocalDate startDate, LocalDate endDate, LocalDate date) {
		if (date.isBefore(startDate))
			throw new InvalidLocalDateException(EARLY_DATE_ERROR);
		if (date.isAfter(endDate))
			throw new InvalidLocalDateException(LATE_DATE_ERROR);
	}
}
