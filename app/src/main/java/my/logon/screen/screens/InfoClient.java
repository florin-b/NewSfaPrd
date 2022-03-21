/**
 * @author florinb
 *
 */
package my.logon.screen.screens;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.model.HandleJSONData;
import my.logon.screen.model.UserInfo;
import my.logon.screen.R;
import my.logon.screen.utils.UtilsUser;
import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import my.logon.screen.beans.BeanAdresaLivrare;
import my.logon.screen.beans.BeanClient;

public class InfoClient extends Activity implements AsyncTaskListener {

	Button clientBtn;
	String filiala = "", nume = "", cod = "";
	String clientResponse = "";
	String codClient = "";
	String numeClient = "";
	String depart = "";
	String codClientVar = "";
	String numeClientVar = "";
	ListView listViewClienti, listViewInfo_1;
	ArrayList<HashMap<String, String>> arrayInfo_1 = null;
	SimpleAdapter adapterUserInfo_1;

	private EditText txtNumeClient;
	private TextView textNumeClient, textCodClient;

	private NumberFormat nf2;
	int selectedInfoOption = 0;

	private static ArrayList<HashMap<String, String>> listClienti = null;
	public SimpleAdapter adapterClienti;

	private HashMap<String, String> artMap = null;
	private LinearLayout head_container, list_container;

	private Dialog optionsInfoDialog;

	String[] judete = { "ALBA", "ARAD", "ARGES", "BACAU", "BIHOR", "BISTRITA-NASAUD", "BOTOSANI", "BRAILA", "BRASOV",
			"BUCURESTI", "BUZAU", "CALARASI", "CARAS-SEVERIN", "CLUJ", "CONSTANTA", "COVASNA", "DAMBOVITA", "DOLJ",
			"GALATI", "GIURGIU", "GORJ", "HARGHITA", "HUNEDOARA", "IALOMITA", "IASI", "ILFOV", "MARAMURES",
			"MEHEDINTI", "MURES", "NEAMT", "OLT", "PRAHOVA", "SALAJ", "SATU-MARE", "SIBIU", "SUCEAVA", "TELEORMAN",
			"TIMIS", "TULCEA", "VALCEA", "VASLUI", "VRANCEA" };

	String[] codJudete = { "01", "02", "03", "04", "05", "06", "07", "09", "08", "40", "10", "51", "11", "12", "13",
			"14", "15", "16", "17", "52", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "31",
			"30", "32", "33", "34", "35", "36", "38", "37", "39" };

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);

		setContentView(R.layout.infoclient_container);

		View vHeader = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.infoclient_cautaclient, null, false);
		vHeader.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		head_container = (LinearLayout) findViewById(R.id.headerContainer);
		head_container.addView(vHeader);

		View vLista = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.infoclient_listaclienti, null, false);
		vLista.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		list_container = (LinearLayout) findViewById(R.id.detailsContainer);
		list_container.addView(vLista);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Informatii client");
		actionBar.setDisplayHomeAsUpEnabled(true);

		createObjCautaClient();

		nf2 = NumberFormat.getInstance();
		nf2.setMinimumFractionDigits(2);
		nf2.setMaximumFractionDigits(2);

	}

	private void createObjCautaClient() {
		this.clientBtn = (Button) findViewById(R.id.clientBtn);
		addListenerClient();

		txtNumeClient = (EditText) findViewById(R.id.txtNumeClient);
		txtNumeClient.setHint("Introduceti nume client");
		txtNumeClient.requestFocus();
		InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.showSoftInput(txtNumeClient, InputMethodManager.SHOW_IMPLICIT);

		listViewClienti = (ListView) findViewById(R.id.listVClienti);
		listClienti = new ArrayList<HashMap<String, String>>();
		adapterClienti = new SimpleAdapter(this, listClienti, R.layout.customrownumeclient, new String[] {
				"numeClient", "codClient" }, new int[] { R.id.textNumeClient, R.id.textCodClient });

		listViewClienti.setAdapter(adapterClienti);
		addListenerListClienti();

	}

	private void createObjAfisClientInfo() {
		textNumeClient = (TextView) findViewById(R.id.textNumeClient);
		textCodClient = (TextView) findViewById(R.id.textCodClient);

		textNumeClient.setText(numeClient);
		textCodClient.setText(codClient);

	}

	private void CreateMenu(Menu menu) {
		MenuItem mnu1 = menu.add(0, 0, 0, "Client");

		mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		MenuItem mnu2 = menu.add(0, 1, 1, "Info");

		mnu2.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		CreateMenu(menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case 0:

			numeClient = "";
			codClient = "";

			head_container.removeAllViews();
			list_container.removeAllViews();

			View vHeader = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
					R.layout.infoclient_cautaclient, null, false);
			vHeader.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

			head_container.addView(vHeader);

			View vLista = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
					R.layout.infoclient_listaclienti, null, false);
			vLista.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

			list_container.addView(vLista);

			createObjCautaClient();

			return true;

		case 1:

			if (numeClient.equals("")) {
				Toast.makeText(getApplicationContext(), "Selectati un client!", Toast.LENGTH_SHORT).show();
				return true;
			}

			String[] options = { "Pondere art. B ultimele 30 zile", "Adrese, contacte", "Credit" };

			optionsInfoDialog = new Dialog(InfoClient.this);
			optionsInfoDialog.setContentView(R.layout.infoclient_optionsdialog);
			optionsInfoDialog.setTitle("Selectati o optiune:");

			Spinner spinnerSelInfo = (Spinner) optionsInfoDialog.findViewById(R.id.spinnerOptionsInfo);

			ArrayList<HashMap<String, String>> listInfo = new ArrayList<HashMap<String, String>>();
			SimpleAdapter adapterOptions = new SimpleAdapter(this, listInfo, R.layout.customrowselinterval,
					new String[] { "optInterval" }, new int[] { R.id.textTipInterval });

			HashMap<String, String> temp;

			for (int ii = 0; ii < options.length; ii++) {
				temp = new HashMap<String, String>(10, 0.75f);
				temp.put("optInterval", options[ii]);
				listInfo.add(temp);
			}

			spinnerSelInfo.setAdapter(adapterOptions);

			spinnerSelInfo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

					selectedInfoOption = pos;

				}

				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub

				}

			});

			Button btnOkInfo = (Button) optionsInfoDialog.findViewById(R.id.btnOkInfo);
			btnOkInfo.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {

					optionsInfoDialog.dismiss();
					afisClientInfo();

				}
			});

			optionsInfoDialog.show();
			return true;

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

	private void afisClientInfo() {

		try {

			HashMap<String, String> params = new HashMap<String, String>();

			params.put("codClient", codClient);
			params.put("filiala", UserInfo.getInstance().getUnitLog());
			params.put("depart", UserInfo.getInstance().getCodDepart());
			params.put("tipInfo", String.valueOf(selectedInfoOption));

			AsyncTaskWSCall call = new AsyncTaskWSCall(this, "getClientInfo", params);
			call.getCallResults();

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	@SuppressWarnings("unchecked")
	private void fillClientInfo(String clientInfo) {

		if (!clientInfo.trim().equals("")) {

			View vLista = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
					R.layout.infoclient_list1, null, false);
			vLista.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

			if (list_container.getChildCount() == 0) {
				list_container.addView(vLista);
				listViewInfo_1 = (ListView) findViewById(R.id.listInfoClient_1);

				arrayInfo_1 = new ArrayList<HashMap<String, String>>();
				adapterUserInfo_1 = new SimpleAdapter(this, arrayInfo_1, R.layout.infoclient_1_rowlayout, new String[] {
						"textLinie1", "textLinie2" }, new int[] { R.id.textLinie1, R.id.textLinie2 });
				arrayInfo_1.clear();

			}

			if (selectedInfoOption == 0) // pondere B 30 zile
			{

				// ***** elimiare existente

				int ii = 0;

				for (ii = 0; ii < arrayInfo_1.size(); ii++) {
					artMap = (HashMap<String, String>) adapterUserInfo_1.getItem(ii);

					if (artMap.get("textLinie1").contains("Pondere")) {
						arrayInfo_1.remove(ii);
						ii--;
					}

					listViewInfo_1.setAdapter(adapterUserInfo_1);
					adapterUserInfo_1.notifyDataSetChanged();

				}// sf. for

				// ***** sf. eliminare

				HashMap<String, String> temp;

				temp = new HashMap<String, String>(1, 0.75f);
				temp.put("textLinie1", "Pondere art. B in ultimele 30 zile: ");
				temp.put("textLinie2", clientInfo + "% ");
				arrayInfo_1.add(temp);

				listViewInfo_1.setAdapter(adapterUserInfo_1);

			}

			if (selectedInfoOption == 1) // adrese de livrare
			{

				// ***** eliminare existente

				int ii = 0;

				for (ii = 0; ii < arrayInfo_1.size(); ii++) {
					artMap = (HashMap<String, String>) adapterUserInfo_1.getItem(ii);

					if (artMap.get("textLinie1").contains("Adresa")) {
						arrayInfo_1.remove(ii);
						ii--;
					}

					listViewInfo_1.setAdapter(adapterUserInfo_1);
					adapterUserInfo_1.notifyDataSetChanged();

				}// sf. for

				// ***** sf. eliminare

				HandleJSONData objClientList = new HandleJSONData(this, clientInfo);
				ArrayList<BeanAdresaLivrare> adresaArray = objClientList.decodeJSONAdresaLivrare();

				HashMap<String, String> temp;
				String strAdresa = "";
				for (int i = 0; i < adresaArray.size(); i++) {
					temp = new HashMap<String, String>(10, 0.75f);
					strAdresa = getNumeJudet(adresaArray.get(i).getCodJudet()) + "; " + adresaArray.get(i).getOras()
							+ "; " + adresaArray.get(i).getStrada() + "; " + adresaArray.get(i).getNrStrada() + ";";

					temp.put("textLinie1", "Adresa " + String.valueOf(i + 1) + ":");
					temp.put("textLinie2", strAdresa);
					arrayInfo_1.add(temp);

				}

				listViewInfo_1.setAdapter(adapterUserInfo_1);
				/*
				// pers. contact
				if (tokenLinie.length - 1 == i) {
					oAdresa = tokenLinie[i];
					tokenAdresa = oAdresa.split("#");

					if (!tokenAdresa[0].trim().equals("")) {
						strAdresa = tokenAdresa[0];

						if (!tokenAdresa[1].trim().equals("")) {
							strAdresa += ", tel: " + tokenAdresa[1];
						}

						temp = new HashMap<String, String>(1, 0.75f);
						temp.put("textLinie1", "Pers. contact: ");
						temp.put("textLinie2", strAdresa);
						arrayInfo_1.add(temp);
						listViewInfo_1.setAdapter(adapterUserInfo_1);
					}
				}
				*/

			}// sf. adr. livrare

			if (selectedInfoOption == 2) // credit
			{

				// ***** eliminare existente

				int ii = 0;

				for (ii = 0; ii < arrayInfo_1.size(); ii++) {
					artMap = (HashMap<String, String>) adapterUserInfo_1.getItem(ii);

					if (artMap.get("textLinie1").contains("Limita")) {
						arrayInfo_1.remove(ii);
						ii--;
					}

					if (artMap.get("textLinie1").contains("Rest")) {
						arrayInfo_1.remove(ii);
						ii--;
					}

					listViewInfo_1.setAdapter(adapterUserInfo_1);
					adapterUserInfo_1.notifyDataSetChanged();

				}// sf. for

				// ***** sf. eliminare

				HashMap<String, String> temp;
				String[] tokenCredit = clientInfo.split("#");

				NumberFormat nf = NumberFormat.getInstance();
				nf.setMinimumFractionDigits(2);
				nf.setMaximumFractionDigits(2);

				temp = new HashMap<String, String>();
				temp.put("textLinie1", "Limita credit: ");
				temp.put("textLinie2", nf.format(Double.parseDouble(tokenCredit[0])) + " RON");
				arrayInfo_1.add(temp);

				temp = new HashMap<String, String>();
				temp.put("textLinie1", "Rest credit: ");
				temp.put("textLinie2", nf.format(Double.parseDouble(tokenCredit[1])) + " RON");
				arrayInfo_1.add(temp);

				listViewInfo_1.setAdapter(adapterUserInfo_1);

			}

		}

	}

	private String getNumeJudet(String codJudet) {
		String numeJudet = "nedefinit";

		int i = 0;
		for (i = 0; i < codJudete.length; i++) {

			if (codJudet.equals(codJudete[i])) {

				numeJudet = judete[i];
				break;
			}

		}

		return numeJudet;
	}

	@SuppressWarnings("unchecked")
	public void addListenerListClienti() {

		listViewClienti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

				artMap = (HashMap<String, String>) adapterClienti.getItem(position);

				numeClient = artMap.get("numeClient");

				codClient = artMap.get("codClient");

				head_container.removeAllViews();
				list_container.removeAllViews();

				View vHeader = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
						R.layout.infoclient_selectedclient, null, false);
				vHeader.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT));

				head_container.addView(vHeader);

				createObjAfisClientInfo();
			}
		});

	}

	public void populateListViewClient(String clientResponse) {

		HandleJSONData objClientList = new HandleJSONData(this, clientResponse);
		ArrayList<BeanClient> clientArray = objClientList.decodeJSONClientList();

		if (clientArray.size() > 0) {
			HashMap<String, String> temp;

			for (int i = 0; i < clientArray.size(); i++) {
				temp = new HashMap<String, String>();

				temp.put("numeClient", clientArray.get(i).getNumeClient());
				temp.put("codClient", clientArray.get(i).getCodClient());
				listClienti.add(temp);
			}
		} else {
			Toast.makeText(getApplicationContext(), "Nu exista inregistrari!", Toast.LENGTH_SHORT).show();

		}

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

			HashMap<String, String> params = new HashMap<String, String>();

			String numeClient = txtNumeClient.getText().toString().trim().replace('*', '%');
			String depSel = "";

			depSel = UserInfo.getInstance().getCodDepart();
			if (CreareComanda.canalDistrib.equals("20"))
				depSel = "11";
			
			String tipUserSap = UserInfo.getInstance().getTipUserSap();

			if (UtilsUser.isDV() && UserInfo.getInstance().getInitDivizie().equals("11"))
				tipUserSap = "SDIP";

			params.put("numeClient", numeClient);
			params.put("depart", depSel);
			params.put("departAg", UserInfo.getInstance().getCodDepart());
			params.put("unitLog", UserInfo.getInstance().getUnitLog());
			params.put("tipUserSap", tipUserSap);

			AsyncTaskWSCall call = new AsyncTaskWSCall(this, "cautaClientAndroid", params);
			call.getCallResults();

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	private void listClients(String clientResponse) {

		if (clientResponse.length() > 0) {
			listClienti.clear();
			adapterClienti.notifyDataSetChanged();

			populateListViewClient(clientResponse);
			listViewClienti.setAdapter(adapterClienti);

			InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			mgr.hideSoftInputFromWindow(txtNumeClient.getWindowToken(), 0);
			txtNumeClient.setText("");
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
		if (methodName.equals("getClientInfo")) {
			fillClientInfo((String)result);
		}

		if (methodName.equals("cautaClientAndroid")) {
			listClients((String)result);
		}

	}

}