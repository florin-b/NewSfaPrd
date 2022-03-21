/**
 * @author florinb
 * 
 */
package my.logon.screen.screens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.logon.screen.listeners.DlDAOListener;
import my.logon.screen.model.DlDAO;
import my.logon.screen.model.HandleJSONData;
import my.logon.screen.model.UserInfo;
import my.logon.screen.adapters.ArticoleCLPAdapter;
import my.logon.screen.adapters.ComenziDLAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import my.logon.screen.beans.ArticolCLP;
import my.logon.screen.beans.BeanDocumentCLP;
import my.logon.screen.beans.ComandaCLP;
import my.logon.screen.beans.DateLivrareCLP;
import my.logon.screen.enums.EnumDlDAO;
import my.logon.screen.R;

public class AfisareDispozitiiLivrareActivity extends Activity implements DlDAOListener {

	Spinner spinnerCmdClp;

	public static String selectedCmd = "", selectedCmdSap = "";
	private ListView listViewArtCmdClp;

	private TextView textAdrLivr, textPersContact, textTelefon, textOras, textJudet, textDataLivrare, textTipMarfa, textMasa, textTipCamion, textTipIncarcare,
			textAprobatOC, textTipPlata, textObservatii, textTransport;

	private LinearLayout layoutCmdCondHead;

	private static int restrictiiAfisare = 0, intervalAfisare = 0;

	DlDAO operatiiComenzi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.afisare_dispozitii_livrare);

		operatiiComenzi = new DlDAO(this);
		operatiiComenzi.setDlDAOListener(this);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Afisare DL");
		actionBar.setDisplayHomeAsUpEnabled(true);

		spinnerCmdClp = (Spinner) findViewById(R.id.spinnerCmdClp);

		spinnerCmdClp.setVisibility(View.INVISIBLE);
		addSpinnerListener();

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

		layoutCmdCondHead = (LinearLayout) findViewById(R.id.layoutCmdCondHead);
		layoutCmdCondHead.setVisibility(View.INVISIBLE);

		textAprobatOC = (TextView) findViewById(R.id.textAprobatOC);

		textTipPlata = (TextView) findViewById(R.id.textTipPlata);
		textObservatii = (TextView) findViewById(R.id.textObservatii);
		textTransport = (TextView) findViewById(R.id.textTransport);

		performGetComenziClp();

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

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case 0:

			final CharSequence[] constrCmd = { "Astazi", "In ultimele 7 zile", "In ultimele 30 de zile" };
			AlertDialog.Builder builderConstr = new AlertDialog.Builder(this);
			AlertDialog alertConstr;
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

		case android.R.id.home:

			clearAllData();
			UserInfo.getInstance().setParentScreen("");
			Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);

			startActivity(nextScreen);

			finish();

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	public void performGetComenziClp() {

		HashMap<String, String> params = new HashMap<String, String>();

		String localTipUser = "NN";

		if (UserInfo.getInstance().getTipAcces().equals("9") || UserInfo.getInstance().getTipAcces().equals("27")) {
			localTipUser = "AV";
		}

		if (UserInfo.getInstance().getTipAcces().equals("10"))
			localTipUser = "SD";

		params.put("filiala", UserInfo.getInstance().getUnitLog());
		params.put("depart", UserInfo.getInstance().getCodDepart());
		params.put("tipClp", String.valueOf(restrictiiAfisare));
		params.put("interval", String.valueOf(intervalAfisare));
		params.put("tipUser", localTipUser);
		params.put("codUser", UserInfo.getInstance().getCod());

		operatiiComenzi.getListComenzi(params);

	}

	void populateCmdList(String cmdList) {

		HandleJSONData objDocList = new HandleJSONData(this, cmdList);
		ArrayList<BeanDocumentCLP> listDocumente = objDocList.decodeJSONDocumentCLP();

		ComenziDLAdapter adapterComenziClp = new ComenziDLAdapter(this, listDocumente);
		spinnerCmdClp.setAdapter(adapterComenziClp);

		if (listDocumente.size() > 0) {
			spinnerCmdClp.setVisibility(View.VISIBLE);
			listViewArtCmdClp.setVisibility(View.VISIBLE);
			layoutCmdCondHead.setVisibility(View.VISIBLE);

		} else {
			layoutCmdCondHead.setVisibility(View.INVISIBLE);
			spinnerCmdClp.setVisibility(View.INVISIBLE);
			listViewArtCmdClp.setVisibility(View.INVISIBLE);
			Toast.makeText(getApplicationContext(), "Nu exista comenzi!", Toast.LENGTH_SHORT).show();
		}

	}

	private void performArtCmd() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("nrCmd", selectedCmd);
		operatiiComenzi.getArticoleComandaJSON(params);

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
			textTipMarfa.setText(dateLivrare.getTipMarfa());
			textMasa.setText(dateLivrare.getMasa());
			textTipCamion.setText(dateLivrare.getTipCamion());
			textTipIncarcare.setText(dateLivrare.getTipIncarcare());
			textTransport.setText(dateLivrare.getMijlocTransport());
			textAprobatOC.setText(dateLivrare.getAprobatOC());
			textTipPlata.setText(dateLivrare.getTipPlata());
			textObservatii.setText(dateLivrare.getObsComanda());

			((TextView) findViewById(R.id.textNrCT)).setText(dateLivrare.getNrCT());

			List<ArticolCLP> listArticole = dateComanda.getArticole();

			ArticoleCLPAdapter adapterArticole = new ArticoleCLPAdapter(this, listArticole);
			listViewArtCmdClp.setAdapter(adapterArticole);

		}

	}

	private void clearAllData() {

		selectedCmd = "";
		selectedCmdSap = "";
		restrictiiAfisare = 0;
		intervalAfisare = 0;

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

	void addSpinnerListener() {
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

	public void operationDlComplete(EnumDlDAO methodName, Object result) {
		switch (methodName) {
		case GET_LIST_COMENZI:
			populateCmdList((String) result);
			break;
		case GET_ARTICOLE_COMANDA_JSON:
			populateArtCmdList(operatiiComenzi.decodeArticoleComanda((String) result));
			break;
		default:
			break;

		}

	}
}
