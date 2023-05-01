package com.travel.role.domain.board.service;

import com.travel.role.domain.board.dto.request.BoardRequestDTO;
import com.travel.role.domain.board.entity.Board;
import com.travel.role.domain.board.entity.BookInfo;
import com.travel.role.domain.board.entity.ScheduleInfo;
import com.travel.role.domain.board.repository.BoardRepository;
import com.travel.role.domain.board.repository.BookInfoRepository;
import com.travel.role.domain.board.repository.ScheduleInfoRepository;
import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.room.service.RoomParticipantReadService;
import com.travel.role.domain.room.service.RoomReadService;
import com.travel.role.domain.user.entity.User;
import com.travel.role.domain.user.service.UserReadService;
import com.travel.role.global.exception.room.InvalidLocalDateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.travel.role.global.exception.dto.ExceptionMessage.INVALID_DATE_ERROR;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {
    private final UserReadService userReadService;
    private final RoomReadService roomReadService;
    private final BoardRepository boardRepository;
    private final BookInfoRepository bookInfoRepository;
    private final ScheduleInfoRepository scheduleInfoRepository;
    private final RoomParticipantReadService roomParticipantReadService;

    public void addSchedule(String email, BoardRequestDTO boardRequestDTO) {
        User user = userReadService.findUserByEmailOrElseThrow(email);

        Room room = roomReadService.findRoomByIdOrElseThrow(boardRequestDTO.getRoomId());

        roomParticipantReadService.checkParticipant(user, room);

        validateDate(room, boardRequestDTO);

        Board board = boardRepository.save(Board.of(room, boardRequestDTO));

        scheduleInfoRepository.save(ScheduleInfo.of(board, boardRequestDTO));

        if (boardRequestDTO.getIsBookRequired())
            bookInfoRepository.save(BookInfo.from(board));
    }

    public void validateDate(Room room, BoardRequestDTO boardRequestDTO) {
        LocalDate start = room.getTravelStartDate();
        LocalDate end = room.getTravelEndDate();
        LocalDate addTime = boardRequestDTO.getScheduleDate().toLocalDate();

        if (addTime.isBefore(start))
            throw new InvalidLocalDateException(INVALID_DATE_ERROR);
        if (addTime.isAfter(end))
            throw new InvalidLocalDateException(INVALID_DATE_ERROR);
    }

}
