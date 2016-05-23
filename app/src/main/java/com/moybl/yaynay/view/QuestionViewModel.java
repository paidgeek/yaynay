package com.moybl.yaynay.view;

import com.google.api.client.util.DateTime;

import android.databinding.Bindable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.moybl.yaynay.backend.yayNay.model.Question;

public class QuestionViewModel extends RecyclerView.ViewHolder {

	private Question mQuestion;

	public QuestionViewModel(View itemView, Question question) {
		super(itemView);

		mQuestion = question;
	}

	public void onYayClick(View view) {
	}

	public void onNayClick(View view) {
	}

	@Bindable
	public DateTime getAskedAt() {
		return mQuestion.getAskedAt();
	}

	@Bindable
	public String getText() {
		return mQuestion.getText();
	}

}
