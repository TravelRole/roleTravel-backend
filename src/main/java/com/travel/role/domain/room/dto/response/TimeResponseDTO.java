package com.travel.role.domain.room.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class TimeResponseDTO {
    private Long idx;
    private LocalDate date;
    private String day;

    private TimeResponseDTO(Long idx, LocalDate date, String day) {
        this.idx = idx;
        this.date = date;
        this.day = day;
    }

    public static TimeResponseDTO from(Long idx, LocalDate date, String day) {
        return new TimeResponseDTO(idx, date, day);
    }

}
