package com.moybl.yaynay.backend;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import android.content.Context;
import android.os.AsyncTask;

import com.moybl.yaynay.R;
import com.moybl.yaynay.backend.accountApi.AccountApi;
import com.moybl.yaynay.backend.accountApi.model.Asker;
import com.moybl.yaynay.backend.questionApi.QuestionApi;
import com.moybl.yaynay.backend.questionApi.model.Question;

import java.io.IOException;
import java.util.List;

public class YayNayClient {

	private static YayNayClient instance;

	public synchronized static YayNayClient getInstance() {
		if (instance == null) {
			instance = new YayNayClient();
		}

		return instance;
	}

	private AccountApi accountApi;
	private QuestionApi questionApi;
	private Context mContext;
	private String mIdToken;
	private Asker mAsker;

	private YayNayClient() {
	}

	public void signIn(final YayNayResultCallback<ObjectResult<Asker>> callback) {
		new AsyncTask<Void, Void, ObjectResult<Asker>>() {
			@Override
			protected ObjectResult<Asker> doInBackground(Void... params) {
				setUpService();

				try {
					Asker asker = accountApi.signIn()
							.execute();

					return new ObjectResult<>(asker);
				} catch (IOException e) {
					e.printStackTrace();
				}

				return new ObjectResult<>();
			}

			@Override
			protected void onPostExecute(ObjectResult<Asker> result) {
				mAsker = result.getObject();
				callback.onResult(result);
			}
		}.execute();
	}

	public void ask(String question, final YayNayResultCallback<VoidResult> callback) {
		new AsyncTask<String, Void, VoidResult>() {
			@Override
			protected VoidResult doInBackground(String... params) {
				setUpService();

				try {
					questionApi.ask(params[0])
							.execute();

					return new VoidResult(true);
				} catch (IOException e) {
					e.printStackTrace();
				}

				return new VoidResult(false);
			}

			@Override
			protected void onPostExecute(VoidResult result) {
				callback.onResult(result);
			}
		}.execute(question);
	}

	public void getMyQuestions(final YayNayResultCallback<ObjectResult<List<Question>>> callback) {
		new AsyncTask<Void, Void, ObjectResult<List<Question>>>() {
			@Override
			protected ObjectResult<List<Question>> doInBackground(Void... params) {
				setUpService();

				try {
					List<Question> questions = questionApi.getMyQuestions()
							.execute()
							.getItems();

					return new ObjectResult<>(questions);
				} catch (IOException e) {
					e.printStackTrace();
				}

				return new ObjectResult<>();
			}

			@Override
			protected void onPostExecute(ObjectResult<List<Question>> result) {
				callback.onResult(result);
			}
		}.execute();
	}

	private void setUpService() {
		if (accountApi == null) {
			Credential credential = new Credential(BearerToken.authorizationHeaderAccessMethod())
					.setFromTokenResponse(new TokenResponse().setAccessToken(mIdToken));

			accountApi = new AccountApi.Builder(AndroidHttp.newCompatibleTransport(),
					new AndroidJsonFactory(), credential)
					.setApplicationName(mContext.getPackageName())
					.setRootUrl(mContext.getString(R.string.api_url))
					.build();

			questionApi = new QuestionApi.Builder(AndroidHttp.newCompatibleTransport(),
					new AndroidJsonFactory(), credential)
					.setApplicationName(mContext.getPackageName())
					.setRootUrl(mContext.getString(R.string.api_url))
					.build();
		}
	}

	public Context getContext() {
		return mContext;
	}

	public void setContext(Context context) {
		this.mContext = context;
	}

	public String getIdToken() {
		return mIdToken;
	}

	public void setIdToken(String idToken) {
		this.mIdToken = idToken;
	}

	public Asker getAsker() {
		return mAsker;
	}

	public void setAsker(Asker asker) {
		this.mAsker = asker;
	}

}
