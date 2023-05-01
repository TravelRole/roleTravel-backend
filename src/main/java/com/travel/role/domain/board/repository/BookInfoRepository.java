package com.travel.role.domain.board.repository;

import com.travel.role.domain.board.entity.BookInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookInfoRepository extends JpaRepository<BookInfo, Long> {
}
