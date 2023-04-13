package com.travel.role.domain.room.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.travel.role.domain.user.domain.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "ROOM_PARTICIPANT_INFO")
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomParticipant {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "room_participant_id")
	private Long id;

	@Column(name ="joined_at",nullable = false)
	private LocalDateTime joinedAt;

	@Column(name = "is_paid", nullable = false)
	private Boolean isPaid;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id")
	private Room room;

}
