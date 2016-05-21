package com.moybl.yaynay.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterable;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.api.users.User;

import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Api(
		name = "questionApi",
		version = "v1",
		namespace = @ApiNamespace(
				ownerDomain = "backend.yaynay.moybl.com",
				ownerName = "backend.yaynay.moybl.com",
				packagePath = ""
		),
		scopes = {Constants.EMAIL_SCOPE},
		clientIds = {Constants.WEB_CLIENT_ID, Constants.ANDROID_CLIENT_ID},
		audiences = {Constants.ANDROID_AUDIENCE}
)
public class QuestionEndpoint {

	private static final Logger log = Logger.getLogger(QuestionEndpoint.class.getName());

	@ApiMethod(
			name = "ask",
			httpMethod = ApiMethod.HttpMethod.POST,
			authenticators = GoogleUserAuthenticator.class
	)
	public void ask(@Named("question") String text, User user) throws UnauthorizedException {
		if (user == null) {
			throw new UnauthorizedException("Unauthorized");
		}

		Asker asker = OfyService.ofy()
				.load()
				.type(Asker.class)
				.filter("email", user.getEmail())
				.first()
				.now();

		Question question = new Question();
		question.setAskerId(asker.getId());
		question.setText(text);

		asker.setQuestionCount(asker.getQuestionCount() + 1);

		OfyService.ofy()
				.save()
				.entities(question, asker)
				.now();
	}

	@ApiMethod(
			name = "getMyQuestions",
			httpMethod = ApiMethod.HttpMethod.GET,
			authenticators = GoogleUserAuthenticator.class
	)
	public CollectionResponse<Question> getMyQuestions(@Nullable @Named("cursor") String cursorString, User user) throws UnauthorizedException {
		if (user == null) {
			throw new UnauthorizedException("Unauthorized");
		}

		Long askerId = OfyService.ofy()
				.load()
				.type(Asker.class)
				.filter("email", user.getEmail())
				.keys()
				.first()
				.now()
				.getId();

		Query<Question> query = OfyService.ofy()
				.load()
				.type(Question.class)
				.filter("askerId", askerId)
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

}
