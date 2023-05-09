package com.travel.role.domain.room.entity;

import com.travel.role.domain.room.dto.request.MakeRoomRequestDTO;
import com.travel.role.global.domain.BaseCreateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

	@Column(name = "travel_expense", nullable = false)
	private int travelExpense;

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

	public Room(Long id, String roomName, LocalDate travelStartDate, LocalDate travelEndDate, Long roomImage,
		String location, String roomInviteCode, LocalDateTime roomExpiredTime) {
		this.id = id;
		this.roomName = roomName;
		this.travelExpense = 0;
		this.travelStartDate = travelStartDate;
		this.travelEndDate = travelEndDate;
		this.roomImage = roomImage;
		this.location = location;
		this.roomInviteCode = roomInviteCode;
		this.roomExpiredTime = roomExpiredTime;
	}

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

	public void updateTravelExpense(int travelExpense) {
		this.travelExpense = travelExpense;
	}
}
