package com.travel.role.domain.room.domain;

import lombok.*;

import javax.persistence.*;

@Table(name = "want_place")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WantPlace {

    @Id
    @Column(name = "place_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false, updatable = false)
    private Room room;

    @Column(name = "place_name", length = 100, nullable = false)
    private String placeName;

    @Column(name = "place_address", length = 100, nullable = false)
    private String placeAddress;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    private WantPlace(Room room, String placeName, String placeAddress, Double latitude, Double longitude) {
        this.room = room;
        this.placeName = placeName;
        this.placeAddress = placeAddress;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static WantPlace of(Room room, String placeName, String placeAddress, Double latitude, Double longitude) {
        return new WantPlace(room, placeName, placeAddress, latitude, longitude);
    }
}
