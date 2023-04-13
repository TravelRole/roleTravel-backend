package com.travel.role.domain.room.dao;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.room.domain.Room;
import com.travel.role.domain.room.domain.RoomParticipant;
import com.travel.role.domain.room.dto.MakeRoomRequestDTO;
import com.travel.role.domain.user.dao.UserRepository;
import com.travel.role.domain.user.domain.User;
import com.travel.role.domain.user.dto.auth.SignUpRequestDTO;

@SpringBootTest
@Transactional
class RoomRepositoryTest {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private RoomParticipantRepository roomParticipantRepository;

	@BeforeEach
	void before() {
		SignUpRequestDTO signup1 = new SignUpRequestDTO("해찬", "haechan@naver.com", "1234", LocalDate.now());
		SignUpRequestDTO signup2 = new SignUpRequestDTO("깐잽", "Ggan@naver.com", "1234", LocalDate.now());
		SignUpRequestDTO signup3 = new SignUpRequestDTO("찬유", "ChanYoo@naver.com", "1234", LocalDate.now());
		SignUpRequestDTO signup4 = new SignUpRequestDTO("유해", "Yoohae@naver.com", "1234", LocalDate.now());

		User user1 = User.of(signup1, "1234");
		User user2 = User.of(signup2, "1234");
		User user3 = User.of(signup3, "1234");
		User user4 = User.of(signup4, "1234");

		user1 = userRepository.save(user1);
		user2 = userRepository.save(user2);
		user3 = userRepository.save(user3);
		user4 = userRepository.save(user4);

		MakeRoomRequestDTO makeRoom1 = new MakeRoomRequestDTO("room1", LocalDate.of(2023, 1, 1),
			LocalDate.of(2023, 1, 3),
			3, "광양");

		MakeRoomRequestDTO makeRoom2 = new MakeRoomRequestDTO("2번방입니다!!", LocalDate.of(2023, 1, 5),
			LocalDate.of(2023, 1, 10),
			3, "스울");

		Room room1 = Room.of(makeRoom1, "1234");
		Room room2 = Room.of(makeRoom2, "1234");

		room1 = roomRepository.save(room1);
		room2 = roomRepository.save(room2);

		RoomParticipant roomParticipant1 = new RoomParticipant(null, LocalDateTime.now(), true, user1, room1);
		RoomParticipant roomParticipant2 = new RoomParticipant(null, LocalDateTime.now(), true, user2, room1);
		RoomParticipant roomParticipant3 = new RoomParticipant(null, LocalDateTime.now(), true, user3, room1);
		RoomParticipant roomParticipant4 = new RoomParticipant(null, LocalDateTime.now(), true, user1, room2);
		RoomParticipant roomParticipant5 = new RoomParticipant(null, LocalDateTime.now(), true, user4, room2);

		roomParticipant1 = roomParticipantRepository.save(roomParticipant1);
		roomParticipant2 = roomParticipantRepository.save(roomParticipant2);
		roomParticipant3 = roomParticipantRepository.save(roomParticipant3);
		roomParticipant4 = roomParticipantRepository.save(roomParticipant4);
		roomParticipant5 = roomParticipantRepository.save(roomParticipant5);
	}

	@Test
	void before_each의_정보가_제대로_들어갔는지() {
		List<User> userResult = userRepository.findAll();
		List<Room> roomResult = roomRepository.findAll();
		List<RoomParticipant> roomParticipantResult = roomParticipantRepository.findAll();

		assertThat(userResult.size()).isEqualTo(4);
		assertThat(roomResult.size()).isEqualTo(2);
		assertThat(roomParticipantResult.size()).isEqualTo(5);
	}
}