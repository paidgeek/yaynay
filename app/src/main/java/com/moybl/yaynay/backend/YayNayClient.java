package com.moybl.yaynay.backend;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.moybl.yaynay.R;
import com.moybl.yaynay.backend.yayNayApi.YayNayApi;
import com.moybl.yaynay.backend.yayNayApi.model.Asker;

import java.io.IOException;

public class YayNayClient {

	private static YayNayApi yayNayApiService;

	private Context mContext;
	private String mIdToken;

	private YayNayClient() {
	}

	public void signIn(final YayNayResultCallback<ObjectResult<Asker>> callback) {
		new AsyncTask<Void, Void, ObjectResult<Asker>>() {
			@Override
			protected ObjectResult<Asker> doInBackground(Void... params) {
				setUpService();

				try {
					Asker asker = yayNayApiService.signIn()
							.execute();

					return new ObjectResult<>(true, asker);
				} catch (IOException e) {
					e.printStackTrace();
				}

				return new ObjectResult<>(false, null);
			}

			@Override
			protected void onPostExecute(ObjectResult<Asker> result) {
				Log.d("TOKEN", mIdToken);

				callback.onResult(result);
			}
		}.execute();
	}

	private void setUpService() {
		if (yayNayApiService == null) {
			/*
			String audience = "server:client_id:" + mContext.getString(R.string.web_client_id);
			GoogleAccountCredential credential = GoogleAccountCredential.usingAudience(mContext, audience);
			credential.setSelectedAccountName("tin.rabzelj@gmail.com");
			*/

			Credential credential = new Credential(BearerToken.authorizationHeaderAccessMethod()).setFromTokenResponse(new TokenResponse().setAccessToken(mIdToken));

			YayNayApi.Builder builder = new YayNayApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), credential)
					.setApplicationName(mContext.getPackageName())
					.setRootUrl(mContext.getString(R.string.api_url));

			yayNayApiService = builder.build();
		}
	}

	public static class Builder {

		private YayNayClient mYayNayClient;
		private Context mContext;

		public Builder(Context context) {
			mYayNayClient = new YayNayClient();
			mYayNayClient.mContext = context;
		}

		public Builder setIdToken(String idToken) {
			mYayNayClient.mIdToken = idToken;

			return this;
		}

		public YayNayClient build() {
			return mYayNayClient;
		}

	}

}
