package com.moybl.yaynay.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Entity
public class QuestionFollower {

	@Id
	private Long id;
	private Long questionId;
	private List<Long> askerIds;
	private int followerCount;

	public QuestionFollower() {
		askerIds = new ArrayList<>();
	}

	public void addFollower(Long askerId) {
		askerIds.add(askerId);
		followerCount++;
	}

	public Long getId() {
		return id;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public List<Long> getAskerIds() {
		return askerIds;
	}

	public int getFollowerCount() {
		return followerCount;
	}

}
