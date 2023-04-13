package com.travel.role.domain.room.service;

import com.travel.role.domain.room.dao.RoomRepository;
import com.travel.role.domain.room.dao.WantPlaceRepository;
import com.travel.role.domain.room.domain.Room;
import com.travel.role.domain.room.domain.RoomParticipant;
import com.travel.role.domain.room.domain.WantPlace;
import com.travel.role.domain.room.dto.WantPlaceRequestDTO;
import com.travel.role.domain.user.dao.UserRepository;
import com.travel.role.domain.user.domain.User;
import com.travel.role.domain.user.exception.RoomInfoNotFoundException;
import com.travel.role.domain.user.exception.UserInfoNotFoundException;
import com.travel.role.global.auth.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static com.travel.role.global.exception.ExceptionMessage.ROOM_NOT_FOUND;
import static com.travel.role.global.exception.ExceptionMessage.USERNAME_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class WantPlaceService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final WantPlaceRepository wantPlaceRepository;

    public void addWantPlace(UserPrincipal userPrincipal, WantPlaceRequestDTO wantPlaceRequestDTO) {
        User loginUser = findUser(userPrincipal);
        Room room = findRoom(wantPlaceRequestDTO.getRoomId());
        Set<RoomParticipant> participants = room.getRoomParticipants();

        for (RoomParticipant participant : participants) {
            User user = participant.getUser();
            if (loginUser.getId().equals(user.getId())){
                wantPlaceRepository.save(WantPlace.of(room, wantPlaceRequestDTO));
            }
        }
    }

    private Room findRoom(Long id) {
        return roomRepository.findByIdWithParticipants(id)
                .orElseThrow(() -> new RoomInfoNotFoundException(ROOM_NOT_FOUND));
    }

    private User findUser(UserPrincipal userPrincipal) {
        return userRepository.findByEmail(userPrincipal.getEmail())
                .orElseThrow(() -> new UserInfoNotFoundException(USERNAME_NOT_FOUND));
    }
}
