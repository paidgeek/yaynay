package com.moybl.yaynay;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.moybl.yaynay.backend.ObjectResult;
import com.moybl.yaynay.backend.YayNayClient;
import com.moybl.yaynay.backend.YayNayResultCallback;
import com.moybl.yaynay.backend.yayNay.model.Asker;

import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends AppCompatActivity {

	private static final Logger logger = Logger.getLogger(SignInActivity.class.getName());
	private static final int RC_GOOGLE_SIGN_IN = 9001;

	private GoogleApiClient mGoogleApiClient;
	@BindView(R.id.google_sign_in_button)
	protected SignInButton mSignInButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_in_activity);
		ButterKnife.bind(this);

		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestEmail()
				.requestIdToken(getString(R.string.web_client_id))
				.build();
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.enableAutoManage(this, null)
				.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
				.build();

		mSignInButton.setSize(SignInButton.SIZE_WIDE);
		mSignInButton.setScopes(gso.getScopeArray());

		silentSignIn();
	}

	private void silentSignIn() {
		OptionalPendingResult<GoogleSignInResult> pendingResult =
				Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
		if (pendingResult.isDone()) {
			GoogleSignInResult result = pendingResult.get();

			if (result.isSuccess()) {
				handleSuccessfulSignIn(result);
			}
		} else {
			final ProgressDialog progressDialog = ProgressDialog.show(this, null, getString(R.string.signing_in), true);

			pendingResult.setResultCallback(new ResultCallback<GoogleSignInResult>() {
				@Override
				public void onResult(@NonNull GoogleSignInResult result) {
					progressDialog.dismiss();

					if (result.isSuccess()) {
						handleSuccessfulSignIn(result);
					}
				}
			});
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RC_GOOGLE_SIGN_IN) {
			GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

			if (result.isSuccess()) {
				handleSuccessfulSignIn(result);
			}
		}
	}

	private void handleSuccessfulSignIn(GoogleSignInResult result) {
		GoogleSignInAccount account = result.getSignInAccount();
		String idToken = account.getIdToken();

		YayNayClient yayNayClient = YayNayClient.getInstance();
		yayNayClient.setContext(this);
		yayNayClient.setIdToken(idToken);

		Log.d("TOKEN", idToken);

		yayNayClient.insertGoogle(new YayNayResultCallback<ObjectResult<Asker>>() {
			@Override
			public void onResult(@NonNull ObjectResult<Asker> result) {
				if (result.isSuccess()) {
					Intent intent = new Intent(getApplicationContext(), HomeActivity.class);

					startActivity(intent);
				}
			}
		});
	}

	@OnClick(R.id.google_sign_in_button)
	public void onGoogleSignInClick() {
		Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
		startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
	}

}
