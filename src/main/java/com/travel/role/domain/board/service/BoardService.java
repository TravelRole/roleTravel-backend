package com.travel.role.domain.board.service;

import static com.travel.role.global.exception.dto.ExceptionMessage.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.board.dto.request.BoardRequestDTO;
import com.travel.role.domain.board.dto.request.BookRequestDTO;
import com.travel.role.domain.board.dto.response.BookInfoResponseDTO;
import com.travel.role.domain.accounting.entity.AccountingInfo;
import com.travel.role.domain.board.entity.Board;
import com.travel.role.domain.board.entity.BookInfo;
import com.travel.role.domain.board.entity.PaymentMethod;
import com.travel.role.domain.board.entity.ScheduleInfo;
import com.travel.role.domain.accounting.repository.AccountingInfoRepository;
import com.travel.role.domain.board.repository.BoardRepository;
import com.travel.role.domain.board.repository.BookInfoRepository;
import com.travel.role.domain.board.repository.ScheduleInfoRepository;
import com.travel.role.domain.room.entity.Room;
import com.travel.role.domain.room.entity.RoomRole;
import com.travel.role.domain.room.repository.ParticipantRoleRepository;
import com.travel.role.domain.room.service.RoomParticipantReadService;
import com.travel.role.domain.room.service.RoomReadService;
import com.travel.role.domain.user.entity.User;
import com.travel.role.domain.user.service.UserReadService;
import com.travel.role.global.exception.room.InvalidLocalDateException;
import com.travel.role.global.exception.room.UserHaveNotPrivilegeException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {
	private final UserReadService userReadService;
	private final RoomReadService roomReadService;
	private final BoardReadService boardReadService;
	private final BoardRepository boardRepository;
	private final BookInfoRepository bookInfoRepository;
	private final ScheduleInfoRepository scheduleInfoRepository;
	private final AccountingInfoRepository accountingInfoRepository;
	private final ParticipantRoleRepository participantRoleRepository;
	private final RoomParticipantReadService roomParticipantReadService;

	public void modifyBookInfo(String email, Long roomId, BookRequestDTO bookRequestDTO) {

		User user = userReadService.findUserByEmailOrElseThrow(email);
		Room room = roomReadService.findRoomByIdOrElseThrow(roomId);
		roomParticipantReadService.checkParticipant(user, room);
		validateRoomRole(user, room);

		modifyPaymentMethodAndPrice(bookRequestDTO.getAccountingInfoId(), bookRequestDTO.getPaymentMethod(),
			bookRequestDTO.getPrice());
		modifyEtc(bookRequestDTO.getBookInfoId(), bookRequestDTO.getBookEtc());
	}

	private void modifyPaymentMethodAndPrice(Long id, PaymentMethod paymentMethod, Integer price) {
		AccountingInfo accountingInfo = boardReadService.findAccountingInfoByIdOrElseThrow(id);
		accountingInfo.updatePaymentMethodAndPrice(paymentMethod, price);
	}

	private void modifyEtc(Long id, String etc) {
		BookInfo bookInfo = boardReadService.findBookInfoByIdOrElseThrow(id);
		bookInfo.updateEtc(etc);
	}

	public void modifyIsBooked(String email, Long roomId, Long bookInfoId, Boolean isBooked) {

		User user = userReadService.findUserByEmailOrElseThrow(email);
		Room room = roomReadService.findRoomByIdOrElseThrow(roomId);
		roomParticipantReadService.checkParticipant(user, room);
		validateRoomRole(user, room);

		BookInfo bookInfo = boardReadService.findBookInfoByIdOrElseThrow(bookInfoId);
		bookInfo.updateIsBook(!isBooked);
	}

	private void validateRoomRole(User user, Room room) {
		boolean isExist = participantRoleRepository.existsByUserAndRoomAndRoomRoleIn(user, room,
			Arrays.asList(RoomRole.ADMIN, RoomRole.SCHEDULE));
		if (!isExist)
			throw new UserHaveNotPrivilegeException();
	}

	public List<BookInfoResponseDTO> getBookInfo(String email, Long roomId, LocalDate date) {

		User user = userReadService.findUserByEmailOrElseThrow(email);
		Room room = roomReadService.findRoomByIdOrElseThrow(roomId);
		roomParticipantReadService.checkParticipant(user, room);

		validateDate(room.getTravelStartDate(), room.getTravelEndDate(), date);

		return getBookInfoResult(
			boardRepository.findBoardByRoomIdAndScheduleDate(roomId, date.atStartOfDay(), date.atTime(LocalTime.MAX)));
	}

	private List<BookInfoResponseDTO> getBookInfoResult(List<Board> boardList) {
		return boardList.stream()
			.map(board -> BookInfoResponseDTO.of(board, board.getScheduleInfo(), board.getAccountingInfo(),
				board.getAccountingInfo().getBookInfo()))
			.collect(Collectors.toList());
	}

	public void addSchedule(String email, Long roomId, BoardRequestDTO boardRequestDTO) {

		User user = userReadService.findUserByEmailOrElseThrow(email);
		Room room = roomReadService.findRoomByIdOrElseThrow(roomId);
		roomParticipantReadService.checkParticipant(user, room);
		validateDate(room.getTravelStartDate(), room.getTravelEndDate(),
			boardRequestDTO.getScheduleDate().toLocalDate());

		Board board = boardRepository.save(Board.of(room, boardRequestDTO));
		scheduleInfoRepository.save(ScheduleInfo.of(board, boardRequestDTO));

		if (boardRequestDTO.getIsBookRequired()) {
			BookInfo bookInfo = bookInfoRepository.save(BookInfo.builder()
				.isBooked(false)
				.build());
			accountingInfoRepository.save(AccountingInfo.builder()
				.board(board)
				.bookInfo(bookInfo)
				.category(boardRequestDTO.getCategory())
				.paymentName(boardRequestDTO.getPlaceName())
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
