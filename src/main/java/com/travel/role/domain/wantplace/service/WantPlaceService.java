package com.travel.role.domain.wantplace.service;

import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.room.entity.RoomRole;
import com.travel.role.domain.room.repository.ParticipantRoleRepository;
import com.travel.role.domain.room.service.RoomParticipantReadService;
import com.travel.role.domain.room.service.RoomReadService;
import com.travel.role.domain.user.entity.User;
import com.travel.role.domain.user.service.UserReadService;
import com.travel.role.domain.wantplace.dto.request.WantPlaceRequestDTO;
import com.travel.role.domain.wantplace.dto.response.WantPlaceDTO;
import com.travel.role.domain.wantplace.dto.response.WantPlaceResponseDTO;
import com.travel.role.domain.wantplace.entity.WantPlace;
import com.travel.role.domain.wantplace.repository.WantPlaceRepository;
import com.travel.role.global.exception.user.PlaceInfoNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class WantPlaceService {
	private final UserReadService userReadService;
	private final RoomReadService roomReadService;
	private final WantPlaceRepository wantPlaceRepository;
	private final RoomParticipantReadService roomParticipantReadService;
	private final ParticipantRoleRepository participantRoleRepository;

	public void deleteWantPlace(String email, Long roomId, Long placeId) {
		User user = userReadService.findUserByEmailOrElseThrow(email);
		Room room = roomReadService.findRoomByIdOrElseThrow(roomId);
		roomParticipantReadService.checkParticipant(user, room);
		deleteWantPlaceById(placeId);
	}

	public WantPlaceResponseDTO getWantPlaceList(String email, Long roomId) {
		User user = userReadService.findUserByEmailOrElseThrow(email);
		Room room = roomReadService.findRoomByIdOrElseThrow(roomId);
		roomParticipantReadService.checkParticipant(user, room);
		List<WantPlaceDTO> wantPlaceDTOS = getWantPlaceList(roomId);
		return WantPlaceResponseDTO.of(wantPlaceDTOS, checkRole(user, room));
	}

	public void addWantPlace(String email, WantPlaceRequestDTO wantPlaceRequestDTO) {
		User user = userReadService.findUserByEmailOrElseThrow(email);
		Room room = roomReadService.findRoomByIdOrElseThrow(wantPlaceRequestDTO.getRoomId());
		roomParticipantReadService.checkParticipant(user, room);
		wantPlaceRepository.save(WantPlace.of(room, wantPlaceRequestDTO));
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
		return wantPlaceRepository.findByRoomIdWithRole(roomId)
				.stream()
				.map(WantPlaceDTO::from)
				.collect(Collectors.toList());
	}

}
