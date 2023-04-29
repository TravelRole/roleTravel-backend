package com.travel.role.domain.board.service;

import com.travel.role.domain.board.dto.request.BoardRequestDTO;
import com.travel.role.domain.board.entity.Board;
import com.travel.role.domain.board.entity.BookBoard;
import com.travel.role.domain.board.entity.ScheduleBoard;
import com.travel.role.domain.board.repository.BoardRepository;
import com.travel.role.domain.board.repository.BookBoardRepository;
import com.travel.role.domain.board.repository.ScheduleBoardRepository;
import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.room.service.RoomParticipantReadService;
import com.travel.role.domain.room.service.RoomReadService;
import com.travel.role.domain.user.entity.User;
import com.travel.role.domain.user.service.UserReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {
    private final UserReadService userReadService;
    private final RoomReadService roomReadService;
    private final BoardRepository boardRepository;
    private final BookBoardRepository bookBoardRepository;
    private final ScheduleBoardRepository scheduleBoardRepository;
    private final RoomParticipantReadService roomParticipantReadService;

    public void addSchedule(String email, BoardRequestDTO boardRequestDTO) {
        User user = userReadService.findUserByEmailOrElseThrow(email);
        Room room = roomReadService.findRoomByIdOrElseThrow(boardRequestDTO.getRoomId());
        roomParticipantReadService.checkParticipant(user, room);
        Board board = boardRepository.save(Board.of(room, boardRequestDTO));
        scheduleBoardRepository.save(ScheduleBoard.of(board, boardRequestDTO));
        addBookBoard(board, boardRequestDTO);
    }

    private void addBookBoard(Board board, BoardRequestDTO boardRequestDTO) {
        BookBoard bookBoard = BookBoard.builder()
                .board(board)
                .category(boardRequestDTO.getCategory())
                .isBooked(false)
                .price(BigDecimal.valueOf(0))
                .etc(null)
                .build();
        bookBoardRepository.save(bookBoard);

    }
}
