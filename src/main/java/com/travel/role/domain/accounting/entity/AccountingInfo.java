package com.travel.role.domain.accounting.entity;

import com.travel.role.domain.board.entity.Board;
import com.travel.role.domain.board.entity.BookInfo;
import com.travel.role.domain.room.entity.Room;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "accounting_info")
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountingInfo {
    @Id
    @Column(name = "accounting_info_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false, updatable = false)
    private Room room;

    @Column(name = "payment_method", length = 10)
    private String paymentMethod;

    @Column(name = "payment_name", length = 100)
    private String paymentName;

    @Column
    private Integer price;

    @Column(name = "accounting_etc", length = 100)
    private String accountingEtc;

    @Column(name = "payment_time")
    private LocalDateTime paymentTime;

    @Enumerated(EnumType.STRING)
    @Column(length = 100)
    private Category category;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_info_id")
    private BookInfo bookInfo;
}
