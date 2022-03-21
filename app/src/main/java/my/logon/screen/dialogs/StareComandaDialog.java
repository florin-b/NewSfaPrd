package my.logon.screen.dialogs;

import my.logon.screen.listeners.StareComandaListener;

import my.logon.screen.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.RadioGroup;

public class StareComandaDialog extends Dialog {

	private RadioGroup radioGroupClnt;

	private Button btnOkClnt;
	private EditText textTelClient;
	private StareComandaListener listener;
	private boolean afisToateComenzile;

	public StareComandaDialog(Context context, String agent, String filiala) {
		super(context);

		setContentView(R.layout.afis_stare_comanda_dialog);
		setTitle("Afisare comenzi");
		setCancelable(true);

		setUpLayout();

	}

	public void showDialog() {
		this.show();
	}

	private void dismissCustomDialog() {
		this.dismiss();
	}

	private void setUpLayout() {

		radioGroupClnt = (RadioGroup) findViewById(R.id.radioGroup1);
		textTelClient = (EditText) findViewById(R.id.text_telefon_client);

		afisToateComenzile = true;

		addRadioListener();

		btnOkClnt = (Button) findViewById(R.id.btnOkClnt);
		addBtnOkListener();

	}

	private void addRadioListener() {
		radioGroupClnt.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			public void onCheckedChanged(RadioGroup group, int checkedId) {

				switch (checkedId) {
				case R.id.radioToate:
					afisToateComenzile = true;
					textTelClient.setVisibility(View.INVISIBLE);
					break;
				case R.id.radioTel:
					afisToateComenzile = false;
					textTelClient.setVisibility(View.VISIBLE);
					break;
				}

			}
		});

	}

	private void addBtnOkListener() {
		btnOkClnt.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				if (listener != null) {

					String telefonClient = "-1";

					if (!afisToateComenzile)
						telefonClient = textTelClient.getText().toString().trim();

					listener.selectedClientTelefon(telefonClient);

					dismissCustomDialog();
				}

			}
		});
	}

	public void setStareComandaListener(StareComandaListener listener) {
		this.listener = listener;
	}

}
