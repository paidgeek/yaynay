package com.moybl.yaynay;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;
import android.widget.Toast;

import com.moybl.yaynay.backend.myApi.MyApi;

import java.io.IOException;

public class EndpointAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {

	private static MyApi myApiService = null;
	private Context context;

	@Override
	protected String doInBackground(Pair<Context, String>... params) {
		context = params[0].first;
		String name = params[0].second;

		if (myApiService == null) {  // Only do this once
			MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
					.setRootUrl(context.getString(R.string.api_url));

			myApiService = builder.build();
		}

		try {
			return myApiService.sayHi(name).execute().getData();
		} catch (IOException e) {
			return e.getMessage();
		}
	}

	@Override
	protected void onPostExecute(String result) {
		Toast.makeText(context, result, Toast.LENGTH_LONG).show();
	}

}
