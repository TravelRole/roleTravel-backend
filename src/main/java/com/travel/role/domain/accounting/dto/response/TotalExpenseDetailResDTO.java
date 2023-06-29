package com.travel.role.domain.accounting.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class TotalExpenseDetailResDTO {

	private int totalExpense;
}
