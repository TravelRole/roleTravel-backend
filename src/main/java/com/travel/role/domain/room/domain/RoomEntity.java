package com.travel.role.domain.room.domain;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Table(name = "ROOM_INFO")
@Getter //lombok: getter, setter
@Entity
@AllArgsConstructor // 모든 필드 값 파라미터로 하는 생성자
@NoArgsConstructor // 기본 생성자
@Builder
public class RoomEntity extends BaseTime{

    @Id //식별자
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키 자동생성 => DB에 위임 (AUTO_INCREMENT)
    private Long id;

    @Column(nullable = false, length=100)
    private String roomName;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime travelStartDate;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime travelEndDate;

    @Column(nullable = false)
    private String roomImage;

    @Column(nullable = false, length=20)
    private String totalParticipants;

    @Column(nullable = false, length=20)
    private String roomPassword;

}