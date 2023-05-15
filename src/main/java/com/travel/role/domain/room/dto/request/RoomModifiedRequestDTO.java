package com.travel.role.domain.room.dto.request;

import static com.travel.role.global.exception.dto.ExceptionMessage.*;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomModifiedRequestDTO {
	@NotBlank(message = ROOM_NAME_NOT_EMPTY)
	private String roomName;
	@NotBlank(message = PLACE_NAME_VALUE_NOT_EMPTY)
	private String location;
	@JsonFormat(pattern = "yyyy/MM/dd")
	private LocalDate startDate;
	@JsonFormat(pattern = "yyyy/MM/dd")
	private LocalDate endDate;
	private List<RoomRoleDTO> userRoles;
}
