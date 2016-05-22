package com.moybl.yaynay;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class QuestionView extends RecyclerView.ViewHolder {

	private TextView mTextView;
	private View mYayButton;
	private View mNayButton;

	public QuestionView(View itemView, TextView text, View yayButton, View nayButton) {
		super(itemView);

		mTextView = text;
		mYayButton = yayButton;
		mNayButton = nayButton;
	}

	public TextView getTextView() {
		return mTextView;
	}

	public View getNayButton() {
		return mNayButton;
	}

	public View getYayButton() {
		return mYayButton;
	}

}
