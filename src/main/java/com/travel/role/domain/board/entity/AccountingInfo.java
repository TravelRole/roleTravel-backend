package com.travel.role.domain.board.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.travel.role.domain.room.entity.Room;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", length = 10)
    private PaymentMethod paymentMethod;

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

    public void updatePaymentMethodAndPrice(PaymentMethod paymentMethod, int price) {
        this.paymentMethod = paymentMethod;
        this.price = price;
    }
}
