package com.travel.role.domain.travelessential.dto.request;

import static com.travel.role.global.exception.dto.ExceptionMessage.*;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.travel.role.domain.travelessential.entity.EssentialCategory;
import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.travelessential.entity.TravelEssential;
import com.travel.role.domain.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TravelEssentialReqDTO {

	@NotNull(message = INVALID_ESSENTIAL_CATEGORY)
	private EssentialCategory category;

	@Valid
	private List<TravelEssentialItemDTO> items;

	public List<TravelEssential> toTravelEssentials(User user, Room room) {

		return this.items.stream()
			.map(item -> TravelEssential.builder()
				.user(user)
				.room(room)
				.category(this.category)
				.isChecked(false)
				.itemName(item.getItem())
				.build()
			).collect(Collectors.toList());
	}
}
