package com.travel.role.domain.room.dto;

import com.travel.role.domain.room.domain.RoomEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor // 기본 생성자 만듦
@AllArgsConstructor
@Data
public class RoomInfoDTO {
    private String roomName;
    private LocalDateTime travelStartDate;
    private LocalDateTime travelEndDate;
    private String roomImage;
    private String totalParticipants;
    private String roomPassword;


    public RoomInfoDTO(final RoomEntity entity){
        this.roomName=entity.getRoomName();
        this.travelStartDate=entity.getTravelStartDate();
        this.travelEndDate=entity.getTravelEndDate();
        this.roomImage=entity.getRoomImage();
        this.totalParticipants=entity.getTotalParticipants();
        this.roomPassword=entity.getRoomPassword();
    }
}
