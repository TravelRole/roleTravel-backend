package com.travel.role.domain.room.service;

import com.travel.role.domain.room.repository.ParticipantRoleRepository;
import com.travel.role.domain.room.repository.RoomParticipantRepository;
import com.travel.role.domain.room.repository.RoomRepository;
import com.travel.role.domain.room.repository.WantPlaceRepository;
import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.room.entity.RoomRole;
import com.travel.role.domain.room.entity.WantPlace;
import com.travel.role.domain.room.dto.WantPlaceDTO;
import com.travel.role.domain.room.dto.WantPlaceRequestDTO;
import com.travel.role.domain.room.dto.WantPlaceResponseDTO;
import com.travel.role.domain.user.repository.UserRepository;
import com.travel.role.domain.user.entity.User;
import com.travel.role.global.exception.user.PlaceInfoNotFoundException;
import com.travel.role.global.exception.user.RoomInfoNotFoundException;
import com.travel.role.global.exception.user.UserInfoNotFoundException;
import com.travel.role.global.exception.user.UserNotParticipateRoomException;
import com.travel.role.global.auth.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WantPlaceService {
	private final UserRepository userRepository;
	private final RoomRepository roomRepository;
	private final WantPlaceRepository wantPlaceRepository;
	private final RoomParticipantRepository roomParticipantRepository;
	private final ParticipantRoleRepository participantRoleRepository;

	public void deleteWantPlace(UserPrincipal userPrincipal, Long roomId, Long placeId) {
		User user = findUser(userPrincipal);
		Room room = findRoom(roomId);
		checkParticipant(user, room);
		deleteWantPlaceById(placeId);
	}

	public WantPlaceResponseDTO getWantPlaceList(UserPrincipal userPrincipal, Long roomId) {
		User user = findUser(userPrincipal);
		Room room = findRoom(roomId);
		checkParticipant(user, room);
		List<WantPlaceDTO> wantPlaceDTOS = getWantPlaceList(roomId);
		return WantPlaceResponseDTO.of(wantPlaceDTOS, checkRole(user, room));
	}

	public void addWantPlace(UserPrincipal userPrincipal, WantPlaceRequestDTO wantPlaceRequestDTO) {
		User user = findUser(userPrincipal);
		Room room = findRoom(wantPlaceRequestDTO.getRoomId());
		checkParticipant(user, room);
		wantPlaceRepository.save(WantPlace.of(room, wantPlaceRequestDTO));
	}

	private Room findRoom(Long roomId) {
		return roomRepository.findById(roomId).orElseThrow(RoomInfoNotFoundException::new);
	}

	private User findUser(UserPrincipal userPrincipal) {
		return userRepository.findByEmail(userPrincipal.getEmail()).orElseThrow(UserInfoNotFoundException::new);
	}

	private void checkParticipant(User user,Room room) {
		if (!roomParticipantRepository.existsByUserAndRoom(user, room)) {
			throw new UserNotParticipateRoomException();
		}
	}

	private boolean checkRole(User user,Room room) {
		return participantRoleRepository.existsByUserAndRoomAndRoomRoleIn(user, room, Arrays.asList(RoomRole.ADMIN, RoomRole.SCHEDULE));
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
