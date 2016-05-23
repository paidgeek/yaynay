package com.moybl.yaynay.backend.auth;

import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Authenticator;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import com.moybl.yaynay.backend.OfyService;
import com.moybl.yaynay.backend.model.Asker;

import javax.servlet.http.HttpServletRequest;

public class YayNayAuthenticator implements Authenticator {

	@Override
	public User authenticate(HttpServletRequest request) {
		String askerId = request.getHeader("X-ASKER-ID");
		String sessionToken = request.getHeader("X-SESSION-TOKEN");

		if (askerId == null || sessionToken == null) {
			return null;
		}

		Key askerKey = KeyFactory.createKey("Asker", Long.valueOf(askerId));
		Asker asker = OfyService.ofy()
				.load()
				.type(Asker.class)
				.filterKey(askerKey)
				.first()
				.now();

		if (asker == null || !sessionToken.equals(asker.getSessionToken())) {
			return null;
		}

		AuthUser user = new AuthUser(askerId);
		user.setAsker(asker);

		return user;
	}

}
