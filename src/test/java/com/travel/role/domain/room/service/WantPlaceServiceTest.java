package com.travel.role.domain.room.service;

import static com.travel.role.global.exception.ExceptionMessage.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.travel.role.domain.room.dao.RoomRepository;
import com.travel.role.domain.room.dao.WantPlaceRepository;
import com.travel.role.domain.room.domain.*;
import com.travel.role.domain.room.dto.WantPlaceRequestDTO;
import com.travel.role.domain.user.dao.UserRepository;
import com.travel.role.domain.user.domain.User;
import com.travel.role.domain.user.exception.RoomInfoNotFoundException;
import com.travel.role.domain.user.exception.UserInfoNotFoundException;
import com.travel.role.domain.user.exception.UserNotParticipateRoomException;
import com.travel.role.global.auth.token.UserPrincipal;
import java.util.*;

import static com.travel.role.global.exception.ExceptionMessage.ROOM_NOT_FOUND;
import static com.travel.role.global.exception.ExceptionMessage.USERNAME_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @InjectMocks
    private WantPlaceService wantPlaceService;

    @Test
    void 가고싶은_장소_추가_성공() {
        // given
        User user1 = new User(1L, "kh", "asd@gmail.com", "1234", null, null, null, LocalDate.now(),
                null, null, null);
        User user2 = new User(2L, "hk", "asdd@gmail.com", "1234", null, null, null, LocalDate.now(),
                null, null, null);


        Room room2 = new Room(1L, "1번 방", LocalDate.now(), LocalDate.now(), null, "제주", "1234", LocalDateTime.now() , null);

        ParticipantRole participantRole = new ParticipantRole(1L, RoomRole.SCHEDULE, null);
        List<ParticipantRole> participantRoles = new ArrayList<>();
        participantRoles.add(participantRole);
        RoomParticipant roomParticipant1 = new RoomParticipant(1L, LocalDateTime.now(), true, user1, room2, participantRoles);
        RoomParticipant roomParticipant2 = new RoomParticipant(1L, LocalDateTime.now(), true, user2, room2, participantRoles);

        Set<RoomParticipant> participants = new HashSet<>();
        participants.add(roomParticipant1);
        participants.add(roomParticipant2);

        Room room = new Room(1L, "1번 방", LocalDate.now(), LocalDate.now(), null, "제주", "1234", LocalDateTime.now() , participants);

        WantPlace wantPlace = WantPlace.of(room, getWantPlaceRequestDto());

        given(userRepository.findByEmail(anyString()))
                .willReturn(Optional.of(user2));
        given(roomRepository.findByIdWithParticipants(anyLong()))
                .willReturn(Optional.of(room));

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

    @Test
    void 방에_참여하지_않은_사용자가_해당_api_호출했을_경우() {
        // given
        User user1 = new User(1L, "kh", "asd@gmail.com", "1234", null, null, null, LocalDate.now(),
                null, null, null);
        User user2 = new User(2L, "hk", "asdd@gmail.com", "1234", null, null, null, LocalDate.now(),
                null, null, null);

        Room room2 = new Room(1L, "1번 방", LocalDate.now(), LocalDate.now(), null, "제주", "1234", LocalDateTime.now() , null);
        RoomParticipant roomParticipant1 = new RoomParticipant(1L, LocalDateTime.now(), true, user2, room2, null);

        Set<RoomParticipant> participants = new HashSet<>();
        participants.add(roomParticipant1);

        Room room = new Room(1L, "1번 방", LocalDate.now(), LocalDate.now(), null, "제주", "1234", LocalDateTime.now() , participants);

        given(userRepository.findByEmail(anyString()))
                .willReturn(Optional.of(user1));
        given(roomRepository.findByIdWithParticipants(anyLong()))
                .willReturn(Optional.of(room));

        // when, then
        assertThrows(UserNotParticipateRoomException.class, () -> wantPlaceService.addWantPlace(makeUserPrincipal(), getWantPlaceRequestDto()));
    }

    @Test
    void 해당하는_유저가_존재하지_않는_경우() {
        // given
        given(userRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());

        // when, then
        Assertions.assertThatThrownBy(() -> wantPlaceService.addWantPlace(makeUserPrincipal(), getWantPlaceRequestDto()))
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

    private static WantPlaceRequestDTO getWantPlaceRequestDto() {
        return new WantPlaceRequestDTO(
                1L, "제주도", "제주도", "1234",
                123.0, 456.0);
    }

    private static UserPrincipal makeUserPrincipal() {
        return new UserPrincipal(1L, "asdd@gmail.com", "1234", null);
    }
}
