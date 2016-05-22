package com.moybl.yaynay.backend;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.moybl.yaynay.backend.model.Asker;
import com.moybl.yaynay.backend.model.Question;

public class OfyService {

	static {
		ObjectifyService.register(Asker.class);
		ObjectifyService.register(Question.class);
	}

	public static Objectify ofy() {
		return ObjectifyService.ofy();
	}

	public static ObjectifyFactory factory() {
		return ObjectifyService.factory();
	}

}
