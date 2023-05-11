package com.travel.role.domain.accounting.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.accounting.dto.request.ExpenseDetailCreateReqDTO;
import com.travel.role.domain.accounting.dto.response.ExpenseDetailCreateResDTO;
import com.travel.role.domain.accounting.entity.AccountingInfo;
import com.travel.role.domain.accounting.repository.AccountingInfoRepository;
import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.room.entity.RoomRole;
import com.travel.role.domain.room.service.ParticipantRoleReadService;
import com.travel.role.domain.room.service.RoomParticipantReadService;
import com.travel.role.domain.room.service.RoomReadService;
import com.travel.role.domain.user.entity.User;
import com.travel.role.domain.user.service.UserReadService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountingInfoService {

	private final AccountingInfoRepository accountingInfoRepository;
	private final UserReadService userReadService;
	private final RoomParticipantReadService roomParticipantReadService;
	private final ParticipantRoleReadService participantRoleReadService;
	private final RoomReadService roomReadService;

	public ExpenseDetailCreateResDTO createExpenseDetail(Long roomId, String email,
		ExpenseDetailCreateReqDTO requestDTO) {

		User loginUser = userReadService.findUserByEmailOrElseThrow(email);
		Room room = roomReadService.findRoomByIdOrElseThrow(roomId);

		roomParticipantReadService.checkParticipant(loginUser, room);
		participantRoleReadService.validUserRoleInRoom(loginUser, room, RoomRole.getAccountingRoles());

		AccountingInfo accountingInfo = requestDTO.toAccountingInfo(room);
		accountingInfo = accountingInfoRepository.save(accountingInfo);

		return ExpenseDetailCreateResDTO.from(accountingInfo);
	}
}
