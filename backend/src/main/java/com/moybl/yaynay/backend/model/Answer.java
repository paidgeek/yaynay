package com.moybl.yaynay.backend.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Answer {

	@Id
	private Long id;
	@Index
	private Long questionId;
	@Index
	private Long askerId;
	private boolean state;

	public Long getId() {
		return id;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public Long getAskerId() {
		return askerId;
	}

	public void setAskerId(Long askerId) {
		this.askerId = askerId;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public boolean getState() {
		return state;
	}

}
