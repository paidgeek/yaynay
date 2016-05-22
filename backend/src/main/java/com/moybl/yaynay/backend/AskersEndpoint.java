package com.moybl.yaynay.backend;

import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;

import com.googlecode.objectify.Key;
import com.moybl.yaynay.backend.auth.GoogleAuthenticator;
import com.moybl.yaynay.backend.model.Asker;

import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;

public class AskersEndpoint extends YayNayEndpoint {

	private static final Logger log = Logger.getLogger(AskersEndpoint.class.getName());

	@ApiMethod(
			name = "askers.insertGoogle",
			httpMethod = ApiMethod.HttpMethod.POST
			//authenticators = GoogleAuthenticator.class
	)
	public Asker insertGoogle(User user) throws UnauthorizedException, IOException {
		if (user == null) {
			throw new UnauthorizedException("Unauthorized");
		}

		Asker asker = OfyService.ofy()
				.load()
				.type(Asker.class)
				.filter("googleId", user.getUserId())
				.first()
				.now();

		if (asker == null) {
			asker = new Asker();

			asker.setGoogleId(user.getUserId());
			asker.setEmail(user.getEmail());
		}

		asker.setSessionToken(Util.generateSessionToken());

		OfyService.ofy()
				.save()
				.entity(asker)
				.now();

		return asker;
	}

}
