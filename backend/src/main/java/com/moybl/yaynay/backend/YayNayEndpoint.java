package com.moybl.yaynay.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiNamespace;

@Api(
		name = "yaynay",
		version = "v1",
		namespace = @ApiNamespace(
				ownerDomain = "backend.yaynay.moybl.com",
				ownerName = "backend.yaynay.moybl.com",
				packagePath = ""
		),
		scopes = {Constants.EMAIL_SCOPE}
		//clientIds = {Constants.WEB_CLIENT_ID, Constants.ANDROID_CLIENT_ID}
		//audiences = {Constants.ANDROID_AUDIENCE}
)
public abstract class YayNayEndpoint {
}
