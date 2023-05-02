package com.travel.role.domain.board.entity;

import com.travel.role.domain.board.dto.request.BoardRequestDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "schedule_info")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleInfo {
    @Id
    private Long id;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(name = "place_name", length = 100, nullable = false)
    private String placeName;

    @Column(name = "place_address", length = 100, nullable = false)
    private String placeAddress;

    @Column(name = "schedule_etc", length = 100)
    private String scheduleEtc;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    public static ScheduleInfo of(Board board, BoardRequestDTO boardRequestDTO) {
        return new ScheduleInfo(board, boardRequestDTO);
    }

    private ScheduleInfo(Board board, BoardRequestDTO boardRequestDTO) {
        this.board = board;
        this.latitude = boardRequestDTO.getLatitude();
        this.longitude = boardRequestDTO.getLongitude();
        this.placeName = boardRequestDTO.getPlaceName();
        this.placeAddress = boardRequestDTO.getPlaceAddress();
        this.scheduleEtc = boardRequestDTO.getEtc();
    }
}
