package my.logon.screen.screens;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import my.logon.screen.R;
import my.logon.screen.listeners.CnpDialogListener;
import my.logon.screen.utils.UtilsCheck;

public class CnpDialog extends Dialog {

	private Button btnSave;
	private EditText editTextValue;
	private TextView textStatus;
	private CnpDialogListener listener;

	public CnpDialog(Context context) {
		super(context);
		setContentView(R.layout.cnp_dialog);
		setTitle("Atentie!");
		setCancelable(true);
		setupLayout();

	}

	private void setupLayout() {

		btnSave = (Button) findViewById(R.id.btnSave);

		setListenerOkButton();

		editTextValue = (EditText) findViewById(R.id.textValoareLinie);
		editTextValue.setSelection(editTextValue.getText().length());

		textStatus = (TextView) findViewById(R.id.textStatus);
		textStatus.setVisibility(View.INVISIBLE);

	}

	private void setListenerOkButton() {
		btnSave.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (!UtilsCheck.isCnpValid(editTextValue.getText().toString().trim())) {
					textStatus.setText("Cod invalid.");
					textStatus.setVisibility(View.VISIBLE);
					return;
				}

				if (listener != null) {
					listener.cnpSaved(editTextValue.getText().toString().trim());
					dismiss();
				}

			}
		});
	}



	public void setCnpListener(CnpDialogListener listener) {
		this.listener = listener;
	}

}
