package com.moybl.yaynay.backend;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import android.content.Context;
import android.os.AsyncTask;

import com.moybl.yaynay.R;
import com.moybl.yaynay.backend.yaynayService.YaynayService;
import com.moybl.yaynay.backend.yaynayService.model.Asker;
import com.moybl.yaynay.backend.yaynayService.model.Question;

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

	private YaynayService yaynayService;
	private Context mContext;
	private String mIdToken;
	private Asker mAsker;

	private YayNayClient() {
	}

	public void signIn(final YayNayResultCallback<ObjectResult<Asker>> callback) {
		doServiceCall(new ServiceCall<ObjectResult<Asker>>() {
			@Override
			public ObjectResult<Asker> procedure() {
				try {
					Asker asker = yaynayService.signIn()
							.execute();

					return new ObjectResult<>(asker);
				} catch (IOException e) {
					e.printStackTrace();
				}

				return new ObjectResult<>();
			}

			@Override
			public void finished(ObjectResult<Asker> result) {
				mAsker = result.getObject();
				callback.onResult(result);
			}
		});
	}

	public void postQuestion(final String question, final YayNayResultCallback<VoidResult> callback) {
		doServiceCall(new ServiceCall<VoidResult>() {
			@Override
			public VoidResult procedure() {
				try {
					yaynayService.questions()
							.post(question)
							.execute();

					return new VoidResult(true);
				} catch (IOException e) {
					e.printStackTrace();
				}

				return new VoidResult(false);
			}

			@Override
			public void finished(VoidResult result) {
				callback.onResult(result);
			}
		});
	}

	public void asker(final YayNayResultCallback<ObjectResult<List<Question>>> callback) {
		asker(null, callback);
	}

	public void asker(final Long askerId, final YayNayResultCallback<ObjectResult<List<Question>>> callback) {
		doServiceCall(new ServiceCall<ObjectResult<List<Question>>>() {
			@Override
			public ObjectResult<List<Question>> procedure() {
				try {
					List<Question> questions = yaynayService.questions()
							.asker()
							.setAskerId(askerId)
							.execute()
							.getItems();

					return new ObjectResult<>(questions);
				} catch (IOException e) {
					e.printStackTrace();
				}

				return new ObjectResult<>();
			}

			@Override
			public void finished(ObjectResult<List<Question>> result) {
				callback.onResult(result);
			}
		});
	}

	public void feed(final YayNayResultCallback<ObjectResult<List<Question>>> callback) {
		doServiceCall(new ServiceCall<ObjectResult<List<Question>>>() {
			@Override
			public ObjectResult<List<Question>> procedure() {
				try {
					List<Question> questions = yaynayService.questions()
							.feed()
							.execute()
							.getItems();

					return new ObjectResult<>(questions);
				} catch (IOException e) {
					e.printStackTrace();
				}

				return new ObjectResult<>();
			}

			@Override
			public void finished(ObjectResult<List<Question>> result) {
				callback.onResult(result);
			}
		});
	}

	public void postAnswer(final long questionId, final boolean state, final YayNayResultCallback<VoidResult> callback) {
		doServiceCall(new ServiceCall<VoidResult>() {
			@Override
			public VoidResult procedure() {
				try {
					yaynayService.answers()
							.post(questionId, state)
							.execute();

					return new VoidResult(true);
				} catch (IOException e) {
					e.printStackTrace();
				}

				return new VoidResult(false);
			}

			@Override
			public void finished(VoidResult result) {
				callback.onResult(result);
			}
		});
	}

	private void setUpService() {
		if (yaynayService == null) {
			Credential credential = new Credential(BearerToken.authorizationHeaderAccessMethod())
					.setFromTokenResponse(new TokenResponse().setAccessToken(mIdToken));

			yaynayService = new YaynayService.Builder(AndroidHttp.newCompatibleTransport(),
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

	private interface ServiceCall<T> {
		T procedure();

		void finished(T result);
	}

	private <T> void doServiceCall(final ServiceCall<T> serviceCall) {
		new AsyncTask<Void, Void, T>() {
			@Override
			protected T doInBackground(Void... params) {
				setUpService();
				return serviceCall.procedure();
			}

			@Override
			protected void onPostExecute(T result) {
				serviceCall.finished(result);
			}
		}.execute();
	}

}
