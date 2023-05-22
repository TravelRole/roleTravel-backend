package com.travel.role.integration.room;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.room.dto.request.ExitRoomRequestDTO;
import com.travel.role.domain.room.dto.request.RoomModifiedRequestDTO;
import com.travel.role.domain.room.dto.request.RoomRoleDTO;
import com.travel.role.domain.room.dto.response.AllPlanDTO;
import com.travel.role.domain.room.dto.response.AllPlanResponseDTO;
import com.travel.role.domain.room.dto.response.RoomInfoResponseDTO;
import com.travel.role.domain.room.dto.response.RoomResponseDTO;
import com.travel.role.domain.room.dto.response.RoomRoleInfoDTO;
import com.travel.role.domain.room.dto.response.SidebarResponseDTO;
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
import com.travel.role.global.exception.user.UserInfoNotFoundException;

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
    private EntityManager em;

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
        assertThat(roomResult.size()).isEqualTo(5);
        assertThat(roomParticipantResult.size()).isEqualTo(16);
    }

    @Test
    void 방의_정보를_제대로_불러오는지() {
        // given
        String email = "gy@naver.com";
        // when
        List<RoomResponseDTO> roomList = roomService.getRoomList(email);

        // then
        assertThat(roomList.size()).isEqualTo(2);
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

        RoomModifiedRequestDTO roomModifiedRequestDTO = new RoomModifiedRequestDTO("광양 펜션잡고 놀자", "경주", LocalDate.of(2023, 6, 16),
            LocalDate.of(2023, 6, 17),
            List.of(roomRoleDTO1, roomRoleDTO2, roomRoleDTO3, roomRoleDTO4));

        //when
        roomService.modifyRoomInfo("Junsik@naver.com", roomModifiedRequestDTO, 4L);

        //then
        List<ParticipantRole> participantRoles = participantRoleRepository.findUserAndRoomByRoomId(4L);
        assertThat(participantRoles.size()).isEqualTo(5);
    }

    @Test
    void 모든_여행계획_일정_가져오기_테스트() {
        // when
        AllPlanResponseDTO allPlan = roomService.getAllPlan("kmimi@naver.com", 1L);

        int scheduleCount = 0;
        for (AllPlanDTO data : allPlan.getData()) {
            scheduleCount += data.getSchedules().size();
        }

        // then
        assertThat(allPlan.getTotalExpense()).isEqualTo(1360000);
        assertThat(scheduleCount).isEqualTo(15);
        assertThat(allPlan.getData().size()).isEqualTo(5);
        assertThat(allPlan.getData().get(0).getDate()).isEqualTo(LocalDate.of(2023, 8, 10));
    }

    @Test
    void 방_수정_정보_가져오는_테스트() {
        // given
        RoomInfoResponseDTO roomInfo = roomService.getRoomInfo("Junsik@naver.com", 4L);
        List<RoomRoleInfoDTO> roles = roomInfo.getRoles();
        // when, then
        assertThat(roles.size()).isEqualTo(4);
        assertThat(roomInfo.getLocation()).isEqualTo("순천시");
        assertThat(roomInfo.getStartDate()).isEqualTo(LocalDate.of(2023,6,25));
        assertThat(roomInfo.getEndDate()).isEqualTo(LocalDate.of(2023,6,26));
        assertThat(roomInfo.getRoles().get(0).getRoles()).contains(RoomRole.ADMIN);
    }

    @Test
    void 사이드바_정보_가져오는_테스트() {
        // given
        String email = "mogu@naver.com";
        Long roomId = 4L;

        // when
        SidebarResponseDTO sidebar = roomService.getSidebar(email, roomId);

        // then
        assertThat(sidebar.getRoomImage()).isEqualTo(1);
        assertThat(sidebar.getRoomName()).isEqualTo("여수에서 간장게장");
        assertThat(sidebar.getRoles()).contains(RoomRole.RESERVATION, RoomRole.SCHEDULE);
    }

    @Test
    void 스페이스_탈퇴시_총무인데_총무인원을_설정하지_않았을_경우() {
        // given
        String email = "gy@naver.com";
        Long roomId = 1L;
        ExitRoomRequestDTO exitRoomRequestDTO = new ExitRoomRequestDTO();
        // when, then
        assertThatThrownBy(() -> {
            roomService.exitRoom(email, roomId, exitRoomRequestDTO);
        }).isInstanceOf(UserInfoNotFoundException.class);
    }

    @Test
    void 스페이스_탈퇴시_총무인_경우() {
        //given
        String email = "gy@naver.com";
        Long roomId = 1L;
        ExitRoomRequestDTO exitRoomRequestDTO = new ExitRoomRequestDTO("kmimi@naver.com");

        //when
        roomService.exitRoom(email, roomId, exitRoomRequestDTO);

        List<ParticipantRole> participantRoles = participantRoleRepository.findByRoomIdAndEmail(1L, "kmimi@naver.com");
        List<ParticipantRole> deleteRoles = participantRoleRepository.findByRoomIdAndEmail(1L, email);


        //then
        assertThat(deleteRoles).isEmpty();
        assertThat(participantRoles).hasSize(1);
        assertThat(participantRoles.get(0).getRoomRole()).isEqualTo(RoomRole.ADMIN);
    }

    @Test
    void 스페이스_탈퇴시_나만_존재하는_방인_경우() {
        //given
        String email = "gy@naver.com";
        Long roomId = 5L;

        //when
        roomService.exitRoom(email, roomId, new ExitRoomRequestDTO());
        em.flush();
        em.clear();

        Optional<Room> room = roomRepository.findById(5L);
        Optional<RoomParticipant> roomParticipant = roomParticipantRepository.findById(17L);
        Optional<ParticipantRole> participantRole = participantRoleRepository.findById(20L);
        //then
        assertThat(room.isEmpty()).isTrue();
        assertThat(roomParticipant.isEmpty()).isTrue();
        assertThat(participantRole.isEmpty()).isTrue();
    }
}