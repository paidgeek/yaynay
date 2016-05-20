package com.moybl.yaynay;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import android.app.Application;

public class YayNay extends Application {

	private static YayNay instance;

	public static synchronized YayNay getInstance() {
		return instance;
	}

	private GoogleSignInAccount mGoogleAccount;

	@Override
	public void onCreate() {
		super.onCreate();

		instance = this;
	}

	public GoogleSignInAccount getGoogleAccount() {
		return mGoogleAccount;
	}

	public void setGoogleAccount(GoogleSignInAccount googleAccount) {
		this.mGoogleAccount = googleAccount;
	}

}
