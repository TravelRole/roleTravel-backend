package com.travel.role.domain.board.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Table(name = "book_board")
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookBoard {
    @Id
    @Column(name = "board_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(name = "is_booked")
    private Boolean isBooked;

    @Column(precision = 12, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(length = 100)
    private Category category;

    @Column
    private String etc;
}
