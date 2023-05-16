package com.travel.role.domain.schedule.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.book.service.BookService;
import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.room.entity.RoomRole;
import com.travel.role.domain.room.service.ParticipantRoleReadService;
import com.travel.role.domain.room.service.RoomParticipantReadService;
import com.travel.role.domain.room.service.RoomReadService;
import com.travel.role.domain.schedule.dto.request.ScheduleRequestDTO;
import com.travel.role.domain.schedule.dto.response.ScheduleResponseDTO;
import com.travel.role.domain.schedule.entity.Board;
import com.travel.role.domain.schedule.repository.BoardRepository;
import com.travel.role.domain.schedule.repository.ScheduleInfoRepository;
import com.travel.role.domain.user.entity.User;
import com.travel.role.domain.user.service.UserReadService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleService {
	private final BookService bookService;
	private final UserReadService userReadService;
	private final RoomReadService roomReadService;
	private final BoardReadService boardReadService;
	private final ScheduleReadService scheduleReadService;
	private final RoomParticipantReadService roomParticipantReadService;
	private final ParticipantRoleReadService participantRoleReadService;
	private final BoardRepository boardRepository;
	private final ScheduleInfoRepository scheduleInfoRepository;

	public void modifySchedule(String email, Long roomId, ScheduleRequestDTO scheduleRequestDTO) {

		User user = userReadService.findUserByEmailOrElseThrow(email);
		Room room = roomReadService.findRoomByIdOrElseThrow(roomId);

		roomParticipantReadService.checkParticipant(user, room);
		participantRoleReadService.validateUserRoleInRoom(user, room, RoomRole.getScheduleRoles());

		boardReadService.findBoardByIdOrElseThrow(scheduleRequestDTO.getBoardId());
		scheduleReadService.findScheduleInfoByIdOrElseThrow(scheduleRequestDTO.getMapPlaceId());

	}

	public void deleteSchedule(String email, Long roomId, List<Long> scheduleIds) {

		User user = userReadService.findUserByEmailOrElseThrow(email);
		Room room = roomReadService.findRoomByIdOrElseThrow(roomId);

		roomParticipantReadService.checkParticipant(user, room);
		participantRoleReadService.validateUserRoleInRoom(user, room, RoomRole.getScheduleRoles());

		deleteScheduleById(scheduleIds);
	}

	private void deleteScheduleById(List<Long> scheduleIds) {

		scheduleInfoRepository.deleteAllByIds(scheduleIds);
		boardRepository.deleteAllByIds(scheduleIds);
	}

	public List<ScheduleResponseDTO> getSchedule(String email, Long roomId, LocalDate date) {

		User user = userReadService.findUserByEmailOrElseThrow(email);
		Room room = roomReadService.findRoomByIdOrElseThrow(roomId);

		roomParticipantReadService.checkParticipant(user, room);
		bookService.validateDate(room.getTravelStartDate(), room.getTravelEndDate(), date);

		return getScheduleInfo(boardReadService.findScheduleForDate(roomId, date));
	}

	private List<ScheduleResponseDTO> getScheduleInfo(List<Board> boardList) {
		return boardList.stream()
			.map(board -> ScheduleResponseDTO.from(board))
			.collect(Collectors.toList());
	}

}
