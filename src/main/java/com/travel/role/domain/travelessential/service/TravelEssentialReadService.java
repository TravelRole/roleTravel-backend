package com.travel.role.domain.travelessential.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.room.service.RoomParticipantReadService;
import com.travel.role.domain.room.service.RoomReadService;
import com.travel.role.domain.travelessential.dto.response.TravelEssentialResDTO;
import com.travel.role.domain.travelessential.entity.EssentialCategory;
import com.travel.role.domain.travelessential.repository.TravelEssentialRepository;
import com.travel.role.domain.user.entity.User;
import com.travel.role.domain.user.service.UserReadService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TravelEssentialReadService {

	private final TravelEssentialRepository travelEssentialRepository;
	private final UserReadService userReadService;
	private final RoomReadService roomReadService;
	private final RoomParticipantReadService roomParticipantReadService;

	@Transactional(readOnly = true)
	public Map<EssentialCategory, List<TravelEssentialResDTO>> readAllGroupByCategory(String email, Long roomId) {

		User findUser = userReadService.findUserByEmailOrElseThrow(email);
		Room findRoom = roomReadService.findRoomByIdOrElseThrow(roomId);

		roomParticipantReadService.checkParticipant(findUser, findRoom);

		return travelEssentialRepository.readAllGroupByEssentialCategory(findUser.getId(), findRoom.getId());
	}
}
