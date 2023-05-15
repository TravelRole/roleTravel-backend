package com.travel.role.domain.schedule.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.board.entity.Board;
import com.travel.role.domain.board.service.BoardReadService;
import com.travel.role.domain.board.service.BoardService;
import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.room.service.RoomParticipantReadService;
import com.travel.role.domain.room.service.RoomReadService;
import com.travel.role.domain.schedule.dto.response.ScheduleResponseDTO;
import com.travel.role.domain.user.entity.User;
import com.travel.role.domain.user.service.UserReadService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleService {
	private final BoardService boardService;
	private final UserReadService userReadService;
	private final RoomReadService roomReadService;
	private final BoardReadService boardReadService;
	private final RoomParticipantReadService roomParticipantReadService;

	public List<ScheduleResponseDTO> getSchedule(String email, Long roomId, LocalDate date) {

		User user = userReadService.findUserByEmailOrElseThrow(email);
		Room room = roomReadService.findRoomByIdOrElseThrow(roomId);
		roomParticipantReadService.checkParticipant(user, room);
		boardService.validateDate(room.getTravelStartDate(), room.getTravelEndDate(), date);

		return getScheduleInfo(boardReadService.findScheduleForDate(roomId, date));
	}

	private List<ScheduleResponseDTO> getScheduleInfo(List<Board> boardList) {
		return boardList.stream()
			.map(board -> ScheduleResponseDTO.from(board))
			.collect(Collectors.toList());
	}

}
