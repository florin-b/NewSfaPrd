/**
 * @author florinb
 *
 */
package my.logon.screen.screens;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import my.logon.screen.R;
import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.model.UserInfo;

public class ReduceriUlterioare extends Activity implements AsyncTaskListener {

	Button stocBtn, clientBtn, articoleBtn, livrareBtn, saveRedBtn;
	String filiala = " ", nume = " ", codAg = " ";
	String codClient = " ", numeClient = " ";

	TextView textTipReducere, textTipFrecventa, textStartValabil, textStopValabil, labelValTotDepHead, textValTotDepHead;
	TextView labelTipReducere, labelTipFrecventa, labelStartValabil;
	TextView labelProcRedB5, textProcRedB5, labelCoefCalit, textCoefCalitHead;

	private SimpleAdapter adapter, adapter2;

	private static ArrayList<HashMap<String, String>> list1 = new ArrayList<HashMap<String, String>>();
	private static ArrayList<HashMap<String, String>> list2 = new ArrayList<HashMap<String, String>>();
	public static String codClientRed = "";
	public static String numeClientRed = "";
	public static String articoleReduceri = "";
	public static String exceptii = "";
	public static String numeDepart = "";
	public static String codDepart = "";
	public static String unitLog = "";
	public static String redFinal = "";
	public static String clienti = "";
	public static String tipReducere = "";
	public static String frecventaRedNume = "";
	public static String frecventaRedCod;
	public static String startValabil = "";
	public static String stopValabil = "";
	public static String procRedB5 = "-1";
	public static String coefCalit = "-1";
	public static String codReducere = "-1";
	public static String valTotDepart = "-1";
	public static boolean selTotDepart = false;
	public static boolean allowSintExc = false;
	public static boolean allowArtExc = false;

	private String reducereFinalaStr = "";
	private String articoleFinaleStr = "";
	private String clientFinalStr = "";

	private ProgressBar mProgress;
	private Timer myTimer;
	private int progressVal = 0;
	private Handler logonHandler = new Handler();
	String tipAcces;
	ListView clientListView, artListView;
	private int listViewSelClient = -1;
	private int listViewSelArt = -1;
	public static boolean selTipClient = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.reduceriulterioareheader);

		checkStaticVars();

		artListView = (ListView) findViewById(R.id.listArt);

		adapter = new SimpleAdapter(this, list1, R.layout.customrowarticolreducere, new String[] { "numeArt", "codArt", "cantArt", "umArt", "procent",
				"lprocent", "tipArt", "obs1", "obs2", "obs3" }, new int[] { R.id.textNumeArt, R.id.textCodArt, R.id.textCantArt, R.id.textUmArt,
				R.id.textProcent, R.id.labelProcent, R.id.textTipArt, R.id.textData1, R.id.textData2, R.id.textData3 }

		);

		artListView.setAdapter(adapter);
		artListView.setClickable(true);
		addListenerArtList();
		registerForContextMenu(artListView);

		this.saveRedBtn = (Button) findViewById(R.id.saveRedBtn);
		addListenerSaveRedBtn();

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Creare sablon");
		actionBar.setDisplayHomeAsUpEnabled(true);

		textTipReducere = (TextView) findViewById(R.id.textTipReducere);
		textTipFrecventa = (TextView) findViewById(R.id.textTipFrecventa);
		textStartValabil = (TextView) findViewById(R.id.textStartValabil);
		textStopValabil = (TextView) findViewById(R.id.textStopValabil);
		textCoefCalitHead = (TextView) findViewById(R.id.textCoefCalitHead);
		textValTotDepHead = (TextView) findViewById(R.id.textValTotDepHead);

		labelTipReducere = (TextView) findViewById(R.id.labelTipReducere);
		labelTipFrecventa = (TextView) findViewById(R.id.labelTipFrecventa);
		labelStartValabil = (TextView) findViewById(R.id.labelStartValabil);
		labelCoefCalit = (TextView) findViewById(R.id.labelCoefCalit);
		labelValTotDepHead = (TextView) findViewById(R.id.labelValTotDepHead);

		textValTotDepHead.setVisibility(View.INVISIBLE);
		labelValTotDepHead.setVisibility(View.INVISIBLE);

		labelProcRedB5 = (TextView) findViewById(R.id.labelProcRedB5);
		textProcRedB5 = (TextView) findViewById(R.id.textProcRedB5);

		labelProcRedB5.setVisibility(View.GONE);
		textProcRedB5.setVisibility(View.GONE);

		labelTipReducere.setVisibility(View.GONE);
		labelTipFrecventa.setVisibility(View.GONE);
		labelStartValabil.setVisibility(View.GONE);
		labelCoefCalit.setVisibility(View.GONE);
		textCoefCalitHead.setVisibility(View.GONE);

		saveRedBtn.setVisibility(View.GONE);

		mProgress = (ProgressBar) findViewById(R.id.progress_bar_savered);
		mProgress.setVisibility(View.INVISIBLE);

		clientListView = (ListView) findViewById(R.id.clientList);
		list2 = new ArrayList<HashMap<String, String>>();
		adapter2 = new SimpleAdapter(this, list2, R.layout.rowlayoutclientred, new String[] { "numeClient", "codClient" }, new int[] { R.id.textNumeClient,
				R.id.textCodClient }

		);

		clientListView.setAdapter(adapter2);
		clientListView.setClickable(true);
		registerForContextMenu(clientListView);
		addListenerClientList();

	}

	private void CreateMenu(Menu menu) {

		MenuItem mnu0 = menu.add(0, 0, 0, "Tip");

		mnu0.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		MenuItem mnu1 = menu.add(0, 1, 1, "Clienti");

		mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		MenuItem mnu2 = menu.add(0, 2, 2, "Articole");

		mnu2.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		MenuItem mnu3 = menu.add(0, 3, 3, "Exceptii");

		mnu3.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case 0:
			if (list1.size() > 0) {
				Toast.makeText(getApplicationContext(), "Stergeti mai intai toate articolele!", Toast.LENGTH_LONG).show();
			} else {
				Intent nextScreenTipRed = new Intent(getApplicationContext(), SelectTipReducere.class);
				startActivity(nextScreenTipRed);
			}

			return true;

		case 1:

			Intent nextScreenCl = new Intent(getApplicationContext(), SelectClientReduceri.class);
			startActivity(nextScreenCl);
			return true;

		case 2:

			if (tipReducere.equals("")) {
				Toast.makeText(getApplicationContext(), "Selectati tipul de reducere!", Toast.LENGTH_LONG).show();
			} else {
				Intent nextScreenAr = new Intent(getApplicationContext(), SelectArtReduceri.class);
				startActivity(nextScreenAr);
			}

			return true;

		case 3:

			if (tipReducere.equals("")) {
				Toast.makeText(getApplicationContext(), "Selectati tipul de reducere!", Toast.LENGTH_LONG).show();
			} else {
				Intent nextScreenEx = new Intent(getApplicationContext(), SelectExceptiiReduceri.class);
				startActivity(nextScreenEx);
			}

			return true;

		case android.R.id.home:
			returnToHome();
			return true;

		}
		return false;
	}

	private void returnToHome() {

		if (list2.size() == 0 && list1.size() == 0) {

			articoleReduceri = "";
			numeClientRed = "";
			codClientRed = "";
			tipReducere = "";

			if (list1 != null) {
				list1.clear();
				adapter.notifyDataSetChanged();
			}

			if (list2 != null) {
				list2.clear();
				adapter2.notifyDataSetChanged();
			}

			UserInfo.getInstance().setParentScreen("");

			Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);

			startActivity(nextScreen);

			finish();
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Datele se vor pierde. Continuati?").setCancelable(false).setPositiveButton("Da", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {

					articoleReduceri = "";
					numeClientRed = "";
					codClientRed = "";
					tipReducere = "";

					if (list1 != null) {
						list1.clear();
						adapter.notifyDataSetChanged();
					}

					if (list2 != null) {
						list2.clear();
						adapter2.notifyDataSetChanged();
					}

					UserInfo.getInstance().setParentScreen("");

					Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);

					startActivity(nextScreen);

					finish();
				}
			}).setNegativeButton("Nu", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			}).setTitle("Atentie!").setIcon(R.drawable.warning96);

			AlertDialog alert = builder.create();
			alert.show();

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		CreateMenu(menu);
		return true;
	}

	@Override
	public void onResume() {

		super.onResume();
		checkStaticVars();

		if (tipReducere.length() > 0) {

			labelTipReducere.setVisibility(View.VISIBLE);
			labelTipFrecventa.setVisibility(View.VISIBLE);
			labelStartValabil.setVisibility(View.VISIBLE);

			labelCoefCalit.setVisibility(View.VISIBLE);
			textCoefCalitHead.setVisibility(View.VISIBLE);

			textTipReducere.setText(tipReducere);
			textTipFrecventa.setText(frecventaRedNume);
			textStartValabil.setText(startValabil);
			textStopValabil.setText(stopValabil);
			textCoefCalitHead.setText(coefCalit);

			if (!procRedB5.equals("-1")) {
				labelProcRedB5.setVisibility(View.VISIBLE);
				textProcRedB5.setVisibility(View.VISIBLE);
				textProcRedB5.setText(procRedB5);
			} else {
				labelProcRedB5.setVisibility(View.GONE);
				textProcRedB5.setVisibility(View.GONE);
				textProcRedB5.setText("");
			}

			if (selTotDepart) {
				textValTotDepHead.setVisibility(View.VISIBLE);
				labelValTotDepHead.setVisibility(View.VISIBLE);
				textValTotDepHead.setText(valTotDepart);
			} else {
				textValTotDepHead.setVisibility(View.INVISIBLE);
				labelValTotDepHead.setVisibility(View.INVISIBLE);
			}

		} else {

			tipReducere = textTipReducere.getText().toString();
			frecventaRedNume = textTipFrecventa.getText().toString();
			startValabil = textStartValabil.getText().toString();
			stopValabil = textStopValabil.getText().toString();

		}

		if (clienti.length() > 0) {

			if (SelectClientReduceri.selTipClient != ReduceriUlterioare.selTipClient) {
				list2.clear();
				clientListView.setAdapter(adapter2);
				ReduceriUlterioare.selTipClient = SelectClientReduceri.selTipClient;
			}

			HashMap<String, String> temp;
			String[] tokenLinie = clienti.split("@@");
			String[] tokenClient;
			String client = "";

			int nrArticole = 0;
			String codCl = "";
			String numeCl = "";
			boolean artExist = false;
			clientFinalStr = "";

			// ----parcurgere articole de adaugat
			for (int i = 0; i < tokenLinie.length; i++) {
				temp = new HashMap<String, String>(50, 0.75f);
				client = tokenLinie[i];
				tokenClient = client.split("#");

				codCl = tokenClient[0];
				numeCl = tokenClient[1];

				nrArticole = list2.size();

				String item = "";
				String newClCode = "";

				artExist = false;

				for (int j = 0; j < nrArticole; j++) {

					item = clientListView.getItemAtPosition(j).toString();
					String[] token = item.split(",");
					newClCode = token[0].substring(token[0].indexOf('=') + 1, token[0].length());

					if (codCl.equals(newClCode)) {
						artExist = true;
					}

					if (codCl.equals("00") && !newClCode.equals("00")) {
						artExist = true;
					}

					if (!codCl.equals("00") && newClCode.equals("00")) {
						artExist = true;
					}

				}

				if (!artExist) {
					temp.put("numeClient", numeCl);
					temp.put("codClient", codCl);

					if (clientFinalStr.equals("")) {
						clientFinalStr += codCl;
					} else {
						clientFinalStr += ";" + codCl;
					}

					list2.add(temp);
				}

			}

			clientListView.setAdapter(adapter2);
			clienti = "";

		} else {

		}

		if (articoleReduceri.length() > 0) {
			// articole existente

			saveRedBtn.setVisibility(View.VISIBLE);

			HashMap<String, String> temp;
			String[] tokenLinie = articoleReduceri.split("@@");
			String[] tokenArticol;
			String client = "";
			String tipArtRed = "", sintetic = "";
			boolean condSintetic = false;

			for (int i = 0; i < tokenLinie.length; i++) {
				temp = new HashMap<String, String>(30, 0.75f);
				client = tokenLinie[i];
				tokenArticol = client.split("#");

				// pentru reduceri B2 nu se accepta doua materiale din acelasi
				// sintetic
				if (tipReducere.equals("B2")) {
					condSintetic = false;

					// parcurgere articole existente
					int nrArt = list1.size();
					for (int j = 0; j < nrArt; j++) {

						Object o = this.adapter.getItem(j);

						String[] token = o.toString().split(",");

						sintetic = token[8].substring(token[8].indexOf('=') + 1, token[8].length() - 1);

						// exista un articol din acelasi sintetic
						if (sintetic.equals(tokenArticol[10])) {
							condSintetic = true;
						}

					}

					// sf. art. exist.
				} else {
					condSintetic = false;
				}
				//

				if (!condSintetic) {
					temp.put("numeArt", tokenArticol[1]);
					temp.put("codArt", tokenArticol[2]);

					tipArtRed = "sintetic";
					if (tokenArticol[0].equals("0")) {
						tipArtRed = "sintetic";
						allowArtExc = true;
					}
					if (tokenArticol[0].equals("1")) {
						tipArtRed = "articol";
					}
					if (tokenArticol[0].equals("2")) {
						tipArtRed = "nivel1";
						allowSintExc = true;
					}

					temp.put("tipArt", tipArtRed);

					temp.put("obs1", " ");
					temp.put("obs2", " ");

					temp.put("obs3", tokenArticol[10]);

					String valRedArt = "-1";
					if (!tokenArticol[3].equals("-1")) // reducere cantitativa
					{
						valRedArt = tokenArticol[3];
					} else // reducere valorica
					{
						valRedArt = tokenArticol[4];
					}

					temp.put("cantArt", String.format("%.02f", Double.parseDouble(valRedArt)).toString());

					temp.put("umArt", tokenArticol[5]);
					temp.put("procent", tokenArticol[6]);
					temp.put("lprocent", "%");

					list1.add(temp);
				} else {
					Toast.makeText(getApplicationContext(), "Un articol din sinteticul " + sintetic + " exista deja! ", Toast.LENGTH_LONG).show();
				}
			}

			artListView.setAdapter(adapter);

			articoleReduceri = "";
		}

		// articole exceptii
		if (exceptii.length() > 0) {

			HashMap<String, String> temp;
			String[] tokenLinie = exceptii.split("@@");
			String[] tokenArticol;
			String client = "";

			for (int i = 0; i < tokenLinie.length; i++) {
				temp = new HashMap<String, String>(30, 0.75f);
				client = tokenLinie[i];
				tokenArticol = client.split("#");

				temp.put("numeArt", tokenArticol[1]);
				temp.put("codArt", tokenArticol[0]);
				temp.put("tipArt", tokenArticol[3]);
				temp.put("obs1", "Exceptie");
				temp.put("obs2", " ");
				temp.put("obs3", " ");
				temp.put("cantArt", " ");
				temp.put("umArt", " ");
				temp.put("procent", " ");
				temp.put("lprocent", " ");
				list1.add(temp);

			}

			artListView.setAdapter(adapter);

			exceptii = "";

		}

	}

	public void addListenerClientList() {

		clientListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

				listViewSelClient = position;
				return false;

			}
		});
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		int menuItemIndex = item.getItemId();

		if (menuItemIndex == 0) // stergere
		{
			if (listViewSelClient >= 0) {
				list2.remove(listViewSelClient);
				adapter2.notifyDataSetChanged();
				listViewSelClient = -1;

			}

			if (listViewSelArt >= 0) {

				list1.remove(listViewSelArt);
				adapter.notifyDataSetChanged();
				listViewSelArt = -1;

			}
		}

		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {

		if (v.getId() == R.id.clientList) {

			Object obj = this.adapter2.getItem(listViewSelClient);
			String[] token = obj.toString().split(",");
			String clientSel = token[1].substring(token[1].indexOf('=') + 1, token[1].length() - 1);
			menu.setHeaderTitle(clientSel);
			menu.add(Menu.NONE, 0, 0, "Sterge");

		}

		if (v.getId() == R.id.listArt) {

			Object obj = this.adapter.getItem(listViewSelArt);

			String[] token = obj.toString().split(",");
			String clientSel = token[0].substring(token[0].indexOf('=') + 1, token[0].length());
			menu.setHeaderTitle(clientSel);
			menu.add(Menu.NONE, 0, 0, "Sterge");

		}

	}

	public void addListenerSaveRedBtn() {
		saveRedBtn.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {

				try {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:

						if (list1.size() == 0) {
							Toast.makeText(getApplicationContext(), "Adaugati cel putin un articol sau sintetic!", Toast.LENGTH_SHORT).show();
						} else if (list2.size() == 0) {
							Toast.makeText(getApplicationContext(), "Selectati cel putin un client!", Toast.LENGTH_SHORT).show();

						}

						else if (textTipReducere.getText().toString().equals("")) {
							Toast.makeText(getApplicationContext(), "Selectati tipul de reducere!", Toast.LENGTH_SHORT).show();

						} else {
							mProgress.setVisibility(View.VISIBLE);
							mProgress.setProgress(0);
							progressVal = 0;
							myTimer = new Timer();
							myTimer.schedule(new UpdateProgress(), 15, 15);
						}

						return true;

					case MotionEvent.ACTION_UP:

						if (mProgress.getVisibility() == View.VISIBLE) {
							myTimer.cancel();
							mProgress.setVisibility(View.INVISIBLE);
							return true;
						}

					}
				} catch (Exception ex) {
					Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
				}

				return false;
			}

		});

	}

	class UpdateProgress extends TimerTask {
		public void run() {
			progressVal++;
			if (mProgress.getProgress() == 50) {
				logonHandler.post(new Runnable() {
					public void run() {

						clientFinalStr = prepareClientForDelivery();
						articoleFinaleStr = prepareArtForDelivery();
						String sablonID = "-1"; // doar pentru comenzile
												// angajament

						if (procRedB5.equals(""))
							procRedB5 = "0";

						clientFinalStr += "#" + tipReducere + "#" + frecventaRedCod + "#" + UserInfo.getInstance().getCod() + "#"
								+ UserInfo.getInstance().getUnitLog() + "#" + UserInfo.getInstance().getCodDepart() + "#" + startValabil + "#" + stopValabil
								+ "#" + coefCalit + "#" + valTotDepart + "#" + String.valueOf(ReduceriUlterioare.selTipClient) + "#" + procRedB5 + "#"
								+ sablonID + "@";

						reducereFinalaStr = clientFinalStr + articoleFinaleStr;

						performSaveRed();

						articoleReduceri = "";
						articoleFinaleStr = "";
						clientFinalStr = "";

					}
				});

				myTimer.cancel();
			} else {
				mProgress.setProgress(progressVal);
			}

		}
	}

	private void performSaveRed() {
		try {

			HashMap<String, String> params = new HashMap<String, String>();

			params.put("artRed", reducereFinalaStr);
			AsyncTaskWSCall call = new AsyncTaskWSCall(this, "saveRedAndroid", params);
			call.getCallResults();

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	public String prepareClientForDelivery() {
		String retVal = "";

		int nrArticole = list2.size();
		String item = "", clientCode = "";

		for (int j = 0; j < nrArticole; j++) {

			item = clientListView.getItemAtPosition(j).toString();
			String[] token = item.split(",");
			clientCode = token[0].substring(token[0].indexOf('=') + 1, token[0].length());

			if (clientCode.equals("00")) {
				retVal = "01;02;03;04;05;06";
				continue;
			}

			if (retVal.equals(""))
				retVal += clientCode;
			else
				retVal += ";" + clientCode;

		}

		return retVal;
	}

	private static String getFieldValue(String brutValue, String fieldName) {
		String[] tokenValue = brutValue.toString().split(",");
		String fieldValue = "";

		for (int i = 0; i < tokenValue.length; i++) {
			if (foundField(tokenValue[i], fieldName)) {
				if (i == tokenValue.length - 1)
					fieldValue = tokenValue[i].substring(tokenValue[i].indexOf('=') + 1, tokenValue[i].length() - 1);
				else
					fieldValue = tokenValue[i].substring(tokenValue[i].indexOf('=') + 1, tokenValue[i].length());
				break;
			}
		}

		return fieldValue;
	}

	private static boolean foundField(String currentField, String searchField) {
		if (searchField.toLowerCase().equals("procent") && !currentField.toLowerCase().contains("lprocent"))
			return currentField.toLowerCase().contains(searchField.toLowerCase());
		else if (!currentField.toLowerCase().contains("lprocent"))
			return currentField.toLowerCase().contains(searchField.toLowerCase());

		return false;
	}

	public String prepareArtForDelivery() {
		String retVal = "";
		String tipExceptie = "";
		String tokTipArticol = "";
		String tokCodArticol = "";
		String tokNumeArticol = "";
		String tokValPrag = "";
		String tokUmPrag = "";
		String tokProcent = "";
		String tokExceptii = "";

		int count = list1.size();

		for (int j = 0; j < count; j++) {

			Object o = this.adapter.getItem(j);

			tokTipArticol = getFieldValue(o.toString(), "tipart");

			tokCodArticol = getFieldValue(o.toString(), "codart");

			tokNumeArticol = getFieldValue(o.toString(), "numeart");

			tokValPrag = getFieldValue(o.toString(), "cantart");

			tokUmPrag = getFieldValue(o.toString(), "umart");

			tokProcent = getFieldValue(o.toString(), "procent");

			tokExceptii = getFieldValue(o.toString(), "obs1");

			if (tokExceptii.contains("Exceptie")) {
				tokExceptii = tokCodArticol;
				tipExceptie = tokTipArticol;

				if (tokTipArticol.equals("sintetic"))
					tipExceptie = "1";
				if (tokTipArticol.equals("articol"))
					tipExceptie = "2";
				if (tokTipArticol.equals("nivel1"))
					tipExceptie = "3";

				tokTipArticol = "3";
				tokCodArticol = " ";
				tokNumeArticol = " ";
				tokValPrag = " ";
				tokUmPrag = " ";
				tokProcent = " ";

			}

			if (tokTipArticol.equals("sintetic")) {
				tokTipArticol = "1";
			}

			if (tokTipArticol.equals("articol")) {
				tokTipArticol = "2";
			}

			if (tokTipArticol.equals("nivel1")) {
				tokTipArticol = "4";
			}

			retVal += tokTipArticol + "#" + tokCodArticol + "#" + tokNumeArticol + "#" + tokValPrag + "#" + tokUmPrag + "#" + tokProcent + "#" + tokExceptii
					+ "#" + tipExceptie + "@";

		}

		return retVal;
	}

	public void addListenerClientBtn() {
		clientBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (list1.size() == 0) {
					Intent nextScreen = new Intent(getApplicationContext(), SelectClientCmd.class);
					startActivity(nextScreen);
				} else {
					Toast.makeText(getApplicationContext(), "Stergeti mai intai toate articolele!", Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	public void addListenerArticoleBtn() {
		articoleBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (codClientRed.length() > 0) {
					Intent nextScreen = new Intent(getApplicationContext(), SelectArtCmd.class);
					startActivity(nextScreen);
				} else {
					Toast.makeText(getApplicationContext(), "Selectati mai intai clientul!", Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	public void addListenerLivrareBtn() {
		livrareBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (codClientRed.length() > 0) {
					Intent nextScreen = new Intent(getApplicationContext(), SelectAdrLivrCmd.class);
					startActivity(nextScreen);
				} else {
					Toast.makeText(getApplicationContext(), "Selectati mai intai clientul!", Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	private void saveCmdStatus(String saveResponse) {
		if (saveResponse.substring(0, 1).equals("B")) {
			try {

				// curatenie ecran

				list1.clear();
				adapter.notifyDataSetChanged();

				list2.clear();
				adapter.notifyDataSetChanged();

				numeClientRed = "";
				articoleReduceri = "";
				tipReducere = "";
				clienti = "";

				textTipReducere.setText("");
				textTipFrecventa.setText("");
				textStartValabil.setText("");
				textStopValabil.setText("");
				textCoefCalitHead.setText("");

				labelProcRedB5.setVisibility(View.GONE);
				textProcRedB5.setVisibility(View.GONE);
				textProcRedB5.setText("");

				labelTipReducere.setVisibility(View.GONE);
				labelTipFrecventa.setVisibility(View.GONE);
				labelStartValabil.setVisibility(View.GONE);
				labelCoefCalit.setVisibility(View.GONE);
				textValTotDepHead.setVisibility(View.GONE);
				labelValTotDepHead.setVisibility(View.GONE);

				saveRedBtn.setVisibility(View.GONE);

				Toast.makeText(getApplicationContext(), "Sablonul " + saveResponse + " a fost salvat!", Toast.LENGTH_LONG).show();

			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
			}

		} else {
			Toast.makeText(getApplicationContext(), "Sablonul NU a fost salvat!", Toast.LENGTH_LONG).show();
		}
	}

	private void checkStaticVars() {
		// pentru in idle mare variabilele statice se sterg si setarile locale
		// se reseteaza

		// resetare locale la idle
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
		returnToHome();
		return;
	}

	public void addListenerArtList() {
		artListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

				listViewSelArt = position;
				return false;

			}
		});
	}

	public void onTaskComplete(String methodName, Object result) {
		if (methodName.equals("saveRedAndroid")) {
			saveCmdStatus((String) result);
		}

	}

}