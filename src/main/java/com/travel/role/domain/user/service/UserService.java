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
import com.travel.role.global.s3.S3Service;

import lombok.RequiredArgsConstructor;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final S3Service s3Service;

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

	public UserProfileDetailResDTO modifyUserProfile(String email, UserProfileModifyReqDTO reqDTO) {

		User findUser = findUserByEmailOrElseThrow(email);

		String name = reqDTO.getName();
		LocalDate birth = reqDTO.getBirth();
		findUser.update(name, birth);

		return UserProfileDetailResDTO.fromUser(findUser);
	}

	@Transactional(readOnly = true)
	public String getPreSignedUrlForProfileImage(String email) {

		User findUser = findUserByEmailOrElseThrow(email);

		return s3Service.getPreSignedUrl(S3Service.USER_PROFILE_IMAGE_PATH, findUser.getId().toString());
	}

	public void modifyProfileImageUrl(String email) {

		User findUser = findUserByEmailOrElseThrow(email);

		String key = S3Service.USER_PROFILE_IMAGE_PATH + findUser.getId();
		s3Service.checkObjectExistsOrElseThrow(key, "회원 프로필");
		String profileImageUrl = s3Service.getObjectUrl(key);

		findUser.updateProfileImageUrl(profileImageUrl);
	}

	private User findUserByEmailOrElseThrow(String email) {

		return userRepository.findByEmail(email)
			.orElseThrow(() -> new UserInfoNotFoundException(
				USERNAME_NOT_FOUND));
	}
}
