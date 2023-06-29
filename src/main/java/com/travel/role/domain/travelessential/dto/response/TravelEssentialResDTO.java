package com.travel.role.domain.travelessential.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TravelEssentialResDTO {

	private Long id;
	private String itemName;
	private Boolean isChecked;
}
