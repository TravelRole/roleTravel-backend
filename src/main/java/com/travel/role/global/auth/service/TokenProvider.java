package com.travel.role.global.auth.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.travel.role.global.auth.dto.TokenMapping;
import com.travel.role.global.auth.token.UserPrincipal;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenProvider {

	private final CustomUserDetailService customUserDetailService;

	private static final String SECRET_KEY = "secretsegseigjesilgjesigjesiljgesilgjeislfilesnilvsenilsenfklesnfiesseifnesilnesi21tgf8h3igh38o2ur59t23utg9ehjnwasiotu89023uqjrtfi3qgh0983y12ht923h90gh3qw2g923h9g230hng239gh";

	public TokenMapping createToken(Authentication authentication) {
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

		final Date expiredDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));

		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		SecretKey key = Keys.hmacShaKeyFor(keyBytes);

		String accessToken = Jwts.builder()
			.setIssuedAt(new Date())
			.setExpiration(expiredDate)
			.setIssuer("Travel-Role")
			.setSubject(Long.toString(userPrincipal.getId()))
			.signWith(key, SignatureAlgorithm.HS512).compact();

		String refreshToken = Jwts.builder()
			.setExpiration(expiredDate)
			.signWith(key, SignatureAlgorithm.HS512).compact();

		return new TokenMapping(userPrincipal.getEmail(), accessToken, refreshToken);
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
			.parseClaimsJwt(token)
			.getBody();

		return Long.parseLong(claims.getSubject());
	}
}
