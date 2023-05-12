package com.travel.role.integration.room;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.room.dto.request.RoomModifiedRequestDTO;
import com.travel.role.domain.room.dto.request.RoomRoleDTO;
import com.travel.role.domain.room.dto.response.RoomInfoResponseDTO;
import com.travel.role.domain.room.dto.response.RoomResponseDTO;
import com.travel.role.domain.room.dto.response.RoomRoleInfoDTO;
import com.travel.role.domain.room.entity.ParticipantRole;
import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.room.entity.RoomParticipant;
import com.travel.role.domain.room.entity.RoomRole;
import com.travel.role.domain.room.repository.ParticipantRoleRepository;
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
    private ParticipantRoleRepository participantRoleRepository;

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

    @Test
    void 방_총무_변경_테스트() {
        //given
        RoomRoleDTO roomRoleDTO1 = new RoomRoleDTO("Junsik@naver.com", List.of(RoomRole.NONE));
        RoomRoleDTO roomRoleDTO2 = new RoomRoleDTO("haechan@naver.com", List.of(RoomRole.RESERVATION));
        RoomRoleDTO roomRoleDTO3 = new RoomRoleDTO("mogu@naver.com", List.of(RoomRole.ADMIN));
        RoomRoleDTO roomRoleDTO4 = new RoomRoleDTO("dsl@naver.com", List.of(RoomRole.ACCOUNTING, RoomRole.SCHEDULE));

        RoomModifiedRequestDTO roomModifiedRequestDTO = new RoomModifiedRequestDTO("광양 펜션잡고 놀자", LocalDate.of(2023, 6, 16),
            LocalDate.of(2023, 6, 17),
            List.of(roomRoleDTO1, roomRoleDTO2, roomRoleDTO3, roomRoleDTO4));

        //when
        roomService.modifyRoomInfo("Junsik@naver.com", roomModifiedRequestDTO, 4L);

        //then
        List<ParticipantRole> participantRoles = participantRoleRepository.findUserAndRoomByRoomId(4L);
        assertThat(participantRoles.size()).isEqualTo(5);
    }

    @Test
    void 방_수정_정보_테스트() {
        // given
        RoomInfoResponseDTO roomInfo = roomService.getRoomInfo("Junsik@naver.com", 4L);
        List<RoomRoleInfoDTO> roles = roomInfo.getRoles();
        // when, then
        assertThat(roles.size()).isEqualTo(4);
        assertThat(roomInfo.getRoomName()).isEqualTo("여수에서 간장게장");
        assertThat(roomInfo.getStartDate()).isEqualTo(LocalDate.of(2023,6,25));
        assertThat(roomInfo.getEndDate()).isEqualTo(LocalDate.of(2023,6,26));

    }
}