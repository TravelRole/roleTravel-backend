package com.travel.role.global.auth.service;


import com.travel.role.domain.room.dao.RoomRepository;
import com.travel.role.domain.room.domain.RoomEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RoomInfoService {
    private RoomRepository roomRepository;

    public List<RoomEntity> create(final RoomEntity entity){

        validate(entity);
        roomRepository.save(entity);
        return roomRepository.findAll();
    }

    public List<RoomEntity> read(){
        return roomRepository.findAll();
    }

    public void validate(final RoomEntity entity){
        if(entity == null){
            log.warn("Entity가 null인 상태입니다.");
            throw new RuntimeException("Entity cannot be null.");
        }
    }
}
