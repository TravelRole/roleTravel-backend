package com.travel.role.domain.travelessential.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "travel_essential")
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TravelEssential {

	@Id
	@Column(name = "travel_essential_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id", nullable = false, updatable = false)
	private Room room;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false, updatable = false)
	private User user;

	@Column(length = 20, nullable = false)
	@Enumerated(EnumType.STRING)
	private EssentialCategory category;

	@Column(name = "is_checked", nullable = false, columnDefinition = "TINYINT(1)")
	@ColumnDefault(value = "0")
	private Boolean isChecked;

	@Column(name = "item_name", length = 100, nullable = false)
	private String itemName;
}





