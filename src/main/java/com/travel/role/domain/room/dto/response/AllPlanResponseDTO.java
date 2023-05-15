package com.travel.role.domain.room.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AllPlanResponseDTO {

	private Integer totalExpense;
	private List<AllPlanDTO> data;
}
