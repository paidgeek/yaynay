package com.moybl.yaynay.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Date;

@Entity
public class Question {

	@Id
	private Long id;
	@Index
	private Date askedAt;
	private String text;
	private String askerName;
	private String askerDisplayName;
	@Index
	private Long askerId;

	public Long getId() {
		return id;
	}

	public Date getAskedAt() {
		return askedAt;
	}

	public void setAskedAt(Date askedAt) {
		this.askedAt = askedAt;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getAskerName() {
		return askerName;
	}

	public void setAskerName(String askerName) {
		this.askerName = askerName;
	}

	public String getAskerDisplayName() {
		return askerDisplayName;
	}

	public void setAskerDisplayName(String askerDisplayName) {
		this.askerDisplayName = askerDisplayName;
	}

	public Long getAskerId() {
		return askerId;
	}

	public void setAskerId(Long askerId) {
		this.askerId = askerId;
	}

}
