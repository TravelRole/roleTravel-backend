package com.travel.role.domain.user.service;

import static com.travel.role.global.exception.ExceptionMessage.*;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.travel.role.domain.user.dao.UserRepository;
import com.travel.role.domain.user.domain.User;
import com.travel.role.domain.user.dto.UserProfileResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;

	public UserProfileResponseDTO getBasicProfile(String email) {
		User findUser = userRepository.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException(
				USERNAME_NOT_FOUND));

		return new UserProfileResponseDTO(findUser.getName(), findUser.getEmail(), findUser.getProfile());
	}
}
