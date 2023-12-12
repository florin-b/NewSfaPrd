/**
 * @author florinb
 *
 */
package my.logon.screen.screens;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import my.logon.screen.R;
import my.logon.screen.adapters.ComenziBlocateAdapter;
import my.logon.screen.beans.BeanComandaCreata;
import my.logon.screen.enums.EnumComenziDAO;
import my.logon.screen.listeners.ComenziDAOListener;
import my.logon.screen.model.ComenziDAO;
import my.logon.screen.model.ClientiGenericiGedInfoStrings;
import my.logon.screen.model.UserInfo;

public class ComenziBlocateLimCredit extends Activity implements ComenziDAOListener {

	Button refreshCmdBtn;
	String filiala = "", nume = "", cod = "";
	public static String unitLog = "";
	public static String numeDepart = "";
	public static String codDepart = "";
	private SimpleAdapter adapter;

	private Spinner spinnerComenzi;
	public SimpleAdapter adapterArtCond;
	private static ArrayList<HashMap<String, String>> listComenzi = new ArrayList<HashMap<String, String>>();
	private static ArrayList<HashMap<String, String>> arrayListArticole = new ArrayList<HashMap<String, String>>();
	public static String selectedCmd = "";

	private String selectedClientCode = "-1";

	private TextView textTipPlata, textAdrLivr, textTotalCmd, textOrasModif, textJudetModif;
	private TextView textPersContact, textTelefon, textCantar, textTransport, textFactRed;

	public static String codClientVar = "";
	public static String numeClientVar = "";
	public static String articoleComanda = "", numeArtSelContextMenu = "", codArtSelContextMenu = "";
	public static double totalComanda = 0, stocArtCond = 0;
	public static String dateLivrare = "";

	public static String tipAcces;
	private ListView listViewArticole;

	private ComenziDAO comenziDAO;
	private LinearLayout layoutDetaliiComanda;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.comenziblocatelimcredit);

		comenziDAO = ComenziDAO.getInstance(this);
		comenziDAO.setComenziDAOListener(this);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Comenzi blocate pt. limita de credit");
		actionBar.setDisplayHomeAsUpEnabled(true);

		checkStaticVars();

		spinnerComenzi = (Spinner) findViewById(R.id.spinnerCmd);

		spinnerComenzi.setOnItemSelectedListener(new MyOnItemSelectedListener());
		spinnerComenzi.setVisibility(View.INVISIBLE);

		listViewArticole = (ListView) findViewById(R.id.listArtModif);

		this.refreshCmdBtn = (Button) findViewById(R.id.refreshCmdBtn);
		addListenerRefreshCmdBtn();

		adapter = new SimpleAdapter(this, arrayListArticole, R.layout.customrowcmdsim, new String[] { "nrCrt", "numeArt", "codArt", "cantArt", "umArt",
				"pretArt", "monedaArt", "depozit", "status", "procent", "procFact", "zDis", "tipAlert", "procAprob" }, new int[] { R.id.textNrCrt,
				R.id.textNumeArt, R.id.textCodArt, R.id.textCantArt, R.id.textUmArt, R.id.textPretArt, R.id.textMonedaArt, R.id.textDepozit,
				R.id.textStatusArt, R.id.textProcRed, R.id.textProcFact, R.id.textZDIS, R.id.textAlertUsr, R.id.textProcAprobModif });

		listViewArticole.setAdapter(adapter);
		listViewArticole.setVisibility(View.INVISIBLE);

		addListenerListArtModif();
		registerForContextMenu(listViewArticole);

		textTipPlata = (TextView) findViewById(R.id.textTipPlata);
		textAdrLivr = (TextView) findViewById(R.id.textAdrLivr);
		textPersContact = (TextView) findViewById(R.id.textPersContact);
		textTelefon = (TextView) findViewById(R.id.textTelefon);
		textCantar = (TextView) findViewById(R.id.textCantar);
		textTransport = (TextView) findViewById(R.id.textTransport);

		textTotalCmd = (TextView) findViewById(R.id.textTotalCmd);

		textFactRed = (TextView) findViewById(R.id.textFactRed);

		textOrasModif = (TextView) findViewById(R.id.textOrasModif);
		textJudetModif = (TextView) findViewById(R.id.textJudetModif);

		textTotalCmd.setVisibility(View.GONE);

		textFactRed.setVisibility(View.GONE);

		textCantar.setText("");
		textFactRed.setText("");

		layoutDetaliiComanda = (LinearLayout) findViewById(R.id.layoutDetaliiComanda);
		layoutDetaliiComanda.setVisibility(View.INVISIBLE);

		refreshCommandList();

	}

	@Override
	public void onResume() {
		super.onResume();
		checkStaticVars();
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case android.R.id.home:

			UserInfo.getInstance().setParentScreen("");
			arrayListArticole.clear();
			listComenzi.clear();

			Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
			startActivity(nextScreen);
			finish();
			return true;

		}
		return false;
	}

	public void addListenerRefreshCmdBtn() {
		refreshCmdBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				clearAllData();
				refreshCommandList();

			}
		});

	}

	private void refreshCommandList() {

		try {

			String tipUser = "AV";

			if (UserInfo.getInstance().getTipAcces().equals("9"))
				tipUser = "AV";

			else if (UserInfo.getInstance().getTipAcces().equals("10"))
				tipUser = "SD";

			else if (UserInfo.getInstance().getTipAcces().equals("12") || UserInfo.getInstance().getTipAcces().equals("14"))
				tipUser = "DV";

			else if (UserInfo.getInstance().getTipAcces().equals("27"))
				tipUser = "KA";
			
			else if (UserInfo.getInstance().getTipAcces().equals("32") || UserInfo.getInstance().getTipUserSap().equals("SDKA"))
				tipUser = "SK";				

			else if (UserInfo.getInstance().getTipAcces().equals("35"))
				tipUser = "DK";

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("filiala", UserInfo.getInstance().getUnitLog());
			params.put("codUser", UserInfo.getInstance().getCod());
			params.put("tipCmd", "3"); // blocate lim. credit
			params.put("depart", UserInfo.getInstance().getCodDepart());
			params.put("tipUser", tipUser);

			comenziDAO.getListComenzi(params);

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	public class MyOnItemSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
			BeanComandaCreata comanda = ((BeanComandaCreata) parent.getAdapter().getItem(pos));

			if (comanda != null)
				getArtSelCmd(comanda);

		}

		public void onNothingSelected(AdapterView<?> arg0) {

		}

	}

	private void getArtSelCmd(BeanComandaCreata comanda) {

		textTipPlata.setText("");
		textAdrLivr.setText("");
		textPersContact.setText("");
		textTelefon.setText("");
		textCantar.setText("");
		textTransport.setText("");

		textTotalCmd.setVisibility(View.INVISIBLE);
		textTotalCmd.setText("0.00");

		selectedCmd = comanda.getId();
		totalComanda = Double.parseDouble(comanda.getSuma().replace(",", ""));
		textTotalCmd.setText(String.format("%.02f", totalComanda));
		selectedClientCode = comanda.getCodClient();
		codClientVar = selectedClientCode;
		numeClientVar = comanda.getNumeClient();

		performArtCmd();

		textTipPlata.setVisibility(View.VISIBLE);
		textAdrLivr.setVisibility(View.VISIBLE);
		textPersContact.setVisibility(View.VISIBLE);
		textTelefon.setVisibility(View.VISIBLE);
		textCantar.setVisibility(View.VISIBLE);
		textTransport.setVisibility(View.VISIBLE);

	}

	private void performArtCmd() {

		try {

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("nrCmd", selectedCmd);
			params.put("afisCond", "1");

			comenziDAO.getArticoleComanda(params);

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	private void populateArtCmdList(String articoleComanda) {

		arrayListArticole.clear();
		adapter.notifyDataSetChanged();

		if (!articoleComanda.equals("-1") && articoleComanda.length() > 0) {
			HashMap<String, String> temp;
			String[] tokenMain = articoleComanda.split("@@");
			String[] tokenAntet = tokenMain[0].split("#");
			String tipPlata = "", tipTransport = "", cantar = "", factRed = "";

			listViewArticole.setVisibility(View.VISIBLE);
			layoutDetaliiComanda.setVisibility(View.VISIBLE);

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
				tipTransport = "Transport client";
			}
			if (tokenAntet[3].equals("TRAP")) {
				tipTransport = "Transport propriu";
			}
			if (tokenAntet[3].equals("TERT")) {
				tipTransport = "Transport terti";
			}

			if (!tipTransport.equals(""))
				textTransport.setText(tipTransport);

			if (tokenAntet[5].equals("1")) {
				cantar = "DA";
			}
			if (tokenAntet[5].equals("0")) {
				cantar = "NU";
			}

			if (!cantar.equals(""))
				textCantar.setText(cantar);

			if (tokenAntet[6].equals("X")) {
				factRed = "2 facturi";
			}
			if (tokenAntet[6].equals(" ")) {
				factRed = "1 factura (red. in pret)";
			}
			if (tokenAntet[6].equals("R")) {
				factRed = "1 factura (red. separat)";
			}

			if (!factRed.equals(""))
				textFactRed.setText(factRed);

			textAdrLivr.setText(tokenAntet[2]);
			textPersContact.setText(tokenAntet[0]);
			textTelefon.setText(tokenAntet[1]);
			textOrasModif.setText(tokenAntet[7]);
			textJudetModif.setText(ClientiGenericiGedInfoStrings.numeJudet(tokenAntet[8]));

			int nrArt = Integer.parseInt(tokenAntet[9]); // nr. articole comanda

			String[] tokenArt;
			String client = "";
			String statusArt = " ";

			for (int i = 1; i <= nrArt; i++) {

				statusArt = "";

				temp = new HashMap<String, String>();
				client = tokenMain[i];
				tokenArt = client.split("#");

				temp.put("nrCrt", String.valueOf(i) + ".");
				temp.put("numeArt", tokenArt[2]);
				temp.put("codArt", tokenArt[1]);
				temp.put("cantArt", String.format("%.02f", Float.parseFloat(tokenArt[3])).toString());
				temp.put("umArt", tokenArt[6]);
				temp.put("pretArt", String.format("%.02f", Float.parseFloat(tokenArt[5])).toString());

				temp.put("monedaArt", "RON");
				temp.put("depozit", tokenArt[4]);

				if (tokenArt[0].equals("9")) {
					statusArt = "Stoc insuficient";
				}
				if (tokenArt[0].equals("19")) {
					statusArt = "Articol fara pret";
				}
				if (tokenArt[0].equals("16")) {
					statusArt = "Articol modificat";
				}
				if (tokenArt[0].equals("17")) {
					statusArt = "Articol adaugat";
				}
				if (tokenArt[0].equals("18")) {
					statusArt = "Articol sters";
				}

				if (!statusArt.equals("")) {
					temp.put("status", statusArt);
				} else {
					temp.put("status", " ");
				}

				arrayListArticole.add(temp);

			}// sf. for

			listViewArticole.setAdapter(adapter);
			textTotalCmd.setVisibility(View.VISIBLE);
			textFactRed.setVisibility(View.VISIBLE);

		}

	}

	protected void populateCmdList(List<BeanComandaCreata> listComenziCreate) {

		listComenzi.clear();

		if (listComenziCreate.size() > 0) {
			ComenziBlocateAdapter adapter = new ComenziBlocateAdapter(this, listComenziCreate);
			spinnerComenzi.setAdapter(adapter);
			spinnerComenzi.setVisibility(View.VISIBLE);

		} else {
			Toast.makeText(getApplicationContext(), "Nu exista comenzi!", Toast.LENGTH_SHORT).show();
			spinnerComenzi.setVisibility(View.INVISIBLE);
			layoutDetaliiComanda.setVisibility(View.INVISIBLE);
		}

	}

	boolean isDirectorOrSd() {
		return UserInfo.getInstance().getTipAcces().equals("10") || UserInfo.getInstance().getTipAcces().equals("12")
				|| UserInfo.getInstance().getTipAcces().equals("14");
	}

	boolean isDirector() {
		return UserInfo.getInstance().getTipAcces().equals("12") || UserInfo.getInstance().getTipAcces().equals("14");
	}

	public void addListenerListArtModif() {
		listViewArticole.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

				return false;

			}
		});
	}

	private void clearAllData() {
		// curatenie ecran
		arrayListArticole.clear();
		adapter.notifyDataSetChanged();

		textTotalCmd.setText("");
		textTipPlata.setText("");
		textAdrLivr.setText("");
		textPersContact.setText("");
		textTelefon.setText("");
		textCantar.setText("");
		textTransport.setText("");
		textOrasModif.setText("");
		textJudetModif.setText("");

		textTotalCmd.setVisibility(View.GONE);
		textTipPlata.setVisibility(View.GONE);
		textAdrLivr.setVisibility(View.GONE);
		textPersContact.setVisibility(View.GONE);
		textTelefon.setVisibility(View.GONE);
		textCantar.setVisibility(View.GONE);
		textTransport.setVisibility(View.GONE);

		textFactRed.setVisibility(View.GONE);

		numeClientVar = "";
		articoleComanda = "";
		dateLivrare = "";

		articoleComanda = "";

		numeClientVar = "";
		codClientVar = "";
		totalComanda = 0;

	}

	private void checkStaticVars() {

		String locLang = getBaseContext().getResources().getConfiguration().locale.getLanguage();

		if (!locLang.toLowerCase(Locale.getDefault()).equals("en")) {

			String languageToLoad = "en";
			Locale locale = new Locale(languageToLoad);
			Locale.setDefault(locale);
			Configuration config = new Configuration();
			config.locale = locale;
			getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
		}

		// restart app la idle
		if (UserInfo.getInstance().getCod().equals("")) {

			Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
		}

	}

	@Override
	public void onBackPressed() {
		UserInfo.getInstance().setParentScreen("");
		arrayListArticole.clear();
		listComenzi.clear();

		Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
		startActivity(nextScreen);
		finish();
		return;
	}

	@SuppressWarnings("unchecked")
	public void operationComenziComplete(EnumComenziDAO methodName, Object result) {
		switch (methodName) {
		case GET_LIST_COMENZI:
			populateCmdList((List<BeanComandaCreata>) result);
			break;
		case GET_ARTICOLE_COMANDA:
			populateArtCmdList((String) result);
			break;
		default:
			break;

		}

	}

}