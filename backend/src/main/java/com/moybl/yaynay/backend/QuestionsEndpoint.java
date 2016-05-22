package com.moybl.yaynay.backend;

import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.api.users.User;

import com.googlecode.objectify.cmd.Query;
import com.moybl.yaynay.backend.model.Asker;
import com.moybl.yaynay.backend.model.Question;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class QuestionsEndpoint extends YayNayEndpoint {

	private static final Logger log = Logger.getLogger(QuestionsEndpoint.class.getName());

	@ApiMethod(
			name = "questions.insert",
			httpMethod = ApiMethod.HttpMethod.POST
	)
	public void insert(User user, @Named("question") String text) throws UnauthorizedException {
		if (user == null) {
			throw new UnauthorizedException("Unauthorized");
		}

		Asker asker = OfyService.ofy()
				.load()
				.type(Asker.class)
				.filter("googleId", user.getUserId())
				.first()
				.now();

		Question question = new Question();
		question.setAskedAt(new Date());
		question.setAskerId(asker.getId());
		question.setText(text);

		asker.setQuestionCount(asker.getQuestionCount() + 1);

		OfyService.ofy()
				.save()
				.entities(question, asker)
				.now();
	}

	@ApiMethod(
			name = "questions.list",
			httpMethod = ApiMethod.HttpMethod.GET
	)
	public CollectionResponse<Question> list(User user, @Nullable @Named("cursor") String cursorString) throws UnauthorizedException {
		if (user == null) {
			throw new UnauthorizedException("Unauthorized");
		}

		/*
		Long askerId = OfyService.ofy()
				.load()
				.type(Asker.class)
				.filter("email", user.getEmail())
				.keys()
				.first()
				.now()
				.getId();
		*/

		Query<Question> query = OfyService.ofy()
				.load()
				.type(Question.class)
				.order("-askedAt")
				.limit(10);

		if (cursorString != null) {
			query = query.startAt(Cursor.fromWebSafeString(cursorString));
		}

		List<Question> questions = new ArrayList<>();
		QueryResultIterator<Question> i = query.iterator();

		while (i.hasNext()) {
			questions.add(i.next());
		}

		Cursor cursor = i.getCursor();

		return CollectionResponse.<Question>builder()
				.setItems(questions)
				.setNextPageToken(cursor.toWebSafeString())
				.build();
	}
/*
	@ApiMethod(
			name = "questions.listByUser",
			httpMethod = ApiMethod.HttpMethod.GET,
			authenticators = GoogleUserAuthenticator.class
	)
	public CollectionResponse<Question> getQuestionsByUser(@Nullable @Named("askerId") Long askerId, @Nullable @Named("cursor") String cursorString, User user) throws UnauthorizedException {
		if (user == null) {
			throw new UnauthorizedException("Unauthorized");
		}

		if (askerId == null) {
			askerId = OfyService.ofy()
					.load()
					.type(Asker.class)
					.filter("googleId", user.getUserId())
					.keys()
					.first()
					.now()
					.getId();
		}

		Query<Question> query = OfyService.ofy()
				.load()
				.type(Question.class)
				.filter("askerId", askerId)
				.order("-askedAt")
				.limit(20);

		if (cursorString != null) {
			query = query.startAt(Cursor.fromWebSafeString(cursorString));
		}

		List<Question> questions = new ArrayList<>();
		QueryResultIterator<Question> i = query.iterator();

		while (i.hasNext()) {
			questions.add(i.next());
		}

		Cursor cursor = i.getCursor();

		return CollectionResponse.<Question>builder()
				.setItems(questions)
				.setNextPageToken(cursor.toWebSafeString())
				.build();
	}
*/
}
