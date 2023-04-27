package com.travel.role.domain.room.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.room.entity.TravelEssentials;

public interface TravelEssentialRepository extends JpaRepository<TravelEssentials, Long>, TravelEssentialQuerydsl {

	@Modifying
	@Transactional
	@Query("DELETE FROM TravelEssentials te "
		+ "WHERE te.user.id = :userId AND te.room.id = :roomId AND te.id in :ids")
	void deleteByUserAndRoomAndSearchIds(@Param("userId") Long userId, @Param("roomId") Long roomId,
		@Param("ids") List<Long> ids);

	@Modifying
	@Transactional
	@Query("UPDATE TravelEssentials te SET te.isChecked = :check WHERE te.user.id = :userId AND te.room.id = :roomId AND te.id in :ids")
	void updateCheckEssentialsByUserAndRoomAndEssentialIds(@Param("userId") Long userId, @Param("roomId") Long roomId,
		@Param("ids") List<Long> ids, @Param("check") Boolean check);
}
