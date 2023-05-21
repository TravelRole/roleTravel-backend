package com.travel.role.domain.book.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.book.entity.BookInfo;

public interface BookInfoRepository extends JpaRepository<BookInfo, Long> {

	@Modifying
	@Transactional
	@Query("DELETE FROM BookInfo WHERE id in :ids")
	void deleteAllByIds(@Param("ids") List<Long> ids);
}
