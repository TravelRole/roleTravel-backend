package com.travel.role.domain.board.repository;

import com.travel.role.domain.board.entity.ScheduleBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleBoardRepository extends JpaRepository<ScheduleBoard, Long> {
}
