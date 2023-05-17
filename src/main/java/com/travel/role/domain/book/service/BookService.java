package com.travel.role.domain.book.service;

import static com.travel.role.global.exception.dto.ExceptionMessage.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.accounting.entity.AccountingInfo;
import com.travel.role.domain.accounting.entity.PaymentMethod;
import com.travel.role.domain.accounting.repository.AccountingInfoRepository;
import com.travel.role.domain.accounting.service.AccountingInfoReadService;
import com.travel.role.domain.book.dto.request.BookInfoRequestDTO;
import com.travel.role.domain.book.dto.request.BookModifyRequestDTO;
import com.travel.role.domain.book.dto.request.BookedRequestDTO;
import com.travel.role.domain.book.dto.response.BookInfoResponseDTO;
import com.travel.role.domain.book.entity.BookInfo;
import com.travel.role.domain.book.repository.BookInfoRepository;
import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.room.entity.RoomRole;
import com.travel.role.domain.room.service.ParticipantRoleReadService;
import com.travel.role.domain.room.service.RoomParticipantReadService;
import com.travel.role.domain.room.service.RoomReadService;
import com.travel.role.domain.schedule.entity.Board;
import com.travel.role.domain.schedule.entity.ScheduleInfo;
import com.travel.role.domain.schedule.repository.BoardRepository;
import com.travel.role.domain.schedule.repository.ScheduleInfoRepository;
import com.travel.role.domain.schedule.service.BoardReadService;
import com.travel.role.domain.user.entity.User;
import com.travel.role.domain.user.service.UserReadService;
import com.travel.role.global.exception.room.InvalidLocalDateException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {
	private final UserReadService userReadService;
	private final RoomReadService roomReadService;
	private final BookReadService bookReadService;
	private final BoardReadService boardReadService;
	private final BoardRepository boardRepository;
	private final BookInfoRepository bookInfoRepository;
	private final ScheduleInfoRepository scheduleInfoRepository;
	private final AccountingInfoRepository accountingInfoRepository;
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
		if (!bookedRequestDTO.getIsBooked())
			modifyPaymentTime(bookedRequestDTO.getAccountingInfoId(), bookedRequestDTO.getPaymentTime());
	}

	private void modifyIsBooked(Long bookInfoId, Boolean isBooked) {

		BookInfo bookInfo = bookReadService.findBookInfoByIdOrElseThrow(bookInfoId);
		bookInfo.updateIsBooked(isBooked);
	}

	private void modifyPaymentTime(Long accountingInfoId, LocalDate paymentTime) {

		AccountingInfo accountingInfo = accountingInfoReadService.findAccountingInfoByIdOrElseThrow(accountingInfoId);
		accountingInfo.updatePaymentTime(paymentTime);
	}

	public List<BookInfoResponseDTO> getBookInfo(String email, Long roomId, LocalDate date) {

		User user = userReadService.findUserByEmailOrElseThrow(email);
		Room room = roomReadService.findRoomByIdOrElseThrow(roomId);
		roomParticipantReadService.checkParticipant(user, room);

		validateDate(room.getTravelStartDate(), room.getTravelEndDate(), date);

		return getBookInfoResult(boardReadService.findBookInfoForDate(roomId, date));
	}

	private List<BookInfoResponseDTO> getBookInfoResult(List<Board> boardList) {

		return boardList.stream()
			.map(board -> BookInfoResponseDTO.from(board))
			.collect(Collectors.toList());
	}

	public void addSchedule(String email, Long roomId, BookInfoRequestDTO bookInfoRequestDTO) {

		User user = userReadService.findUserByEmailOrElseThrow(email);
		Room room = roomReadService.findRoomByIdOrElseThrow(roomId);
		roomParticipantReadService.checkParticipant(user, room);
		validateDate(room.getTravelStartDate(), room.getTravelEndDate(),
			bookInfoRequestDTO.getScheduleDate().toLocalDate());

		Board board = boardRepository.save(Board.of(room, bookInfoRequestDTO));
		scheduleInfoRepository.save(ScheduleInfo.of(board, bookInfoRequestDTO));

		if (bookInfoRequestDTO.getIsBookRequired()) {
			BookInfo bookInfo = bookInfoRepository.save(BookInfo.builder()
				.isBooked(false)
				.build());
			accountingInfoRepository.save(AccountingInfo.builder()
				.board(board)
				.bookInfo(bookInfo)
				.category(bookInfoRequestDTO.getCategory())
				.paymentName(bookInfoRequestDTO.getPlaceName())
				.price(0)
				.room(room)
				.build());
		}
	}

	public void validateDate(LocalDate startDate, LocalDate endDate, LocalDate date) {

		if (date.isBefore(startDate))
			throw new InvalidLocalDateException(EARLY_DATE_ERROR);
		if (date.isAfter(endDate))
			throw new InvalidLocalDateException(LATE_DATE_ERROR);
	}
}
