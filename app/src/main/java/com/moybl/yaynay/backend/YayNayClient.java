package com.moybl.yaynay.backend;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.moybl.yaynay.R;
import com.moybl.yaynay.backend.yayNayApi.YayNayApi;

import java.io.IOException;

public class YayNayClient {

	private static YayNayApi yayNayApiService;

	private Context mContext;
	private String mIdToken;

	private YayNayClient() {
	}

	public void signIn(final YayNayResultCallback<ObjectResult> callback) {
		new AsyncTask<Void, Void, ObjectResult>() {
			@Override
			protected ObjectResult doInBackground(Void... params) {
				setUpService();

				try {
					String data = yayNayApiService.signIn(mIdToken)
							.execute()
							.getData();

					return new ObjectResult(true, data);
				} catch (IOException e) {
					e.printStackTrace();
				}

				return new ObjectResult(false, null);
			}

			@Override
			protected void onPostExecute(ObjectResult result) {
				Log.d("TOKEN", mIdToken);

				callback.onResult(result);
			}
		}.execute();
	}

	private void setUpService() {
		if (yayNayApiService == null) {
			String audience = "server:client_id:" + mContext.getString(R.string.web_client_id);
			GoogleAccountCredential credential = GoogleAccountCredential.usingAudience(mContext, audience);
			credential.setSelectedAccountName("tin.rabzelj@gmail.com");

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
