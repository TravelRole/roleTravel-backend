package com.travel.role.unit.room.service;

import static org.mockito.BDDMockito.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.travel.role.domain.room.repository.ParticipantRoleRepository;
import com.travel.role.domain.room.service.ParticipantRoleReadService;
import com.travel.role.global.exception.user.RoomInfoNotFoundException;

@ExtendWith(MockitoExtension.class)
public class ParticipantRoleReadServiceTest {

	@Mock
	private ParticipantRoleRepository participantRoleRepository;

	@InjectMocks
	private ParticipantRoleReadService participantRoleReadService;

	@Test
	void 방에_해당_정보가_없는_경우() {
		// given
		given(participantRoleRepository.findUserByRoomId(anyLong()))
			.willReturn(List.of());

		// when, then
		Assertions.assertThatThrownBy(() -> participantRoleReadService.findUserByRoomId(1L))
			.isInstanceOf(RoomInfoNotFoundException.class);
	}
}
