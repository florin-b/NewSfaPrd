package my.logon.screen.dialogs;

import java.text.NumberFormat;

import my.logon.screen.listeners.PretTransportDialogListener;
import my.logon.screen.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PretTransportDialog extends Dialog {

	private double totalComanda;
	private String valTransp;
	private PretTransportDialogListener listener;

	public PretTransportDialog(Context context, double totalComanda, String valTransp) {
		super(context);

		this.totalComanda = totalComanda;
		this.valTransp = valTransp;

		setContentView(R.layout.valtranspdlgbox);
		setTitle("Confirmare comanda GED");

		setUpLayout();
	}

	public void showDialog() {
		this.show();
	}

	private void dismissThisDialog() {
		this.dismiss();
	}

	void setUpLayout() {
		NumberFormat nf2 = NumberFormat.getInstance();
		nf2.setMinimumFractionDigits(2);
		nf2.setMaximumFractionDigits(2);

		TextView textValCmd = (TextView) findViewById(R.id.textValCmd);
		textValCmd.setText(nf2.format(totalComanda));

		TextView textValTransp = (TextView) findViewById(R.id.textValTransp);
		textValTransp.setText(nf2.format(Double.parseDouble((valTransp))));

		double totCmd = Double.parseDouble(valTransp) + totalComanda;

		TextView textTotCmd = (TextView) findViewById(R.id.textTotCmd);
		textTotCmd.setText(nf2.format(totCmd));

		Button btnOkCmd = (Button) findViewById(R.id.btnOkCmd);
		btnOkCmd.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				if (listener != null) {
					listener.opTransportComplete(true);
				}

				dismissThisDialog();

			}
		});

		Button btnCancelCmd = (Button) findViewById(R.id.btnCancelCmd);
		btnCancelCmd.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				if (listener != null) {
					listener.opTransportComplete(false);
				}

				dismissThisDialog();

			}
		});

	}

	public void setTransportDialogListener(PretTransportDialogListener listener) {
		this.listener = listener;
	}

}
