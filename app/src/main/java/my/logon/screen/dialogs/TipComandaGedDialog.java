package my.logon.screen.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import my.logon.screen.R;
import my.logon.screen.enums.EnumComenziDAO;
import my.logon.screen.enums.TipCmdGed;
import my.logon.screen.listeners.ComenziDAOListener;
import my.logon.screen.listeners.TipCmdGedListener;
import my.logon.screen.model.ComenziDAO;
import my.logon.screen.model.UserInfo;
import my.logon.screen.utils.UtilsUser;

public class TipComandaGedDialog extends Dialog implements ComenziDAOListener {

	private TipCmdGedListener listener;
	private Context context;
	private TipCmdGed tipComanda = TipCmdGed.COMANDA_VANZARE;
	private ComenziDAO comandaDAO;
	private Spinner spinnerComenziAmob;
	private String idComanda;
	private String codFilialaDest = "";

	private String[] numeFiliala = { "Bacau", "Baia Mare", "Brasov", "Buzau", "Brasov-central", "Buc. Andronache", "Buc. Militari", "Buc. Otopeni",
			"Buc. Glina", "Constanta", "Cluj", "Craiova", "Focsani", "Galati", "Hunedoara", "Iasi", "Oradea", "Piatra Neamt", "Pitesti", "Ploiesti",
			"Sibiu", "Suceava", "Timisoara", "Tg. Mures" };

	private String[] codFiliala = { "BC10", "MM10", "BV10", "BZ10", "BV90", "BU13", "BU11", "BU12", "BU10", "CT10", "CJ10", "DJ10", "VN10", "GL10",
			"HD10", "IS10", "BH10", "NT10", "AG10", "PH10", "SB10", "SV10", "TM10", "MS10" };

	public TipComandaGedDialog(Context context) {
		super(context);
		this.context = context;

	}

	public void showDialog() {
		setUpLayout();

		comandaDAO = ComenziDAO.getInstance(context);
		comandaDAO.setComenziDAOListener(TipComandaGedDialog.this);

		this.show();

	}

	private void setUpLayout() {
		setContentView(R.layout.tipcomandageddialog);
		setTitle("Selectati tipul de comanda");
		setCancelable(true);

		final RadioButton radioNoua = (RadioButton) findViewById(R.id.radioNoua);
		final RadioButton radioAmob = (RadioButton) findViewById(R.id.radioAmob);
		final RadioButton radioCLP = (RadioButton) findViewById(R.id.radioCLP);
		final RadioButton radioDL = (RadioButton) findViewById(R.id.radioDL);
		final RadioButton radioACZC = (RadioButton) findViewById(R.id.radioACZC);

		if (UtilsUser.isUserIP())
			radioCLP.setVisibility(View.INVISIBLE);

		final Spinner spinnerFilialeClp = (Spinner) findViewById(R.id.spinFilialaCLP);
		final TextView textInfoClp = (TextView) findViewById(R.id.textInfoClp);

		ArrayList<HashMap<String, String>> listFiliale = new ArrayList<HashMap<String, String>>();
		final SimpleAdapter adapterFiliale = new SimpleAdapter(context, listFiliale, R.layout.rowlayoutjudete,
				new String[] { "numeJudet", "codJudet" }, new int[] { R.id.textNumeJudet, R.id.textCodJudet });

		fillFiliale(listFiliale);
		spinnerFilialeClp.setAdapter(adapterFiliale);

		spinnerComenziAmob = (Spinner) findViewById(R.id.spinComenziAmob);
		setSpinnerAmobListener();

		Button btnOkTipCmd = (Button) findViewById(R.id.btnOkTipCmd);
		btnOkTipCmd.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				if (radioNoua.isChecked())
					tipComanda = TipCmdGed.COMANDA_VANZARE;
				else if (radioDL.isChecked())
					tipComanda = TipCmdGed.DISPOZITIE_LIVRARE;
				else if (radioACZC.isChecked())
					tipComanda = TipCmdGed.ARTICOLE_COMANDA;
				else if (radioAmob.isChecked()) {
					tipComanda = TipCmdGed.COMANDA_AMOB;
					if (idComanda.equals("-1"))
						return;

				} else if (radioCLP.isChecked()) {
					if (spinnerFilialeClp.getSelectedItemPosition() == 0) {
						Toast.makeText(context, "Selectati filiala", Toast.LENGTH_LONG).show();
						return;
					}

					@SuppressWarnings("unchecked")
					HashMap<String, String> artMap = (HashMap<String, String>) adapterFiliale.getItem(spinnerFilialeClp.getSelectedItemPosition());
					codFilialaDest = artMap.get("codJudet");
					tipComanda = TipCmdGed.COMANDA_LIVRARE;

				}

				if (listener != null)
					listener.tipComandaSelected(tipComanda, idComanda, codFilialaDest);

				dismiss();

			}
		});

		radioNoua.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				spinnerComenziAmob.setVisibility(View.INVISIBLE);
				spinnerFilialeClp.setVisibility(View.INVISIBLE);
				textInfoClp.setVisibility(View.INVISIBLE);

			}

		});

		radioCLP.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				spinnerFilialeClp.setVisibility(View.VISIBLE);
				textInfoClp.setVisibility(View.VISIBLE);

			}

		});

		radioDL.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				spinnerComenziAmob.setVisibility(View.INVISIBLE);
				spinnerFilialeClp.setVisibility(View.INVISIBLE);
				textInfoClp.setVisibility(View.INVISIBLE);

			}

		});

		radioAmob.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				spinnerComenziAmob.setVisibility(View.VISIBLE);
				getComenziAMOB();

			}

		});

		radioACZC.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				spinnerComenziAmob.setVisibility(View.INVISIBLE);
				spinnerFilialeClp.setVisibility(View.INVISIBLE);
				textInfoClp.setVisibility(View.INVISIBLE);

			}

		});

	}

	private void setSpinnerAmobListener() {
		spinnerComenziAmob.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	private void getComenziAMOB() {

	}

	public void setTipCmdGedListener(TipCmdGedListener listener) {
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

	@Override
	public void operationComenziComplete(EnumComenziDAO methodName, Object result) {

	}

}
