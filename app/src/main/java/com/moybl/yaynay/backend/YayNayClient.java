package com.moybl.yaynay.backend;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.http.HttpHeaders;

import android.content.Context;
import android.os.AsyncTask;

import com.moybl.yaynay.R;
import com.moybl.yaynay.backend.yayNay.YayNay;
import com.moybl.yaynay.backend.yayNay.model.Asker;
import com.moybl.yaynay.backend.yayNay.model.Question;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class YayNayClient {

	private static YayNayClient instance;

	public synchronized static YayNayClient getInstance() {
		if (instance == null) {
			instance = new YayNayClient();
		}

		return instance;
	}

	private YayNay mYayNay;
	private Context mContext;
	private String mIdToken;
	private Asker mAsker;

	public void insertGoogle(final YayNayResultCallback<ObjectResult<Asker>> callback) {
		doServiceCall(new ServiceCall<ObjectResult<Asker>>() {
			@Override
			public ObjectResult<Asker> procedure() {
				try {
					Credential credential = new Credential(BearerToken.authorizationHeaderAccessMethod())
							.setFromTokenResponse(new TokenResponse().setAccessToken(mIdToken));

					YayNay yayNay = new YayNay.Builder(AndroidHttp.newCompatibleTransport(),
							new AndroidJsonFactory(), credential)
							.setApplicationName(mContext.getPackageName())
							.setRootUrl(mContext.getString(R.string.api_url))
							.build();

					Asker asker = yayNay.askers()
							.insertGoogle()
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
				createAuthenticatedClient();
				callback.onResult(result);
			}
		});
	}

	public void insertQuestion(final String question, final YayNayResultCallback<VoidResult> callback) {
		doServiceCall(new ServiceCall<VoidResult>() {
			@Override
			public VoidResult procedure() {
				try {
					mYayNay.questions()
							.insert(question)
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

	public void listByAsker(final YayNayResultCallback<ObjectResult<List<Question>>> callback) {
		listByAsker(null, callback);
	}

	public void listByAsker(final Long askerId, final YayNayResultCallback<ObjectResult<List<Question>>> callback) {
		doServiceCall(new ServiceCall<ObjectResult<List<Question>>>() {
			@Override
			public ObjectResult<List<Question>> procedure() {
				try {
					List<Question> questions = mYayNay.questions()
							.listByAsker()
							.setAskerId(askerId)
							.execute()
							.getItems();

					if (questions == null) {
						questions = Collections.emptyList();
					}

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
					List<Question> questions = mYayNay.questions()
							.feed()
							.execute()
							.getItems();

					if (questions == null) {
						questions = Collections.emptyList();
					}

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

	public void insertAnswer(final long questionId, final boolean state, final YayNayResultCallback<VoidResult> callback) {
		doServiceCall(new ServiceCall<VoidResult>() {
			@Override
			public VoidResult procedure() {
				try {
					mYayNay.answers()
							.insert(questionId, state)
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

	private void createAuthenticatedClient() {
		if (mAsker == null) {
			return;
		}

		mYayNay = new YayNay.Builder(AndroidHttp.newCompatibleTransport(),
				new AndroidJsonFactory(), null)
				.setApplicationName(mContext.getPackageName())
				.setRootUrl(mContext.getString(R.string.api_url))
				.setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
					@Override
					public void initialize(AbstractGoogleClientRequest<?> request) throws IOException {
						HttpHeaders headers = request.getRequestHeaders();
						headers.set("X-ASKER-ID", mAsker.getId());
						headers.set("X-SESSION-TOKEN", mAsker.getSessionToken());
						request.setRequestHeaders(headers);
					}
				})
				.build();
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
				return serviceCall.procedure();
			}

			@Override
			protected void onPostExecute(T result) {
				serviceCall.finished(result);
			}
		}.execute();
	}

}
