package com.travel.role.domain.room.dto.request;

import java.util.Set;

import com.travel.role.domain.room.entity.RoomRole;

import lombok.Data;

@Data
public class RoomRoleDTO {
	private String name;
	private Set<RoomRole> roles;
}
