package com.travel.role.domain.room.dto.response;

import java.util.List;

import com.travel.role.domain.room.entity.RoomRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomRoleInfoDTO {
	private String name;
	private String email;
	private String profile;
	private List<RoomRole> roles;
}
