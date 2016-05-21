package com.moybl.yaynay.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;

import java.io.IOException;
import java.util.logging.Logger;

@Api(
		name = "yayNayApi",
		version = "v1",
		namespace = @ApiNamespace(
				ownerDomain = "backend.yaynay.moybl.com",
				ownerName = "backend.yaynay.moybl.com",
				packagePath = ""
		),
		scopes = {Constants.EMAIL_SCOPE},
		clientIds = {Constants.WEB_CLIENT_ID, Constants.ANDROID_CLIENT_ID},
		audiences = {Constants.ANDROID_AUDIENCE}
)
public class YayNayEndpoint {

	private static final Logger log = Logger.getLogger(YayNayEndpoint.class.getName());

	@ApiMethod(
			name = "signIn",
			httpMethod = ApiMethod.HttpMethod.GET,
			authenticators = {GoogleUserAuthenticator.class})
	public Asker signIn(User user)
			throws UnauthorizedException, IOException {
		if (user == null) {
			throw new UnauthorizedException("Unauthorized");
		}

		String googleId = user.getUserId();
		String email = user.getEmail();

		Asker asker = OfyService.ofy()
				.load()
				.type(Asker.class)
				.filter("googleId", googleId)
				.first()
				.now();

		if (asker == null) {
			asker = new Asker();

			asker.setEmail(email);
			asker.setGoogleId(googleId);

			OfyService.ofy()
					.save()
					.entity(asker)
					.now();
		}

		return asker;
	}

}
