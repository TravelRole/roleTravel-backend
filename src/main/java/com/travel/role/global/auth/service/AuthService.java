package com.travel.role.global.auth.service;

import static com.travel.role.global.exception.dto.ExceptionMessage.*;

import java.time.LocalDateTime;

import javax.mail.SendFailedException;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.user.dto.CheckIdRequest;
import com.travel.role.domain.user.dto.CheckIdResponse;
import com.travel.role.domain.user.dto.ConfirmUserRequestDTO;
import com.travel.role.domain.user.dto.ConfirmUserResponseDTO;
import com.travel.role.domain.user.dto.NewPasswordRequestDTO;
import com.travel.role.domain.user.dto.auth.LoginRequestDTO;
import com.travel.role.domain.user.dto.auth.SignUpRequestDTO;
import com.travel.role.domain.user.dto.auth.SignUpResponseDTO;
import com.travel.role.domain.user.entity.Provider;
import com.travel.role.domain.user.entity.User;
import com.travel.role.domain.user.repository.UserRepository;
import com.travel.role.domain.user.service.UserReadService;
import com.travel.role.global.auth.dto.AccessTokenRequestDTO;
import com.travel.role.global.auth.dto.TokenMapping;
import com.travel.role.global.auth.entity.AuthInfo;
import com.travel.role.global.auth.repository.AuthReadService;
import com.travel.role.global.auth.repository.AuthRepository;
import com.travel.role.global.auth.service.mail.MailService;
import com.travel.role.global.exception.auth.InvalidTokenException;
import com.travel.role.global.exception.auth.NotExistTokenException;
import com.travel.role.global.util.PasswordGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

	private final AuthenticationManager authenticationManager;
	private final TokenProvider tokenProvider;
	private final UserRepository userRepository;
	private final AuthRepository authRepository;
	private final AuthReadService authReadService;
	private final PasswordEncoder passwordEncoder;
	private final PasswordGenerator passwordGenerator;
	private final MailService mailService;
	private final UserReadService userReadService;

	private static final String SUCCESS_SIGN_UP = "회원가입에 성공하셨습니다";
	private static final String SUCCESS_MESSAGE = "성공하셨습니다.";
	private static final int MAX_PASSWORD_LENGTH = 16;

	@Transactional
	public SignUpResponseDTO signUp(SignUpRequestDTO signUpRequestDTO) {
		userReadService.validateUserExistByEmail(signUpRequestDTO.getEmail());

		User user = User.of(signUpRequestDTO,
			passwordEncoder.encode(signUpRequestDTO.getPassword()));
		user = userRepository.save(user);

		AuthInfo authInfo = AuthInfo.of(Provider.local, user);
		authRepository.save(authInfo);

		return new SignUpResponseDTO(user.getEmail(), user.getName(), SUCCESS_SIGN_UP, LocalDateTime.now());
	}

	@Transactional
	public TokenMapping login(LoginRequestDTO loginRequestDTO) {
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(
				loginRequestDTO.getEmail(),
				loginRequestDTO.getPassword()
			)
		);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		TokenMapping tokenMapping = tokenProvider.createToken(authentication);
		updateToken(tokenMapping);

		return tokenMapping;
	}

	@Transactional
	public void updateToken(TokenMapping tokenMapping) {
		AuthInfo authInfo = authReadService.findUserByEmailOrElseThrow(tokenMapping.getUserEmail());
		authInfo.updateRefreshToken(tokenMapping.getRefreshToken());
	}

	public AccessTokenRequestDTO refresh(final String refreshToken) {
		validateToken(refreshToken);

		AuthInfo authInfo = authReadService.findUserByRefreshTokenOrElseThrow(refreshToken);
		UsernamePasswordAuthenticationToken authentication = tokenProvider.getAuthenticationByEmail(
			authInfo.getUser().getEmail());

		return tokenProvider.refreshAccessToken(authentication);
	}

	@Transactional
	public void validateToken(final String refreshToken) {
		if (refreshToken == null) {
			throw new NotExistTokenException();
		}

		AuthInfo authInfo = authReadService.findUserByRefreshTokenOrElseThrow(refreshToken);

		Long refreshTokenExpirationTime = 0L;
		try {
			refreshTokenExpirationTime = tokenProvider.getTokenExpiration(refreshToken);
		} catch (Exception e) {
			authInfo.deleteRefreshToken();
			throw new InvalidTokenException(INVALID_TOKEN);
		}

		if (refreshTokenExpirationTime < 0) {
			authInfo.deleteRefreshToken();
			throw new InvalidTokenException(INVALID_TOKEN);
		}
	}

	public ConfirmUserResponseDTO findId(ConfirmUserRequestDTO dto) {
		User user = userReadService.findByNameAndBirthOrElseThrow(
			dto.getName(), dto.getBirth());

		return new ConfirmUserResponseDTO(SUCCESS_MESSAGE, HttpStatus.OK, user.getEmail(),
			user.getCreateDate().toLocalDate());
	}

	public CheckIdResponse confirmId(CheckIdRequest checkIdRequest) {
		boolean result = userReadService.existsByEmail(checkIdRequest.getEmail());
		return new CheckIdResponse(result);
	}

	@Transactional
	public void changePassword(NewPasswordRequestDTO dto) throws SendFailedException {
		User user = userReadService.findUserByNameAndBirthAndEmailOrElseThrow(dto.getName(), dto.getBirth(), dto.getEmail());

		String randomPassword = passwordGenerator.generateRandomPassword(MAX_PASSWORD_LENGTH);
		user.updatePassword(passwordEncoder.encode(randomPassword));

		mailService.sendPasswordMail(randomPassword, user.getEmail());
	}
}
