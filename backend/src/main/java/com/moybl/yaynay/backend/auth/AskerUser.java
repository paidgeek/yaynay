package com.moybl.yaynay.backend.auth;

import com.google.api.server.spi.auth.common.User;

import com.moybl.yaynay.backend.model.Asker;

public class AskerUser extends User {

	private Asker asker;

	public AskerUser(Asker asker) {
		super(null, null);

		this.asker = asker;
	}

	public Asker getAsker() {
		return asker;
	}

	public void setAsker(Asker asker) {
		this.asker = asker;
	}

}
