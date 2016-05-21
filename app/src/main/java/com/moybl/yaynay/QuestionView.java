package com.moybl.yaynay;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class QuestionView extends RecyclerView.ViewHolder {

	private TextView mTextView;
	private Button mYayButton;
	private Button mNayButton;

	public QuestionView(View itemView, TextView text, Button yayButton, Button nayButton) {
		super(itemView);

		mTextView = text;
		mYayButton = yayButton;
		mNayButton = nayButton;
	}

	public TextView getTextView() {
		return mTextView;
	}

	public Button getNayButton() {
		return mNayButton;
	}

	public Button getYayButton() {
		return mYayButton;
	}

}
