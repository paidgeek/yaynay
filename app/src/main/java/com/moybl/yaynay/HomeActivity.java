package com.moybl.yaynay;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.moybl.yaynay.backend.ObjectResult;
import com.moybl.yaynay.backend.VoidResult;
import com.moybl.yaynay.backend.YayNayClient;
import com.moybl.yaynay.backend.YayNayResultCallback;
import com.moybl.yaynay.backend.yaynayService.model.Question;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {

	private QuestionsAdapter mQuestionsAdapter;
	@BindView(R.id.questions_refresh)
	protected SwipeRefreshLayout mQuestionsRefresh;
	@BindView(R.id.questions_recycler)
	protected RecyclerView mQuestionRecycler;
	private YayNayClient mClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);
		ButterKnife.bind(this);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		mQuestionsRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				refreshQuestions();
			}
		});

		mQuestionRecycler.setLayoutManager(new LinearLayoutManager(this));
		mQuestionsAdapter = new QuestionsAdapter();
		mQuestionRecycler.setAdapter(mQuestionsAdapter);

		mClient = YayNayClient.getInstance();
	}

	@OnClick(R.id.home_fab)
	public void onFabClick() {
		AskDialog askDialog = new AskDialog();
		askDialog.setAskDialogListener(new AskDialog.AskDialogListener() {
			@Override
			public void onAsk(String question) {
				ask(question);
			}

			@Override
			public void onCancel() {
			}
		});
		askDialog.show(getFragmentManager(), "ask");
	}

	private void refreshQuestions() {
		mClient.asker(new YayNayResultCallback<ObjectResult<List<Question>>>() {
			@Override
			public void onResult(@NonNull ObjectResult<List<Question>> result) {
				if (result.isSuccess()) {
					List<Question> questions = mQuestionsAdapter.getQuestions();

					questions.clear();
					questions.addAll(result.getObject());

					mQuestionsAdapter.notifyDataSetChanged();
				}

				mQuestionsRefresh.setRefreshing(false);
			}
		});
	}

	private void ask(String question) {
		mClient.postQuestion(question, new YayNayResultCallback<VoidResult>() {
			@Override
			public void onResult(@NonNull VoidResult result) {
			}
		});
	}

}
