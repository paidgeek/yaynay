package com.moybl.yaynay.backend;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

public class OfyService {

	static {
		ObjectifyService.register(Asker.class);
		ObjectifyService.register(Follower.class);
		ObjectifyService.register(Question.class);
		ObjectifyService.register(QuestionFollower.class);
	}

	public static Objectify ofy() {
		return ObjectifyService.ofy();
	}

	public static ObjectifyFactory factory() {
		return ObjectifyService.factory();
	}

}
