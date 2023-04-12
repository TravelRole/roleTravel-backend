package com.travel.role.domain.user.dto;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmUserResponseDTO {
	private String message;
	private HttpStatus httpStatus;
	private String email;
	private LocalDate createdAt;
}
