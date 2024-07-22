/**
 * @author florinb
 *
 */
package my.logon.screen.screens;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import my.logon.screen.R;
import my.logon.screen.adapters.CautareClientiAdapter;
import my.logon.screen.beans.BeanClient;
import my.logon.screen.beans.DetaliiClient;
import my.logon.screen.enums.EnumClienti;
import my.logon.screen.helpers.DialogHelper;
import my.logon.screen.listeners.OperatiiClientListener;
import my.logon.screen.model.ClientiGenericiGedInfoStrings;
import my.logon.screen.model.DateLivrare;
import my.logon.screen.model.OperatiiClient;
import my.logon.screen.model.UserInfo;
import my.logon.screen.utils.UtilsGeneral;
import my.logon.screen.utils.UtilsUser;

public class SelectClientCmd extends ListActivity implements OperatiiClientListener {

	Button clientBtn, saveClntBtn;
	String filiala = "", nume = "", cod = "";
	String clientResponse = "";
	String codClient = "";
	String numeClient = "";
	String depart = "";
	String codClientVar = "";
	String numeClientVar = "", tipClientVar = "";

	private EditText txtNumeClient;
	private TextView codClientText, tipClient;
	private TextView numeClientText;
	private TextView adresaText;
	private TextView limCreditText;
	private TextView restCreditText;
	private TextView clientBlocatText, filClientText;
	private CheckBox cmdAngaj;

	ToggleButton tglTipComanda;
	private NumberFormat nf2;

	private static ArrayList<HashMap<String, String>> listClienti = null;

	LinearLayout resLayout;

	OperatiiClient opClient;
	private Spinner spinnerAgenti;
	private RadioButton radioCmdFerma, radioCmdSimulata;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.selectclientcmdheader);

		opClient = new OperatiiClient(this);
		opClient.setOperatiiClientListener(this);

		this.clientBtn = (Button) findViewById(R.id.clientBtn);
		addListenerClient();

		this.saveClntBtn = (Button) findViewById(R.id.saveClntBtn);
		addListenerSave();

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Selectie client");
		actionBar.setDisplayHomeAsUpEnabled(true);

		checkStaticVars();

		resLayout = (LinearLayout) findViewById(R.id.resLayout);
		resLayout.setVisibility(View.INVISIBLE);

		txtNumeClient = (EditText) findViewById(R.id.txtNumeClient);

		codClientText = (TextView) findViewById(R.id.textCodClient);
		numeClientText = (TextView) findViewById(R.id.textNumeClient);
		adresaText = (TextView) findViewById(R.id.textAdrClient);
		limCreditText = (TextView) findViewById(R.id.textLimitaCredit);
		restCreditText = (TextView) findViewById(R.id.textRestCredit);
		clientBlocatText = (TextView) findViewById(R.id.clientBlocat);
		tipClient = (TextView) findViewById(R.id.tipClient);
		filClientText = (TextView) findViewById(R.id.filClient);

		cmdAngaj = (CheckBox) findViewById(R.id.checkboxCmdAng);

		txtNumeClient.setHint("Introduceti nume client");

		listClienti = new ArrayList<HashMap<String, String>>();

		saveClntBtn.setVisibility(View.INVISIBLE);

		this.tglTipComanda = (ToggleButton) findViewById(R.id.tglTipComanda);
		addListenerTipComanda();

		if (UserInfo.getInstance().getTipAcces().equals("27")) // KA fara
																// clienti ged
		{
			this.tglTipComanda.setVisibility(View.INVISIBLE);
			cmdAngaj.setVisibility(View.INVISIBLE);
		}

		CreareComanda.canalDistrib = "10";

		nf2 = NumberFormat.getInstance();
		nf2.setMinimumFractionDigits(2);
		nf2.setMaximumFractionDigits(2);

		spinnerAgenti = ((Spinner) findViewById(R.id.spinnerAgenti));
		setSpinnerAgentiListener();

		if (UtilsUser.isSuperAv() || UtilsUser.isSMR() || UtilsUser.isCVR() || UtilsUser.isSSCM() || UtilsUser.isCGED() || UtilsUser.isOIVPD()
				|| UtilsUser.isASDL()) {
			((LinearLayout) findViewById(R.id.layoutAgentClient)).setVisibility(View.VISIBLE);
		}

		radioCmdFerma = (RadioButton) findViewById(R.id.radio_cmd_ferma);
		radioCmdSimulata = (RadioButton) findViewById(R.id.radio_cmd_simulata);

		radioCmdFerma.setVisibility(View.INVISIBLE);
		radioCmdSimulata.setVisibility(View.INVISIBLE);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:

			DateLivrare dateLivrareInstance = DateLivrare.getInstance();

			dateLivrareInstance.setOras("");
			dateLivrareInstance.setStrada("");
			dateLivrareInstance.setCodJudet("");
			CreareComanda.limitaCredit = 0;
			CreareComanda.restCredit = 0;
			CreareComanda.termenPlata = "";
			dateLivrareInstance.setPersContact("");
			dateLivrareInstance.setNrTel("");
			codClientVar = "";
			numeClientVar = "";
			tipClientVar = "";
			dateLivrareInstance.setPersContact("");
			dateLivrareInstance.setNrTel("");
			dateLivrareInstance.setDateLivrare("");

			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	public void addListenerTipComanda() {
		tglTipComanda.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (tglTipComanda.isChecked()) {

					CreareComanda.canalDistrib = "20";

					listClienti.clear();

					numeClientText.setText("");
					codClientText.setText("");
					adresaText.setText("");
					limCreditText.setText("");
					restCreditText.setText("");
					codClientVar = "";
					numeClientVar = "";
					tipClient.setVisibility(View.GONE);
					filClientText.setVisibility(View.GONE);
					cmdAngaj.setVisibility(View.INVISIBLE);
					clientBlocatText.setText("");

				} else {

					CreareComanda.canalDistrib = "10";

					listClienti.clear();

					numeClientText.setText("");
					codClientText.setText("");
					adresaText.setText("");
					limCreditText.setText("");
					restCreditText.setText("");
					codClientVar = "";
					numeClientVar = "";
					tipClient.setVisibility(View.GONE);
					filClientText.setVisibility(View.GONE);
					cmdAngaj.setVisibility(View.VISIBLE);
					clientBlocatText.setText("");

				}

			}
		});

	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		BeanClient client = (BeanClient) l.getAdapter().getItem(position);
		numeClient = client.getNumeClient();
		codClient = client.getCodClient();
		tipClientVar = client.getTipClient();

		if (UtilsUser.isSuperAv() || UtilsUser.isSMR() || UtilsUser.isCVR() || UtilsUser.isSSCM() || UtilsUser.isCGED() || UtilsUser.isOIVPD()
				|| UtilsUser.isASDL()) {

			String[] tokAgenti = client.getAgenti().split("@");

			ArrayList<HashMap<String, String>> listAgenti = new ArrayList<HashMap<String, String>>();

			HashMap<String, String> agent = new HashMap<String, String>();
			agent.put("numeAgent", "Selectati un agent");
			agent.put("codAgent", "");

			listAgenti.add(agent);

			for (int i = 0; i < tokAgenti.length; i++) {
				agent = new HashMap<String, String>();

				agent.put("numeAgent", tokAgenti[i].split("#")[1]);
				agent.put("codAgent", tokAgenti[i].split("#")[0]);
				listAgenti.add(agent);
			}

			SimpleAdapter adapterAgenti = new SimpleAdapter(this, listAgenti, R.layout.rowlayoutagenti, new String[] { "numeAgent", "codAgent" }, new int[] {
					R.id.textNumeAgent, R.id.textCodAgent });

			spinnerAgenti.setAdapter(adapterAgenti);

			if (tokAgenti.length == 1)
				spinnerAgenti.setSelection(1);

		}

		performClientDetails();

	}

	private void setSpinnerAgentiListener() {

		spinnerAgenti.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				@SuppressWarnings("unchecked")
				HashMap<String, String> artMap = (HashMap<String, String>) arg0.getSelectedItem();
				UserInfo.getInstance().setCod(artMap.get("codAgent"));

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

	}

	private void performClientDetails() {

		try {
			HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
			params.put("codClient", codClient);
			params.put("depart", getExceptiiDepartament());
			params.put("codUser", UserInfo.getInstance().getCod());

			opClient.getDetaliiClient(params);

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	public void addListenerSave() {
		saveClntBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (codClientVar.length() == 0) {
					Toast.makeText(getApplicationContext(), "Selectati un client!", Toast.LENGTH_SHORT).show();
				} else {

					if ((UtilsUser.isSuperAv() || UtilsUser.isASDL()) && spinnerAgenti.getSelectedItemPosition() == 0) {
						Toast.makeText(getApplicationContext(), "Selectati un agent.", Toast.LENGTH_LONG).show();
						return;
					}

					CreareComanda.codClientVar = codClientVar;
					CreareComanda.numeClientVar = numeClientVar;
					CreareComanda.tipClientVar = tipClientVar;

					CreareComanda.cmdAngajament = false;

					DateLivrare.getInstance().setRefClient(((EditText) findViewById(R.id.textRefClient)).getText().toString().trim());

					if (CreareComanda.canalDistrib.equals("10")) {

						if (cmdAngaj.isChecked())
							CreareComanda.cmdAngajament = true;
					}

					if (radioCmdFerma.isChecked()) {
						CreareComanda.tipComanda = "N";
					} else if (radioCmdSimulata.isChecked()) {
						CreareComanda.cmdAngajament = false;
						CreareComanda.tipComanda = "S";
					}

					finish();
				}

			}
		});

	}

	public void populateListViewClient(List<BeanClient> listClienti) {
		CautareClientiAdapter adapterClienti = new CautareClientiAdapter(this, listClienti);
		setListAdapter(adapterClienti);

	}

	public void addListenerClient() {
		clientBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					if (txtNumeClient.length() > 0) {
						performListClients();
					} else {
						Toast.makeText(getApplicationContext(), "Introduceti nume client!", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception ex) {
					Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	private void performListClients() {
		try {

			String numeClient = txtNumeClient.getText().toString().trim().replace('*', '%');

			HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
			params.put("numeClient", numeClient);
			params.put("depart", getExceptiiDepartament());
			params.put("departAg", UserInfo.getInstance().getCodDepart());
			params.put("unitLog", UserInfo.getInstance().getUnitLog());
			params.put("codUser", UserInfo.getInstance().getCod());
			params.put("tipUserSap", UserInfo.getInstance().getTipUserSap());

			opClient.getListClienti(params);

			InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	String getExceptiiDepartament() {
		String depSel = UserInfo.getInstance().getCodDepart();
		if (CreareComanda.canalDistrib.equals("20"))
			depSel = "11";

		// poate selecta client din orice departament
		if (!CreareComanda.canalDistrib.equals("20") && UserInfo.getInstance().getTipUser().equals("KA")) {
			depSel = "00";
		}

		return depSel;
	}

	void listClientDetails(DetaliiClient detaliiClient) {

		resLayout.setVisibility(View.VISIBLE);

		numeClientText.setText(numeClient);
		codClientText.setText(codClient);

		adresaText.setText(detaliiClient.getOras() + " " + detaliiClient.getStrada() + " " + detaliiClient.getNrStrada());

		CreareComanda.limitaCredit = Double.parseDouble(detaliiClient.getLimitaCredit());
		CreareComanda.restCredit = Double.parseDouble(detaliiClient.getRestCredit());

		limCreditText.setText(nf2.format(CreareComanda.limitaCredit));
		restCreditText.setText(nf2.format(CreareComanda.restCredit));

		saveClntBtn.setVisibility(View.VISIBLE);

		filClientText.setText(detaliiClient.getFiliala());

		CreareComanda.termenPlata = detaliiClient.getTermenPlata();
		CreareComanda.cursValutar = Double.parseDouble(detaliiClient.getCursValutar());

		CreareComanda.tipPlataContract = detaliiClient.getTipPlata();

		((TextView) findViewById(R.id.diviziiClient)).setText(detaliiClient.getDivizii());

		DateLivrare.getInstance().setClientBlocat(detaliiClient.getStare().equals("X"));

		if (detaliiClient.getStare().equals("X")) {
			clientBlocatText.setVisibility(View.VISIBLE);
			clientBlocatText.setText("Blocat : " + detaliiClient.getMotivBlocare());
		} else {
			clientBlocatText.setVisibility(View.INVISIBLE);
			clientBlocatText.setText("");
		}

		DateLivrare dateLivrareInstance = DateLivrare.getInstance();
		dateLivrareInstance.setOras(detaliiClient.getOras());
		dateLivrareInstance.setStrada(detaliiClient.getStrada() + " " + detaliiClient.getNrStrada());
		dateLivrareInstance.setCodJudet(detaliiClient.getRegiune());

		if (CreareComanda.canalDistrib.equals("10"))
			dateLivrareInstance.setRedSeparat("R");
		else
			dateLivrareInstance.setRedSeparat(" ");

		dateLivrareInstance.setPersContact(detaliiClient.getPersContact());
		dateLivrareInstance.setNrTel(detaliiClient.getTelefon());
		dateLivrareInstance.setClientFurnizor(detaliiClient.isFurnizor());
		dateLivrareInstance.setDiviziiClient(detaliiClient.getDivizii());

		codClientVar = codClient;
		numeClientVar = numeClient;

		dateLivrareInstance.setDateLivrare(ClientiGenericiGedInfoStrings.numeJudet(dateLivrareInstance.getCodJudet()) + " " + dateLivrareInstance.getOras() + " "
				+ dateLivrareInstance.getStrada() + "#" + dateLivrareInstance.getPersContact() + "#" + dateLivrareInstance.getNrTel() + "#NU#O#TRAP#NU");

		dateLivrareInstance.setMail(detaliiClient.getEmail());

		tipClient.setText(detaliiClient.getTipClient());
		saveClntBtn.setVisibility(View.VISIBLE);

		if (detaliiClient.getErrMsg() != null && !detaliiClient.getErrMsg().trim().isEmpty() ) {
			new DialogHelper().showInfoDialog(this, "\n" + detaliiClient.getErrMsg() + "\n");
			saveClntBtn.setVisibility(View.INVISIBLE);
		}

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

		DateLivrare dateLivrareInstance = DateLivrare.getInstance();

		dateLivrareInstance.setOras("");
		dateLivrareInstance.setStrada("");
		dateLivrareInstance.setCodJudet("");
		CreareComanda.limitaCredit = 0;
		CreareComanda.restCredit = 0;
		CreareComanda.termenPlata = "";
		dateLivrareInstance.setPersContact("");
		dateLivrareInstance.setNrTel("");
		codClientVar = "";
		numeClientVar = "";
		tipClientVar = "";
		dateLivrareInstance.setPersContact("");
		dateLivrareInstance.setNrTel("");
		dateLivrareInstance.setDateLivrare("");
		dateLivrareInstance.setRefClient("");

		finish();
		return;
	}

	public void operationComplete(EnumClienti methodName, Object result) {
		switch (methodName) {
		case GET_LISTA_CLIENTI:
			populateListViewClient(opClient.deserializeListClienti((String) result));
			break;

		case GET_DETALII_CLIENT:
			listClientDetails(opClient.deserializeDetaliiClient((String) result));
			break;

		default:
			break;
		}

	}

}