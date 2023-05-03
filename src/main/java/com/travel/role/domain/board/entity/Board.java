package com.travel.role.domain.board.entity;

import com.travel.role.domain.board.dto.request.BoardRequestDTO;
import com.travel.role.domain.room.entity.Room;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "board")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Board {

    @Id
    @Column(name = "board_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false, updatable = false)
    private Room room;

    @Column(name = "schedule_date")
    private LocalDateTime scheduleDate;

    @Column
    private String link;

    @Enumerated(EnumType.STRING)
    @Column(length = 100)
    private Category category;

    @OneToOne(mappedBy = "board", cascade = CascadeType.ALL)
    private ScheduleInfo scheduleInfo;

    @OneToOne(mappedBy = "board", cascade = CascadeType.ALL)
    private BookInfo bookInfo;

    private Board(Room room, BoardRequestDTO boardRequestDTO) {
        this.room = room;
        this.scheduleDate = boardRequestDTO.getScheduleDate();
        this.link = boardRequestDTO.getLink();
        this.category = boardRequestDTO.getCategory();
    }

    public static Board of(Room room, BoardRequestDTO boardRequestDTO) {
        return new Board(room, boardRequestDTO);
    }
}
