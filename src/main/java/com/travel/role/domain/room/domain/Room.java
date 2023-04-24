package com.travel.role.domain.room.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.travel.role.domain.room.dto.MakeRoomRequestDTO;
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

	@Column(name = "tarvel_start_date", nullable = false)
	private LocalDate travelStartDate;

	@Column(name = "travel_end_date", nullable = false)
	private LocalDate travelEndDate;

	@Column(name = "room_image")
	private Long roomImage;

	@Column(nullable = false)
	private String location;

	@Column(name = "room_invite_code")
	private String roomInviteCode;

	@Column(name = "room_expired_time")
	private LocalDateTime roomExpiredTime;

	@OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
	private Set<RoomParticipant> roomParticipants = new HashSet<>();

	public static Room of(MakeRoomRequestDTO makeRoomRequestDTO) {
		return Room.builder()
			.location(makeRoomRequestDTO.getLocation())
			.roomName(makeRoomRequestDTO.getRoomName())
			.travelEndDate(makeRoomRequestDTO.getTravelEndDate())
			.travelStartDate(makeRoomRequestDTO.getTravelStartDate())
			.roomImage(makeRoomRequestDTO.getRoomImage())
			.build();
	}

	public void updateInviteCode(String inviteCode, LocalDateTime now) {
		this.roomInviteCode = inviteCode;
		this.roomExpiredTime = now;
	}
}
