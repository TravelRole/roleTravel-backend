package com.travel.role.global.util;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

@Component
public class PasswordGenerator {
	private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

	public String generateRandomPassword(int len) {
		SecureRandom random = new SecureRandom();

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < len; i++) {
			int index = random.nextInt(CHARS.length());
			sb.append(CHARS.charAt(index));
		}

		return sb.toString();
	}
}
