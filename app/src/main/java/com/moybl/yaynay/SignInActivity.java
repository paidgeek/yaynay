package com.moybl.yaynay;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.tasks.OnSuccessListener;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.moybl.yaynay.backend.ObjectResult;
import com.moybl.yaynay.backend.YayNayClient;
import com.moybl.yaynay.backend.YayNayResultCallback;

public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

	private static final String TAG = "SignInActivity";
	private static final int RC_SIGN_IN = 9001;

	private GoogleApiClient mGoogleApiClient;
	private YayNayClient mYayNayClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);

		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestEmail()
				.requestIdToken(getString(R.string.web_client_id))
				.build();
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.enableAutoManage(this, this)
				.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
				.build();

		SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
		signInButton.setSize(SignInButton.SIZE_STANDARD);
		signInButton.setScopes(gso.getScopeArray());

		OptionalPendingResult<GoogleSignInResult> pendingResult =
				Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
		if (pendingResult.isDone()) {
			// There's immediate result available.
			updateButtonsAndStatusFromSignInResult(pendingResult.get());
		} else {
			// There's no immediate result ready, displays some progress indicator and waits for the
			// async callback.
			//showProgressIndicator();
			pendingResult.setResultCallback(new ResultCallback<GoogleSignInResult>() {
				@Override
				public void onResult(@NonNull GoogleSignInResult result) {
					updateButtonsAndStatusFromSignInResult(result);
					//hideProgressIndicator();
				}
			});
		}
	}

	private void updateButtonsAndStatusFromSignInResult(GoogleSignInResult result) {
		if (result.isSuccess()) {
			handleSuccessfulSignIn(result);
		} else {
			findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					signIn();
				}
			});
		}
	}

	private void signIn() {
		Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
		startActivityForResult(signInIntent, RC_SIGN_IN);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RC_SIGN_IN) {
			GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

			Log.d(TAG, "handleSignInResult:" + result.isSuccess());

			if (result.isSuccess()) {
				handleSuccessfulSignIn(result);
			}
		}
	}

	private void handleSuccessfulSignIn(GoogleSignInResult result) {
		GoogleSignInAccount account = result.getSignInAccount();
		String idToken = account.getIdToken();

		mYayNayClient = new YayNayClient.Builder(this)
				.setIdToken(idToken)
				.build();

		mYayNayClient.signIn(new YayNayResultCallback<ObjectResult>() {
			@Override
			public void onResult(@NonNull ObjectResult result) {
				if (result.isSuccess()) {
					Log.d(TAG, result.getData()
							.toString());
				}
			}
		});

/*		YayNay.getInstance()
				.setGoogleAccount(result.getSignInAccount());

		Intent intent = new Intent(this, HomeActivity.class);

		startActivity(intent);*/
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
	}

}
