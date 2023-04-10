package com.travel.role.global.util;

import java.security.SecureRandom;


public class PasswordGenerator {
	private static final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private static StringBuffer sb = new StringBuffer();

	public static String generateRandomPassword(int len) {
		SecureRandom random = new SecureRandom();

		for (int i = 0; i < len; i++) {
			int index = random.nextInt(chars.length());
			sb.append(chars.charAt(index));
		}

		return sb.toString();
	}
}
