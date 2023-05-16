package com.travel.role.domain.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travel.role.domain.book.entity.BookInfo;

public interface BookInfoRepository extends JpaRepository<BookInfo, Long> {
}
