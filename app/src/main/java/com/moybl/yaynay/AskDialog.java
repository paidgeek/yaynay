package com.moybl.yaynay;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class AskDialog extends DialogFragment {

	public interface AskDialogListener {
		void onAsk(String question);

		void onCancel();
	}

	private AskDialogListener mAskDialogListener;

	public void setAskDialogListener(AskDialogListener askDialogListener) {
		this.mAskDialogListener = askDialogListener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();

		final View view = inflater.inflate(R.layout.dialog_ask, null);

		builder.setView(view)
				.setPositiveButton(R.string.ask, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						if (mAskDialogListener != null) {
							EditText editQuestion = (EditText) view.findViewById(R.id.edit_question);
							mAskDialogListener.onAsk(editQuestion.getText()
									.toString());
						}
					}
				})
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						AskDialog.this.getDialog()
								.cancel();

						if (mAskDialogListener != null) {
							mAskDialogListener.onCancel();
						}
					}
				});
		return builder.create();
	}

}
