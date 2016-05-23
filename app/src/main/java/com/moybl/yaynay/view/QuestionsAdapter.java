package com.moybl.yaynay.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moybl.yaynay.BR;
import com.moybl.yaynay.R;
import com.moybl.yaynay.backend.yayNay.model.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionViewModel> {

	private List<Question> mQuestions;

	public QuestionsAdapter() {
		mQuestions = new ArrayList<>();
	}

	public List<Question> getQuestions() {
		return mQuestions;
	}

	@Override
	public QuestionViewModel onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.question, parent, false);

		return new QuestionViewModel(v);
	}

	@Override
	public void onBindViewHolder(QuestionViewModel holder, int position) {
		Question question = mQuestions.get(position);

		holder.setQuestion(question);
		holder.getBinding()
				.setVariable(BR.question, holder);
		holder.getBinding()
				.executePendingBindings();
	}

	@Override
	public int getItemCount() {
		return mQuestions.size();
	}

}
