package com.travel.role.global.auth.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.travel.role.global.auth.dto.AccessTokenResponse;
import com.travel.role.global.auth.dto.TokenMapping;
import com.travel.role.global.auth.token.UserPrincipal;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
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

	private static final Long ACCESS_TOKEN_EXPIRATION = 1000L * 30;
	private static final Long REFRESH_TOKEN_EXPIRATION = 1000L * 60 * 5;
	private static final String SECRET_KEY = "secretsegseigjesilgjesigjesiljgesilgjeislfilesnilvsenilsenfklesnfiesseifnesilnesi21tgf8h3igh38o2ur59t23utg9ehjnwasiotu89023uqjrtfi3qgh0983y12ht923h90gh3qw2g923h9g230hng239gh";

	public TokenMapping createToken(Authentication authentication) {
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		SecretKey key = Keys.hmacShaKeyFor(keyBytes);

		String accessToken = createAccessToken(userPrincipal, key);

		String refreshToken = createRefreshToken(key);

		return new TokenMapping(userPrincipal.getEmail(), accessToken, refreshToken);
	}

	public AccessTokenResponse refreshAccessToken(Authentication authentication) {
		UserPrincipal userPrincipal = (UserPrincipal)authentication.getPrincipal();

		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		SecretKey key = Keys.hmacShaKeyFor(keyBytes);

		String accessToken = createAccessToken(userPrincipal, key);

		return new AccessTokenResponse(accessToken);
	}

	private static String createAccessToken(UserPrincipal userPrincipal, SecretKey key) {
		Date now = new Date();
		return Jwts.builder()
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION))
			.setIssuer("Travel-Role")
			.setSubject(Long.toString(userPrincipal.getId()))
			.signWith(key, SignatureAlgorithm.HS512).compact();
	}

	private static String createRefreshToken(SecretKey key) {
		Date now = new Date();
		return Jwts.builder()
			.setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION))
			.signWith(key, SignatureAlgorithm.HS512).compact();
	}

	public UsernamePasswordAuthenticationToken getAuthenticationById(String token) {
		Long userId = getUserIdFromToken(token);
		UserDetails userDetails = customUserDetailService.loadUserById(userId);
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}

	public Long getUserIdFromToken(String token) {
		Claims claims = Jwts.parserBuilder()
			.setSigningKey(SECRET_KEY)
			.build()
			.parseClaimsJws(token)
			.getBody();

		return Long.parseLong(claims.getSubject());
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
			return true;
		} catch (ExpiredJwtException e) {
			log.info("validation 결과 Jwt 토큰이 잘못되었습니다 : {}", e.getMessage());
		} catch (Exception e) {
			log.info("JWT 토큰이 잘못되었습니다 : {}", e.getMessage());
		}
		return false;
	}

	public Long getTokenExpiration(String token) {
		Date expiration = Jwts.parserBuilder()
			.setSigningKey(SECRET_KEY)
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
