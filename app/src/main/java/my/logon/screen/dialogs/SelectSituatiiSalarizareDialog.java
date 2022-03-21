package my.logon.screen.dialogs;

import my.logon.screen.listeners.SituatieSalarizareListener;
import my.logon.screen.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import my.logon.screen.enums.EnumSituatieSalarizare;

public class SelectSituatiiSalarizareDialog extends Dialog {

	private SituatieSalarizareListener listener;
	private Button btnOkSituatie;
	private RadioButton radioAgenti;

	public SelectSituatiiSalarizareDialog(Context context) {
		super(context);

		setContentView(R.layout.situatii_salarizare);
		setTitle("Salarizare");
		setCancelable(true);

		setUpLayout();

	}

	public void showDialog() {
		this.show();
	}

	private void dismissCustomDialog() {
		this.dismiss();
	}

	public void setSituatieListener(SituatieSalarizareListener listener) {
		this.listener = listener;
	}

	private void setUpLayout() {
		radioAgenti = (RadioButton) findViewById(R.id.radioAgenti);
		btnOkSituatie = (Button) findViewById(R.id.btnOkSituatie);
		setBtnOkListener();

	}

	private void setBtnOkListener() {
		btnOkSituatie.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				if (listener != null) {

					EnumSituatieSalarizare tipSituatie = EnumSituatieSalarizare.SEF_DEP;

					if (radioAgenti.isChecked())
						tipSituatie = EnumSituatieSalarizare.AGENTI;

					listener.tipSituatieSalSelected(tipSituatie);
					dismissCustomDialog();
				}

			}
		});
	}

}
