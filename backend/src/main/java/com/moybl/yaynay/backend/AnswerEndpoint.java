package com.moybl.yaynay.backend;

import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import com.moybl.yaynay.backend.auth.AskerUser;
import com.moybl.yaynay.backend.auth.YayNayAuthenticator;
import com.moybl.yaynay.backend.model.Answer;
import com.moybl.yaynay.backend.model.Asker;
import com.moybl.yaynay.backend.model.Question;

public class AnswerEndpoint extends YayNayEndpoint {

	@ApiMethod(
			name = "answers.insert",
			httpMethod = ApiMethod.HttpMethod.POST,
			authenticators = YayNayAuthenticator.class
	)
	public void insert(User user, @Named("questionId") Long questionId, @Named("state") boolean state)
			throws UnauthorizedException, NotFoundException {
		if (user == null) {
			throw new UnauthorizedException("Unauthorized");
		}

		Asker asker = ((AskerUser) user).getAsker();
		Key questionKey = KeyFactory.createKey("Question", questionId);

		Question question = OfyService.ofy()
				.load()
				.type(Question.class)
				.filterKey(questionKey)
				.first()
				.now();

		if (question == null) {
			throw new NotFoundException("question: " + questionId);
		}

		Answer answer = new Answer();
		answer.setState(state);
		answer.setAskerId(asker.getId());
		answer.setQuestionId(question.getId());

		if (state) {
			question.setYayCount(question.getYayCount() + 1);
		} else {
			question.setNayCount(question.getNayCount() + 1);
		}

		OfyService.ofy()
				.save()
				.entities(question, answer)
				.now();
	}

}
