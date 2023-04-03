package com.travel.role.domain.room.controller;

import com.travel.role.domain.room.domain.RoomEntity;
import com.travel.role.domain.room.exception.StartdateBiggerException;
import com.travel.role.global.dto.ResponseDTO;
import com.travel.role.domain.room.dto.RoomInfoDTO;
import com.travel.role.domain.room.service.RoomInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.travel.role.domain.room.exception.RoomExceptionMessage.STARTDATE_IS_BIGGER;


@RestController
@RequiredArgsConstructor
//@RequestMapping("/")
public class RoomInfoController {
    private final RoomInfoService roomInfoService;

    @PostMapping("/room-info/create")
    @Transactional
    public ResponseEntity<?> createRoom(@RequestBody RoomInfoDTO dto) {

            LocalDateTime SD = dto.getTravelStartDate();
            LocalDateTime ED = dto.getTravelEndDate();
            if(!ED.isAfter(SD)){
                throw new StartdateBiggerException(STARTDATE_IS_BIGGER);
            }


                RoomEntity entity = RoomEntity.toEntity(dto);

                RoomInfoDTO roomInfoDTO = roomInfoService.create(entity);

                List<RoomInfoDTO> dtos = new ArrayList<RoomInfoDTO>();
                dtos.add(roomInfoDTO);

                ResponseDTO<RoomInfoDTO> response = ResponseDTO.<RoomInfoDTO>builder()
                        .data(dtos).build();


                return ResponseEntity.ok().body(response);

    }
    @GetMapping("/room-info/read")
    @Transactional
    public ResponseEntity<?> readRoomInfoList(){
        List<RoomEntity> entities = roomInfoService.read();
        List<RoomInfoDTO> dtos = entities.stream().map((e)->(new RoomInfoDTO(e))).collect(Collectors.toList());
        ResponseDTO<RoomInfoDTO> response = ResponseDTO.<RoomInfoDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);
    }

}
