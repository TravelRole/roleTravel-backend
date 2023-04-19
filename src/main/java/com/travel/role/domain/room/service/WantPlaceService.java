package com.travel.role.domain.room.service;

import com.travel.role.domain.room.dao.ParticipantRoleRepository;
import com.travel.role.domain.room.dao.RoomRepository;
import com.travel.role.domain.room.dao.WantPlaceRepository;
import com.travel.role.domain.room.domain.Room;
import com.travel.role.domain.room.domain.RoomParticipant;
import com.travel.role.domain.room.domain.RoomRole;
import com.travel.role.domain.room.domain.WantPlace;
import com.travel.role.domain.room.dto.WantPlaceDTO;
import com.travel.role.domain.room.dto.WantPlaceRequestDTO;
import com.travel.role.domain.room.dto.WantPlaceResponseDTO;
import com.travel.role.domain.user.dao.UserRepository;
import com.travel.role.domain.user.domain.User;
import com.travel.role.domain.user.exception.PlaceInfoNotFoundException;
import com.travel.role.domain.user.exception.RoomInfoNotFoundException;
import com.travel.role.domain.user.exception.UserInfoNotFoundException;
import com.travel.role.domain.user.exception.UserNotParticipateRoomException;
import com.travel.role.global.auth.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    private final ParticipantRoleRepository participantRoleRepository;

    public void deleteWantPlace(UserPrincipal userPrincipal, Long roomId, Long placeId) {
        User loginUser = findUser(userPrincipal);
        Room room = findRoom(roomId);
        checkParticipant(loginUser, room.getRoomParticipants());
        deleteWantPlaceById(placeId);
    }

    public WantPlaceResponseDTO getWantPlaceList(UserPrincipal userPrincipal, Long roomId) {
        User loginUser = findUser(userPrincipal);
        Room room = findRoom(roomId);
        RoomParticipant roomParticipant = checkParticipant(loginUser, room.getRoomParticipants());
        List<WantPlaceDTO> wantPlaceDTOS = getWantPlaceList(roomId);
        return WantPlaceResponseDTO.of(wantPlaceDTOS, checkRole(roomParticipant));
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

    private RoomParticipant checkParticipant(User loginUser, Set<RoomParticipant> participants) {
        for (RoomParticipant roomParticipant : participants) {
            if (loginUser.getId().equals(roomParticipant.getUser().getId())) {
                return roomParticipant;
            }
        }
        throw new UserNotParticipateRoomException();
    }

    private boolean checkRole(RoomParticipant roomParticipant) {
        return participantRoleRepository.existsByRoomParticipantAndRoomRoleIn(roomParticipant, Arrays.asList(RoomRole.ADMIN, RoomRole.SCHEDULE));
    }

    private void deleteWantPlaceById(Long placeId) {
        try {
            wantPlaceRepository.deleteById(placeId);
        } catch (EmptyResultDataAccessException e) {
            throw new PlaceInfoNotFoundException();
        }
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
