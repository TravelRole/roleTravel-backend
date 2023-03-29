package com.travel.role.domain.user.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignUpResponseDTO {
	private String email;
	private String name;
	private String message;
	private LocalDateTime time;
}
