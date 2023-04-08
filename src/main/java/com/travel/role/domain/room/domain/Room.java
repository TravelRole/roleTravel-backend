package com.travel.role.domain.room.domain;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedAttributeNode;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "ROOM_INFO")
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Room {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "room_name", nullable = false)
	private String roomName;

	@Column(name = "tarvel_start_date")
	private LocalDate travelStartDate;

	@Column(name = "travel_end_date")
	private LocalDate travelEndDate;

	@Column(name = "room_image")
	private String roomImage;

	@Column(name = "total_participants")
	private Integer totalParticipants;

	@Column(name = "room_password")
	private String roomPassword;
}
