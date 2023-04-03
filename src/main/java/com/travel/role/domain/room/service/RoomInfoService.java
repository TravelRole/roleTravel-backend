package com.travel.role.domain.room.service;


import com.travel.role.domain.room.dao.RoomRepository;
import com.travel.role.domain.room.domain.RoomEntity;
import com.travel.role.domain.room.dto.RoomInfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.travel.role.domain.room.exception.RoomExceptionMessage.*;


import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomInfoService {
    private final RoomRepository roomRepository;


    public RoomInfoDTO create(final RoomEntity entity){
        roomRepository.save(entity);

        RoomInfoDTO roomInfoDTO = new RoomInfoDTO();
        roomInfoDTO.setRoomName(entity.getRoomName());
        roomInfoDTO.setTravelStartDate(entity.getTravelStartDate());
        roomInfoDTO.setTravelEndDate(entity.getTravelEndDate());
        roomInfoDTO.setRoomImage(entity.getRoomImage());
        roomInfoDTO.setTotalParticipants(entity.getTotalParticipants());
        roomInfoDTO.setRoomPassword(entity.getRoomPassword());

        return roomInfoDTO;
    }

    @Transactional(readOnly=true)
    public List<RoomEntity> read(){
        return roomRepository.findAll();
    }

}
