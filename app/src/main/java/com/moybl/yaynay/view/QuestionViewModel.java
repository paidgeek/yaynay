package com.moybl.yaynay.view;

import com.google.api.client.util.DateTime;

import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.moybl.yaynay.R;
import com.moybl.yaynay.backend.VoidResult;
import com.moybl.yaynay.backend.YayNayClient;
import com.moybl.yaynay.backend.YayNayResultCallback;
import com.moybl.yaynay.backend.yayNay.model.Question;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class QuestionViewModel extends RecyclerView.ViewHolder implements Observable {

	private transient PropertyChangeRegistry mCallbacks;
	private ViewDataBinding mBinding;
	private Question mQuestion;

	public QuestionViewModel(View itemView) {
		super(itemView);

		mBinding = DataBindingUtil.bind(itemView);
	}

	public void onYayClick(View view) {
		YayNayClient.getInstance()
				.insertAnswer(mQuestion.getId(), true, new YayNayResultCallback<VoidResult>() {
					@Override
					public void onResult(@NonNull VoidResult result) {
						if (result.isSuccess()) {
							mQuestion.setYayCount(mQuestion.getYayCount() + 1);
						}
					}
				});
	}

	public void onNayClick(View view) {
		YayNayClient.getInstance()
				.insertAnswer(mQuestion.getId(), false, new YayNayResultCallback<VoidResult>() {
					@Override
					public void onResult(@NonNull VoidResult result) {
						if (result.isSuccess()) {
							mQuestion.setNayCount(mQuestion.getNayCount() + 1);
						}
					}
				});
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

	@Bindable
	public String getAskerDisplayName() {
		return mQuestion.getAskerDisplayName();
	}

	@Bindable
	public String getAskerName() {
		return mQuestion.getAskerName();
	}

	@Bindable
	public String getAskerPicture() {
		return mQuestion.getAskerPicture();
	}

	@Bindable
	public long getYayCount() {
		return mQuestion.getYayCount();
	}

	@Bindable
	public long getNayCount() {
		return mQuestion.getNayCount();
	}

	@Bindable
	public float getAnswerWeight() {
		float w = 0.5f;
		long sum = getYayCount() + getNayCount();

		if (sum < 0) {
			sum = Long.MAX_VALUE;
		}

		if (sum > 0) {
			w = getYayCount() / (float) sum;
		}

		return w;
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

	@BindingAdapter({"bind:askerPicture"})
	public static void bindAskerPicture(ImageView view, String askerPicture) {
		Picasso.with(view.getContext())
				.load(askerPicture)
				.placeholder(R.drawable.user)
				.into(view);
	}

	@BindingAdapter({"bind:answerWeight"})
	public static void bindAnswerWeight(LinearLayout linearLayout, float weight) {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
		lp.weight = weight;
		linearLayout.setLayoutParams(lp);
	}

}
