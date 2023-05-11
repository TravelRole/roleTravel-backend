package com.travel.role.domain.room.dto.response;

import com.travel.role.domain.room.entity.Room;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExpenseResponseDTO {

	private Integer expenses;

	public static ExpenseResponseDTO from(Room room) {

		return new ExpenseResponseDTO(room.getTravelExpense());
	}
}
