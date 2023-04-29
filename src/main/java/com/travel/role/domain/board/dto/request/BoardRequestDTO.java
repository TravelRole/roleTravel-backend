package com.travel.role.domain.board.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.travel.role.global.exception.dto.ExceptionMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BoardRequestDTO {
    @NotNull(message = ExceptionMessage.ROOM_ID_VALUE_NOT_EMPTY)
    private Long roomId;

    @NotBlank(message = ExceptionMessage.PLACE_NAME_VALUE_NOT_EMPTY)
    private String placeName;

    @NotBlank(message = ExceptionMessage.PLACE_ADDRESS_VALUE_NOT_EMPTY)
    private String placeAddress;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime scheduleDate;

    private String link;

    private Boolean isBookRequired;

    @NotNull(message = ExceptionMessage.LATITUDE_VALUE_NOT_EMPTY)
    private Double latitude;

    @NotNull(message = ExceptionMessage.LONGITUDE_NOT_EMPTY)
    private Double longitude;

    private String etc;
}
