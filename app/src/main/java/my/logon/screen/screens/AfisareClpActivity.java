/**
 * @author florinb
 * 
 */
package my.logon.screen.screens;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import my.logon.screen.listeners.ClpDAOListener;
import my.logon.screen.model.ClpDAO;
import my.logon.screen.model.HandleJSONData;
import my.logon.screen.model.UserInfo;
import my.logon.screen.adapters.ArticoleCLPAdapter;
import my.logon.screen.adapters.ComenziCLPAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import my.logon.screen.beans.ArticolCLP;
import my.logon.screen.beans.BeanDocumentCLP;
import my.logon.screen.beans.ComandaCLP;
import my.logon.screen.beans.DateLivrareCLP;
import my.logon.screen.enums.EnumClpDAO;
import my.logon.screen.R;

public class AfisareClpActivity extends Activity implements ClpDAOListener {

	private Spinner spinnerCmdClp;

	private static ArrayList<HashMap<String, String>> listComenziClp, listArtCmdClp = new ArrayList<HashMap<String, String>>();
	public static String selectedCmd = "", selectedCmdSap = "", clpDeSters = "", strCodStatusCmd = "";

	private ListView listViewArtCmdClp;

	private TextView textAdrLivr, textPersContact, textTelefon, textOras, textJudet, textDataLivrare, textTipPlata, textTipTransport, textAprobatOC,
			textObservatii, textTipMarfa, textMasa, textTipCamion, textTipIncarcare;

	private TextView textAcceptDV, textDataInc;

	private LinearLayout layoutCmdCondHead;

	private Integer tipOpCmdClp = -1;
	private static int restrictiiAfisare = 0, intervalAfisare = 0;

	private Button delClpBtn;

	ClpDAO operatiiClp;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.aprobare_clp);

		operatiiClp = new ClpDAO(this);
		operatiiClp.setClpDAOListener(this);

		final ActionBar actionBar = getActionBar();
		actionBar.setTitle("Afisare CLP");
		actionBar.setDisplayHomeAsUpEnabled(true);

		spinnerCmdClp = (Spinner) findViewById(R.id.spinnerCmdClp);
		listComenziClp = new ArrayList<HashMap<String, String>>();

		addSpinnerCmdListener();

		spinnerCmdClp.setVisibility(View.INVISIBLE);

		listViewArtCmdClp = (ListView) findViewById(R.id.listArtCmdClp);
		listViewArtCmdClp.setVisibility(View.INVISIBLE);

		textAdrLivr = (TextView) findViewById(R.id.textAdrLivr);
		textPersContact = (TextView) findViewById(R.id.textPersContact);
		textTelefon = (TextView) findViewById(R.id.textTelefon);
		textOras = (TextView) findViewById(R.id.textOras);
		textJudet = (TextView) findViewById(R.id.textJudet);
		textDataLivrare = (TextView) findViewById(R.id.textDataLivrare);

		textTipMarfa = (TextView) findViewById(R.id.textTipMarfa);
		textMasa = (TextView) findViewById(R.id.textMasa);
		textTipCamion = (TextView) findViewById(R.id.textTipCamion);
		textTipIncarcare = (TextView) findViewById(R.id.textTipIncarcare);

		textObservatii = (TextView) findViewById(R.id.textObservatii);

		textAcceptDV = (TextView) findViewById(R.id.textAcceptDV);
		textDataInc = (TextView) findViewById(R.id.textDataInc);

		layoutCmdCondHead = (LinearLayout) findViewById(R.id.layoutCmdCondHead);
		layoutCmdCondHead.setVisibility(View.INVISIBLE);

		textTipPlata = (TextView) findViewById(R.id.textTipPlata);
		textTipTransport = (TextView) findViewById(R.id.textTransport);
		textAprobatOC = (TextView) findViewById(R.id.textAprobatOC);

		delClpBtn = (Button) findViewById(R.id.delClpBtn);
		delClpBtn.setVisibility(View.INVISIBLE);
		addListenerDelClpBtn();

		performGetComenziClp();

	}

	private void addSpinnerCmdListener() {
		spinnerCmdClp.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				BeanDocumentCLP documentClp = ((BeanDocumentCLP) parent.getAdapter().getItem(position));
				selectedCmd = documentClp.getNrDocument();
				selectedCmdSap = documentClp.getNrDocumentSap();

				performArtCmd();

			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		CreateMenu(menu);
		return true;
	}

	private void CreateMenu(Menu menu) {

		MenuItem mnu1 = menu.add(0, 0, 0, "Interval");

		mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		if (UserInfo.getInstance().getTipAcces().equals("10")) {
			MenuItem mnu2 = menu.add(0, 1, 1, "Cereri");

			mnu2.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case 0:

			final CharSequence[] constrCmd = { "Astazi", "In ultimele 7 zile", "In ultimele 30 de zile" };
			final AlertDialog.Builder builderConstr = new AlertDialog.Builder(this);
			final AlertDialog alertConstr;
			builderConstr.setTitle("Afiseaza cererile create");
			builderConstr.setCancelable(true);
			builderConstr.setSingleChoiceItems(constrCmd, intervalAfisare, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {

					intervalAfisare = which;

					performGetComenziClp();
					dialog.cancel();

				}
			});

			alertConstr = builderConstr.create();
			alertConstr.setCancelable(true);
			alertConstr.show();

			return true;

		case 1:

			final CharSequence[] optTipC = { "Create", "Primite si aprobate", "Primite si respinse" };
			final AlertDialog.Builder builderTipC = new AlertDialog.Builder(this);
			AlertDialog alertTipC;
			builderTipC.setTitle("Afiseaza cererile ");
			builderTipC.setCancelable(true);
			builderTipC.setSingleChoiceItems(optTipC, restrictiiAfisare, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {

					restrictiiAfisare = which;

					performGetComenziClp();
					dialog.cancel();

				}
			});

			alertTipC = builderTipC.create();
			alertTipC.setCancelable(true);
			alertTipC.show();

			return true;

		case android.R.id.home:

			clearAllData();
			UserInfo.getInstance().setParentScreen("");
			final Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);

			startActivity(nextScreen);

			finish();

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	public void performGetComenziClp() {

		String localTipUser = "NN";

		if (UserInfo.getInstance().getTipAcces().equals("9") || UserInfo.getInstance().getTipAcces().equals("27")) {
			localTipUser = "AV";
		}

		if (UserInfo.getInstance().getTipAcces().equals("17")) {
			localTipUser = "CV";
		}

		if (UserInfo.getInstance().getTipAcces().equals("10")) {
			localTipUser = "SD";
		}

		if (UserInfo.getInstance().getTipAcces().equals("12") || UserInfo.getInstance().getTipAcces().equals("14")) {
			localTipUser = "DV";
		}
		
		if (UserInfo.getInstance().getTipAcces().equals("18"))
			localTipUser = "SM";

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("filiala", UserInfo.getInstance().getUnitLog());
		params.put("depart", UserInfo.getInstance().getCodDepart());
		params.put("tipClp", String.valueOf(restrictiiAfisare));
		params.put("interval", String.valueOf(intervalAfisare));
		params.put("tipUser", localTipUser);
		params.put("codUser", UserInfo.getInstance().getCod());
		params.put("tipUserSap", UserInfo.getInstance().getTipUserSap());

		operatiiClp.getListComenzi(params);

	}

	protected void populateCmdList(String cmdList) {

		HandleJSONData objDocList = new HandleJSONData(this, cmdList);
		ArrayList<BeanDocumentCLP> docCLPArray = objDocList.decodeJSONDocumentCLP();

		ComenziCLPAdapter adapterComenziClp = new ComenziCLPAdapter(this, docCLPArray, restrictiiAfisare);
		spinnerCmdClp.setAdapter(adapterComenziClp);

		if (docCLPArray.size() > 0) {
			if (UserInfo.getInstance().getTipAcces().equals("9") || UserInfo.getInstance().getTipAcces().equals("10")
					|| UserInfo.getInstance().getTipAcces().equals("27")) {
				delClpBtn.setVisibility(View.VISIBLE);
			}

			spinnerCmdClp.setVisibility(View.VISIBLE);
			layoutCmdCondHead.setVisibility(View.VISIBLE);
		} else {
			listComenziClp.clear();
			spinnerCmdClp.setAdapter(adapterComenziClp);
			spinnerCmdClp.setVisibility(View.INVISIBLE);

			listArtCmdClp.clear();
			listViewArtCmdClp.setVisibility(View.INVISIBLE);

			layoutCmdCondHead.setVisibility(View.INVISIBLE);
			Toast.makeText(getApplicationContext(), "Nu exista comenzi!", Toast.LENGTH_SHORT).show();

			delClpBtn.setVisibility(View.INVISIBLE);
		}

	}

	private void performArtCmd() {

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("nrCmd", selectedCmd);

		operatiiClp.getArticoleComandaJSON(params);

	}

	private void populateArtCmdList(ComandaCLP dateComanda) {
		if (dateComanda.getArticole().size() > 0) {
			listViewArtCmdClp.setVisibility(View.VISIBLE);
			DateLivrareCLP dateLivrare = dateComanda.getDateLivrare();

			textAdrLivr.setText(dateLivrare.getAdrLivrare());
			textPersContact.setText(dateLivrare.getPersContact());
			textTelefon.setText(dateLivrare.getTelefon());
			textOras.setText(dateLivrare.getOras());
			textJudet.setText(dateLivrare.getJudet());
			textDataLivrare.setText(dateLivrare.getData());
			textTipPlata.setText(dateLivrare.getTipPlata());
			textTipMarfa.setText(dateLivrare.getTipMarfa());
			textMasa.setText(dateLivrare.getMasa());
			textTipCamion.setText(dateLivrare.getTipCamion());
			textTipIncarcare.setText(dateLivrare.getTipIncarcare());
			textTipTransport.setText(dateLivrare.getMijlocTransport());
			textAprobatOC.setText(dateLivrare.getAprobatOC());
			textObservatii.setText(dateLivrare.getObsComanda());

			textAcceptDV.setText(dateLivrare.getAcceptDV());
			textDataInc.setText(dateLivrare.getDataIncarcare());

			List<ArticolCLP> listArticole = dateComanda.getArticole();

			ArticoleCLPAdapter adapterArticole = new ArticoleCLPAdapter(this, listArticole);
			listViewArtCmdClp.setAdapter(adapterArticole);

		}
	}

	public void addListenerDelClpBtn() {
		delClpBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (!clpDeSters.equals("0") || strCodStatusCmd.equals("3") || strCodStatusCmd.equals("5")) {
					Toast.makeText(getApplicationContext(), "Acest document nu poate fi sters!", Toast.LENGTH_LONG).show();
				} else {
					showConfirmationDeletionAlert();
				}

			}
		});

	}

	public void showConfirmationDeletionAlert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Stergeti comanda?").setCancelable(false).setPositiveButton("Da", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				tipOpCmdClp = 2; // stergere
				opereazaComandaClp();

			}
		}).setNegativeButton("Nu", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();

			}
		}).setTitle("Confirmare").setIcon(R.drawable.warning96);

		AlertDialog alert = builder.create();
		alert.show();

	}

	public void opereazaComandaClp() {

		try {

			HashMap<String, String> params = new HashMap<String, String>();

			NumberFormat nf3 = new DecimalFormat("00000000");
			String fullCode = nf3.format(Integer.parseInt(UserInfo.getInstance().getCod())).toString();

			params.put("nrCmd", selectedCmd);
			params.put("nrCmdSAP", selectedCmdSap);
			params.put("tipOp", String.valueOf(tipOpCmdClp));
			params.put("codUser", fullCode);

			operatiiClp.operatiiComanda(params);

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	private void refreshClpList() {

		performGetComenziClp();

	}

	private void clearAllData() {
		selectedCmd = "";
		selectedCmdSap = "";
		clpDeSters = "";
		strCodStatusCmd = "";
		tipOpCmdClp = -1;
		restrictiiAfisare = 0;
		intervalAfisare = 0;
	}

	@Override
	public void onResume() {

		super.onResume();
		checkStaticVars();
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

		clearAllData();
		UserInfo.getInstance().setParentScreen("");
		Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);

		startActivity(nextScreen);

		finish();
		return;
	}

	public void operationClpComplete(EnumClpDAO methodName, Object result) {
		switch (methodName) {
		case GET_LIST_COMENZI:
			populateCmdList((String) result);
			break;
		case GET_ARTICOLE_COMANDA_JSON:
			populateArtCmdList(operatiiClp.decodeArticoleComanda((String) result));
			break;
		case OPERATIE_COMANDA:
			Toast.makeText(getApplicationContext(), (String) result, Toast.LENGTH_SHORT).show();
			refreshClpList();
		default:
			break;

		}

	}

}
