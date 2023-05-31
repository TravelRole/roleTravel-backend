package com.travel.role.domain.book.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.accounting.entity.AccountingInfo;
import com.travel.role.domain.accounting.entity.PaymentMethod;
import com.travel.role.domain.accounting.service.AccountingInfoReadService;
import com.travel.role.domain.book.dto.request.BookModifyRequestDTO;
import com.travel.role.domain.book.dto.request.BookedRequestDTO;
import com.travel.role.domain.book.dto.response.BookInfoResponseDTO;
import com.travel.role.domain.book.entity.BookInfo;
import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.room.entity.RoomRole;
import com.travel.role.domain.room.service.ParticipantRoleReadService;
import com.travel.role.domain.room.service.RoomParticipantReadService;
import com.travel.role.domain.room.service.RoomReadService;
import com.travel.role.domain.schedule.entity.Board;
import com.travel.role.domain.schedule.service.ScheduleService;
import com.travel.role.domain.user.entity.User;
import com.travel.role.domain.user.service.UserReadService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {
	private final UserReadService userReadService;
	private final RoomReadService roomReadService;
	private final BookReadService bookReadService;
	private final ScheduleService scheduleService;
	private final AccountingInfoReadService accountingInfoReadService;
	private final RoomParticipantReadService roomParticipantReadService;
	private final ParticipantRoleReadService participantRoleReadService;

	public void modifyBookInfo(String email, Long roomId, BookModifyRequestDTO bookModifyRequestDTO) {

		User user = userReadService.findUserByEmailOrElseThrow(email);
		Room room = roomReadService.findRoomByIdOrElseThrow(roomId);
		roomParticipantReadService.checkParticipant(user, room);
		participantRoleReadService.validateUserRoleInRoom(user, room, RoomRole.getReservationRoles());

		modifyPaymentMethodAndPrice(bookModifyRequestDTO.getAccountingInfoId(), bookModifyRequestDTO.getPaymentMethod(),
			bookModifyRequestDTO.getPrice());
		modifyEtc(bookModifyRequestDTO.getBookInfoId(), bookModifyRequestDTO.getBookEtc());
	}

	private void modifyPaymentMethodAndPrice(Long id, PaymentMethod paymentMethod, Integer price) {

		AccountingInfo accountingInfo = accountingInfoReadService.findAccountingInfoByIdOrElseThrow(id);
		accountingInfo.updatePaymentMethodAndPrice(paymentMethod, price);
	}

	private void modifyEtc(Long id, String etc) {

		BookInfo bookInfo = bookReadService.findBookInfoByIdOrElseThrow(id);
		bookInfo.updateEtc(etc);
	}

	public void modifyIsBookedAndPaymentTime(String email, Long roomId, BookedRequestDTO bookedRequestDTO) {

		User user = userReadService.findUserByEmailOrElseThrow(email);
		Room room = roomReadService.findRoomByIdOrElseThrow(roomId);
		roomParticipantReadService.checkParticipant(user, room);
		participantRoleReadService.validateUserRoleInRoom(user, room, RoomRole.getReservationRoles());

		modifyIsBooked(bookedRequestDTO.getBookInfoId(), !bookedRequestDTO.getIsBooked());
		modifyPaymentTime(bookedRequestDTO.getAccountingInfoId(), bookedRequestDTO.getPaymentTime(),
			bookedRequestDTO.getIsBooked());
	}

	private void modifyIsBooked(Long bookInfoId, Boolean isBooked) {

		BookInfo bookInfo = bookReadService.findBookInfoByIdOrElseThrow(bookInfoId);
		bookInfo.updateIsBooked(isBooked);
	}

	private void modifyPaymentTime(Long accountingInfoId, LocalDate paymentTime, Boolean isBooked) {

		AccountingInfo accountingInfo = accountingInfoReadService.findAccountingInfoByIdOrElseThrow(accountingInfoId);
		if (!isBooked)
			accountingInfo.updatePaymentTime(paymentTime);
		else
			accountingInfo.updatePaymentTime(null);
	}

	public List<BookInfoResponseDTO> getBookInfo(String email, Long roomId, LocalDate date) {

		User user = userReadService.findUserByEmailOrElseThrow(email);
		Room room = roomReadService.findRoomByIdOrElseThrow(roomId);
		roomParticipantReadService.checkParticipant(user, room);

		scheduleService.validateDate(room.getTravelStartDate(), room.getTravelEndDate(), date);

		return getBookInfoResult(bookReadService.findBookInfoForDate(roomId, date));
	}

	private List<BookInfoResponseDTO> getBookInfoResult(List<Board> boardList) {

		return boardList.stream()
			.map(board -> BookInfoResponseDTO.from(board))
			.collect(Collectors.toList());
	}

}
