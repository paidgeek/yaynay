package com.moybl.yaynay.backend;

import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

import com.moybl.yaynay.backend.auth.GoogleAuthenticator;
import com.moybl.yaynay.backend.model.Asker;

import java.io.IOException;

public class AskersEndpoint extends YayNayEndpoint {

	@ApiMethod(
			name = "askers.insertGoogle",
			httpMethod = ApiMethod.HttpMethod.POST,
			authenticators = GoogleAuthenticator.class
	)
	public Asker insertGoogle(User user) throws UnauthorizedException, IOException {
		if (user == null) {
			throw new UnauthorizedException("Unauthorized");
		}

		Asker asker = OfyService.ofy()
				.load()
				.type(Asker.class)
				.filter("googleId", user.getId())
				.first()
				.now();

		if (asker == null) {
			asker = new Asker();

			asker.setGoogleId(user.getId());
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
