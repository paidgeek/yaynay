package com.moybl.yaynay;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

	private static final String TAG = "HomeActivity";

	public static class MessageViewHolder extends RecyclerView.ViewHolder {
		public TextView messageTextView;
		public TextView messengerTextView;
		public CircleImageView messengerImageView;

		public MessageViewHolder(View v) {
			super(v);
			messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
			messengerTextView = (TextView) itemView.findViewById(R.id.messengerTextView);
			messengerImageView = (CircleImageView) itemView.findViewById(R.id.messengerImageView);
		}

	}

	public static final String MESSAGES_CHILD = "messages";
	public static final String FRIENDLY_MSG_LENGTH = "friendly_msg_length";
	private static final int REQUEST_INVITE = 1;
	public static final int DEFAULT_MSG_LENGTH_LIMIT = 10;
	public static final String ANONYMOUS = "anonymous";
	private static final String MESSAGE_SENT_EVENT = "message_sent";
	private String mUsername;
	private String mPhotoUrl;
	private SharedPreferences mSharedPreferences;
	private GoogleApiClient mGoogleApiClient;

	private Button mSendButton;
	private RecyclerView mMessageRecyclerView;
	private LinearLayoutManager mLinearLayoutManager;
	private ProgressBar mProgressBar;
	private EditText mMessageEditText;
	private DatabaseReference mFirebaseDatabaseReference;
	private FirebaseRecyclerAdapter<FriendlyMessage, MessageViewHolder>
			mFirebaseAdapter;

	private FirebaseRemoteConfig mFirebaseRemoteConfig;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		// Set default username is anonymous.
		mUsername = ANONYMOUS;

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
				.addApi(Auth.GOOGLE_SIGN_IN_API)
				.build();

		// Initialize ProgressBar and RecyclerView.
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
		mMessageRecyclerView = (RecyclerView) findViewById(R.id.messageRecyclerView);
		mLinearLayoutManager = new LinearLayoutManager(this);
		mLinearLayoutManager.setStackFromEnd(true);
		mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);

		mProgressBar.setVisibility(ProgressBar.INVISIBLE);

		mMessageEditText = (EditText) findViewById(R.id.messageEditText);
		mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mSharedPreferences
				.getInt(FRIENDLY_MSG_LENGTH, DEFAULT_MSG_LENGTH_LIMIT))});
		mMessageEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				if (charSequence.toString().trim().length() > 0) {
					mSendButton.setEnabled(true);
				} else {
					mSendButton.setEnabled(false);
				}
			}

			@Override
			public void afterTextChanged(Editable editable) {
			}
		});

		mSendButton = (Button) findViewById(R.id.sendButton);
		mSendButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				FriendlyMessage friendlyMessage = new
						FriendlyMessage(mMessageEditText.getText().toString(),
						mUsername,
						mPhotoUrl);
				mFirebaseDatabaseReference.child(MESSAGES_CHILD)
						.push().setValue(friendlyMessage);
				mMessageEditText.setText("");
			}
		});

		mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
		mFirebaseAdapter = new FirebaseRecyclerAdapter<FriendlyMessage,
				MessageViewHolder>(
				FriendlyMessage.class,
				R.layout.item_message,
				MessageViewHolder.class,
				mFirebaseDatabaseReference.child(MESSAGES_CHILD)) {

			@Override
			protected void populateViewHolder(MessageViewHolder viewHolder,
														 FriendlyMessage friendlyMessage, int position) {
				mProgressBar.setVisibility(ProgressBar.INVISIBLE);
				viewHolder.messageTextView.setText(friendlyMessage.getText());
				viewHolder.messengerTextView.setText(friendlyMessage.getName());
				if (friendlyMessage.getPhotoUrl() == null) {
					viewHolder.messengerImageView
							.setImageDrawable(ContextCompat
									.getDrawable(HomeActivity.this,
											R.drawable.common_full_open_on_phone));
				} else {
					Glide.with(HomeActivity.this)
							.load(friendlyMessage.getPhotoUrl())
							.into(viewHolder.messengerImageView);
				}
			}
		};

		mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
			@Override
			public void onItemRangeInserted(int positionStart, int itemCount) {
				super.onItemRangeInserted(positionStart, itemCount);
				int friendlyMessageCount = mFirebaseAdapter.getItemCount();
				int lastVisiblePosition =
						mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
				// If the recycler view is initially being loaded or the
				// user is at the bottom of the list, scroll to the bottom
				// of the list to show the newly added message.
				if (lastVisiblePosition == -1 ||
						(positionStart >= (friendlyMessageCount - 1) &&
								lastVisiblePosition == (positionStart - 1))) {
					mMessageRecyclerView.scrollToPosition(positionStart);
				}
			}
		});

		mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
		mMessageRecyclerView.setAdapter(mFirebaseAdapter);
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
		// An unresolvable error has occurred and Google APIs (including Sign-In) will not
		// be available.
		Log.d(TAG, "onConnectionFailed:" + connectionResult);
		Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
	}

}
