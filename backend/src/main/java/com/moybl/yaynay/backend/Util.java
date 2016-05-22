package com.moybl.yaynay.backend;

import com.google.api.client.util.Base64;

import java.security.SecureRandom;

public class Util {

	private static final int SESSION_TOKEN_BYTES = 180;
	private static final SecureRandom random = new SecureRandom();

	public static String generateSessionToken() {
		byte[] randomBytes = new byte[SESSION_TOKEN_BYTES];
		random.nextBytes(randomBytes);

		return Base64.encodeBase64URLSafeString(randomBytes);
	}

}
