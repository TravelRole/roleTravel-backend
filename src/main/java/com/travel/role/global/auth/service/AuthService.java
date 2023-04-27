package com.travel.role.global.auth.service;

import static com.travel.role.global.exception.dto.ExceptionMessage.*;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.mail.SendFailedException;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.user.repository.UserRepository;
import com.travel.role.domain.user.entity.User;
import com.travel.role.domain.user.dto.CheckIdRequest;
import com.travel.role.domain.user.dto.CheckIdResponse;
import com.travel.role.domain.user.dto.ConfirmUserRequestDTO;
import com.travel.role.domain.user.dto.ConfirmUserResponseDTO;
import com.travel.role.domain.user.dto.NewPasswordRequestDTO;
import com.travel.role.domain.user.dto.auth.SignUpResponseDTO;
import com.travel.role.global.exception.user.UserInfoNotFoundException;
import com.travel.role.global.auth.dto.TokenResponse;
import com.travel.role.domain.user.dto.auth.LoginRequestDTO;
import com.travel.role.domain.user.dto.auth.SignUpRequestDTO;
import com.travel.role.global.auth.dto.TokenMapping;
import com.travel.role.global.auth.exception.InvalidTokenException;
import com.travel.role.global.auth.exception.NotExistTokenException;
import com.travel.role.global.auth.service.mail.MailService;
import com.travel.role.global.exception.user.AlreadyExistUserException;
import com.travel.role.global.util.PasswordGenerator;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

	private final AuthenticationManager authenticationManager;
	private final TokenProvider tokenProvider;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final PasswordGenerator passwordGenerator;
	private final MailService mailService;

	private static final String SUCCESS_SIGN_UP = "회원가입에 성공하셨습니다";
	private static final String SUCCESS_MESSAGE = "성공하셨습니다.";

	@Transactional
	public SignUpResponseDTO signUp(SignUpRequestDTO signUpRequestDTO) {
		if (userRepository.existsByEmail(signUpRequestDTO.getEmail())) {
			throw new AlreadyExistUserException();
		}

		User newUser = User.of(signUpRequestDTO,
			passwordEncoder.encode(signUpRequestDTO.getPassword()));
		userRepository.save(newUser);

		return new SignUpResponseDTO(newUser.getEmail(), newUser.getName(), SUCCESS_SIGN_UP, LocalDateTime.now());
	}

	@Transactional
	public TokenMapping signIn(LoginRequestDTO loginRequestDTO) {
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
		User findUser = userRepository.findByEmail(tokenMapping.getUserEmail()).orElseThrow(
			() -> new UsernameNotFoundException(USERNAME_NOT_FOUND)
		);
		findUser.updateRefreshToken(tokenMapping.getRefreshToken());
	}

	public TokenResponse refresh(final String refreshToken, String accessToken) {
		validateToken(refreshToken, accessToken);

		User findUser = userRepository.findByRefreshToken(refreshToken)
			.orElseThrow(() -> new UsernameNotFoundException(USERNAME_NOT_FOUND));
		UsernamePasswordAuthenticationToken authentication = tokenProvider.getAuthenticationByEmail(
			findUser.getEmail());

		return tokenProvider.refreshAccessToken(authentication);
	}

	@Transactional
	public void validateToken(final String refreshToken, String accessToken) {
		if (refreshToken == null || accessToken == null) {
			throw new NotExistTokenException();
		}

		Optional<User> findUser = userRepository.findByRefreshToken(refreshToken);

		if (findUser.isEmpty()) {
			throw new InvalidTokenException(INVALID_TOKEN);
		}

		Long refreshTokenExpirationTime = 0L;
		try {
			refreshTokenExpirationTime = tokenProvider.getTokenExpiration(refreshToken);
		} catch (Exception e) {
			findUser.get().deleteRefreshToken();
			throw new InvalidTokenException(INVALID_TOKEN);
		}

		if (refreshTokenExpirationTime < 0) {
			findUser.get().deleteRefreshToken();
			throw new InvalidTokenException(INVALID_TOKEN);
		}

		try {
			tokenProvider.getTokenExpiration(accessToken);
		} catch (ExpiredJwtException ignored) {
		} catch (Exception e) {
			findUser.get().deleteRefreshToken();
			throw new InvalidTokenException(e.getMessage());
		}
	}

	@Transactional(readOnly = true)
	public ConfirmUserResponseDTO findId(ConfirmUserRequestDTO confirmUserRequestDTO) {
		User user = userRepository.findByNameAndBirth(confirmUserRequestDTO.getName(),
				confirmUserRequestDTO.getBirth())
			.orElseThrow(UserInfoNotFoundException::new);

		return new ConfirmUserResponseDTO(SUCCESS_MESSAGE, HttpStatus.OK, user.getEmail(),
			user.getCreateDate().toLocalDate());
	}

	public CheckIdResponse confirmId(CheckIdRequest checkIdRequest) {
		boolean result = userRepository.existsByEmail(checkIdRequest.getEmail());
		return new CheckIdResponse(result);
	}

	@Transactional
	public void changePassword(NewPasswordRequestDTO newPasswordRequestDTO) throws SendFailedException {
		User user = checkValidateUser(newPasswordRequestDTO);

		String randomPassword = passwordGenerator.generateRandomPassword(16);
		user.updatePassword(passwordEncoder.encode(randomPassword));

		mailService.sendPasswordMail(randomPassword, user.getEmail());
	}

	private User checkValidateUser(NewPasswordRequestDTO dto) {
		return userRepository.findByNameAndBirthAndEmail(dto.getName(), dto.getBirth(), dto.getEmail())
			.orElseThrow(UserInfoNotFoundException::new);
	}
}
