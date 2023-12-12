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
import my.logon.screen.model.ClientiGenericiGedInfoStrings;
import my.logon.screen.model.UserInfo;
import my.logon.screen.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.ToggleButton;

public class GradReduceri extends Activity implements AsyncTaskListener {

	Button quitBtn;
	String filiala = "", nume = "", codAgent = "";
	public static String unitLog = "";
	public static String numeDepart = "";
	public static String codDepart = "";

	public SimpleAdapter adapterGradRed;
	private static ArrayList<HashMap<String, String>> listSabloane = null;
	private String selectedAgent = "-1";
	private String sablonSel = "", codClntSablon = "", pozArtSablon = "";

	String tipArtSablon = "", codArtSablon = "";
	static String tipAcces;

	ToggleButton redAfis;
	ListView listViewGradRed;
	private int listViewSelRed = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.gradreduceri);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Incarcare sablon");
		actionBar.setDisplayHomeAsUpEnabled(true);

		checkStaticVars();

		listViewGradRed = (ListView) findViewById(R.id.listGradReduceri);

		listSabloane = new ArrayList<HashMap<String, String>>();
		adapterGradRed = new SimpleAdapter(this, listSabloane,
				R.layout.rowgradreducere, new String[] { "nrCrt", "numeClient",
						"codRed", "dataStart", "dataStop", "agent", "numeArt",
						"cantArt", "umArt", "codArt", "tipArt", "procent",
						"lprocent", "obs1", "obs2", "obs3", "pozArt",
						"codClient" }, new int[] { R.id.nrCrt, R.id.textClient,
						R.id.textIdRed, R.id.textDataStart, R.id.textDataStop,
						R.id.textAgent, R.id.textNumeArt, R.id.textCantArt,
						R.id.textUmArt, R.id.textCodArt, R.id.textTipArt,
						R.id.textProcent, R.id.labelProcent, R.id.textData1,
						R.id.textData2, R.id.textData3, R.id.textPozArt,
						R.id.textCodClient });

		listViewGradRed.setAdapter(adapterGradRed);
		listViewGradRed.setClickable(true);
		registerForContextMenu(listViewGradRed);
		addListenerGradRed();

		selectedAgent = UserInfo.getInstance().getCod();
		try {

			performGetSabloane();

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(),
					Toast.LENGTH_SHORT).show();
		}

	}

	private void performGetSabloane() {

		HashMap<String, String> params = new HashMap<String, String>();

		String tipUser = "";
		if (UserInfo.getInstance().getTipAcces().equals("9")) {
			tipUser = "AV";
		}
		if (UserInfo.getInstance().getTipAcces().equals("10")) {
			tipUser = "SD";
		}
		if (UserInfo.getInstance().getTipAcces().equals("14")
				|| UserInfo.getInstance().getTipAcces().equals("12")) {
			tipUser = "DV";
		}

		NumberFormat nf3 = new DecimalFormat("00000000");
		String fullCode = nf3.format(Integer.parseInt(selectedAgent))
				.toString();
		params.put("codUser", fullCode);
		params.put("filiala", UserInfo.getInstance().getUnitLog());
		params.put("depart", UserInfo.getInstance().getCodDepart());
		params.put("tipUser", tipUser);

		AsyncTaskWSCall call = new AsyncTaskWSCall(this, "getGradSabloane",
				params);
		call.getCallResults();

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
			Intent nextScreen = new Intent(getApplicationContext(),
					MainMenu.class);

			startActivity(nextScreen);

			finish();
			return true;

		}
		return false;
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		int menuItemIndex = item.getItemId();
		if (menuItemIndex == 0) // stergere
		{
			if (listViewSelRed >= 0) {
				// trimitere sablon spre aprobare
				try {

					performAprobaSablon();
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), e.toString(),
							Toast.LENGTH_SHORT).show();
				}

			}
		}

		return true;
	}

	private void performAprobaSablon() {
		HashMap<String, String> params = new HashMap<String, String>();

		params.put("codSablon", sablonSel);
		params.put("pozitie", pozArtSablon);
		params.put("codClient", codClntSablon);

		AsyncTaskWSCall call = new AsyncTaskWSCall(this, "trimiteSablonAprob",
				params);
		call.getCallResults();

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		if (v.getId() == R.id.listGradReduceri) {

			Object obj = this.adapterGradRed.getItem(listViewSelRed);
			String[] token = obj.toString().split(",");

			if (token.length == 18) {
				sablonSel = token[7].substring(token[7].indexOf('=') + 1,
						token[7].length());

				codClntSablon = token[0].substring(token[0].indexOf('=') + 1,
						token[0].length());

				pozArtSablon = token[9].substring(token[9].indexOf('=') + 1,
						token[9].length());

				tipArtSablon = token[14].substring(token[14].indexOf('=') + 1,
						token[14].length());

				codArtSablon = token[16].substring(token[16].indexOf('=') + 1,
						token[16].length());

				if (!sablonSel.trim().equals("")) {
					menu.setHeaderTitle("Sablon " + sablonSel + " - "
							+ tipArtSablon + " " + codArtSablon);
					menu.add(Menu.NONE, 0, 0, "Trimite spre aprobare");
				}
			}

		}

	}

	public void displayResponse(String response) {

		if (!response.equals("-1")) {
			Toast.makeText(getApplicationContext(), "Cererea a fost trimisa! ",
					Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(getApplicationContext(), "Eroare trimitere cerere!",
					Toast.LENGTH_LONG).show();
		}

	}


	protected void populateSablonList(String sablonList) {

		String tipArt = "";
		String codMat = "", numeMat = "", cantMat = "", umMat = "", cantRealiz = "", umRealiz = "";
		int crt = 1;

		listSabloane.clear();
		adapterGradRed.notifyDataSetChanged();

		if (sablonList.contains("#")) {

			HashMap<String, String> temp;
			String[] sablon = sablonList.split("!!");
			String[] token, antet, detalii;

			for (int i = 0; i < sablon.length; i++) {

				token = sablon[i].split("@@");

				for (int j = 1; j < token.length; j++) {
					antet = token[0].split("#");
					detalii = token[j].split("#");

					temp = new HashMap<String, String>(50, 0.75f);

					temp.put("nrCrt", String.valueOf(crt) + ".");

					if (antet[5].length() == 2) {
						temp.put("numeClient",
								ClientiGenericiGedInfoStrings.getTipClient(antet[5]));
					} else {
						temp.put("numeClient", antet[3]);
					}

					temp.put("codRed", antet[0]);
					temp.put("dataStart", antet[1]);
					temp.put("dataStop", antet[2]);
					temp.put("agent", antet[4]);
					temp.put("codClient", antet[5]);

					if (!detalii[0].equals("-2"))// nu este exceptie
					{

						if (detalii[0].equals("-1"))// nu este sintetic
						{

							if (detalii[2].equals("-1")) // nu este articol
							{
								codMat = detalii[13];
								numeMat = detalii[14];
								tipArt = "Nivel1";
							} else // este articol
							{
								codMat = detalii[2];
								numeMat = detalii[3];
								tipArt = "Articol";
							}
						} else // este sintetic
						{
							codMat = detalii[0];
							numeMat = detalii[1];
							tipArt = "Sintetic";
						}

						temp.put("numeArt", numeMat);
						temp.put("codArt", codMat);

						if (detalii[4].equals("0")) {
							cantMat = detalii[6];
							umMat = "RON";
						} else {
							cantMat = detalii[4];
							umMat = detalii[5];
						}

						temp.put("cantArt", cantMat);
						temp.put("umArt", umMat);
						temp.put("tipArt", tipArt);

						temp.put("procent", detalii[8]);
						temp.put("lprocent", "%");

						if (detalii[9].equals("-2")) // nu exista realizari
						{
							cantRealiz = "0";
							umRealiz = umMat;
						} else {
							if (detalii[9].equals("-1")) // prag realizat
															// valoric
							{
								cantRealiz = detalii[11];
								umRealiz = detalii[12];
							} else {
								cantRealiz = detalii[9];
								umRealiz = detalii[10];
							}
						}

						temp.put("obs1", "La zi:");
						temp.put("obs2", cantRealiz);
						temp.put("obs3", umRealiz);
						temp.put("pozArt", detalii[15]);

						listSabloane.add(temp);
					} else // afisare exceptii
					{

						temp = new HashMap<String, String>();

						temp.put("nrCrt", String.valueOf(crt) + ".");
						temp.put("numeClient", "Exceptie");
						temp.put("codRed", antet[0]);
						temp.put("dataStart", antet[1]);
						temp.put("dataStop", antet[2]);
						temp.put("agent", antet[4]);
						temp.put("numeArt", detalii[2]);
						temp.put("codArt", detalii[1]);
						temp.put("procent", " ");
						temp.put("carProc", " ");
						temp.put("umArt", " ");
						temp.put("tipArt", " ");
						temp.put("obs1", "");
						temp.put("obs2", " ");
						temp.put("obs3", " ");
						listSabloane.add(temp);
					}

					crt++;

				}// for j
			}

			listViewGradRed.setAdapter(adapterGradRed);

		} else {
			Toast.makeText(getApplicationContext(), "Nu exista sabloane!",
					Toast.LENGTH_SHORT).show();
		}

	}

	public void addListenerGradRed() {

		listViewGradRed
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int position, long arg3) {

						listViewSelRed = position;

						return false;

					}
				});
	}

	private void checkStaticVars() {
		// pentru in idle mare variabilele statice se sterg si setarile locale
		// se reseteaza

		// resetare locale la idle
		String locLang = getBaseContext().getResources().getConfiguration().locale
				.getLanguage();

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
					.getLaunchIntentForPackage(
							getBaseContext().getPackageName());
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

	public void onTaskComplete(String methodName, Object result) {
		if (methodName.equals("trimiteSablonAprob")) {
			displayResponse((String)result);
		}

		if (methodName.equals("getGradSabloane")) {
			populateSablonList((String)result);
		}

	}

}