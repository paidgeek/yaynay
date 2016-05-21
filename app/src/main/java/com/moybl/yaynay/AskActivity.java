package com.moybl.yaynay;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.moybl.yaynay.backend.VoidResult;
import com.moybl.yaynay.backend.YayNayClient;
import com.moybl.yaynay.backend.YayNayResultCallback;

public class AskActivity extends AppCompatActivity {

	public static final int RESULT_ERROR = 1000;
	public static final String KEY_QUESTION = "question";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ask);

		final EditText editQuestion = (EditText) findViewById(R.id.edit_question);
		findViewById(R.id.ask_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ask(editQuestion.getText()
						.toString());
			}
		});
	}

	private void ask(final String question) {
		YayNayClient.getInstance()
				.ask(question, new YayNayResultCallback<VoidResult>() {
					@Override
					public void onResult(@NonNull VoidResult result) {
						Intent data = new Intent();

						data.putExtra(KEY_QUESTION, question);

						if (result.isSuccess()) {
							setResult(RESULT_OK, data);
						} else {
							setResult(RESULT_ERROR, data);
						}

						finish();
					}
				});
	}
}
