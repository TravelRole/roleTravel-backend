package com.travel.role.domain.room.controller;

import com.travel.role.domain.room.dao.RoomRepository;
import com.travel.role.domain.room.dto.RoomInfoDTO;
import com.travel.role.domain.room.exception.RoomExceptionMessage;
import com.travel.role.domain.room.exception.StartdateBiggerException;
import com.travel.role.domain.room.service.RoomInfoService;
import com.travel.role.domain.user.dto.SignUpRequestDTO;
import com.travel.role.global.exception.ExceptionMessage;
import com.travel.role.global.exception.user.AlreadyExistUserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class RoomInfoControllerTest {
    @InjectMocks
    private RoomInfoController roomInfoController;
    @Mock
    private RoomRepository roomRepository;

    @Mock
    private RoomInfoService roomInfoService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void 방_생성_테스트() throws Exception{
        String content = "{\"roomName\": \"가평갑시다\", \"travelStartDate\": \"2023-03-24T06:34:20\"," +
                "\"travelEndDate\": \"2023-03-30T06:34:20\", \"roomImage\": \"goodimage.png\"," +
                "\"totalParticipants\": \"5\", \"roomPassword\": \"1234\"}";

        mockMvc.perform(
                post("/room-info/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    public void 방_조회_테스트() throws Exception{
        mockMvc.perform(
                        get("/room-info/read"))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    public void 시작날짜가_종료날짜보다_큰경우_예외_발생() {
        // given
        RoomInfoDTO roomInfoDTO = createErrorRequestDTO();

        // when, then
        assertThatThrownBy(() -> roomInfoController.createRoom(roomInfoDTO))
                .isInstanceOf(StartdateBiggerException.class)
                .hasMessageContaining(RoomExceptionMessage.STARTDATE_IS_BIGGER);
    }

    private RoomInfoDTO createErrorRequestDTO() {
        RoomInfoDTO roomInfoDTO = new RoomInfoDTO();
        roomInfoDTO.setRoomName("부산으로떠나요!");
        roomInfoDTO.setTravelStartDate(LocalDateTime.parse("2023-04-30T06:59:20"));
        roomInfoDTO.setTravelEndDate(LocalDateTime.parse("2023-03-24T06:34:20"));
        roomInfoDTO.setRoomImage("verygoodimage.png");
        roomInfoDTO.setTotalParticipants("6");
        roomInfoDTO.setRoomPassword("4242");
        return roomInfoDTO;
    }


}