package my.logon.screen.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import my.logon.screen.R;
import my.logon.screen.listeners.MarjaComandaIPListener;

public class MarjaComandaIPDialog extends Dialog {

	private TextView textAlert;
	private Button btnOk;
	private String alertText;
	private boolean isBlocat;
	public MarjaComandaIPListener listener;


	public MarjaComandaIPDialog(Context context, boolean isBlocat) {
		super(context);
		this.isBlocat = isBlocat;
		setContentView(R.layout.info_comanda_ip_dialog);
		setupLayout();
	}



	private void setupLayout() {
		textAlert = (TextView) findViewById(R.id.textAlert);
		btnOk = (Button) findViewById(R.id.btnOk);

		textAlert.setText("Marja comenzii este mica!\nCosturile de livrare sunt semnificative!");

		if (isBlocat) {
			setTitle("Atentie!");
			btnOk.setText("Revenire");
		}
		else {
			setTitle("Info");
			btnOk.setText("Continua");
		}

		setListenerBtnOk();

	}

	@Override
	public void show() {
		super.show();
	}

	private void setListenerBtnOk() {
		btnOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (listener !=null)
					listener.comandaIPSelected(isBlocat);

				dismiss();

			}
		});
	}

	public void setMarjaComamdaIPListener(MarjaComandaIPListener listener){
		this.listener = listener;
	}

}
