package com.travel.role.domain.room.service;


import com.travel.role.domain.room.dao.RoomRepository;
import com.travel.role.domain.room.domain.RoomEntity;
import com.travel.role.domain.room.exception.NullEntityException;
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


    public List<RoomEntity> create(final RoomEntity entity){

        validate(entity);
        return Collections.singletonList(roomRepository.save(entity));
    }

    @Transactional(readOnly=true)
    public List<RoomEntity> read(){
        return roomRepository.findAll();
    }

    private void validate(final RoomEntity entity){
        if(entity == null){
            throw new NullEntityException(ENTITY_IS_NULL);
        }
    }
}
