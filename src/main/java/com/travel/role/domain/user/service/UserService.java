package com.travel.role.domain.user.service;

import static com.travel.role.global.exception.dto.ExceptionMessage.*;

import java.time.LocalDate;
import java.util.Objects;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.user.dto.UserPasswordModifyReqDTO;
import com.travel.role.domain.user.dto.UserProfileDetailResDTO;
import com.travel.role.domain.user.dto.UserProfileModifyReqDTO;
import com.travel.role.domain.user.dto.UserProfileResponseDTO;
import com.travel.role.domain.user.entity.User;
import com.travel.role.global.exception.user.InputValueNotMatchException;
import com.travel.role.global.s3.S3Service;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

	private final PasswordEncoder passwordEncoder;
	private final S3Service s3Service;
	private final UserReadService userReadService;

	@Transactional(readOnly = true)
	public UserProfileResponseDTO getBasicProfile(String email) {

		User findUser = userReadService.findUserByEmailOrElseThrow(email);

		return new UserProfileResponseDTO(findUser.getName(), findUser.getEmail(), findUser.getProfile());
	}

	@Transactional(readOnly = true)
	public UserProfileDetailResDTO getUserProfile(String email) {

		User findUser = userReadService.findUserByEmailOrElseThrow(email);

		return UserProfileDetailResDTO.fromUser(findUser);
	}

	public UserProfileDetailResDTO modifyUserProfile(String email, UserProfileModifyReqDTO reqDTO) {

		User findUser = userReadService.findUserByEmailOrElseThrow(email);

		String name = reqDTO.getName();
		LocalDate birth = reqDTO.getBirth();
		findUser.update(name, birth);

		return UserProfileDetailResDTO.fromUser(findUser);
	}

	public void modifyPassword(String email, UserPasswordModifyReqDTO reqDTO) {

		User findUser = userReadService.findUserByEmailOrElseThrow(email);

		String newPassword = reqDTO.getNewPassword();

		checkInputPasswordMatch(newPassword, reqDTO.getNewPasswordCheck());
		checkPassword(findUser.getPassword(), reqDTO.getPassword());

		findUser.updatePassword(passwordEncoder.encode(newPassword));
	}

	private void checkInputPasswordMatch(String password, String passwordCheck) {

		if (!Objects.equals(password, passwordCheck)) {
			throw new InputValueNotMatchException("비밀번호", "비밀번호 확인");
		}
	}

	private void checkPassword(String encodedPassword, String inputPassword) {

		if (!passwordEncoder.matches(inputPassword, encodedPassword)) {
			throw new BadCredentialsException(INVALID_PASSWORD);
		}
	}

	@Transactional(readOnly = true)
	public String getPreSignedUrlForProfileImage(String email) {

		User findUser = userReadService.findUserByEmailOrElseThrow(email);

		return s3Service.getPreSignedUrl(S3Service.USER_PROFILE_IMAGE_PATH, findUser.getId().toString());
	}

	public void modifyProfileImageUrl(String email) {

		User findUser = userReadService.findUserByEmailOrElseThrow(email);

		String key = S3Service.USER_PROFILE_IMAGE_PATH + findUser.getId();
		s3Service.checkObjectExistsOrElseThrow(key, "회원 프로필");
		String profileImageUrl = s3Service.getObjectUrl(key);

		findUser.updateProfileImageUrl(profileImageUrl);
	}
}
