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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false, updatable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", updatable = false)
    private Comment parent;

    private Comment(User user, Room room, Comment parent, String content) {
        this.user = user;
        this.room = room;
        this.parent = parent;
        this.content = content;
    }

    public static Comment of(User user, Room room, Comment parent, String content) {

        return new Comment(user, room, parent, content);
    }
}
