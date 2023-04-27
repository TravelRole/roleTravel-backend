package com.travel.role.domain.room.entity;

import com.travel.role.domain.user.entity.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "ROOM_PARTICIPANT_INFO")
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
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
