package com.travel.role.domain.comment.entity;

import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.user.entity.User;
import com.travel.role.global.domain.BaseTime;

import lombok.*;

import javax.persistence.*;

import org.hibernate.annotations.ColumnDefault;

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

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "from_user_id", nullable = false, updatable = false)
	private User fromUser; // 작성자

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_user_id", updatable = false)
	private User toUser; // 어떤 댓글의 주인에게 대댓글을 남겼는지

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "room_id", nullable = false, updatable = false)
	private Room room;

	@Column(name = "deleted", nullable = false, columnDefinition = "TINYINT(1)")
	@ColumnDefault(value = "0")
	private Boolean deleted;

	@Builder
	public Comment(Room room, User fromUser, User toUser, String content, Long groupId) {
		this.content = content;
		this.groupId = groupId;
		this.fromUser = fromUser;
		this.toUser = toUser;
		this.room = room;
		this.deleted = false;
	}

	public static Comment ofParent(User fromUser, Room room, String content) {

		return new Comment(room, fromUser, null, content, null);
	}

	public static Comment ofChild(User fromUser, User toUser, Room room, Long groupId, String content) {

		return new Comment(room, fromUser, toUser, content, groupId);
	}

	public void update(String content) {
		this.content = content;
	}

	public void setGroupId(Long groupId){
		this.groupId = groupId;
	}
}
