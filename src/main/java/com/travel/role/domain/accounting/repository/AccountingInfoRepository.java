package com.travel.role.domain.accounting.repository;

import com.travel.role.domain.accounting.entity.AccountingInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountingInfoRepository extends JpaRepository<AccountingInfo, Long> {
}
