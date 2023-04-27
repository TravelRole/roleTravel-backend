package com.travel.role.domain.room.entity;

import com.travel.role.domain.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import org.hibernate.annotations.ColumnDefault;

@Table(name = "travel_essentials")
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TravelEssentials {

	@Id
	@Column(name = "travel_essentials_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id", nullable = false, updatable = false)
	private Room room;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false, updatable = false)
	private User user;

	@Column(length = 20, nullable = false)
	@Enumerated(EnumType.ORDINAL)
	private EssentialCategory category;

	@Column(name = "is_checked", nullable = false, columnDefinition = "TINYINT(1)")
	@ColumnDefault(value = "0")
	private Boolean isChecked;

	@Column(name = "item_name", length = 100, nullable = false)
	private String itemName;
}





