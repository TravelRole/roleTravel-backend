package com.travel.role.domain.room.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travel.role.domain.room.domain.ParticipantRole;

public interface ParticipantRoleRepository extends JpaRepository<ParticipantRole, Long> {
}
