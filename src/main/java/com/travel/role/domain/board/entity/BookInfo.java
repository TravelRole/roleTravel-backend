package com.travel.role.domain.board.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Table(name = "book_info")
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookInfo {
    @Id
    @Column(name = "book_info_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_booked", nullable = false, columnDefinition = "TINYINT(1)")
    @ColumnDefault(value = "0")
    private Boolean isBooked;

    @Column(name = "book_etc", length = 100)
    private String bookEtc;
}
