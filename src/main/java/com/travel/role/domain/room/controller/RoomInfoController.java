package com.travel.role.domain.room.controller;

import com.travel.role.domain.room.domain.RoomEntity;
import com.travel.role.global.dto.ResponseDTO;
import com.travel.role.domain.room.dto.RoomInfoDTO;
import com.travel.role.domain.room.service.RoomInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/")
public class RoomInfoController {
    private final RoomInfoService roomInfoService;

    @PostMapping("/room-info/create")
    public ResponseEntity<?> createRoom(@RequestBody RoomInfoDTO dto) {
            RoomEntity entity = RoomEntity.toEntity(dto);

            RoomInfoDTO roomInfoDTO = roomInfoService.create(entity);

            List<RoomInfoDTO> dtos = new ArrayList<RoomInfoDTO>();
            dtos.add(roomInfoDTO);

            ResponseDTO<RoomInfoDTO> response = ResponseDTO.<RoomInfoDTO>builder()
                    .data(dtos).build();


            return ResponseEntity.ok().body(response);
    }
    @GetMapping("/room-info")
    public ResponseEntity<?> readRoomInfoList(){
        List<RoomEntity> entities = roomInfoService.read();
        List<RoomInfoDTO> dtos = entities.stream().map((e)->(new RoomInfoDTO(e))).collect(Collectors.toList());
        ResponseDTO<RoomInfoDTO> response = ResponseDTO.<RoomInfoDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);
    }

}
