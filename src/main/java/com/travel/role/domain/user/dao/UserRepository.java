package com.travel.role.domain.user.dao;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.travel.role.domain.user.domain.Provider;
import com.travel.role.domain.user.domain.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
	Optional<UserEntity> findByEmail(String email);
	Optional<UserEntity> findByRefreshToken(String refreshToken);
	boolean existsByEmail(String email);
	Optional<UserEntity> findByProviderAndProviderId(Provider provider, String providerId);
	Optional<UserEntity> findByNameAndBirth(String name, LocalDate birth);
	Optional<UserEntity> findByNameAndBirthAndEmail(String name, LocalDate birth, String email);
}
