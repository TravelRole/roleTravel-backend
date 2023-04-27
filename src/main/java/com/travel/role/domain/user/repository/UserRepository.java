package com.travel.role.domain.user.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.travel.role.domain.user.entity.Provider;
import com.travel.role.domain.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
	Optional<User> findByRefreshToken(String refreshToken);
	boolean existsByEmail(String email);
	Optional<User> findByProviderAndProviderId(Provider provider, String providerId);
	Optional<User> findByNameAndBirth(String name, LocalDate birth);
	Optional<User> findByNameAndBirthAndEmail(String name, LocalDate birth, String email);
}
