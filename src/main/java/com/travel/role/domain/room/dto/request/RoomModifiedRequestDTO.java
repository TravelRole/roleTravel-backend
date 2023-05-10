package com.travel.role.domain.room.dto.request;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomModifiedRequestDTO {
	private String roomName;
	private LocalDate startDate;
	private LocalDate endDate;
	private List<RoomRoleDTO> userRoles;
}
