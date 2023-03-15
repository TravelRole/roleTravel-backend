package com.travel.role.global.auth.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.user.dao.UserRepository;
import com.travel.role.domain.user.domain.UserEntity;
import com.travel.role.global.auth.token.UserPrincipal;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{

		UserEntity user = userRepository.findByEmail(email)
			.orElseThrow(()->
				new UsernameNotFoundException("유저 정보를 찾을 수 없습니다."));

		return UserPrincipal.create(user);
	}

	@Transactional
	public UserDetails loadUserById(Long id){
		Optional<UserEntity> user = userRepository.findById(id);
		if (user.isEmpty()) {
			throw new RuntimeException("해당하는 유저가 없습니다.");
		}

		return UserPrincipal.create(user.get());
	}
}
