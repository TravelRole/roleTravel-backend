package com.travel.role.domain.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.travel.role.domain.user.domain.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
