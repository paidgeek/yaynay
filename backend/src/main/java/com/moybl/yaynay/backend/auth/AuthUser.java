package com.moybl.yaynay.backend.auth;

import com.google.api.server.spi.auth.common.User;

import com.moybl.yaynay.backend.model.Asker;

public class AuthUser extends User {

	private String email;
	private Asker asker;

	public AuthUser(String id) {
		super(id);
	}

	public AuthUser(String id, String email) {
		super(id, email);

		this.email = email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String getEmail() {
		return email;
	}

	public Asker getAsker() {
		return asker;
	}

	public void setAsker(Asker asker) {
		this.asker = asker;
	}

}
