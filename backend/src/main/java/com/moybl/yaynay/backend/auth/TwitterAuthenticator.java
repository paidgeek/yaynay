package com.moybl.yaynay.backend.auth;

import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Authenticator;

import javax.servlet.http.HttpServletRequest;

public class TwitterAuthenticator implements Authenticator {

	@Override
	public User authenticate(HttpServletRequest httpServletRequest) {
		return null;
	}

}
