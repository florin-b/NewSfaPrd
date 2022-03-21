/**
 * @author florinb
 *
 */
package my.logon.screen.screens;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import my.logon.screen.R;
import my.logon.screen.beans.BeanClient;
import my.logon.screen.connectors.ConnectionStrings;
import my.logon.screen.listeners.SearchClientListener;
import my.logon.screen.model.HandleJSONData;
import my.logon.screen.model.UserInfo;

public class SelectClientReduceri extends Activity implements Observer {

	Button clientBtn, saveClntBtn;
	String filiala = "", nume = "", cod = "";
	String clientResponse = "";
	String codClient = "";
	String numeClient = "";
	String depart = "";
	String codClientVar = "";
	String numeClientVar = "";

	private EditText txtNumeClient;
	private TextView codClientText;
	private TextView numeClientText;
	private TextView adresaText;
	private TextView limCreditText;
	private TextView restCreditText;
	private TextView labelCodClient, labelNumeClient, labelAdrClient, labelLimitaCredit, labelRestCredit;

	ToggleButton tglTipClient;

	private static ArrayList<HashMap<String, String>> listClienti = null;
	public SimpleAdapter adapterClienti;
	ListView listClntRed;
	ActionBar actionBar;
	public static boolean selTipClient;

	SearchClientListener searchClientListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.clientredheader);

		ReduceriUlterioare.clienti = "";

		this.clientBtn = (Button) findViewById(R.id.clientBtn);
		addListenerClient();

		this.saveClntBtn = (Button) findViewById(R.id.saveClntBtn);
		addListenerSave();

		actionBar = getActionBar();
		actionBar.setTitle("Selectie client");
		actionBar.setDisplayHomeAsUpEnabled(true);

		txtNumeClient = (EditText) findViewById(R.id.txtNumeClient);
		codClientText = (TextView) findViewById(R.id.textCodClient);
		numeClientText = (TextView) findViewById(R.id.textNumeClient);
		adresaText = (TextView) findViewById(R.id.textAdrClient);
		limCreditText = (TextView) findViewById(R.id.textLimitaCredit);
		restCreditText = (TextView) findViewById(R.id.textRestCredit);

		txtNumeClient.setHint("Introduceti nume client");

		listClntRed = (ListView) findViewById(R.id.listClntRed);
		listClienti = new ArrayList<HashMap<String, String>>();
		adapterClienti = new SimpleAdapter(this, listClienti, R.layout.customrownumeclient, new String[] {
				"numeClient", "codClient" }, new int[] { R.id.textNumeClient, R.id.textCodClient });

		listClntRed.setAdapter(adapterClienti);
		addListenerClntRed();

		labelCodClient = (TextView) findViewById(R.id.labelCodClient);
		labelNumeClient = (TextView) findViewById(R.id.labelNumeClient);
		labelAdrClient = (TextView) findViewById(R.id.labelAdrClient);
		labelLimitaCredit = (TextView) findViewById(R.id.labelLimitaCredit);
		labelRestCredit = (TextView) findViewById(R.id.labelRestCredit);

		labelCodClient.setVisibility(View.GONE);
		labelNumeClient.setVisibility(View.GONE);
		labelAdrClient.setVisibility(View.GONE);
		labelLimitaCredit.setVisibility(View.GONE);
		labelRestCredit.setVisibility(View.GONE);
		saveClntBtn.setVisibility(View.GONE);

		this.tglTipClient = (ToggleButton) findViewById(R.id.tglTipClient);
		addListenerTipClient();

		selTipClient = false;

		searchClientListener = new SearchClientListener();
		searchClientListener.addObserver(this);

	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case android.R.id.home:
			finish();
			return true;
		}
		return false;
	}

	public void addListenerTipClient() {
		tglTipClient.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (tglTipClient.isChecked()) {

					actionBar.setTitle("SELECTIE GRUP CLIENTI");
					txtNumeClient.setVisibility(View.INVISIBLE);
					clientBtn.setVisibility(View.INVISIBLE);

					HashMap<String, String> temp;

					listClienti.clear();

					temp = new HashMap<String, String>();
					temp.put("numeClient", "Client final");
					temp.put("codClient", "01");
					listClienti.add(temp);

					temp = new HashMap<String, String>();
					temp.put("numeClient", "Constructor general");
					temp.put("codClient", "02");
					listClienti.add(temp);

					temp = new HashMap<String, String>();
					temp.put("numeClient", "Constructor special");
					temp.put("codClient", "03");
					listClienti.add(temp);

					temp = new HashMap<String, String>();
					temp.put("numeClient", "Revanzator");
					temp.put("codClient", "04");
					listClienti.add(temp);

					temp = new HashMap<String, String>();
					temp.put("numeClient", "Producator mobila");
					temp.put("codClient", "05");
					listClienti.add(temp);

					temp = new HashMap<String, String>();
					temp.put("numeClient", "Debitor materiale lemnoase");
					temp.put("codClient", "06");
					listClienti.add(temp);

					temp = new HashMap<String, String>();
					temp.put("numeClient", "Toti clientii");
					temp.put("codClient", "00");
					listClienti.add(temp);

					listClntRed.setAdapter(adapterClienti);

					selTipClient = true;

				} else {

					actionBar.setTitle("SELECTIE CLIENT");
					txtNumeClient.setVisibility(View.VISIBLE);
					clientBtn.setVisibility(View.VISIBLE);
					listClienti.clear();
					listClntRed.setAdapter(adapterClienti);
					selTipClient = false;

				}
			}
		});

	}

	public void addListenerClntRed() {

		listClntRed.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

				Object obj = adapterClienti.getItem(position);
				String[] token = obj.toString().split(",");

				numeClient = token[1].substring(token[1].indexOf('=') + 1, token[1].length() - 1);
				codClient = token[0].substring(token[0].indexOf('=') + 1, token[0].length());

				if (selTipClient) {
					labelCodClient.setVisibility(View.VISIBLE);
					labelNumeClient.setVisibility(View.VISIBLE);
					labelAdrClient.setVisibility(View.INVISIBLE);
					labelLimitaCredit.setVisibility(View.INVISIBLE);
					labelRestCredit.setVisibility(View.INVISIBLE);
					saveClntBtn.setVisibility(View.VISIBLE);

					numeClientText.setText(numeClient);
					codClientText.setText(codClient);
					codClientVar = codClient;
					numeClientVar = numeClient;
				} else {
					performClientDetails();
				}

			}
		});
	}

	private void performClientDetails() {

		try {
			getClientDetails details = new getClientDetails(this);
			details.execute("dummy");
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

					if (ReduceriUlterioare.clienti.indexOf(codClientVar) == -1) // clientul
					// nu
					// e
					// adaugat
					// deja
					{
						if (codClientVar.equals("00"))// toti clientii
						{
							ReduceriUlterioare.clienti = "";
						}
						if (!codClientVar.equals("00") && ReduceriUlterioare.clienti.indexOf("00") != -1) {
							ReduceriUlterioare.clienti = "";
						}
						ReduceriUlterioare.clienti += codClientVar + "#" + numeClientVar + "@@";
					}

					txtNumeClient.setText("");
					codClientText.setText("");
					numeClientText.setText("");
					adresaText.setText("");
					limCreditText.setText("");
					restCreditText.setText("");

					labelCodClient.setVisibility(View.GONE);
					labelNumeClient.setVisibility(View.GONE);
					labelAdrClient.setVisibility(View.GONE);
					labelLimitaCredit.setVisibility(View.GONE);
					labelRestCredit.setVisibility(View.GONE);
					saveClntBtn.setVisibility(View.GONE);

					// finish();
				}

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

			String numeClient = txtNumeClient.getText().toString().trim().replace('*', '%');

			searchClientListener.performSearchResults(this, numeClient, UserInfo.getInstance().getCodDepart(), UserInfo
					.getInstance().getCodDepart(), UserInfo.getInstance().getUnitLog());

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	private class getClientDetails extends AsyncTask<String, Void, String> {
		String errMessage = "";
		Context mContext;
		private ProgressDialog dialog;

		private getClientDetails(Context context) {
			super();
			this.mContext = context;
		}

		protected void onPreExecute() {
			this.dialog = new ProgressDialog(mContext);
			this.dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			this.dialog.setMessage("Asteptati...");
			this.dialog.setCancelable(false);
			this.dialog.show();
		}

		@Override
		protected String doInBackground(String... url) {
			String response = "";
			try {
				SoapObject request = new SoapObject(ConnectionStrings.getInstance().getNamespace(),
						"getClientDetAndroid");
				request.addProperty("codClient", codClient);
				request.addProperty("depart", UserInfo.getInstance().getCodDepart());

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);

				HttpTransportSE androidHttpTransport = new HttpTransportSE(ConnectionStrings.getInstance().getUrl(),
						60000);

				List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
				headerList.add(new HeaderProperty("Authorization", "Basic "
						+ org.kobjects.base64.Base64.encode("bflorin:bflorin".getBytes())));

				androidHttpTransport.call(ConnectionStrings.getInstance().getNamespace() + "getClientDetAndroid",
						envelope, headerList);
				Object result = envelope.getResponse();
				response = result.toString();
			} catch (Exception e) {
				errMessage = e.getMessage();
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO

			try {
				if (dialog != null) {
					this.dialog.dismiss();
					this.dialog = null;
				}

				if (!errMessage.equals("")) {
					Toast toast = Toast.makeText(getApplicationContext(), errMessage, Toast.LENGTH_SHORT);
					toast.show();
				} else {
					listClientDetails(result);
				}
			} catch (Exception e) {
				Log.e("Error", e.toString());
			}
		}

	}

	private void listClientDetails(String detailResponse) {
		if (detailResponse.equals("-1")) {
			Toast.makeText(getApplicationContext(), "Client nedefinit pe acest departament!", Toast.LENGTH_SHORT)
					.show();
			numeClientText.setText("");
			codClientText.setText("");
			adresaText.setText("");
			limCreditText.setText("");
			restCreditText.setText("");
			codClientVar = "";
			numeClientVar = "";
			labelCodClient.setVisibility(View.GONE);
			labelNumeClient.setVisibility(View.GONE);
			labelAdrClient.setVisibility(View.GONE);
			labelLimitaCredit.setVisibility(View.GONE);
			labelRestCredit.setVisibility(View.GONE);
			saveClntBtn.setVisibility(View.GONE);
		} else {

			labelCodClient.setVisibility(View.VISIBLE);
			labelNumeClient.setVisibility(View.VISIBLE);
			labelAdrClient.setVisibility(View.VISIBLE);
			labelLimitaCredit.setVisibility(View.VISIBLE);
			labelRestCredit.setVisibility(View.VISIBLE);
			saveClntBtn.setVisibility(View.VISIBLE);

			String[] token2 = detailResponse.split("#");
			numeClientText.setText(numeClient);
			codClientText.setText(codClient);
			adresaText.setText(token2[0].replace("!", " "));
			limCreditText.setText(token2[1]);
			restCreditText.setText(token2[2]);
			codClientVar = codClient;
			numeClientVar = numeClient;
		}

	}

	@Override
	public void onBackPressed() {
		finish();
		return;
	}

	public void update(Observable observable, Object data) {
		if (searchClientListener != null) {
			if (searchClientListener.getSearchResult().length() > 0) {
				listClienti.clear();
				adapterClienti.notifyDataSetChanged();

				populateListViewClient(searchClientListener.getSearchResult());
				listClntRed.setAdapter(adapterClienti);

				InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				mgr.hideSoftInputFromWindow(txtNumeClient.getWindowToken(), 0);
				txtNumeClient.setText("");
			}
		}

	}

}