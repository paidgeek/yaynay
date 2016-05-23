package com.moybl.yaynay.backend.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Authenticator;

import com.moybl.yaynay.backend.Constants;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

public class GoogleAuthenticator implements Authenticator {

	private GoogleIdTokenVerifier verifier;

	public GoogleAuthenticator() {
		verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
				.setAudience(Collections.singletonList(Constants.ANDROID_AUDIENCE))
				.setIssuer("https://accounts.google.com")
				.build();
	}

	@Override
	public User authenticate(HttpServletRequest request) {
		String token = request.getHeader("Authorization");

		if (token != null) {
			try {
				GoogleIdToken idToken = verifier.verify(token.substring(token.indexOf(' ') + 1));
				GoogleIdToken.Payload payload = idToken.getPayload();

				String id = payload.getSubject();
				String email = payload.getEmail();

				if (id == null || email == null) {
					return null;
				}

				return new AuthUser(id, email);
			} catch (Exception ignored) {
			}
		}

		return null;
	}

}
