/**
 * @author florinb
 *
 */
package my.logon.screen.screens;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.model.ClientiGenericiGedInfoStrings;
import my.logon.screen.model.UserInfo;
import my.logon.screen.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ComenziConditionate extends Activity implements AsyncTaskListener {

	private Spinner spinnerCmdCond;
	public SimpleAdapter adapterComenziCond, adapterArtCmdCond;

	private static ArrayList<HashMap<String, String>> listComenziCond = new ArrayList<HashMap<String, String>>(),
			listArtCmdCond = new ArrayList<HashMap<String, String>>();
	public static String selectedCmd = "";
	ListView listViewArtCmdCond;
	private HashMap<String, String> artMap = null;

	private TextView textTipPlata, textTransport, textAdrLivr, textPersContact, textTelefon, textOras, textJudet, textComentarii;

	LinearLayout layoutCmdCondHead;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.activity_comenzi_conditionate);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Comenzi conditionate");
		actionBar.setDisplayHomeAsUpEnabled(true);

		spinnerCmdCond = (Spinner) findViewById(R.id.spinnerCmdCond);
		spinnerCmdCond.setVisibility(View.INVISIBLE);

		adapterComenziCond = new SimpleAdapter(this, listComenziCond, R.layout.list_comenzi_cond, new String[] { "idCmd", "codClient", "numeClient",
				"data", "suma", "cmdSAP", "ul", "agent" }, new int[] { R.id.textIdCmd, R.id.textCodClient, R.id.textClient, R.id.textData,
				R.id.textSuma, R.id.textCmdSAP, R.id.textUL, R.id.textAgent });

		spinnerCmdCond.setAdapter(adapterComenziCond);
		spinnerCmdCond.setOnItemSelectedListener(new MyOnItemSelectedListener());

		listViewArtCmdCond = (ListView) findViewById(R.id.listArtCmdCond);
		listViewArtCmdCond.setVisibility(View.INVISIBLE);

		adapterArtCmdCond = new SimpleAdapter(this, listArtCmdCond, R.layout.art_comenzi_cond, new String[] { "nrCrt", "numeArt", "codArt",
				"cantArt", "umArt", "pretArt", "monedaArt", "depozit", "status", "procent", "cantArtCond", "pretArtCond" }, new int[] {
				R.id.textNrCrt, R.id.textNumeArt, R.id.textCodArt, R.id.textCantArt, R.id.textUmArt, R.id.textPretArt, R.id.textMonedaArt,
				R.id.textDepozit, R.id.textStatusArt, R.id.textProcRed, R.id.textCantArtCond, R.id.textPretArtCond });

		listViewArtCmdCond.setAdapter(adapterArtCmdCond);

		textTipPlata = (TextView) findViewById(R.id.textTipPlata);
		textTransport = (TextView) findViewById(R.id.textTransport);

		textAdrLivr = (TextView) findViewById(R.id.textAdrLivr);
		textPersContact = (TextView) findViewById(R.id.textPersContact);
		textTelefon = (TextView) findViewById(R.id.textTelefon);
		textOras = (TextView) findViewById(R.id.textOras);
		textJudet = (TextView) findViewById(R.id.textJudet);
		textComentarii = (TextView) findViewById(R.id.textComentarii);

		layoutCmdCondHead = (LinearLayout) findViewById(R.id.layoutCmdCondHead);
		layoutCmdCondHead.setVisibility(View.INVISIBLE);

		performGetComenziConditii();

	}

	private void performGetComenziConditii() {

		try {

			HashMap<String, String> params = new HashMap<String, String>();

			String tipUser = "";
			String departUser = UserInfo.getInstance().getCodDepart();

			if (UserInfo.getInstance().getTipAcces().equals("10"))
				tipUser = "SD";
			else if (UserInfo.getInstance().getTipAcces().equals("12")) {
				tipUser = "DD";

				if (departUser.equals("00"))
					departUser = UserInfo.getInstance().getInitDivizie();
			} else if (UserInfo.getInstance().getTipAcces().equals("14"))
				tipUser = "DV";

			params.put("filiala", UserInfo.getInstance().getUnitLog());
			params.put("depart", departUser);
			params.put("tipUser", tipUser);
			params.put("codUser", UserInfo.getInstance().getCod());

			AsyncTaskWSCall call = new AsyncTaskWSCall(this, "getListComenziConditii", params);
			call.getCallResults();

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:

			UserInfo.getInstance().setParentScreen("");
			Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);

			startActivity(nextScreen);

			finish();

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	protected void populateCmdList(String cmdList) {

		if (!cmdList.equals("-1") && cmdList.length() > 0) {

			NumberFormat nf2 = NumberFormat.getInstance();
			nf2.setMinimumFractionDigits(2);
			nf2.setMaximumFractionDigits(2);

			listComenziCond.clear();
			spinnerCmdCond.setVisibility(View.VISIBLE);
			listViewArtCmdCond.setVisibility(View.VISIBLE);

			HashMap<String, String> temp;
			String[] tokenLinie = cmdList.split("@@");
			String[] tokenClient;
			String client = "", unLog = "";

			for (int i = 0; i < tokenLinie.length; i++) {
				temp = new HashMap<String, String>(20, 0.75f);
				client = tokenLinie[i];
				tokenClient = client.split("#");
				temp.put("idCmd", tokenClient[0] + ".");
				temp.put("numeClient", tokenClient[1]);
				temp.put("data", tokenClient[2]);
				temp.put("suma", nf2.format(Double.parseDouble(tokenClient[3])));
				temp.put("codClient", tokenClient[5]);

				if (!tokenClient[6].equals("-1")) {
					temp.put("cmdSAP", tokenClient[6]);
				} else
					temp.put("cmdSAP", " ");

				unLog = tokenClient[9];

				if (UserInfo.getInstance().getTipAcces().equals("10"))
					unLog = "";

				temp.put("ul", unLog);

				temp.put("agent", tokenClient[13]);

				listComenziCond.add(temp);

			}

			spinnerCmdCond.setAdapter(adapterComenziCond);
			layoutCmdCondHead.setVisibility(View.VISIBLE);

		} else {
			spinnerCmdCond.setVisibility(View.INVISIBLE);
			listViewArtCmdCond.setVisibility(View.INVISIBLE);
			layoutCmdCondHead.setVisibility(View.INVISIBLE);
			Toast.makeText(getApplicationContext(), "Nu exista comenzi!", Toast.LENGTH_SHORT).show();
		}

	}

	private void performArtCmd() {

		try {

			HashMap<String, String> params = new HashMap<String, String>();

			String tipUser = "";

			if (UserInfo.getInstance().getTipAcces().equals("10"))
				tipUser = "SD";

			if (UserInfo.getInstance().getTipAcces().equals("12") || UserInfo.getInstance().getTipAcces().equals("14"))
				tipUser = "DV";

			params.put("nrCmd", selectedCmd);
			params.put("afisCond", "1");
			params.put("tipUser", tipUser);

			AsyncTaskWSCall call = new AsyncTaskWSCall(this, "getCmdArt", params);
			call.getCallResults();

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	public class MyOnItemSelectedListener implements OnItemSelectedListener {

		@SuppressWarnings("unchecked")
		public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {

			artMap = (HashMap<String, String>) spinnerCmdCond.getSelectedItem();

			selectedCmd = artMap.get("idCmd").substring(0, artMap.get("idCmd").length() - 1);
			performArtCmd();

		}

		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}

	}

	private void populateArtCmdList(String articoleComanda) {

		listArtCmdCond.clear();
		adapterArtCmdCond.notifyDataSetChanged();

		NumberFormat nf2 = new DecimalFormat("#0.00");

		if (!articoleComanda.equals("-1") && articoleComanda.length() > 0) {
			HashMap<String, String> temp;
			String[] tokenMain = articoleComanda.split("@@");
			String[] tokenAntet = tokenMain[0].split("#");
			String tipPlata = "", tipTransport = "";

			listViewArtCmdCond.setVisibility(View.VISIBLE);

			// tip plata
			if (tokenAntet[4].equals("B")) {
				tipPlata = "Bilet la ordin";
			}
			if (tokenAntet[4].equals("C")) {
				tipPlata = "Cec";
			}
			if (tokenAntet[4].equals("E")) {
				tipPlata = "Plata in numerar";
			}
			if (tokenAntet[4].equals("L")) {
				tipPlata = "Plata interna buget-trezorerie";
			}
			if (tokenAntet[4].equals("O")) {
				tipPlata = "Ordin de plata";
			}
			if (tokenAntet[4].equals("U")) {
				tipPlata = "Plata interna-alte institutii";
			}
			if (tokenAntet[4].equals("W")) {
				tipPlata = "Plata in strainatate-banci";
			}
			if (!tipPlata.equals(""))
				textTipPlata.setText(tipPlata);

			if (tokenAntet[3].equals("TCLI")) {
				tipTransport = "Client";
			}
			if (tokenAntet[3].equals("TRAP")) {
				tipTransport = "Arabesque";
			}
			if (tokenAntet[3].equals("TERT")) {
				tipTransport = "Terti";
			}

			if (!tipTransport.equals(""))
				textTransport.setText(tipTransport);

			textAdrLivr.setText(tokenAntet[2]);
			textPersContact.setText(tokenAntet[0]);
			textTelefon.setText(tokenAntet[1]);
			textOras.setText(tokenAntet[7]);
			textJudet.setText(ClientiGenericiGedInfoStrings.numeJudet(tokenAntet[8]));

			int nrArt = Integer.parseInt(tokenAntet[9]); // nr. articole comanda

			String[] tokenArt, tokenArtCond;
			String client = "";
			String UmB = "", UmV = "", unitPret = "";

			int ii = 1;

			for (int i = 1; i <= nrArt; i++) {

				temp = new HashMap<String, String>();
				client = tokenMain[i];
				tokenArt = client.split("#");

				UmB = tokenArt[21];
				UmV = tokenArt[6];

				unitPret = "RON";
				if (!UmB.equals(UmV) && !UmB.trim().equals("")) {
					unitPret = "RON/" + System.getProperty("line.separator") + UmB;
				}

				temp.put("nrCrt", String.valueOf(i) + ".");
				temp.put("numeArt", tokenArt[2]);
				temp.put("codArt", tokenArt[1]);
				temp.put("cantArt", nf2.format(Float.parseFloat(tokenArt[3])));

				temp.put("umArt", tokenArt[6]);
				temp.put("pretArt", nf2.format(Float.parseFloat(tokenArt[5])));

				temp.put("monedaArt", unitPret);
				temp.put("depozit", tokenArt[4]);

				temp.put("status", " ");

				temp.put("procent", nf2.format(Float.parseFloat(tokenArt[7])));

				temp.put("cantUmb", tokenArt[20]);
				temp.put("Umb", tokenArt[21]);

				// -----adaugare articole conditii

				for (ii = nrArt + 2; ii < tokenMain.length; ii++) {

					client = tokenMain[ii];
					tokenArtCond = client.split("#");

					if (tokenArtCond[0].equalsIgnoreCase(tokenArt[1])) {
						temp.put("cantArtCond", nf2.format(Double.parseDouble(tokenArtCond[2])));
						temp.put("pretArtCond", nf2.format(Double.parseDouble(tokenArtCond[4])));
						break;
					}

				}

				// -----sf. articole conditii

				listArtCmdCond.add(temp);

			}// sf. for

			String[] antetCnd = tokenMain[nrArt + 1].split("#");
			textComentarii.setText(antetCnd[2].toString());

			listViewArtCmdCond.setAdapter(adapterArtCmdCond);

		}

	}

	@Override
	public void onBackPressed() {

		UserInfo.getInstance().setParentScreen("");
		Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);

		startActivity(nextScreen);

		finish();
		return;
	}

	public void onTaskComplete(String methodName, Object result) {
		if (methodName.equals("getListComenziConditii")) {
			populateCmdList((String) result);
		}

		if (methodName.equals("getCmdArt")) {
			populateArtCmdList((String) result);
		}

	}

}
