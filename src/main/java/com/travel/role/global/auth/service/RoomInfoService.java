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
    @Autowired
    private RoomRepository repository;

    public List<RoomEntity> create(final RoomEntity entity){

        validate(entity);
        return repository.findAll();
    }

    public List<RoomEntity> read(){
        return repository.findAll();
    }

    public void validate(final RoomEntity entity){
        if(entity == null){
            log.warn("Entity가 null인 상태입니다.");
            throw new RuntimeException("Entity cannot be null.");
        }
        repository.save(entity);
    }
}
