package com.travel.role.domain.room;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.room.dao.RoomParticipantRepository;
import com.travel.role.domain.room.dao.RoomRepository;
import com.travel.role.domain.room.domain.Room;
import com.travel.role.domain.room.domain.RoomParticipant;
import com.travel.role.domain.room.dto.MakeRoomRequestDTO;
import com.travel.role.domain.room.dto.RoomResponseDTO;
import com.travel.role.domain.room.service.RoomService;
import com.travel.role.domain.user.dao.UserRepository;
import com.travel.role.domain.user.domain.User;
import com.travel.role.domain.user.dto.auth.SignUpRequestDTO;
import com.travel.role.global.auth.token.UserPrincipal;

@SpringBootTest
@Transactional
class RoomTest {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private RoomParticipantRepository roomParticipantRepository;

	@Autowired
	private RoomService roomService;

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

		MakeRoomRequestDTO makeRoom2 = new MakeRoomRequestDTO("room2", LocalDate.of(2023, 1, 5),
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
		//given
		List<User> userResult = userRepository.findAll();
		List<Room> roomResult = roomRepository.findAll();
		List<RoomParticipant> roomParticipantResult = roomParticipantRepository.findAll();

		//when, then
		assertThat(userResult.size()).isEqualTo(4);
		assertThat(roomResult.size()).isEqualTo(2);
		assertThat(roomParticipantResult.size()).isEqualTo(5);
	}

	@Test
	void 방의_정보를_제대로_불러오는지() {
		// given
		UserPrincipal userPrincipal = new UserPrincipal(1L, "haechan@naver.com", "1234", null);

		// when
		Map<Long, RoomResponseDTO> roomList = roomService.getRoomList(userPrincipal);

		// then
		assertThat(roomList.get(1L).getRoomName()).isEqualTo("room1");
		assertThat(roomList.get(1L).getMembers().size()).isEqualTo(3);
		assertThat(roomList.get(2L).getRoomName()).isEqualTo("room2");
		assertThat(roomList.get(2L).getMembers().size()).isEqualTo(2);
	}
}