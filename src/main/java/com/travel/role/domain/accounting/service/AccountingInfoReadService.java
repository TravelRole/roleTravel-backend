package com.travel.role.domain.accounting.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.accounting.dto.response.ExpenseDetailResDTO;
import com.travel.role.domain.accounting.dto.response.ExpenseDetailsResDTO;
import com.travel.role.domain.accounting.entity.PaymentMethod;
import com.travel.role.domain.accounting.repository.AccountingInfoRepository;
import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.room.service.RoomParticipantReadService;
import com.travel.role.domain.room.service.RoomReadService;
import com.travel.role.domain.user.entity.User;
import com.travel.role.domain.user.service.UserReadService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountingInfoReadService {

	private final UserReadService userReadService;
	private final RoomReadService roomReadService;
	private final RoomParticipantReadService roomParticipantReadService;
	private final AccountingInfoRepository accountingInfoRepository;

	public ExpenseDetailsResDTO getExpenseDetails(String email, Long roomId, LocalDate searchDate,
		PaymentMethod paymentMethod) {

		User loginUser = userReadService.findUserByEmailOrElseThrow(email);
		Room room = roomReadService.findRoomByIdOrElseThrow(roomId);
		roomParticipantReadService.checkParticipant(loginUser, room);

		List<ExpenseDetailResDTO> expenseDetailResDTOS = accountingInfoRepository.findAllByRoomIdAndDateAndPaymentMethod(
			roomId, searchDate, paymentMethod).stream()
			.map(ExpenseDetailResDTO::from)
			.collect(Collectors.toList());

		return ExpenseDetailsResDTO.from(expenseDetailResDTOS);
	}
}
