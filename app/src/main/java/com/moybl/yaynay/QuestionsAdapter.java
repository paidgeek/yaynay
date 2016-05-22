package com.moybl.yaynay;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moybl.yaynay.backend.yaynayService.model.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.BindingHolder> {

	public static class BindingHolder extends RecyclerView.ViewHolder {

		private ViewDataBinding mBinding;

		public BindingHolder(View itemView) {
			super(itemView);

			mBinding = DataBindingUtil.bind(itemView);
		}

		public ViewDataBinding getBinding() {
			return mBinding;
		}

	}

	private List<Question> mQuestions;

	public QuestionsAdapter() {
		mQuestions = new ArrayList<>();
	}

	public List<Question> getQuestions() {
		return mQuestions;
	}

	@Override
	public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.question, parent, false);

		BindingHolder holder = new BindingHolder(v);

		return holder;
	}

	@Override
	public void onBindViewHolder(BindingHolder holder, int position) {
		Question question = mQuestions.get(position);

		holder.getBinding()
				.setVariable(com.moybl.yaynay.BR.question, question);
		holder.getBinding()
				.executePendingBindings();
	}

	@Override
	public int getItemCount() {
		return mQuestions.size();
	}

}
