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
    private static final String PARTICIPANT = "isParticipant";
    private static final String SCHEDULE = "isSchedule";
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final WantPlaceRepository wantPlaceRepository;

    public WantPlaceResponseDTO getRoomList(UserPrincipal userPrincipal, Long id) {
        long idx = 1;
        User loginUser = findUser(userPrincipal);
        Room room = findRoom(id);
        Map<String, Boolean> map = checkParticipant(loginUser, room.getRoomParticipants());

        if (Boolean.TRUE.equals(map.get(PARTICIPANT))) {
            List<WantPlace> wantPlaces = wantPlaceRepository.findByRoomIdWithRole(id);
            List<WantPlaceDTO> wantPlaceDTOS = new ArrayList<>();
            for (WantPlace wantPlace : wantPlaces) {
                wantPlaceDTOS.add(WantPlaceDTO.of(idx++, wantPlace));
            }
            return WantPlaceResponseDTO.of(wantPlaceDTOS, map.get(SCHEDULE));
        }
        else
            throw new UserNotParticipateRoomException(USER_NOT_PARTICIPATE_ROOM);
    }

    public void addWantPlace(UserPrincipal userPrincipal, WantPlaceRequestDTO wantPlaceRequestDTO) {
        User loginUser = findUser(userPrincipal);
        Room room = findRoom(wantPlaceRequestDTO.getRoomId());
        Map<String, Boolean> map = checkParticipant(loginUser, room.getRoomParticipants());

        if (Boolean.TRUE.equals(map.get(PARTICIPANT)))
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

    private Map<String, Boolean> checkParticipant(User loginUser, Set<RoomParticipant> participants) {
        Map<String, Boolean> map = new HashMap<>();
        map.put(SCHEDULE, false);
        map.put(PARTICIPANT, false);

        for (RoomParticipant roomParticipant : participants) {
            if (loginUser.getId().equals(roomParticipant.getUser().getId())) {
                for (ParticipantRole participantRole : roomParticipant.getParticipantRoles()) {
                    if (participantRole.getRoomRole().getValue().equals("SCHEDULE")) {
                        map.replace(SCHEDULE, true);
                    }
                }
                map.replace(PARTICIPANT, true);
            }
        }
        return map;
    }

}
