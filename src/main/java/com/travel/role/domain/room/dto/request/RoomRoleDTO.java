package com.travel.role.domain.room.dto.request;

import java.util.List;

import com.travel.role.domain.room.entity.RoomRole;

import lombok.Data;

@Data
public class RoomRoleDTO {
	private String email;
	private List<RoomRole> roles;
}
