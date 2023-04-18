package com.travel.role.domain.comment.entity;

import com.travel.role.domain.room.domain.Room;
import com.travel.role.domain.user.domain.User;
import com.travel.role.global.domain.BaseTime;

import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(of = {"id"}, callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Comment extends BaseTime {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_id")
	private Long id;

	@Column(nullable = false)
	private String content;

	@Column
	private Long groupId;

	@Column(nullable = false, updatable = false)
	private Integer depth;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false, updatable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "room_id", nullable = false, updatable = false)
	private Room room;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id", updatable = false)
	private Comment parent;

	public Comment(User user, Room room, Comment parent, String content, Long groupId, Integer depth) {
		this.content = content;
		this.groupId = groupId;
		this.depth = depth;
		this.user = user;
		this.room = room;
		this.parent = parent;
	}

	public static Comment ofParent(User user, Room room, String content) {

		return new Comment(user, room, null, content, null, 0);
	}

	public static Comment ofChild(User user, Room room, Comment parent, String content) {

		return new Comment(user, room, parent, content, parent.getGroupId(), parent.getDepth() + 1);
	}

	public void update(String content) {
		this.content = content;
	}

	public void setGroupId(Long groupId){
		this.groupId = groupId;
	}
}
