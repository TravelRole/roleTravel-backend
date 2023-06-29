package com.travel.role.domain.travelessential.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.room.service.RoomParticipantReadService;
import com.travel.role.domain.room.service.RoomReadService;
import com.travel.role.domain.travelessential.dto.request.TravelEssentialCheckReqDTO;
import com.travel.role.domain.travelessential.dto.request.TravelEssentialDeleteReqDTO;
import com.travel.role.domain.travelessential.dto.request.TravelEssentialReqDTO;
import com.travel.role.domain.travelessential.entity.TravelEssential;
import com.travel.role.domain.travelessential.repository.TravelEssentialRepository;
import com.travel.role.domain.user.entity.User;
import com.travel.role.domain.user.service.UserReadService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TravelEssentialService {

	private final TravelEssentialRepository travelEssentialRepository;
	private final RoomReadService roomReadService;
	private final RoomParticipantReadService roomParticipantReadService;
	private final UserReadService userReadService;

	public void createTravelEssentials(String email, Long roomId, TravelEssentialReqDTO reqDTO) {

		User findUser = userReadService.findUserByEmailOrElseThrow(email);
		Room findRoom = roomReadService.findRoomByIdOrElseThrow(roomId);

		roomParticipantReadService.checkParticipant(findUser, findRoom);

		List<TravelEssential> travelEssentials = reqDTO.toTravelEssentials(findUser, findRoom);

		travelEssentialRepository.saveAll(travelEssentials);
	}

	public void deleteTravelEssentials(String email, Long roomId, TravelEssentialDeleteReqDTO reqDTO) {

		User findUser = userReadService.findUserByEmailOrElseThrow(email);
		Room findRoom = roomReadService.findRoomByIdOrElseThrow(roomId);

		roomParticipantReadService.checkParticipant(findUser, findRoom);

		travelEssentialRepository.deleteByUserAndRoomAndSearchIds(findUser.getId(), findRoom.getId(), reqDTO.getIds());
	}

	public void updateCheckTravelEssentials(String email, Long roomId, TravelEssentialCheckReqDTO reqDTO) {

		User findUser = userReadService.findUserByEmailOrElseThrow(email);
		Room findRoom = roomReadService.findRoomByIdOrElseThrow(roomId);

		roomParticipantReadService.checkParticipant(findUser, findRoom);

		travelEssentialRepository.updateCheckEssentialsByUserAndRoomAndEssentialIds(
			findUser.getId(), findRoom.getId(), reqDTO.getIds(), reqDTO.getCheck()
		);
	}

	public void deleteAllByRoomId(Long roomId){
		travelEssentialRepository.deleteAllByRoomId(roomId);
	}

	public void deleteAllByRoomIdAndUserId(Long roomId, Long userId){
		travelEssentialRepository.deleteAllByRoomIdAndUserId(roomId, userId);
	}
}
