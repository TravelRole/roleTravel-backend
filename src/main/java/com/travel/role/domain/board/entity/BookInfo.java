package com.travel.role.domain.board.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "book_info")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookInfo {
    @Id
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(name = "is_booked")
    private Boolean isBooked;

    @Column
    private Integer price;

    @Column(name = "payment_method", length = 10)
    private String paymentMethod;

    @Column(name = "accounting_etc", length = 100)
    private String accountingEtc;

    @Column(name = "book_etc", length = 100)
    private String bookEtc;

    private BookInfo(Board board) {
        this.board = board;
        this.isBooked = false;
        this.price = 0;
        this.paymentMethod = null;
        this.accountingEtc = null;
        this.bookEtc = null;
    }

    public static BookInfo from(Board board) {
        return new BookInfo(board);
    }
}
