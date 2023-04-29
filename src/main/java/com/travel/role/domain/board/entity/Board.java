package com.travel.role.domain.board.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.travel.role.domain.board.dto.request.BoardRequestDTO;
import com.travel.role.domain.room.entity.Room;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "board")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    @Id
    @Column(name = "board_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false, updatable = false)
    private Room room;

    @Column(name = "place_name", length = 100, nullable = false)
    private String placeName;

    @Column(name = "place_address", length = 100, nullable = false)
    private String placeAddress;

    @Column(name = "schedule_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime scheduleDate;

    @Column
    private String link;

    @Column(name = "is_book_required")
    private Boolean isBookRequired;

    @OneToOne(mappedBy = "board", cascade = CascadeType.ALL)
    private ScheduleBoard scheduleBoard;

    @OneToOne(mappedBy = "board", cascade = CascadeType.ALL)
    private BookBoard bookBoard;

    private Board(Room room, BoardRequestDTO boardRequestDTO) {
        this.room = room;
        this.placeName = boardRequestDTO.getPlaceName();
        this.placeAddress = boardRequestDTO.getPlaceAddress();
        this.scheduleDate = boardRequestDTO.getScheduleDate();
        this.link = boardRequestDTO.getLink();
        this.isBookRequired = boardRequestDTO.getIsBookRequired();
    }

    public static Board of(Room room, BoardRequestDTO boardRequestDTO) {
        return new Board(room, boardRequestDTO);
    }
}
