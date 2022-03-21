package my.logon.screen.dialogs;

import java.text.NumberFormat;
import java.util.Locale;

import my.logon.screen.listeners.CostMacaraListener;
import my.logon.screen.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import my.logon.screen.beans.CostDescarcare;

public class CostMacaraDialog extends Dialog {

	private CostMacaraListener listener;
	private CostDescarcare costDescarcare;
	private boolean permiteModif;

	private Context context;

	public CostMacaraDialog(Context context, CostDescarcare costDescarcare, boolean permiteModif) {
		super(context);
		this.context = context;

		this.costDescarcare = costDescarcare;
		this.permiteModif = permiteModif;

		setContentView(R.layout.cost_macara_dialog);
		setTitle("Confirmare cost macara");

		setUpLayout();
	}

	void setUpLayout() {
		NumberFormat nf2 = NumberFormat.getInstance(Locale.US);
		nf2.setMinimumFractionDigits(2);
		nf2.setMaximumFractionDigits(2);

		StringBuilder strCost = new StringBuilder();
		strCost.append("Utilizarea macaralei presupune un cost suplimentar de ");
		strCost.append(nf2.format(costDescarcare.getValoareDescarcare()));
		strCost.append(" RON. Sunteti de acord? ");

		TextView textCostMacara = (TextView) findViewById(R.id.textCost);
		textCostMacara.setText(strCost.toString());

		LinearLayout layoutCostMacara = (LinearLayout) findViewById(R.id.layoutCostMacara);
		TextView textCostMin = (TextView) findViewById(R.id.textCostMinim);

		if (permiteModif) {
			layoutCostMacara.setVisibility(View.VISIBLE);
			textCostMin.setVisibility(View.VISIBLE);
			textCostMin.setText("Cost minim acceptat: " + nf2.format(costDescarcare.getValoareMinDescarcare()) + " RON");
		}

		Button btnOkCmd = (Button) findViewById(R.id.btnOkCmd);
		btnOkCmd.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				if (!isCostDescarcareCorect()) {
					Toast.makeText(context, "Costul este mai mic decat minimul acceptat.", Toast.LENGTH_LONG).show();
					return;
				} else {
					if (listener != null)
						listener.acceptaCostMacara(true, getCostDescarcare());

					dismiss();
				}

			}
		});

		Button btnCancelCmd = (Button) findViewById(R.id.btnCancelCmd);
		btnCancelCmd.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (listener != null)
					listener.acceptaCostMacara(false, 0);

				dismiss();

			}
		});

	}

	private boolean isCostDescarcareCorect() {
		if (!permiteModif)
			return true;
		else {
			String strCostDesc = ((TextView) findViewById(R.id.valoareDescarcare)).getText().toString();

			if (!strCostDesc.isEmpty())
				return Double.parseDouble(strCostDesc) >= costDescarcare.getValoareMinDescarcare();
			else
				return true;

		}

	}

	private double getCostDescarcare() {
		if (!permiteModif)
			return costDescarcare.getValoareDescarcare();
		else {
			String strCostDesc = ((TextView) findViewById(R.id.valoareDescarcare)).getText().toString();

			if (!strCostDesc.isEmpty())
				return Double.parseDouble(strCostDesc);
			else
				return costDescarcare.getValoareDescarcare();

		}
	}

	public void setCostMacaraListener(CostMacaraListener listener) {
		this.listener = listener;
	}

}
