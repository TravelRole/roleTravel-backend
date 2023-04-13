package com.travel.role.domain.room.service;

import com.travel.role.domain.room.dao.RoomParticipantRepository;
import com.travel.role.domain.room.dao.RoomRepository;
import com.travel.role.domain.room.dao.WantPlaceRepository;
import com.travel.role.domain.room.domain.Room;
import com.travel.role.domain.room.domain.RoomParticipant;
import com.travel.role.domain.room.domain.WantPlace;
import com.travel.role.domain.room.dto.MakeRoomRequestDTO;
import com.travel.role.domain.room.dto.WantPlaceRequestDTO;
import com.travel.role.domain.user.dao.UserRepository;
import com.travel.role.domain.user.domain.User;
import com.travel.role.domain.user.exception.RoomInfoNotFoundException;
import com.travel.role.domain.user.exception.UserInfoNotFoundException;
import com.travel.role.global.auth.token.UserPrincipal;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.travel.role.global.exception.ExceptionMessage.ROOM_NOT_FOUND;
import static com.travel.role.global.exception.ExceptionMessage.USERNAME_NOT_FOUND;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WantPlaceServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private WantPlaceRepository wantPlaceRepository;

    @Mock
    private RoomParticipantRepository roomParticipantRepository;

    @InjectMocks
    private WantPlaceService wantPlaceService;

    private User user1;

    private Room room;
    private Room room2;

    private RoomParticipant roomParticipant1;

    private Set<RoomParticipant> participants = new HashSet<>();;

    private WantPlace wantPlace;

    @Test
    void addWantPlaceTest() {
        user1 = new User(1L,"kh","asd@gmail.com","1234",null,null,null,LocalDate.now(),null,null,null,null,null);
        MakeRoomRequestDTO makeRoom1 = new MakeRoomRequestDTO("room1", LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 3),
                3, "광양");

        MakeRoomRequestDTO makeRoom2 = new MakeRoomRequestDTO("2번방입니다!!", LocalDate.of(2023, 1, 5),
                LocalDate.of(2023, 1, 10),
                3, "스울");

        room2  = new Room(1L, "2번 방",LocalDate.now(),LocalDate.now(),"123",2,"1234","제주", null);

        roomParticipant1 = new RoomParticipant(1L, LocalDateTime.now(), true, user1, room2, null);

        participants.add(roomParticipant1);

        room  = new Room(1L, "1번 방",LocalDate.now(),LocalDate.now(),"123",2,"1234","제주", participants);

        wantPlace = WantPlace.of(room,getWantPlaceRequestDto());
        // given
        given(userRepository.findByEmail(anyString()))
                .willReturn(Optional.ofNullable(user1));
        given(roomRepository.findByIdWithParticipants(1L))
                .willReturn(Optional.ofNullable(room));
        given(wantPlaceRepository.save(any()))
                .willReturn(wantPlace);

        // when
        wantPlaceService.addWantPlace(makeUserPrincipal(), getWantPlaceRequestDto());

        // then
        verify(userRepository, times(1)).findByEmail("asd@gmail.com");
        verify(roomRepository, times(1)).findByIdWithParticipants(1L);
        verify(wantPlaceRepository, times(1)).save(any(WantPlace.class));
    }

    @Test
    void 해당하는_유저가_존재하지_않는_경우() {
        // given
        given(userRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());

        // when, then
        Assertions.assertThatThrownBy(() -> wantPlaceService.addWantPlace(makeUserPrincipal(),getWantPlaceRequestDto()))
                .isInstanceOf(UserInfoNotFoundException.class)
                .hasMessageContaining(USERNAME_NOT_FOUND);
    }

    @Test
    void addWantPlaceTest_RoomNotFound() {
        // given
        Optional<User> user = Optional.ofNullable(User.builder()
                .id(1L)
                .email(makeUserPrincipal().getEmail())
                .build());

        given(userRepository.findByEmail(anyString()))
                .willReturn(user);

        given(roomRepository.findByIdWithParticipants(anyLong()))
                .willReturn(Optional.empty());


        // when, then
        Assertions.assertThatThrownBy(() -> wantPlaceService.addWantPlace(makeUserPrincipal(), getWantPlaceRequestDto()))
                .isInstanceOf(RoomInfoNotFoundException.class)
                .hasMessageContaining(ROOM_NOT_FOUND);

    }

    private static WantPlaceRequestDTO getWantPlaceRequestDto(){
        return new WantPlaceRequestDTO(
                1L, "제주도", "제주도", "1234",
                123.0,456.0);
    }

    private static UserPrincipal makeUserPrincipal() {
        return new UserPrincipal(1L, "asd@gmail.com", "1234", null);
    }
}
