package com.moybl.yaynay;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.moybl.yaynay.backend.questionApi.model.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionView> {

	private List<Question> questions;

	public QuestionsAdapter() {
		questions = new ArrayList<>();
	}

	public List<Question> getQuestions() {
		return questions;
	}

	@Override
	public QuestionView onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.layout_question, parent, false);

		TextView text = (TextView) v.findViewById(R.id.question_text);
		Button yayButton = (Button) v.findViewById(R.id.question_button_yay);
		Button nayButton = (Button) v.findViewById(R.id.question_button_nay);

		QuestionView vh = new QuestionView(v, text, yayButton, nayButton);

		return vh;
	}

	@Override
	public void onBindViewHolder(QuestionView holder, int position) {
		Question question = questions.get(position);

		holder.getTextView()
				.setText(question.getText());
	}

	@Override
	public int getItemCount() {
		return questions.size();
	}

}
