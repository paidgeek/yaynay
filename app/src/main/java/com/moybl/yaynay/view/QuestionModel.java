package com.moybl.yaynay.view;

import com.google.api.client.util.DateTime;

import android.databinding.BaseObservable;

import com.moybl.yaynay.backend.yayNay.model.Question;

public class QuestionModel extends BaseObservable {

	private Question mQuestion;

	private DateTime mAskedAt;
	private String mAskerDisplayName;
	private String mAskerName;
	private Long mNayCount;
	private Long mYayCount;
	private String mText;

	public QuestionModel(Question question) {
		mQuestion = question;

		mAskedAt = question.getAskedAt();
		mAskerDisplayName = question.getAskerDisplayName();
		mAskerName = question.getAskerName();
		mNayCount = question.getNayCount();
		mYayCount = question.getYayCount();
		mText = question.getText();
	}

	public Question getQuestion() {
		return mQuestion;
	}

}
