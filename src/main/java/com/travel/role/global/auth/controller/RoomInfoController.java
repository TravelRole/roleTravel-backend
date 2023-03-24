package com.travel.role.global.auth.controller;

import com.travel.role.domain.room.domain.RoomEntity;
import com.travel.role.global.auth.dto.ResponseDTO;
import com.travel.role.global.auth.dto.RoomInfoDTO;
import com.travel.role.global.auth.service.RoomInfoService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/roomInfo")
public class RoomInfoController {
    @Autowired
    private RoomInfoService service;

    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody RoomInfoDTO dto){
        try {
            RoomEntity entity = RoomInfoDTO.toEntity(dto);

            List<RoomEntity> entities = service.create(entity);

            List<RoomInfoDTO> dtos = entities.stream().map(RoomInfoDTO::new)
                    .collect(Collectors.toList());

            ResponseDTO<RoomInfoDTO> response = ResponseDTO.<RoomInfoDTO>builder()
                    .data(dtos).build();

            return ResponseEntity.ok().body(response);


        } catch(Exception e){
            String error = e.getMessage();
            ResponseDTO<RoomInfoDTO> response = ResponseDTO.<RoomInfoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> readRoomInfoList(){
        List<RoomEntity> entities = service.read();
        List<RoomInfoDTO> dtos = entities.stream().map((e)->(new RoomInfoDTO(e))).collect(Collectors.toList());
        ResponseDTO<RoomInfoDTO> response = ResponseDTO.<RoomInfoDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);
    }

}