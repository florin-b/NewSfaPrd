package my.logon.screen.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import my.logon.screen.R;
import my.logon.screen.adapters.AdapterPaleti;
import my.logon.screen.beans.ArticolPalet;
import my.logon.screen.enums.EnumPaleti;
import my.logon.screen.listeners.PaletiListener;

public class CostPaletiDialog extends Dialog {

	private Context context;
	private ListView spinnerPaleti;
	private AdapterPaleti adapterPaleti;
	private List<ArticolPalet> listPaleti;
	private PaletiListener listener;
	private String tipTransport;
	private Button btnAdaugaPaleti;
	private TextView textValPalet;
	private Button btnRenuntaPaleti;
	private TextView textPaletSel;
	private EditText textCantPaletSel;
	private LinearLayout layoutPaletSel;
	private ArticolPalet articol;

	public CostPaletiDialog(Context context, List<ArticolPalet> listPaleti, String tipTransport) {
		super(context);
		this.context = context;
		this.listPaleti = listPaleti;
		this.tipTransport = tipTransport;

		setContentView(R.layout.select_paleti_dialog);
		setTitle("Adaugare paleti");
		setCancelable(false);

		setUpLayout();

	}

	public void showDialog() {
		this.show();
	}

	private void setUpLayout() {

		textPaletSel = (TextView) findViewById(R.id.textPaletSel);
		textCantPaletSel = (EditText) findViewById(R.id.textCantPaletSel);
		setListenerTextCant();

		layoutPaletSel = (LinearLayout) findViewById(R.id.layoutPaletSel);

		spinnerPaleti = (ListView) findViewById(R.id.spinnerPaleti);

		LinearLayout paletiLayout = (LinearLayout) findViewById(R.id.layoutPaleti);

		paletiLayout.getLayoutParams().height = (int) (getScreenHeight(context) * 0.38);

		adapterPaleti = new AdapterPaleti(context, listPaleti);
		spinnerPaleti.setAdapter(adapterPaleti);
		setSpinnerListener();

		textValPalet = (TextView) findViewById(R.id.textValPalet);
		setTotalPaleti();

		btnAdaugaPaleti = (Button) findViewById(R.id.btnOkPalet);
		addBtnAcceptaListener();

		btnRenuntaPaleti = (Button) findViewById(R.id.btnCancelPalet);
		btnRenuntaPaleti.setVisibility(View.INVISIBLE);

		addBtnRespingeListener();

	}

	private void setSpinnerListener() {
		spinnerPaleti.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				articol = (ArticolPalet) parent.getAdapter().getItem(position);

				layoutPaletSel.setVisibility(View.VISIBLE);

				textPaletSel.setText(articol.getNumePalet());
				textCantPaletSel.setText(String.valueOf(articol.getCantitate()));

			}
		});
	}

	private void setListenerTextCant() {

		textCantPaletSel.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				if (s.toString().length() > 0) {
					articol.setCantitate(Integer.parseInt(s.toString()));
					adapterPaleti.notifyDataSetChanged();
					setTotalPaleti();
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				

			}

			@Override
			public void afterTextChanged(Editable s) {
				

			}
		});

	}

	public static int getScreenHeight(Context context) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		return displayMetrics.heightPixels;
	}

	private void setTotalPaleti() {
		double cantTotal = 0;
		double valTotal = 0;
		NumberFormat nf2 = new DecimalFormat("#0.00");

		for (ArticolPalet palet : listPaleti) {
			cantTotal += palet.getCantitate();
			valTotal += palet.getCantitate() * palet.getPretUnit();

		}

		textValPalet.setText((int) cantTotal + " BUC   " + nf2.format(valTotal) + " RON");

	}

	private void addBtnAcceptaListener() {
		btnAdaugaPaleti.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				if (listener != null && verificaNrPaleti()) {

					for (ArticolPalet palet : listPaleti)
						listener.paletiStatus(EnumPaleti.ACCEPTA, palet);

					listener.paletiStatus(EnumPaleti.FINALIZEAZA, null);
					dismiss();

				}

			}
		});
	}

	private boolean verificaNrPaleti(){

		if (tipTransport.equals("TCLI"))
			return true;

		int nrPaleti = 0;

		for (ArticolPalet palet : listPaleti)
			nrPaleti += palet.getCantitate();

		if (nrPaleti > 0)
			return true;
		else {
			Toast.makeText(context, "Adaugati cel putin un palet.", Toast.LENGTH_LONG).show();
			return false;
		}

	}

	private void addBtnRespingeListener() {
		btnRenuntaPaleti.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.paletiStatus(EnumPaleti.RESPINGE, null);
					dismiss();

				}

			}

		});
	}

	public void setPaletiDialogListener(PaletiListener listener) {
		this.listener = listener;
	}

}
