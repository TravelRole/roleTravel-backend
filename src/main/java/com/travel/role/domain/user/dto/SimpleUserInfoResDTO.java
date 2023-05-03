package com.travel.role.domain.user.dto;


import java.util.List;

import com.travel.role.domain.room.entity.RoomRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleUserInfoResDTO {

	private Long id;
	private String name;
	private String profile;
	private List<RoomRole> roles;
}
