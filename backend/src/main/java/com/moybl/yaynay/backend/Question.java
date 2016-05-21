package com.moybl.yaynay.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Question {

	@Id
	private Long id;
	private String text;
	private String askerName;
	private String askerDisplayName;
	private Long askerId;
	private List<Follower> followers;

	public Question() {
		followers = new ArrayList<>();
	}

	public Long getId() {
		return id;
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

	public List<Follower> getFollowers() {
		return followers;
	}
}
