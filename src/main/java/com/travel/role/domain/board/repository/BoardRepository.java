package com.travel.role.domain.board.repository;

import com.travel.role.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query(value = "SELECT DISTINCT b, bi, si " +
            "FROM Board b " +
            "JOIN FETCH b.bookInfo bi " +
            "JOIN FETCH b.scheduleInfo si  " +
            "WHERE b.room.id = :roomId " +
            "AND b.scheduleDate BETWEEN :startOfDay AND :endOfDay")
    List<Board> findBoardByRoomIdAndScheduleDate
            (@Param("roomId") Long roomId, @Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);
}
