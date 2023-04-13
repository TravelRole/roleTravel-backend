package com.travel.role.domain.user.service;

import static com.travel.role.global.exception.ExceptionMessage.*;

import java.time.LocalDate;

import com.travel.role.domain.user.dto.UserProfileDetailResDTO;
import com.travel.role.domain.user.dto.UserProfileModifyReqDTO;
import com.travel.role.domain.user.exception.UserInfoNotFoundException;

import org.springframework.stereotype.Service;

import com.travel.role.domain.user.dao.UserRepository;
import com.travel.role.domain.user.domain.User;
import com.travel.role.domain.user.dto.UserProfileResponseDTO;

import lombok.RequiredArgsConstructor;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	@Transactional(readOnly = true)
	public UserProfileResponseDTO getBasicProfile(String email) {

		User findUser = findUserByEmailOrElseThrow(email);

		return new UserProfileResponseDTO(findUser.getName(), findUser.getEmail(), findUser.getProfile());
	}

	@Transactional(readOnly = true)
	public UserProfileDetailResDTO getUserProfile(String email) {

		User findUser = findUserByEmailOrElseThrow(email);

		return UserProfileDetailResDTO.fromUser(findUser);
	}

	public void modifyUserProfile(String email, UserProfileModifyReqDTO reqDTO){

		User findUser = findUserByEmailOrElseThrow(email);

		String name = reqDTO.getName();
		LocalDate birth = reqDTO.getBirth();
		findUser.update(name, birth);
	}

	private User findUserByEmailOrElseThrow(String email) {

		return userRepository.findByEmail(email)
			.orElseThrow(() -> new UserInfoNotFoundException(
				USERNAME_NOT_FOUND));
	}
}
