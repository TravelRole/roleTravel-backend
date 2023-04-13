package com.travel.role.domain.room.service;

import static com.travel.role.global.exception.ExceptionMessage.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.Tuple;
import com.travel.role.domain.room.dao.ParticipantRoleRepository;
import com.travel.role.domain.room.dao.RoomParticipantRepository;
import com.travel.role.domain.room.dao.RoomRepository;
import com.travel.role.domain.room.domain.ParticipantRole;
import com.travel.role.domain.room.domain.Room;
import com.travel.role.domain.room.domain.RoomParticipant;
import com.travel.role.domain.room.domain.RoomRole;
import com.travel.role.domain.room.dto.MakeRoomRequestDTO;
import com.travel.role.domain.room.dto.MemberDTO;
import com.travel.role.domain.room.dto.RoomResponseDTO;
import com.travel.role.domain.room.exception.InvalidLocalDateException;
import com.travel.role.domain.user.dao.UserRepository;
import com.travel.role.domain.user.domain.User;
import com.travel.role.domain.user.exception.UserInfoNotFoundException;
import com.travel.role.global.auth.token.UserPrincipal;
import com.travel.role.global.util.PasswordGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomService {

	private static final int MAX_PASSWORD_LENGTH = 20;

	private final RoomRepository roomRepository;
	private final UserRepository userRepository;
	private final RoomParticipantRepository roomParticipantRepository;
	private final ParticipantRoleRepository participantRoleRepository;

	public List<RoomResponseDTO> getRoomList(UserPrincipal userPrincipal) {
		List<Tuple> findRoomInfo = roomRepository.getMemberInRoom(userPrincipal.getEmail());

		Map<Long, RoomResponseDTO> hash = new HashMap<>();
		for (Tuple tuple : findRoomInfo) {
			Room room = tuple.get(0, Room.class);
			User user = tuple.get(1, User.class);

			if (hash.containsKey(room.getId())) {
				RoomResponseDTO roomResponseDTO = hash.get(room.getId());
				roomResponseDTO.getMembers().add(new MemberDTO(user.getName(), user.getProfile()));
				hash.put(room.getId(), roomResponseDTO);
			} else {
				List<MemberDTO> members = new ArrayList<>();
				members.add(new MemberDTO(user.getName(), user.getProfile()));

				hash.put(room.getId(), RoomResponseDTO.of(room, members));
			}
		}

		return new ArrayList<>(hash.values());
	}

	public void makeRoom(UserPrincipal userPrincipal, MakeRoomRequestDTO makeRoomRequestDTO) {
		validateDate(makeRoomRequestDTO);

		String randomPassword = PasswordGenerator.generateRandomPassword(MAX_PASSWORD_LENGTH);

		User user = findUser(userPrincipal);
		Room room = roomRepository.save(Room.of(makeRoomRequestDTO, randomPassword));
		RoomParticipant roomParticipant = saveNewRoomParticipant(user, room);
		ParticipantRole participantRole = saveNewParticipantRole(roomParticipant);
	}

	private RoomParticipant saveNewRoomParticipant(User user, Room room) {
		RoomParticipant roomParticipant = RoomParticipant.builder()
			.isPaid(false)
			.joinedAt(LocalDateTime.now())
			.room(room)
			.user(user)
			.isPaid(false)
			.build();

		return roomParticipantRepository.save(roomParticipant);
	}

	private ParticipantRole saveNewParticipantRole(RoomParticipant participant) {
		ParticipantRole newParticipantRole = ParticipantRole.builder()
			.roomRole(RoomRole.ADMIN)
			.roomParticipant(participant)
			.build();

		return participantRoleRepository.save(newParticipantRole);
	}

	private User findUser(UserPrincipal userPrincipal) {
		return userRepository.findByEmail(userPrincipal.getEmail())
			.orElseThrow(() -> new UserInfoNotFoundException(USERNAME_NOT_FOUND));
	}

	private void validateDate(MakeRoomRequestDTO makeRoomRequestDTO) {
		LocalDate start = makeRoomRequestDTO.getTravelStartDate();
		LocalDate end = makeRoomRequestDTO.getTravelEndDate();

		if (start.isAfter(end))
			throw new InvalidLocalDateException(INVALID_DATE_ERROR);
	}
}
