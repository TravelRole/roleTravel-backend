package com.travel.role.global.auth.service;

import static com.travel.role.global.exception.dto.ExceptionMessage.*;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.travel.role.domain.user.dto.auth.AccessTokenResponseDTO;
import com.travel.role.global.auth.dto.TokenMapping;
import com.travel.role.global.auth.token.UserPrincipal;
import com.travel.role.global.exception.auth.InvalidTokenException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenProvider {

	private final CustomUserDetailService customUserDetailService;

	private static Long accessTokenExpiration;

	private static Long refreshTokenExpiration;

	private static String secretKey;

	@Value("${accessTokenExpireTime}")
	public void setAccessTokenExpiration(Long accessTokenExpiration) {
		this.accessTokenExpiration = accessTokenExpiration;
	}

	@Value("${refreshTokenExpireTime}")
	public void setRefreshTokenExpiration(Long refreshTokenExpiration) {
		this.refreshTokenExpiration = refreshTokenExpiration;
	}

	@Value("${secretKey}")
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public TokenMapping createToken(Authentication authentication) {
		UserPrincipal userPrincipal = (UserPrincipal)authentication.getPrincipal();

		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		SecretKey key = Keys.hmacShaKeyFor(keyBytes);

		String accessToken = createAccessToken(userPrincipal, key);

		String refreshToken = createRefreshToken(key);

		return new TokenMapping(userPrincipal.getEmail(), accessToken, refreshToken);
	}

	public AccessTokenResponseDTO refreshAccessToken(Authentication authentication) {
		UserPrincipal userPrincipal = (UserPrincipal)authentication.getPrincipal();

		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		SecretKey key = Keys.hmacShaKeyFor(keyBytes);

		String accessToken = createAccessToken(userPrincipal, key);

		return new AccessTokenResponseDTO(accessToken);
	}

	private static String createAccessToken(UserPrincipal userPrincipal, SecretKey key) {
		Date now = new Date();
		return Jwts.builder()
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + accessTokenExpiration))
			.setIssuer("Travel-Role")
			.setSubject(Long.toString(userPrincipal.getId()))
			.signWith(key, SignatureAlgorithm.HS512).compact();
	}

	private static String createRefreshToken(SecretKey key) {
		Date now = new Date();
		return Jwts.builder()
			.setExpiration(new Date(now.getTime() + refreshTokenExpiration))
			.signWith(key, SignatureAlgorithm.HS512).compact();
	}

	public UsernamePasswordAuthenticationToken getAuthenticationById(String token) {
		Long userId = getUserIdFromToken(token);
		UserDetails userDetails = customUserDetailService.loadUserById(userId);
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}

	public Long getUserIdFromToken(String token) {
		Claims claims = Jwts.parserBuilder()
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(token)
			.getBody();

		return Long.parseLong(claims.getSubject());
	}

	public void validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
		} catch (Exception e) {
			log.info("JWT 토큰이 잘못되었습니다 : {}", e.getMessage());
			throw new InvalidTokenException(INVALID_TOKEN);
		}
	}

	public Long getTokenExpiration(String token) {
		Date expiration = Jwts.parserBuilder()
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(token)
			.getBody()
			.getExpiration();

		return (expiration.getTime() - new Date().getTime());
	}

	public UsernamePasswordAuthenticationToken getAuthenticationByEmail(String email) {
		UserDetails userDetails = customUserDetailService.loadUserByUsername(email);
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}
}
