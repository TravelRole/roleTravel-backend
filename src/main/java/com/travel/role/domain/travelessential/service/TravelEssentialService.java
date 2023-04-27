package com.travel.role.domain.travelessential.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.room.repository.RoomParticipantRepository;
import com.travel.role.domain.room.repository.RoomRepository;
import com.travel.role.domain.travelessential.entity.TravelEssential;
import com.travel.role.domain.travelessential.repository.TravelEssentialRepository;
import com.travel.role.domain.travelessential.entity.EssentialCategory;
import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.travelessential.dto.request.TravelEssentialCheckReqDTO;
import com.travel.role.domain.travelessential.dto.request.TravelEssentialDeleteReqDTO;
import com.travel.role.domain.travelessential.dto.request.TravelEssentialReqDTO;
import com.travel.role.domain.travelessential.dto.response.TravelEssentialResDTO;
import com.travel.role.domain.user.repository.UserRepository;
import com.travel.role.domain.user.entity.User;
import com.travel.role.global.exception.user.RoomInfoNotFoundException;
import com.travel.role.global.exception.user.UserInfoNotFoundException;
import com.travel.role.global.exception.user.UserNotParticipateRoomException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TravelEssentialService {

	private final TravelEssentialRepository travelEssentialRepository;
	private final RoomRepository roomRepository;
	private final UserRepository userRepository;
	private final RoomParticipantRepository roomParticipantRepository;

	public void createTravelEssentials(String email, Long roomId, TravelEssentialReqDTO reqDTO) {

		User findUser = findUserByEmailOrElseThrow(email);
		Room findRoom = findRoomByIdOrElseThrow(roomId);

		checkUserInRoom(findUser, findRoom);

		List<TravelEssential> travelEssentials = reqDTO.toTravelEssentials(findUser, findRoom);

		travelEssentialRepository.saveAll(travelEssentials);
	}

	@Transactional(readOnly = true)
	public Map<EssentialCategory, List<TravelEssentialResDTO>> readAllGroupByCategory(String email, Long roomId) {

		User findUser = findUserByEmailOrElseThrow(email);
		Room findRoom = findRoomByIdOrElseThrow(roomId);

		checkUserInRoom(findUser, findRoom);

		return travelEssentialRepository.readAllGroupByEssentialCategory(findUser.getId(), findRoom.getId());
	}

	public void deleteTravelEssentials(String email, Long roomId, TravelEssentialDeleteReqDTO reqDTO) {

		User findUser = findUserByEmailOrElseThrow(email);
		Room findRoom = findRoomByIdOrElseThrow(roomId);

		checkUserInRoom(findUser, findRoom);

		travelEssentialRepository.deleteByUserAndRoomAndSearchIds(findUser.getId(), findRoom.getId(), reqDTO.getIds());
	}

	public void updateCheckTravelEssentials(String email, Long roomId, TravelEssentialCheckReqDTO reqDTO) {

		User findUser = findUserByEmailOrElseThrow(email);
		Room findRoom = findRoomByIdOrElseThrow(roomId);

		checkUserInRoom(findUser, findRoom);

		travelEssentialRepository.updateCheckEssentialsByUserAndRoomAndEssentialIds(
			findUser.getId(), findRoom.getId(), reqDTO.getIds(), reqDTO.getCheck()
		);
	}

	private User findUserByEmailOrElseThrow(String email) {

		return userRepository.findByEmail(email)
			.orElseThrow(UserInfoNotFoundException::new);
	}

	private Room findRoomByIdOrElseThrow(Long roomId) {

		return roomRepository.findById(roomId)
			.orElseThrow(RoomInfoNotFoundException::new);
	}

	private void checkUserInRoom(User user, Room room) {

		if (!roomParticipantRepository.existsByUserAndRoom(user, room)) {
			throw new UserNotParticipateRoomException();
		}
	}
}
