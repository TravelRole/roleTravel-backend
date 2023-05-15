package com.travel.role.domain.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travel.role.domain.schedule.entity.ScheduleInfo;

public interface ScheduleInfoRepository extends JpaRepository<ScheduleInfo, Long> {
}
