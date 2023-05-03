package com.travel.role.global.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.travel.role.global.auth.entity.AuthInfo;

public interface AuthRepository extends JpaRepository<AuthInfo, Long> {

	@Query("SELECT a FROM AuthInfo a"
		+ " INNER JOIN FETCH a.user u"
		+ " WHERE u.email = :email")
	Optional<AuthInfo> findByEmail(@Param("email") String email);

	@Query("SELECT a, a.user FROM AuthInfo a"
		+ " LEFT JOIN FETCH a.user"
		+ " WHERE a.refreshToken = :refreshToken")
	Optional<AuthInfo> findUserByRefreshToken(@Param("refreshToken") String refreshToken);
}
