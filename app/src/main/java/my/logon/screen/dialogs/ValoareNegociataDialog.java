package my.logon.screen.dialogs;

import my.logon.screen.listeners.ValoareNegociataDialogListener;
import my.logon.screen.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

public class ValoareNegociataDialog extends Dialog {

	private Context context;
	private double valNegociat;
	private boolean totalNegociat;
	private ValoareNegociataDialogListener listener;

	public ValoareNegociataDialog(Context context) {
		super(context);
		this.context = context;

	}

	public void showDialog(double valNegociat, boolean totalNegociat) {
		this.valNegociat = valNegociat;
		this.totalNegociat = totalNegociat;
		setUpLayout();
		this.show();

	}

	public void setValoareNegociataListener(ValoareNegociataDialogListener listener) {
		this.listener = listener;
	}

	private void dismissThisDialog() {
		this.dismiss();
	}

	void setUpLayout() {
		setContentView(R.layout.valnegociatadialogbox);
		setTitle("Comanda cu valoare totala negociata");
		setCancelable(false);

		final LinearLayout layoutValNegociat = (LinearLayout) findViewById(R.id.layoutValNegociat);
		layoutValNegociat.setVisibility(View.INVISIBLE);

		final EditText textValNegociat = (EditText) findViewById(R.id.txtValNegociat);
		textValNegociat.setText(String.valueOf(valNegociat));

		final RadioButton radioDaValNeg = (RadioButton) findViewById(R.id.radio1);
		radioDaValNeg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					layoutValNegociat.setVisibility(View.VISIBLE);
					InputMethodManager mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
					mgr.showSoftInput(textValNegociat, InputMethodManager.SHOW_IMPLICIT);

				}

			}
		});

		final RadioButton radioNuValNeg = (RadioButton) findViewById(R.id.radio2);
		radioNuValNeg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					layoutValNegociat.setVisibility(View.INVISIBLE);

				}

			}
		});

		Button btnOkValNeg = (Button) findViewById(R.id.btnOkValNeg);
		btnOkValNeg.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				if (radioDaValNeg.isChecked()) {
					if (textValNegociat.getText().toString().trim().length() > 0) {
						if (Double.parseDouble(textValNegociat.getText().toString().trim()) > 0) {
							valNegociat = Double.parseDouble(textValNegociat.getText().toString().trim());
							totalNegociat = true;

						}

					} else
						return;
				} else {
					valNegociat = 0;
					totalNegociat = false;
				}

				if (listener != null) {
					listener.operationComplete(valNegociat, totalNegociat);
					dismissThisDialog();
				}

			}
		});

		if (totalNegociat) {
			radioDaValNeg.setChecked(true);
			textValNegociat.setText(String.valueOf(valNegociat));
		} else
			radioDaValNeg.setChecked(false);

	}

}
