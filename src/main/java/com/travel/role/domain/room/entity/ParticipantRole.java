package com.travel.role.domain.room.entity;

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

import com.travel.role.domain.user.entity.User;
import com.travel.role.global.domain.BaseTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "PARTICIPANT_ROLE")
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParticipantRole extends BaseTime {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "participant_role_id")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "room_role")
	private RoomRole roomRole;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id")
	private Room room;

	public void updateRole(RoomRole roomRole) {
		this.roomRole = roomRole;
	}
}
