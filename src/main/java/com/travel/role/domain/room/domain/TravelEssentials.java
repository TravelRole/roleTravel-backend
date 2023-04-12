package com.travel.role.domain.room.domain;

import com.travel.role.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Table(name = "travel_essentials")
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TravelEssentials {

    @Id
    @Column(name = "travel_essentials_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false, updatable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Column(length = 20, nullable = false)
    private String category;

    @Column(name = "is_checked", nullable = false)
    @ColumnDefault("false")
    private Boolean isChecked;

    @Column(name = "item_name", length = 100, nullable = false)
    private String itemName;
}




