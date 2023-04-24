package com.travel.role.domain.comment.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.travel.role.config.TestConfig;
import com.travel.role.domain.comment.dao.CommentRepository;
import com.travel.role.domain.comment.entity.Comment;
import com.travel.role.domain.room.dao.RoomRepository;
import com.travel.role.domain.room.domain.Room;
import com.travel.role.domain.user.dao.UserRepository;
import com.travel.role.domain.user.domain.Provider;
import com.travel.role.domain.user.domain.Role;
import com.travel.role.domain.user.domain.User;

@DataJpaTest
@Import(TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommentRepositoryTest {

	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	private final RoomRepository roomRepository;
	private User user;
	private Room room;

	@Autowired
	public CommentRepositoryTest(CommentRepository commentRepository,
		UserRepository userRepository,
		RoomRepository roomRepository) {
		this.commentRepository = commentRepository;
		this.userRepository = userRepository;
		this.roomRepository = roomRepository;
	}

	@BeforeEach
	void init() {
		user = User.builder()
			.email("kkk1@naver.com")
			.name("name1")
			.password("password1")
			.role(Role.USER)
			.provider(Provider.local)
			.build();

		userRepository.save(user);

		room = Room.builder()
			.roomName("name1")
			.travelStartDate(LocalDate.of(2023, 8, 20))
			.travelEndDate(LocalDate.of(2023, 9, 20))
			.location("location")
			.build();

		roomRepository.save(room);
	}

	@Test
	void 댓글_생성_확인() {

		// given
		String content = "content";
		Comment comment = Comment.ofParent(user, room, content);
		commentRepository.save(comment);
		comment.setGroupId(comment.getId());
		commentRepository.save(comment);

		// when
		Comment findComment = commentRepository.findAll().get(0);

		// then
		assertThat(findComment.getUser().getId()).isEqualTo(user.getId());
		assertThat(findComment.getRoom().getId()).isEqualTo(room.getId());
		assertThat(findComment.getDepth()).isZero();
		assertThat(findComment.getContent()).isEqualTo(content);
		assertThat(findComment.getGroupId()).isNotNull();
		assertThat(findComment.getGroupId()).isEqualTo(findComment.getId());
	}

	@Test
	void 대댓글_생성_확인() {

		// given
		String content1 = "content";
		Comment parent = Comment.ofParent(user, room, content1);

		commentRepository.save(parent);
		parent.setGroupId(parent.getId());
		commentRepository.save(parent);

		String content2 = "content2";
		Comment child = Comment.builder()
			.user(user)
			.room(room)
			.parent(parent)
			.depth(parent.getDepth() + 1)
			.groupId(parent.getGroupId())
			.content(content2)
			.build();

		commentRepository.save(child);

		// when
		Comment findChild = commentRepository.findAll()
			.stream()
			.filter(comment -> comment.getParent() != null)
			.findFirst()
			.get();

		// then
		assertThat(findChild.getUser().getId()).isEqualTo(user.getId());
		assertThat(findChild.getRoom().getId()).isEqualTo(room.getId());
		assertThat(findChild.getDepth()).isEqualTo(parent.getDepth() + 1);
		assertThat(findChild.getContent()).isEqualTo(content2);
		assertThat(findChild.getGroupId()).isEqualTo(parent.getGroupId());
	}

	@Test
	void depth가_0인_댓글_조회_확인() {

		// given
		for (int i = 0; i < 5; i++) {
			Comment comment = Comment.ofParent(user, room, "content");
			commentRepository.save(comment);
		}

		// when
		List<Comment> firstDepthComments = commentRepository.findAllFirstDepthComments();

		// then
		assertThat(firstDepthComments).hasSize(5);
		assertThat(firstDepthComments).extracting(Comment::getDepth).containsOnly(0);
		assertThat(firstDepthComments).extracting(Comment::getCreateDate)
			.isSortedAccordingTo(Comparator.reverseOrder());
	}

	@Test
	void depth가_N인_댓글_조회_확인() {

		// given
		Comment parent = Comment.ofParent(user, room, "content");
		commentRepository.save(parent);
		for (int i = 0; i < 5; i++) {
			Comment comment = Comment.ofChild(user, room, parent, "content");
			commentRepository.save(comment);
		}

		// when
		List<Comment> childComments = commentRepository.findAllChildCommentsByParentId(parent.getId());

		// then
		assertThat(childComments).hasSize(5);
		assertThat(childComments).extracting(Comment::getDepth).containsOnly(1);
		assertThat(childComments).extracting(Comment::getCreateDate)
			.isSortedAccordingTo(Comparator.reverseOrder());
	}

	@Test
	void 댓글_삭제_확인() {
		// given
		int parent1ChildSize = 5;
		Comment parent1 = Comment.ofParent(user, room, "content");
		commentRepository.save(parent1);
		parent1.setGroupId(parent1.getId());
		commentRepository.save(parent1);
		for (int i = 0; i < parent1ChildSize; i++) {
			Comment comment = Comment.ofChild(user, room, parent1, "content");
			commentRepository.save(comment);
		}

		int parent2ChildSize = 10;
		Comment parent2 = Comment.ofParent(user, room, "content");
		commentRepository.save(parent2);
		parent2.setGroupId(parent2.getId());
		commentRepository.save(parent2);
		for (int i = 0; i < parent2ChildSize; i++) {
			Comment comment = Comment.ofChild(user, room, parent2, "content");
			commentRepository.save(comment);
		}

		// when
		commentRepository.deleteAllByGroupIdAndDepth(parent1.getGroupId(), parent1.getDepth());
		List<Comment> comments = commentRepository.findAll();

		// then
		assertThat(comments).hasSize(parent2ChildSize + 1);
		assertThat(comments).extracting(Comment::getGroupId).containsOnly(parent2.getGroupId());
	}
}
