package com.moybl.yaynay.backend.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Authenticator;

import com.moybl.yaynay.backend.Constants;
import com.moybl.yaynay.backend.model.Asker;

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
				String name = (String) payload.get("name");
				String picture = (String) payload.get("picture");

				if (id == null || email == null) {
					return null;
				}

				Asker asker = new Asker();
				asker.setGoogleId(id);
				asker.setEmail(email);
				asker.setDisplayName(name);
				asker.setPicture(picture);

				return new AskerUser(asker);
			} catch (Exception ignored) {
			}
		}

		return null;
	}

}
