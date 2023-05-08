package com.travel.role.domain.board.service;

import com.travel.role.domain.board.dto.request.BoardRequestDTO;
import com.travel.role.domain.board.dto.response.BookInfoResponseDTO;
import com.travel.role.domain.board.entity.AccountingInfo;
import com.travel.role.domain.board.entity.Board;
import com.travel.role.domain.board.entity.BookInfo;
import com.travel.role.domain.board.entity.ScheduleInfo;
import com.travel.role.domain.board.repository.AccountingInfoRepository;
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
import java.util.List;
import java.util.stream.Collectors;

import static com.travel.role.global.exception.dto.ExceptionMessage.EARLY_DATE_ERROR;
import static com.travel.role.global.exception.dto.ExceptionMessage.LATE_DATE_ERROR;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {
    private final UserReadService userReadService;
    private final RoomReadService roomReadService;
    private final BoardRepository boardRepository;
    private final BookInfoRepository bookInfoRepository;
    private final ScheduleInfoRepository scheduleInfoRepository;
    private final AccountingInfoRepository accountingInfoRepository;
    private final RoomParticipantReadService roomParticipantReadService;

    public List<BookInfoResponseDTO> getBookInfo(String email, Long roomId, LocalDate date) {
        User user = userReadService.findUserByEmailOrElseThrow(email);

        Room room = roomReadService.findRoomByIdOrElseThrow(roomId);

        roomParticipantReadService.checkParticipant(user, room);

        validateDate(room.getTravelStartDate(), room.getTravelEndDate(), date);

        return getBookInfoResult(boardRepository.findBoardByRoomIdAndScheduleDate(roomId, date.atStartOfDay(), date.atTime(LocalTime.MAX)));
    }

    private List<BookInfoResponseDTO> getBookInfoResult(List<Board> boardList) {
        return boardList.stream()
                .map(board -> BookInfoResponseDTO.of(board, board.getScheduleInfo(), board.getAccountingInfo(), board.getAccountingInfo().getBookInfo()))
                .collect(Collectors.toList());
    }

    public void addSchedule(String email, BoardRequestDTO boardRequestDTO) {
        User user = userReadService.findUserByEmailOrElseThrow(email);

        Room room = roomReadService.findRoomByIdOrElseThrow(boardRequestDTO.getRoomId());

        roomParticipantReadService.checkParticipant(user, room);

        validateDate(room.getTravelStartDate(), room.getTravelEndDate(), boardRequestDTO.getScheduleDate().toLocalDate());

        Board board = boardRepository.save(Board.of(room, boardRequestDTO));

        scheduleInfoRepository.save(ScheduleInfo.of(board, boardRequestDTO));

        if (boardRequestDTO.getIsBookRequired()) {
            BookInfo bookInfo = bookInfoRepository.save(BookInfo.builder()
                    .isBooked(false)
                    .bookEtc(null)
                    .build());
            accountingInfoRepository.save(AccountingInfo.builder()
                    .accountingEtc(null)
                    .board(board)
                    .bookInfo(bookInfo)
                    .category(boardRequestDTO.getCategory())
                    .paymentMethod(null)
                    .paymentName(boardRequestDTO.getPlaceName())
                    .paymentTime(null)
                    .price(0)
                    .room(room)
                    .build());
        }
    }

    public void validateDate(LocalDate startDate, LocalDate endDate, LocalDate date) {
        if (date.isBefore(startDate))
            throw new InvalidLocalDateException(EARLY_DATE_ERROR);
        if (date.isAfter(endDate))
            throw new InvalidLocalDateException(LATE_DATE_ERROR);
    }

}
