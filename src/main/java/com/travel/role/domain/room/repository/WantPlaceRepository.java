package com.travel.role.domain.room.repository;

import com.travel.role.domain.room.entity.WantPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WantPlaceRepository extends JpaRepository<WantPlace, Long> {
    @Query("SELECT DISTINCT w " +
            "FROM WantPlace w " +
            "JOIN FETCH w.room r " +
            "WHERE r.id = :roomId ORDER BY w.createDate ASC")
    List<WantPlace> findByRoomIdWithRole(@Param("roomId") Long roomId);
}
