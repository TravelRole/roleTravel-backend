package com.travel.role.domain.room.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.room.entity.RoomParticipant;
import com.travel.role.domain.user.entity.User;
import com.travel.role.domain.user.service.UserReadService;
import com.travel.role.domain.wantplace.dto.request.WantPlaceRequestDTO;
import com.travel.role.domain.wantplace.entity.WantPlace;
import com.travel.role.domain.wantplace.repository.WantPlaceRepository;
import com.travel.role.domain.wantplace.service.WantPlaceService;
import com.travel.role.global.auth.token.UserPrincipal;

@ExtendWith(MockitoExtension.class)
class WantPlaceServiceTest {

    @Mock
    private UserReadService userReadService;

    @Mock
    private WantPlaceRepository wantPlaceRepository;

    @InjectMocks
    private WantPlaceService wantPlaceService;
    @Mock
    private RoomReadService roomReadService;
    @Mock
    private RoomParticipantReadService roomParticipantReadService;

    @Test
    void 가고싶은_장소_추가_성공() {
        // given
        User user1 = new User(1L, "kh", "asd@gmail.com", "1234", null, null, null, LocalDate.now(),
                null, null, null);
        User user2 = new User(2L, "hk", "asdd@gmail.com", "1234", null, null, null, LocalDate.now(),
                null, null, null);


        Room room2 = new Room(1L, "1번 방", LocalDate.now(), LocalDate.now(), null, "제주", "1234", LocalDateTime.now() , null);

        RoomParticipant roomParticipant1 = new RoomParticipant(1L, LocalDateTime.now(), true, user1, room2);
        RoomParticipant roomParticipant2 = new RoomParticipant(1L, LocalDateTime.now(), true, user2, room2);

        Set<RoomParticipant> participants = new HashSet<>();
        participants.add(roomParticipant1);
        participants.add(roomParticipant2);

        Room room = new Room(1L, "1번 방", LocalDate.now(), LocalDate.now(), null, "제주", "1234", LocalDateTime.now() , participants);

        WantPlace wantPlace = WantPlace.of(room, getWantPlaceRequestDto());

        given(userReadService.findUserByEmailOrElseThrow(any(UserPrincipal.class)))
                .willReturn(user2);
        given(roomReadService.findRoomByIdOrElseThrow(anyLong()))
                .willReturn(room);
        doNothing()
            .when(roomParticipantReadService).checkParticipant(any(User.class), any(Room.class));

        // when
        wantPlaceService.addWantPlace(makeUserPrincipal(), getWantPlaceRequestDto());

        // then
        Assertions.assertThat(wantPlace.getPlaceName()).isEqualTo("제주도");
        Assertions.assertThat(wantPlace.getPhoneNumber()).isEqualTo("1234");
        Assertions.assertThat(wantPlace.getRoom()).isEqualTo(room);
        Assertions.assertThat(wantPlace.getLatitude()).isEqualTo(123.0);
        Assertions.assertThat(wantPlace.getLongitude()).isEqualTo(456.0);
        verify(wantPlaceRepository, times(1)).save(any(WantPlace.class));
    }

    private static WantPlaceRequestDTO getWantPlaceRequestDto() {
        return new WantPlaceRequestDTO(
                1L, "제주도", "제주도", "1234",
                123.0, 456.0);
    }

    private static UserPrincipal makeUserPrincipal() {
        return new UserPrincipal(1L, "asdd@gmail.com", "1234", null);
    }
}
