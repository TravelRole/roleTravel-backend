package com.travel.role.unit.room.service;

import com.travel.role.domain.board.dto.request.BoardRequestDTO;
import com.travel.role.domain.board.dto.response.BookInfoResponseDTO;
import com.travel.role.domain.board.entity.Board;
import com.travel.role.domain.board.entity.BookInfo;
import com.travel.role.domain.board.entity.Category;
import com.travel.role.domain.board.entity.ScheduleInfo;
import com.travel.role.domain.board.repository.BoardRepository;
import com.travel.role.domain.board.repository.BookInfoRepository;
import com.travel.role.domain.board.repository.ScheduleInfoRepository;
import com.travel.role.domain.board.service.BoardService;
import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.room.service.RoomParticipantReadService;
import com.travel.role.domain.room.service.RoomReadService;
import com.travel.role.domain.user.entity.User;
import com.travel.role.domain.user.service.UserReadService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {
    @Mock
    private UserReadService userReadService;

    @Mock
    private RoomReadService roomReadService;

    @Mock
    private RoomParticipantReadService roomParticipantReadService;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private BookInfoRepository bookInfoRepository;

    @Mock
    private ScheduleInfoRepository scheduleInfoRepository;

    @InjectMocks
    private BoardService boardService;

    @Test
    void 예약페이지_조회_성공(){
        //given
        User user = makeUser(1L);
        Room room = makeRoom(1L);
        LocalDate date = LocalDate.now();
        given(userReadService.findUserByEmailOrElseThrow(anyString()))
                .willReturn(user);
        given(roomReadService.findRoomByIdOrElseThrow(anyLong()))
                .willReturn(room);
        given(boardRepository.findBoardByRoomIdAndScheduleDate(room.getId(),date.atStartOfDay(),date.atTime(LocalTime.MAX)))
                .willReturn(findBoardList());
        doNothing()
                .when(roomParticipantReadService).checkParticipant(any(User.class), any(Room.class));

        //when
        List<BookInfoResponseDTO> result = boardService.getBookInfo("asd@naver.com",1L,LocalDate.now());

        //then
        assertThat(result.get(0).getPlaceName()).isEqualTo("우도");
        assertThat(result.get(0).getCategory()).isEqualTo(Category.ETC);
        assertThat(result.get(0).getPrice()).isEqualTo(0);
    }

    @Test
    void 찜목록에서_일정에_추가_성공(){
        //given
        User user = makeUser(1L);
        Room room = makeRoom(1L);
        given(userReadService.findUserByEmailOrElseThrow(anyString()))
                .willReturn(user);
        given(roomReadService.findRoomByIdOrElseThrow(anyLong()))
                .willReturn(room);
        doNothing()
                .when(roomParticipantReadService).checkParticipant(any(User.class), any(Room.class));

        //when
        boardService.addSchedule("asd@gmail.com",createBoardRequestDTO());

        //then
        then(boardRepository).should(times(1)).save(any(Board.class));
        then(bookInfoRepository).should(times(1)).save(any(BookInfo.class));
        then(scheduleInfoRepository).should(times(1)).save(any(ScheduleInfo.class));
    }
    private User makeUser(Long id) {
        return User.builder()
                .id(id)
                .email("asd@naver.com")
                .name("asd")
                .build();
    }

    private Room makeRoom(Long id) {
        return Room.builder()
                .id(id)
                .location("korea")
                .roomName("asd")
                .travelStartDate(LocalDate.now())
                .travelEndDate(LocalDate.now())
                .build();
    }

    private BoardRequestDTO createBoardRequestDTO(){
        return new BoardRequestDTO(1L,"우도","제주도",LocalDate.now().atTime(LocalTime.now()),null,true, Category.ETC,123.0,456.0,null);
    }

    private List<Board> findBoardList (){
        List<Board> result = new ArrayList<>();
        Board temp = Board.of(makeRoom(1L),createBoardRequestDTO());
        BookInfo bookInfo = BookInfo.from(temp);
        ScheduleInfo scheduleInfo = ScheduleInfo.of(temp, createBoardRequestDTO());
        Board board = Board.builder()
                .id(1L)
                .scheduleDate(LocalDateTime.now())
                .category(Category.ETC)
                .scheduleInfo(scheduleInfo)
                .bookInfo(bookInfo)
                .build();

        result.add(board);

        return result;
    }
}
