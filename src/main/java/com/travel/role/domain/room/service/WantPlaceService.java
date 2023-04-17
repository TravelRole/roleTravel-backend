package com.travel.role.domain.room.service;

import com.travel.role.domain.room.dao.RoomRepository;
import com.travel.role.domain.room.dao.WantPlaceRepository;
import com.travel.role.domain.room.domain.ParticipantRole;
import com.travel.role.domain.room.domain.Room;
import com.travel.role.domain.room.domain.RoomParticipant;
import com.travel.role.domain.room.domain.WantPlace;
import com.travel.role.domain.room.dto.WantPlaceDTO;
import com.travel.role.domain.room.dto.WantPlaceRequestDTO;
import com.travel.role.domain.room.dto.WantPlaceResponseDTO;
import com.travel.role.domain.user.dao.UserRepository;
import com.travel.role.domain.user.domain.User;
import com.travel.role.domain.user.exception.RoomInfoNotFoundException;
import com.travel.role.domain.user.exception.UserInfoNotFoundException;
import com.travel.role.domain.user.exception.UserNotParticipateRoomException;
import com.travel.role.global.auth.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.travel.role.global.exception.ExceptionMessage.*;

@Service
@RequiredArgsConstructor
@Transactional
public class WantPlaceService {
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final WantPlaceRepository wantPlaceRepository;

    public WantPlaceResponseDTO getPlaceList(UserPrincipal userPrincipal, Long roomId) {
        User loginUser = findUser(userPrincipal);
        Room room = findRoom(roomId);
        RoomParticipant roomParticipant = checkParticipantWithReturnRole(loginUser, room.getRoomParticipants());
        List<WantPlaceDTO> wantPlaceDTOS = getWantPlaceList(roomId);
        return WantPlaceResponseDTO.of(wantPlaceDTOS, checkScheduler(roomParticipant.getParticipantRoles()));
    }

    public void addWantPlace(UserPrincipal userPrincipal, WantPlaceRequestDTO wantPlaceRequestDTO) {
        User loginUser = findUser(userPrincipal);
        Room room = findRoom(wantPlaceRequestDTO.getRoomId());
        checkParticipant(loginUser, room.getRoomParticipants());
        wantPlaceRepository.save(WantPlace.of(room, wantPlaceRequestDTO));
    }

    private Room findRoom(Long roomId) {
        return roomRepository.findByIdWithParticipants(roomId)
                .orElseThrow(() -> new RoomInfoNotFoundException(ROOM_NOT_FOUND));
    }

    private User findUser(UserPrincipal userPrincipal) {
        return userRepository.findByEmail(userPrincipal.getEmail())
                .orElseThrow(() -> new UserInfoNotFoundException(USERNAME_NOT_FOUND));
    }

    private RoomParticipant checkParticipantWithReturnRole(User loginUser, Set<RoomParticipant> participants) {
        for (RoomParticipant roomParticipant : participants) {
            if (loginUser.getId().equals(roomParticipant.getUser().getId())) {
                return roomParticipant;
            }
        }
        throw new UserNotParticipateRoomException(USER_NOT_PARTICIPATE_ROOM);
    }

    private void checkParticipant(User loginUser, Set<RoomParticipant> participants) {
        for (RoomParticipant roomParticipant : participants) {
            if (loginUser.getId().equals(roomParticipant.getUser().getId())) {
                return;
            }
        }
        throw new UserNotParticipateRoomException(USER_NOT_PARTICIPATE_ROOM);
    }

    private boolean checkScheduler(List<ParticipantRole> participantRoles) {
        for (ParticipantRole participantRole : participantRoles) {
            if (participantRole.getRoomRole().getValue().equals("SCHEDULE")) {
                return true;
            }
        }
        return false;
    }

    private List<WantPlaceDTO> getWantPlaceList(Long roomId) {
        List<WantPlaceDTO> wantPlaceDTOS = new ArrayList<>();
        List<WantPlace> wantPlaces = wantPlaceRepository.findByRoomIdWithRole(roomId);
        long idx = 1;
        for (WantPlace wantPlace : wantPlaces) {
            wantPlaceDTOS.add(WantPlaceDTO.of(idx++, wantPlace));
        }
        return wantPlaceDTOS;
    }

}
