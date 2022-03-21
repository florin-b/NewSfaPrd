package my.logon.screen.dialogs;

import my.logon.screen.listeners.InputTextDialogListener;
import my.logon.screen.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class InputTextDialog extends Dialog {

	private ImageButton btnSave, btnCancel;
	private EditText editTextValue;
	private InputTextDialogListener listener;

	public InputTextDialog(String dialogName, String initString, Context context) {
		super(context);

		setContentView(R.layout.modifica_valoare_linie_obiectiv);
		setTitle(dialogName);
		setCancelable(true);

		setupLayout(initString);

	}

	private void setupLayout(String initString) {

		btnSave = (ImageButton) findViewById(R.id.btnSave);
		btnCancel = (ImageButton) findViewById(R.id.btnCancel);

		setListenerOkButton();
		setListenerCancelButton();

		editTextValue = (EditText) findViewById(R.id.textValoareLinie);
		editTextValue.setText(initString);
		editTextValue.setSelection(editTextValue.getText().length());

	}

	private void setListenerOkButton() {
		btnSave.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (listener != null) {
					listener.textSaved(editTextValue.getText().toString().trim());
					dismiss();
				}

			}
		});
	}

	private void setListenerCancelButton() {
		btnCancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dismiss();

			}
		});
	}

	public void setInputTextListener(InputTextDialogListener listener) {
		this.listener = listener;
	}

}
