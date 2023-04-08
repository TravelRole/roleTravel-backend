package com.travel.role.domain.room.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.travel.role.global.domain.BaseCreateTime;

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
public class Room extends BaseCreateTime {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "room_id")
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

	@OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<RoomParticipant> roomParticipants = new ArrayList<>();
}
