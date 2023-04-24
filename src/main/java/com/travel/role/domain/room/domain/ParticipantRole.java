package com.travel.role.domain.room.domain;

import com.travel.role.domain.user.domain.User;
import com.travel.role.global.domain.BaseTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
}
