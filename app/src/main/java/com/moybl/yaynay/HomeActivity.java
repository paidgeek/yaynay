package com.moybl.yaynay;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

	private static final int RC_ASK = 9001;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		if (fab != null) {
			fab.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent = new Intent(getApplicationContext(), AskActivity.class);
					startActivityForResult(intent, RC_ASK);
				}
			});
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RC_ASK) {
			String question = data.getStringExtra(AskActivity.KEY_QUESTION);

			if (resultCode == AskActivity.RESULT_OK) {
				Snackbar.make(findViewById(android.R.id.content), "Asked: " + question, Snackbar.LENGTH_LONG)
						.show();
			}
		}
	}

}
