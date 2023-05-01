package com.travel.role.domain.board.service;

import com.travel.role.domain.board.dto.request.BoardRequestDTO;
import com.travel.role.domain.board.dto.response.ScheduleInfoResponseDTO;
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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.travel.role.global.exception.dto.ExceptionMessage.*;

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

    public List<ScheduleInfoResponseDTO> getScheduleInfo(String email, Long roomId, LocalDate date){
        User user = userReadService.findUserByEmailOrElseThrow(email);

        Room room = roomReadService.findRoomByIdOrElseThrow(roomId);

        roomParticipantReadService.checkParticipant(user, room);

        validateDate(room.getTravelStartDate(), room.getTravelEndDate() , date);

        return getScheduleInfoResult(boardRepository.findBoardBookInfoScheduleInfoByRoomIdAndScheduleDate(roomId, date.atStartOfDay(), date.atTime(LocalTime.MAX)));
    }

    private List<ScheduleInfoResponseDTO> getScheduleInfoResult(List<Object[]> boardInfos) {
        List<ScheduleInfoResponseDTO> result = new ArrayList<>();

        for (Object[] objects : boardInfos){
            Board board = (Board) objects[0];
            BookInfo bookInfo = (BookInfo) objects[1];
            ScheduleInfo scheduleInfo = (ScheduleInfo)objects[2];

            result.add(ScheduleInfoResponseDTO.of(board,scheduleInfo,bookInfo));
        }
        return result;
    }

    public void addSchedule(String email, BoardRequestDTO boardRequestDTO) {
        User user = userReadService.findUserByEmailOrElseThrow(email);

        Room room = roomReadService.findRoomByIdOrElseThrow(boardRequestDTO.getRoomId());

        roomParticipantReadService.checkParticipant(user, room);

        validateDate(room.getTravelStartDate(), room.getTravelEndDate() ,boardRequestDTO.getScheduleDate().toLocalDate());

        Board board = boardRepository.save(Board.of(room, boardRequestDTO));

        scheduleInfoRepository.save(ScheduleInfo.of(board, boardRequestDTO));

        if (boardRequestDTO.getIsBookRequired())
            bookInfoRepository.save(BookInfo.from(board));
    }

    public void validateDate(LocalDate startDate, LocalDate endDate, LocalDate date) {
        if (date.isBefore(startDate))
            throw new InvalidLocalDateException(EARLY_DATE_ERROR);
        if (date.isAfter(endDate))
            throw new InvalidLocalDateException(LATE_DATE_ERROR);
    }

}
