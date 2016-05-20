package com.moybl.yaynay.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import com.google.appengine.repackaged.com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.appengine.repackaged.com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.appengine.repackaged.com.google.api.client.http.javanet.NetHttpTransport;
import com.google.appengine.repackaged.com.google.api.client.json.jackson.JacksonFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.logging.Logger;

import javax.inject.Named;

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

	private GoogleIdTokenVerifier mTokenVerifier;

	public YayNayEndpoint() {
		mTokenVerifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
				.setAudience(Collections.singletonList(Constants.ANDROID_AUDIENCE))
				.setIssuer("https://accounts.google.com")
				.build();
	}

	@ApiMethod(
			name = "signIn",
			httpMethod = ApiMethod.HttpMethod.GET)
	public MyBean signIn(@Named("idToken") String idToken, User user)
			throws UnauthorizedException, IOException {
		try {
			GoogleIdToken token = mTokenVerifier.verify(idToken);

			if (token != null) {
				GoogleIdToken.Payload payload = token.getPayload();

				String userId = payload.getSubject();
				String email = payload.getEmail();

				MyBean myBean = new MyBean();

				myBean.setData(email + ", " + userId);

				return myBean;
			} else {
				throw new UnauthorizedException("Invalid id token");
			}
		} catch (GeneralSecurityException e) {
			throw new UnauthorizedException("Invalid id token");
		}
	}

}
