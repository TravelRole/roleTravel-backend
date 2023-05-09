package com.travel.role.integration.room;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.room.dto.response.RoomResponseDTO;
import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.room.entity.RoomParticipant;
import com.travel.role.domain.room.repository.RoomParticipantRepository;
import com.travel.role.domain.room.repository.RoomRepository;
import com.travel.role.domain.room.service.RoomService;
import com.travel.role.domain.user.entity.User;
import com.travel.role.domain.user.repository.UserRepository;

@SpringBootTest
@Transactional
class RoomTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomParticipantRepository roomParticipantRepository;

    @Autowired
    private RoomService roomService;

    @Test
    void before_each의_정보가_제대로_들어갔는지() {
        //given
        List<User> userResult = userRepository.findAll();
        List<Room> roomResult = roomRepository.findAll();
        List<RoomParticipant> roomParticipantResult = roomParticipantRepository.findAll();

        //when, then
        assertThat(userResult.size()).isEqualTo(12);
        assertThat(roomResult.size()).isEqualTo(4);
        assertThat(roomParticipantResult.size()).isEqualTo(16);
    }

    @Test
    void 방의_정보를_제대로_불러오는지() {
        // given
        String email = "gy@naver.com";
        // when
        List<RoomResponseDTO> roomList = roomService.getRoomList(email);

        // then
        assertThat(roomList.size()).isEqualTo(1);
        assertThat(roomList.get(0).getRoomName()).isEqualTo("가아아아아평");
        assertThat(roomList.get(0).getMembers().size()).isEqualTo(5);
    }
}