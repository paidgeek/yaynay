package com.moybl.yaynay;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.moybl.yaynay.backend.ObjectResult;
import com.moybl.yaynay.backend.VoidResult;
import com.moybl.yaynay.backend.YayNayClient;
import com.moybl.yaynay.backend.YayNayResultCallback;
import com.moybl.yaynay.backend.questionApi.model.Question;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

	private QuestionsAdapter mQuestionsAdapter;
	private SwipeRefreshLayout mQuestionsRefresh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		if (fab != null) {
			fab.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					showAskDialog();
				}
			});
		}

		mQuestionsRefresh = (SwipeRefreshLayout) findViewById(R.id.questions_refresh);
		mQuestionsRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				refreshQuestions();
			}
		});

		RecyclerView questionsRecycler = (RecyclerView) findViewById(R.id.questions_recycler);
		questionsRecycler.setLayoutManager(new LinearLayoutManager(this));

		mQuestionsAdapter = new QuestionsAdapter();
		questionsRecycler.setAdapter(mQuestionsAdapter);
	}

	private void showAskDialog() {
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
		YayNayClient.getInstance()
				.getMyQuestions(new YayNayResultCallback<ObjectResult<List<Question>>>() {
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
		YayNayClient.getInstance()
				.ask(question, new YayNayResultCallback<VoidResult>() {
					@Override
					public void onResult(@NonNull VoidResult result) {
					}
				});
	}

}
