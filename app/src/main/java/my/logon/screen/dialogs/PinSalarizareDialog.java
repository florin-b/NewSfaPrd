package my.logon.screen.dialogs;

import java.util.HashMap;

import my.logon.screen.listeners.CodPinDialogListener;
import my.logon.screen.listeners.OperatiiMeniuListener;
import my.logon.screen.model.OperatiiMeniu;
import my.logon.screen.model.UserInfo;
import my.logon.screen.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import my.logon.screen.enums.EnumOperatiiMeniu;

public class PinSalarizareDialog extends Dialog implements OperatiiMeniuListener {

	private Context context;

	private OperatiiMeniu operatiiMeniu;
	private Button btnOkPin;
	private CodPinDialogListener pinListener;

	public PinSalarizareDialog(Context context) {
		super(context);
		this.context = context;

		setContentView(R.layout.pin_salarizare_dialog);
		setTitle("Acces salarizare");
		setCancelable(true);

		operatiiMeniu = new OperatiiMeniu(context);
		operatiiMeniu.setOperatiiMeniuListener(this);
		setUpLayout();

	}

	public void showDialog() {
		this.show();
	}

	private void setUpLayout() {

		if (UserInfo.getInstance().getCodPinMeniu().equals("-1")) {
			((LinearLayout) findViewById(R.id.layoutPin2)).setVisibility(View.VISIBLE);
			((TextView) findViewById(R.id.labelPin1)).setText("Introduceti un cod pin din 4 cifre:");
		}

		btnOkPin = (Button) findViewById(R.id.btnOkPin);
		setBtnOkListener();

	}

	private void setBtnOkListener() {
		btnOkPin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				if (!valideazaCodPin())
					return;

				if (pinListener != null) {
					opereazaCodPin();
				}

				dismiss();

			}
		});
	}

	private boolean valideazaCodPin() {

		if (UserInfo.getInstance().getCodPinMeniu().equals("-1")) {
			String pin1 = ((EditText) findViewById(R.id.textPin1)).getText().toString();
			String pin2 = ((EditText) findViewById(R.id.textPin2)).getText().toString();

			if (pin1.trim().length() < 4 || pin2.trim().length() < 4) {
				Toast.makeText(context, "Codul pin nu are 4 cifre.", Toast.LENGTH_LONG).show();
				return false;
			}

			if (!pin1.trim().equals(pin2.trim())) {
				Toast.makeText(context, "Codurile nu sunt identice.", Toast.LENGTH_LONG).show();
				return false;
			}

			return true;
		} else {
			String pin1 = ((EditText) findViewById(R.id.textPin1)).getText().toString();

			if (!pin1.trim().equals(UserInfo.getInstance().getCodPinMeniu())) {
				Toast.makeText(context, "Cod incorect.", Toast.LENGTH_LONG).show();
				return false;
			}
		}

		return true;

	}

	public void setPinDialogListener(CodPinDialogListener pinListener) {
		this.pinListener = pinListener;
	}

	private void opereazaCodPin() {
		if (UserInfo.getInstance().getCodPinMeniu().equals("-1")) {
			String pin1 = ((EditText) findViewById(R.id.textPin1)).getText().toString();

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("codAgent", UserInfo.getInstance().getCod());
			params.put("codPin", pin1);

			operatiiMeniu.savePinMeniu(params);
		} else {
			String pin1 = ((EditText) findViewById(R.id.textPin1)).getText().toString();

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("codAgent", UserInfo.getInstance().getCod());
			params.put("codPin", pin1);

			operatiiMeniu.deblocheazaMeniu(params);
		}
	}

	@Override
	public void pinCompleted(EnumOperatiiMeniu numeOp, boolean isSuccess) {
		if (pinListener != null) {

			switch (numeOp) {
			case SAVE_PIN:
				if (isSuccess) {
					String pin1 = ((EditText) findViewById(R.id.textPin1)).getText().toString();
					UserInfo.getInstance().setCodPinMeniu(pin1);
					UserInfo.getInstance().setIsMeniuBlocat(false);
				} else
					Toast.makeText(context, "Codul nu a fost salvat.", Toast.LENGTH_LONG).show();
				break;
			case DEBLOCHEAZA_MENIU:
				if (isSuccess)
					UserInfo.getInstance().setIsMeniuBlocat(false);
				else
					Toast.makeText(context, "Cod incorect.", Toast.LENGTH_LONG).show();
				break;
			default:
				break;

			}

			pinListener.codPinComplete(isSuccess);

		}

	}

}
