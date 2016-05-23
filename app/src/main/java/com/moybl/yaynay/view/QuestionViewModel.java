package com.moybl.yaynay.view;

import com.google.api.client.util.DateTime;

import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.moybl.yaynay.backend.yayNay.model.Question;

public class QuestionViewModel extends RecyclerView.ViewHolder implements Observable {

	private transient PropertyChangeRegistry mCallbacks;
	private ViewDataBinding mBinding;
	private Question mQuestion;

	public QuestionViewModel(View itemView) {
		super(itemView);

		mBinding = DataBindingUtil.bind(itemView);
	}

	public void onYayClick(View view) {
		mQuestion.setText(mQuestion.getText() + "1");
		notifyChange();
	}

	public void onNayClick(View view) {
	}

	public Question getQuestion() {
		return mQuestion;
	}

	public void setQuestion(Question mQuestion) {
		this.mQuestion = mQuestion;
	}

	@Bindable
	public DateTime getAskedAt() {
		return mQuestion.getAskedAt();
	}

	@Bindable
	public String getText() {
		return mQuestion.getText();
	}

	public ViewDataBinding getBinding() {
		return mBinding;
	}

	@Override
	public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
		if (mCallbacks == null) {
			mCallbacks = new PropertyChangeRegistry();
		}

		mCallbacks.add(callback);
	}

	@Override
	public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
		if (mCallbacks != null) {
			mCallbacks.remove(callback);
		}
	}

	public synchronized void notifyChange() {
		if (mCallbacks != null) {
			mCallbacks.notifyCallbacks(this, 0, null);
		}
	}

	public void notifyPropertyChanged(int fieldId) {
		if (mCallbacks != null) {
			mCallbacks.notifyCallbacks(this, fieldId, null);
		}
	}

}
