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
	private boolean isBlocat;
	public MarjaComandaIPListener listener;
	private double valoareTaxe;


	public MarjaComandaIPDialog(Context context, boolean isBlocat, double valoareTaxe) {
		super(context);
		this.isBlocat = isBlocat;
		this.valoareTaxe = valoareTaxe;
		setContentView(R.layout.info_comanda_ip_dialog);
		setupLayout();
	}



	private void setupLayout() {
		textAlert = findViewById(R.id.textAlert);
		btnOk = findViewById(R.id.btnOk);

		textAlert.setText("Marja comenzii este mica!\nCosturile de livrare sunt semnificative (" + String.format("%.02f", valoareTaxe) + " lei).");

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
