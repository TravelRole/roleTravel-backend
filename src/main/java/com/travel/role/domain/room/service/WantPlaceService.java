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
import com.travel.role.domain.user.exception.UserNotParticipateRoomException;
import com.travel.role.global.auth.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static com.travel.role.global.exception.ExceptionMessage.*;

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
        boolean isParticipant = checkParticipant(loginUser, room.getRoomParticipants());
        if (isParticipant)
            wantPlaceRepository.save(WantPlace.of(room, wantPlaceRequestDTO));
        else
            throw new UserNotParticipateRoomException(USER_NOT_PARTICIPATE_ROOM);
    }

    private Room findRoom(Long id) {
        return roomRepository.findByIdWithParticipants(id)
                .orElseThrow(() -> new RoomInfoNotFoundException(ROOM_NOT_FOUND));
    }

    private User findUser(UserPrincipal userPrincipal) {
        return userRepository.findByEmail(userPrincipal.getEmail())
                .orElseThrow(() -> new UserInfoNotFoundException(USERNAME_NOT_FOUND));
    }

    private boolean checkParticipant(User loginUser, Set<RoomParticipant> participants) {
        for (RoomParticipant participant : participants) {
            if (loginUser.getId().equals(participant.getUser().getId())) {
                return true;
            }
        }
        return false;
    }
}
