/**
 * @author florinb
 *
 */
package my.logon.screen.screens;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.listeners.CustomSpinnerClass;
import my.logon.screen.listeners.CustomSpinnerListener;
import my.logon.screen.model.ClientiGenericiGedInfoStrings;
import my.logon.screen.model.UserInfo;
import my.logon.screen.R;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.SlidingDrawer;
import android.widget.Spinner;
import android.widget.Toast;

public class AfisReduceri extends ListActivity implements AsyncTaskListener, CustomSpinnerListener {

	Button quitBtn, redStergeBtn;
	String filiala = "", nume = "", codAgent = "";
	public static String unitLog = "";
	public static String numeDepart = "";
	public static String codDepart = "";
	private SimpleAdapter adapter;

	private Spinner spinnerRed;
	public SimpleAdapter adapterComenzi;
	private static ArrayList<HashMap<String, String>> listSabloane = null;
	private static ArrayList<HashMap<String, String>> list1 = null;
	private String selectedSabl = "-1", selectedAgent = "-1", stareSablon = "-1";
	private String cmdNr = null;
	

	static String tipAcces;
	SlidingDrawer slidingDrawerRed;

	private CustomSpinnerClass spinnerListener = new CustomSpinnerClass();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.afisreduceri);

		this.redStergeBtn = (Button) findViewById(R.id.redSterge);
		addListenerSterge();

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Afisare sabloane reduceri");
		actionBar.setDisplayHomeAsUpEnabled(true);

		checkStaticVars();

		spinnerRed = (Spinner) findViewById(R.id.spinnerRed);

		spinnerRed.setOnItemSelectedListener(spinnerListener);
		spinnerListener.setListener(this);

		list1 = new ArrayList<HashMap<String, String>>();

		adapter = new SimpleAdapter(this, list1, R.layout.rowafisartreducere, new String[] { "numeArt", "codArt",
				"cantArt", "umArt", "procent", "carProc", "tipArt", "obs1", "obs2", "obs3" }, new int[] {
				R.id.textNumeArt, R.id.textCodArt, R.id.textCantArt, R.id.textUmArt, R.id.textProcent,
				R.id.labelProcent, R.id.textTipArt, R.id.textData1, R.id.textData2, R.id.textData3 }

		);

		listSabloane = new ArrayList<HashMap<String, String>>();
		adapterComenzi = new SimpleAdapter(this, listSabloane, R.layout.customrowreduceri, new String[] { "nrCrt",
				"numeClient", "codRed", "dataStart", "dataStop", "stare", "pCalit", "valDep" }, new int[] { R.id.nrCrt,
				R.id.textClient, R.id.textIdRed, R.id.textDataStart, R.id.textDataStop, R.id.textStare, R.id.procCalit,
				R.id.valDepart });

		selectedAgent = UserInfo.getInstance().getCod();

		try {
			performGetSabloane();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}

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
			Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);

			startActivity(nextScreen);

			finish();
			return true;

		}
		return false;
	}

	public void addListenerSterge() {
		redStergeBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {

					if (!stareSablon.toLowerCase(Locale.getDefault()).equals("inactiv")) {
						Toast.makeText(getApplicationContext(), "Sablonul este activ, nu poate fi sters!",
								Toast.LENGTH_SHORT).show();
					} else {
						showConfirmationAlert();
					}

				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
				}
			}
		});

	}

	public void showConfirmationAlert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Stergeti sablonul?").setCancelable(false)
				.setPositiveButton("Da", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						performStergeSablon();

					}
				}).setNegativeButton("Nu", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();

					}
				}).setTitle("Confirmare").setIcon(R.drawable.warning96);

		AlertDialog alert = builder.create();
		alert.show();

	}

	public void performStergeSablon() {
		try {

			HashMap<String, String> params = new HashMap<String, String>();

			params.put("codSablon", selectedSabl);
			AsyncTaskWSCall call = new AsyncTaskWSCall(this, "stergeSablonReduceri", params);
			call.getCallResults();

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	public void refreshSablonList(String result) {
		if (!result.equals("-1")) {
			Toast.makeText(getApplicationContext(), "Sablonul a fost sters!", Toast.LENGTH_LONG).show();
			performGetSabloane();
			slidingDrawerRed.animateClose();
		} else {
			Toast.makeText(getApplicationContext(), "Sablonul NU a fost sters!", Toast.LENGTH_LONG).show();
		}
	}

	private void performArtSablon() {

		try {

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("nrSablon", selectedSabl);
			AsyncTaskWSCall call = new AsyncTaskWSCall(this, "getSablonArt", params);
			call.getCallResults();

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	private void populateArtSablonList(String articoleComanda) {

		list1.clear();
		adapter.notifyDataSetChanged();

		if (articoleComanda.contains("#")) {
			HashMap<String, String> temp;
			String[] tokenMain = articoleComanda.split("@@");

			String[] tokenArt;
			String client = "";
			String codMat = "", numeMat = "", cantMat = "", umMat = "";

			String tipArt = "";
			boolean afisHeadExc = false;

			for (int i = 0; i < tokenMain.length; i++) {

				temp = new HashMap<String, String>(30, 0.75f);
				client = tokenMain[i];
				tokenArt = client.split("#");

				if (!tokenArt[0].equals("-2"))// nu este exceptie
				{

					if (tokenArt[0].equals("-1"))// nu este sintetic
					{

						if (tokenArt[2].equals("-1")) // nu este articol
						{
							codMat = tokenArt[13];
							numeMat = tokenArt[14];
							tipArt = "Nivel1";
						} else // este articol
						{
							codMat = tokenArt[2];
							numeMat = tokenArt[3];
							tipArt = "Articol";
						}
					} else // este sintetic
					{
						codMat = tokenArt[0];
						numeMat = tokenArt[1];
						tipArt = "Sintetic";
					}

					temp.put("numeArt", numeMat);
					temp.put("codArt", codMat);

					if (tokenArt[4].equals("0")) {
						cantMat = tokenArt[6];
						umMat = "RON";
					} else {
						cantMat = tokenArt[4];
						umMat = tokenArt[5];
					}

					temp.put("cantArt", cantMat);
					temp.put("umArt", umMat);
					temp.put("tipArt", tipArt);

					temp.put("procent", tokenArt[8]);
					temp.put("carProc", "%");

					temp.put("obs1", " ");
					temp.put("obs2", " ");
					temp.put("obs3", " ");

					list1.add(temp);
				} else // afisare exceptii
				{
					if (!afisHeadExc) {
						temp = new HashMap<String, String>();
						temp.put("numeArt", " ");
						temp.put("codArt", "Exceptii:");
						temp.put("procent", " ");
						temp.put("carProc", " ");
						temp.put("umArt", " ");
						temp.put("tipArt", " ");
						temp.put("obs1", " ");
						temp.put("obs2", " ");
						temp.put("obs3", " ");
						list1.add(temp);

						afisHeadExc = true;
					}

					temp = new HashMap<String, String>();
					temp.put("nrCrt", String.valueOf(i + 1) + ".");
					temp.put("numeArt", tokenArt[2]);
					temp.put("codArt", tokenArt[1]);
					temp.put("procent", " ");
					temp.put("carProc", " ");
					temp.put("umArt", " ");
					temp.put("tipArt", " ");
					temp.put("obs1", "");
					temp.put("obs2", " ");
					temp.put("obs3", " ");
					list1.add(temp);
				}

			}

			setListAdapter(adapter);

			slidingDrawerRed.setVisibility(View.VISIBLE);

		}

	}

	protected void populateSablonList(String sablonList) {

		listSabloane.clear();
		adapterComenzi.notifyDataSetChanged();

		if (sablonList.contains("#")) {

			spinnerRed.setVisibility(View.VISIBLE);
			redStergeBtn.setVisibility(View.VISIBLE);

			HashMap<String, String> temp;
			String[] tokenLinie = sablonList.split("@@");
			String[] tokenClient;
			String client = "";
			String stareRed = "";
			for (int i = 0; i < tokenLinie.length; i++) {
				temp = new HashMap<String, String>();
				client = tokenLinie[i];
				tokenClient = client.split("#");
				temp.put("nrCrt", String.valueOf(i + 1) + ".");

				if (tokenClient[3].length() == 2) {
					temp.put("numeClient", ClientiGenericiGedInfoStrings.getTipClient(tokenClient[3]));
				} else
					temp.put("numeClient", tokenClient[4]);

				if (i == 0)
					selectedSabl = tokenClient[0];
				temp.put("codRed", tokenClient[0]);

				temp.put("dataStart", tokenClient[1]);
				temp.put("dataStop", tokenClient[2]);

				if (tokenClient[5].equals("X")) {
					stareRed = "Inactiv";
				} else
					stareRed = "Activ";

				temp.put("stare", stareRed);
				temp.put("pCalit", "Proc.calit: " + tokenClient[6] + "%");
				temp.put("valDep", "Val.depart: " + tokenClient[7]);

				listSabloane.add(temp);
			}

			spinnerRed.setAdapter(adapterComenzi);

		} else {
			spinnerRed.setVisibility(View.INVISIBLE);
			redStergeBtn.setVisibility(View.INVISIBLE);
			Toast.makeText(getApplicationContext(), "Nu exista sabloane!", Toast.LENGTH_SHORT).show();
		}

	}

	public void performGetSabloane() {
		try {

			HashMap<String, String> params = new HashMap<String, String>();

			NumberFormat nf3 = new DecimalFormat("00000000");
			String fullCode = nf3.format(Integer.parseInt(selectedAgent)).toString();

			params.put("codUser", fullCode);
			params.put("filiala", UserInfo.getInstance().getUnitLog());
			params.put("depart", UserInfo.getInstance().getCodDepart());

			AsyncTaskWSCall call = new AsyncTaskWSCall(this, "getListSabloaneReduceri", params);
			call.getCallResults();

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
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
			getBaseContext().getResources().updateConfiguration(config,
					getBaseContext().getResources().getDisplayMetrics());
		}

		// restart app la idle
		if (UserInfo.getInstance().getCod().equals("")) {

			Intent i = getBaseContext().getPackageManager()
					.getLaunchIntentForPackage(getBaseContext().getPackageName());
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
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

	@Override
	public String toString() {
		return "AfisReduceri [nume=" + nume + ", codAgent=" + codAgent + ", cmdNr=" + cmdNr + "]";
	}

	public void onTaskComplete(String methodName, Object result) {
		if (methodName.equals("stergeSablonReduceri")) {
			refreshSablonList((String) result);
		}

		if (methodName.equals("getListSabloaneReduceri")) {
			populateSablonList((String) result);
		}

		if (methodName.equals("getSablonArt")) {
			populateArtSablonList((String) result);
		}

	}

	public void onSelectedSpinnerItem(int spinnerId, HashMap<String, String> map, int position) {
		if (spinnerId == R.id.spinnerRed) {
			if (!selectedSabl.equals("-1")) {
				list1.clear();
				adapter.notifyDataSetChanged();

				cmdNr = map.get("codRed");
				stareSablon = map.get("stare");

				selectedSabl = cmdNr;
				performArtSablon();
			}
		}

	}

}