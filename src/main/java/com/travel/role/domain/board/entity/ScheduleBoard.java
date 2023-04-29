package com.travel.role.domain.board.entity;

import com.travel.role.domain.board.dto.request.BoardRequestDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "schedule_board")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleBoard {
    @Id
    @Column(name = "board_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column
    private String etc;

    public static ScheduleBoard of(Board board, BoardRequestDTO boardRequestDTO){
        return new ScheduleBoard(board, boardRequestDTO);
    }

    private ScheduleBoard(Board board, BoardRequestDTO boardRequestDTO){
        this.board = board;
        this.latitude = boardRequestDTO.getLatitude();
        this.longitude = boardRequestDTO.getLongitude();
        this.etc = boardRequestDTO.getEtc();
    }
}
