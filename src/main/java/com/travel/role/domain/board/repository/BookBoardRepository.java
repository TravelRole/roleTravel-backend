package com.travel.role.domain.board.repository;

import com.travel.role.domain.board.entity.BookBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookBoardRepository extends JpaRepository<BookBoard, Long> {
}
