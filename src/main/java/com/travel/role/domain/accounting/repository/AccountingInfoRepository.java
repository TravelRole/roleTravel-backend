package com.travel.role.domain.accounting.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.accounting.entity.AccountingInfo;
import com.travel.role.domain.accounting.repository.querydsl.AccountingInfoQuerydsl;

public interface AccountingInfoRepository extends JpaRepository<AccountingInfo, Long>, AccountingInfoQuerydsl {

	@Modifying
	@Transactional
	@Query("DELETE FROM AccountingInfo WHERE id = :ids")
	void deleteAllByIds(@Param("ids") List<Long> ids);
}
