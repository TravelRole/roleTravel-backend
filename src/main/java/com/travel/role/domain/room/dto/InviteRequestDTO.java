package com.travel.role.domain.room.dto;

import java.util.List;

import lombok.Data;

@Data
public class InviteRequestDTO {
	private List<String> selectRole;
}
