package com.travel.role.domain.accounting.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ExpenseDetailsResDTO {

	private Integer totalExpense;
	private List<ExpenseDetailResDTO> expenses;

	public static ExpenseDetailsResDTO from(List<ExpenseDetailResDTO> expenses) {

		int totalExpense = expenses.stream()
			.mapToInt(ExpenseDetailResDTO::getPrice)
			.sum();

		return ExpenseDetailsResDTO.builder()
			.totalExpense(totalExpense)
			.expenses(expenses)
			.build();
	}
}
