package com.travel.role.domain.room.dto.request;

import java.util.List;

import com.travel.role.domain.room.entity.RoomRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomRoleDTO {
	private String email;
	private List<RoomRole> roles;
}
