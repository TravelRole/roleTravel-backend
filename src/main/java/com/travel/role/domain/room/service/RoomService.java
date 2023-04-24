package com.travel.role.domain.room.service;

import com.querydsl.core.Tuple;
import com.travel.role.domain.room.repository.ParticipantRoleRepository;
import com.travel.role.domain.room.repository.RoomParticipantRepository;
import com.travel.role.domain.room.repository.RoomRepository;
import com.travel.role.domain.room.entity.ParticipantRole;
import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.room.entity.RoomParticipant;
import com.travel.role.domain.room.entity.RoomRole;
import com.travel.role.domain.room.dto.MakeRoomRequestDTO;
import com.travel.role.domain.room.dto.MemberDTO;
import com.travel.role.domain.room.dto.RoomResponseDTO;
import com.travel.role.global.exception.common.InvalidLocalDateException;
import com.travel.role.domain.user.repository.UserRepository;
import com.travel.role.domain.user.entity.User;
import com.travel.role.global.exception.user.UserInfoNotFoundException;
import com.travel.role.global.auth.token.UserPrincipal;
import com.travel.role.global.util.PasswordGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.travel.role.global.exception.ExceptionMessage.INVALID_DATE_ERROR;

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
        saveNewRoomParticipant(user, room);
        saveNewParticipantRole(user, room);
    }

    private void saveNewRoomParticipant(User user, Room room) {
        RoomParticipant roomParticipant = RoomParticipant.builder()
                .isPaid(false)
                .joinedAt(LocalDateTime.now())
                .room(room)
                .user(user)
                .build();
        roomParticipantRepository.save(roomParticipant);
    }

    private void saveNewParticipantRole(User user, Room room) {
        ParticipantRole newParticipantRole = ParticipantRole.builder()
                .roomRole(RoomRole.ADMIN)
                .room(room)
                .user(user)
                .build();
        participantRoleRepository.save(newParticipantRole);
    }

    private User findUser(UserPrincipal userPrincipal) {
        return userRepository.findByEmail(userPrincipal.getEmail())
                .orElseThrow(UserInfoNotFoundException::new);
    }

    private void validateDate(MakeRoomRequestDTO makeRoomRequestDTO) {
        LocalDate start = makeRoomRequestDTO.getTravelStartDate();
        LocalDate end = makeRoomRequestDTO.getTravelEndDate();

        if (start.isAfter(end))
            throw new InvalidLocalDateException(INVALID_DATE_ERROR);
    }
}
