package my.logon.screen.dialogs;

import java.util.ArrayList;
import java.util.HashMap;

import my.logon.screen.listeners.TipCmdDistribListener;
import my.logon.screen.R;
import my.logon.screen.model.UserInfo;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import my.logon.screen.enums.TipCmdDistrib;

public class TipComandaDistributieDialog extends Dialog {

	private TipCmdDistribListener listener;
	private Context context;
	private TipCmdDistrib tipComanda = TipCmdDistrib.COMANDA_VANZARE;
	private String codFilialaDest = "";

	private String[] numeFiliala = { "Bacau", "Baia Mare", "Brasov", "Buzau", "Brasov-central", "Buc. Andronache", "Buc. Militari", "Buc. Otopeni",
			"Buc. Glina", "Constanta", "Cluj", "Craiova", "Focsani", "Galati", "Hunedoara", "Iasi", "Oradea", "Piatra Neamt", "Pitesti", "Ploiesti", "Sibiu",
			"Timisoara", "Tg. Mures" };

	private String[] codFiliala = { "BC10", "MM10", "BV10", "BZ10", "BV90", "BU13", "BU11", "BU12", "BU10", "CT10", "CJ10", "DJ10", "VN10", "GL10", "HD10",
			"IS10", "BH10", "NT10", "AG10", "PH10", "SB10", "TM10", "MS10" };

	public TipComandaDistributieDialog(Context context) {
		super(context);
		this.context = context;

	}

	public void showDialog() {
		setUpLayout();
		this.show();

	}

	private void setUpLayout() {
		setContentView(R.layout.tipcomandadistributiedialog);
		setTitle("Selectati tipul de comanda");
		setCancelable(false);

		final RadioButton radioCV = (RadioButton) findViewById(R.id.radioCV);
		final RadioButton radioDL = (RadioButton) findViewById(R.id.radioDL);
		final RadioButton radioCC = (RadioButton) findViewById(R.id.radioCC);
		final RadioButton radioCLP = (RadioButton) findViewById(R.id.radioCLP);

		final Spinner spinnerFilialeClp = (Spinner) findViewById(R.id.spinFilialaCLP);

		ArrayList<HashMap<String, String>> listFiliale = new ArrayList<HashMap<String, String>>();
		final SimpleAdapter adapterFiliale = new SimpleAdapter(context, listFiliale, R.layout.rowlayoutjudete, new String[] { "numeJudet", "codJudet" },
				new int[] { R.id.textNumeJudet, R.id.textCodJudet });

		fillFiliale(listFiliale);
		spinnerFilialeClp.setAdapter(adapterFiliale);

		Button btnOkTipCmd = (Button) findViewById(R.id.btnOkTipCmd);
		btnOkTipCmd.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				if (radioCV.isChecked())
					tipComanda = TipCmdDistrib.COMANDA_VANZARE;
				else if (radioDL.isChecked())
					tipComanda = TipCmdDistrib.DISPOZITIE_LIVRARE;
				else if (radioCC.isChecked())
					tipComanda = TipCmdDistrib.LIVRARE_CUSTODIE;
				else if (radioCLP.isChecked()) {
					if (spinnerFilialeClp.getSelectedItemPosition() == 0) {
						Toast.makeText(context, "Selectati filiala", Toast.LENGTH_LONG).show();
						return;
					}

					@SuppressWarnings("unchecked")
					HashMap<String, String> artMap = (HashMap<String, String>) adapterFiliale.getItem(spinnerFilialeClp.getSelectedItemPosition());
					codFilialaDest = artMap.get("codJudet");
					tipComanda = TipCmdDistrib.COMANDA_LIVRARE;

				}

				if (listener != null)
					listener.tipComandaSelected(tipComanda, codFilialaDest);
				dismiss();
			}
		});

		radioCV.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				spinnerFilialeClp.setVisibility(View.INVISIBLE);

			}

		});

		radioDL.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				spinnerFilialeClp.setVisibility(View.INVISIBLE);

			}

		});

		radioCC.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				spinnerFilialeClp.setVisibility(View.INVISIBLE);

			}

		});

		radioCLP.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				spinnerFilialeClp.setVisibility(View.VISIBLE);

			}

		});

	}

	public void setTipCmdDistribListener(TipCmdDistribListener listener) {
		this.listener = listener;
	}

	private void fillFiliale(ArrayList<HashMap<String, String>> listFiliale) {

		HashMap<String, String> temp;
		int i = 0;

		temp = new HashMap<String, String>();
		temp.put("numeJudet", "Selectati filiala");
		temp.put("codJudet", "");
		listFiliale.add(temp);

		for (i = 0; i < numeFiliala.length; i++) {

			if (!codFiliala[i].equals(UserInfo.getInstance().getUnitLog())) {
				temp = new HashMap<String, String>();
				temp.put("numeJudet", numeFiliala[i]);
				temp.put("codJudet", codFiliala[i]);
				listFiliale.add(temp);
			}

		}

	}

}
