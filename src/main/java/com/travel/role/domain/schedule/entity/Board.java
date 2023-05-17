package com.travel.role.domain.schedule.entity;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.travel.role.domain.accounting.entity.AccountingInfo;
import com.travel.role.domain.accounting.entity.Category;
import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.schedule.dto.request.ScheduleModifyRequestDTO;
import com.travel.role.domain.schedule.dto.request.ScheduleRequestDTO;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "board")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Board {

	@Id
	@Column(name = "board_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id", nullable = false, updatable = false)
	private Room room;

	@Column(name = "schedule_date")
	private LocalDateTime scheduleDate;

	@Column
	private String link;

	@Enumerated(EnumType.STRING)
	@Column(length = 100)
	private Category category;

	@OneToOne(mappedBy = "board", cascade = CascadeType.ALL)
	private ScheduleInfo scheduleInfo;

	@OneToOne(mappedBy = "board", cascade = CascadeType.ALL)
	private AccountingInfo accountingInfo;

	private Board(Room room, ScheduleRequestDTO scheduleRequestDTO) {
		this.room = room;
		this.scheduleDate = scheduleRequestDTO.getScheduleDate();
		this.link = scheduleRequestDTO.getLink();
		this.category = scheduleRequestDTO.getCategory();
	}

	public static Board of(Room room, ScheduleRequestDTO scheduleRequestDTO) {
		return new Board(room, scheduleRequestDTO);
	}

	public void updateTimeAndCategory(ScheduleModifyRequestDTO reqDTO) {
		this.scheduleDate = reqDTO.getScheduleDate();
		this.category = reqDTO.getCategory();
	}

}
