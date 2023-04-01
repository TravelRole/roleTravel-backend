package com.travel.role.domain.room.controller;

import com.travel.role.domain.room.domain.RoomEntity;
import com.travel.role.domain.room.dto.ResponseDTO;
import com.travel.role.domain.room.dto.RoomInfoDTO;
import com.travel.role.domain.room.exception.NullEntityException;
import com.travel.role.domain.room.service.RoomInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.travel.role.domain.room.exception.NullEntityExceptionMessage.ENTITY_IS_NULL;

@RestController
@RequestMapping("/room-info")
public class RoomInfoController {
    private RoomInfoService roomInfoService;
    public RoomInfoController(RoomInfoService roomInfoService){
        this.roomInfoService = roomInfoService;
    }

    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody RoomInfoDTO dto) {
        try {
            RoomEntity entity = RoomInfoDTO.toEntity(dto);

            List<RoomEntity> entities = roomInfoService.create(entity);

            List<RoomInfoDTO> dtos = entities.stream().map(RoomInfoDTO::new)
                    .collect(Collectors.toList());

            ResponseDTO<RoomInfoDTO> response = ResponseDTO.<RoomInfoDTO>builder()
                    .data(dtos).build();

            return ResponseEntity.ok().body(response);


        } catch (NullEntityException e) {
            throw new NullEntityException(ENTITY_IS_NULL);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<RoomInfoDTO> response = ResponseDTO.<RoomInfoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }
    @GetMapping
    public ResponseEntity<?> readRoomInfoList(){
        List<RoomEntity> entities = roomInfoService.read();
        List<RoomInfoDTO> dtos = entities.stream().map((e)->(new RoomInfoDTO(e))).collect(Collectors.toList());
        ResponseDTO<RoomInfoDTO> response = ResponseDTO.<RoomInfoDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);
    }

}
