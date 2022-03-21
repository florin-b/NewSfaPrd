/**
 * @author florinb
 *
 */
package my.logon.screen.screens;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.listeners.OperatiiAgentListener;
import my.logon.screen.model.HandleJSONData;
import my.logon.screen.model.OperatiiAgent;
import my.logon.screen.model.UserInfo;
import my.logon.screen.R;
import my.logon.screen.utils.UtilsUser;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import my.logon.screen.beans.BeanInfoVenituri;

public class InfoVenituri extends Activity implements AsyncTaskListener, OperatiiAgentListener {

	private Spinner spinnerLuna;
	private Spinner spinnerAn, spinnerFiliale, spinnerAgenti;
	private Button btnAfisResult;
	private String[] strArrayLuna = { "Ianuarie", "Februarie", "Martie", "Aprilie", "Mai", "Iunie", "Iulie", "August", "Septembrie", "Octombrie", "Noiembrie",
			"Decembrie" };

	private String[] strArrayAn = { "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020" };

	private String[] strArrayNumeFiliale = { "Andronache", "Bacau", "Baia-Mare", "Brasov", "Constanta", "Cluj", "Craiova", "Focsani", "Galati", "Glina",
			"Iasi", "Militari", "Oradea", "Otopeni", "Piatra-Neamt", "Pitesti", "Ploiesti", "Timisoara", "Tg. Mures" };

	private String[] strArrayCodFiliale = { "BU13", "BC10", "MM10", "BV10", "CT10", "CJ10", "DJ10", "VN10", "GL10", "BU10", "IS10", "BU11", "BH10", "BU12",
			"NT10", "AG10", "PH10", "TM10", "MS10" };

	private LinearLayout layoutSelFiliala, layoutHeaderVenituri;
	private SimpleAdapter adapterFiliale, adapterVenituri;
	private String selectedFiliala = "";

	private ListView listResultsVenituri;
	private ArrayList<HashMap<String, String>> arrayListVenituri = new ArrayList<HashMap<String, String>>();
	private TextView textVenit1, textVenit2, textVenitTotal, textMarja1, textMarja2, textMarjaTotal;

	private OperatiiAgent agent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTheme(R.style.LRTheme);
		setContentView(R.layout.info_venituri_layout);

		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Info venituri");
		actionBar.setDisplayHomeAsUpEnabled(true);

		spinnerLuna = (Spinner) findViewById(R.id.spinnerLuna);
		spinnerAn = (Spinner) findViewById(R.id.spinnerAn);

		ArrayAdapter<String> adapterSpinnerLuna = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, strArrayLuna);
		adapterSpinnerLuna.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerLuna.setAdapter(adapterSpinnerLuna);

		ArrayAdapter<String> adapterSpinnerAn = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, strArrayAn);
		adapterSpinnerAn.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerAn.setAdapter(adapterSpinnerAn);

		// setare val. implicit an curent
		Calendar c = Calendar.getInstance();
		String year = String.valueOf(c.get(Calendar.YEAR));

		for (int ii = 0; ii < strArrayAn.length; ii++) {
			if (year.equals(strArrayAn[ii])) {
				spinnerAn.setSelection(ii);
				break;
			}
		}
		// sf. val. implicit an

		// setare val. implicit luna curenta
		int luna = c.get(Calendar.MONTH);
		spinnerLuna.setSelection(luna);
		// sf. val. implicit luna

		btnAfisResult = (Button) findViewById(R.id.afisResult);
		listenerBtnAfisResult();

		spinnerFiliale = (Spinner) findViewById(R.id.spinnerFiliale);
		setSpinnerFilialaListener();
		spinnerAgenti = (Spinner) findViewById(R.id.spinnerAgenti);

		layoutSelFiliala = (LinearLayout) findViewById(R.id.layoutSelFiliala);

		if (UtilsUser.isDV()) {
			layoutSelFiliala.setVisibility(View.VISIBLE);
			populateListFiliale();

			agent = OperatiiAgent.getInstance();
			agent.setOperatiiAgentListener(this);

		} else {
			layoutSelFiliala.setVisibility(View.GONE);
		}

		layoutHeaderVenituri = (LinearLayout) findViewById(R.id.layoutHeaderVenituri);
		layoutHeaderVenituri.setVisibility(View.INVISIBLE);

		listResultsVenituri = (ListView) findViewById(R.id.listResultsVenituri);
		adapterVenituri = new SimpleAdapter(this, arrayListVenituri, R.layout.customrow_info_venituri, new String[] { "tipInterval", "textVenitNetP", "MP",
				"textVenitNetP1", "MP1", "textVenitNetPT", "MPT" }, new int[] { R.id.textTipInterval, R.id.textVenitNetP, R.id.textMP, R.id.textVenitNetP1,
				R.id.textMP1, R.id.textVenitNetTotal, R.id.textMTotal });

		listResultsVenituri.setAdapter(adapterVenituri);

		listResultsVenituri.setVisibility(View.INVISIBLE);

		addTextViewLabels();

	}

	private void addTextViewLabels() {
		textVenit1 = (TextView) findViewById(R.id.textVenit1);
		textVenit2 = (TextView) findViewById(R.id.textVenit2);
		textVenitTotal = (TextView) findViewById(R.id.textVenitTotal);
		textMarja1 = (TextView) findViewById(R.id.textMarja1);
		textMarja2 = (TextView) findViewById(R.id.textMarja2);
		textMarjaTotal = (TextView) findViewById(R.id.textMarjaTotal);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case android.R.id.home: // iesire
			returnToHome();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void returnToHome() {

		selectedFiliala = "";
		UserInfo.getInstance().setParentScreen("");
		Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
		startActivity(nextScreen);
		finish();

	}

	private void populateListFiliale() {

		ArrayList<HashMap<String, String>> listFiliale = new ArrayList<HashMap<String, String>>();
		adapterFiliale = new SimpleAdapter(this, listFiliale, R.layout.rowlayoutagenti, new String[] { "numeFiliala", "codFiliala" }, new int[] {
				R.id.textNumeAgent, R.id.textCodAgent });

		HashMap<String, String> temp;
		temp = new HashMap<String, String>(1, 0.75f);
		temp.put("numeFiliala", "Filiala");
		temp.put("codFiliala", " ");
		listFiliale.add(temp);

		for (int i = 0; i < strArrayNumeFiliale.length; i++) {

			if (UserInfo.getInstance().getFilialeDV().contains(strArrayCodFiliale[i])) {

				temp = new HashMap<String, String>(50, 0.75f);
				temp.put("numeFiliala", strArrayNumeFiliale[i]);
				temp.put("codFiliala", strArrayCodFiliale[i]);

				listFiliale.add(temp);
			}
		}

		spinnerFiliale.setAdapter(adapterFiliale);

	}

	private void setSpinnerFilialaListener() {
		spinnerFiliale.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				@SuppressWarnings("unchecked")
				HashMap<String, String> artMap = (HashMap<String, String>) spinnerFiliale.getSelectedItem();
				selectedFiliala = artMap.get("codFiliala");

				if (!selectedFiliala.trim().isEmpty()){
					agent.getListaAgenti(selectedFiliala, UserInfo.getInstance().getCodDepart(), InfoVenituri.this, false, null);
					listResultsVenituri.setVisibility(View.INVISIBLE);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	public void listenerBtnAfisResult() {
		btnAfisResult.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (layoutSelFiliala.getVisibility() == View.VISIBLE) {
					if (spinnerFiliale.getSelectedItemPosition() == 0) {
						Toast.makeText(getApplicationContext(), "Selectati filiala!", Toast.LENGTH_SHORT).show();
						return;
					}

				}

				getInfoVenituriData();

			}
		});

	}

	private void getInfoVenituriData() {
		try {

			HashMap<String, String> params = new HashMap<String, String>();

			String localStrFiliala;
			String localCodAgent;

			if (UtilsUser.isDV()) {
				localStrFiliala = selectedFiliala;

				@SuppressWarnings("unchecked")
				HashMap<String, String> artMap = (HashMap<String, String>) spinnerAgenti.getSelectedItem();
				localCodAgent = artMap.get("codAgent");

			} else {
				localStrFiliala = UserInfo.getInstance().getUnitLog();
				localCodAgent = UserInfo.getInstance().getCod();
			}

			params.put("codDepart", UserInfo.getInstance().getCodDepart());
			params.put("filiala", localStrFiliala);
			params.put("luna", String.format("%02d", spinnerLuna.getSelectedItemPosition() + 1));
			params.put("an", String.valueOf(spinnerAn.getSelectedItem()));
			params.put("codAgent", localCodAgent);

			AsyncTaskWSCall call = new AsyncTaskWSCall(this, "getInfoVenituriData", params);
			call.getCallResults();

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	private void populateResultList(String result) {

		HandleJSONData objVenituriList = new HandleJSONData(this, result);
		ArrayList<BeanInfoVenituri> infoVenituriArray = objVenituriList.decodeJSONInfoVenituri();

		if (infoVenituriArray.size() > 0) {

			NumberFormat nf2 = NumberFormat.getInstance();
			nf2.setMinimumFractionDigits(3);
			nf2.setMaximumFractionDigits(3);

			if (UserInfo.getInstance().getCodDepart().startsWith("04")) {
				textVenit1.setText("Venit Net 040");
				textMarja1.setText("Marja Neta 040");

				textVenit2.setText("Venit Net 041");
				textMarja2.setText("Marja Neta 041");

				textVenitTotal.setText("Venit Net 04");
				textMarjaTotal.setText("Marja Neta 04");
			} else {
				textVenit1.setText("Venit Net");
				textMarja1.setText("Marja Neta");

				textVenit2.setText("");
				textMarja2.setText("");

				textVenitTotal.setText("");
				textMarjaTotal.setText("");

			}

			listResultsVenituri.setVisibility(View.VISIBLE);
			layoutHeaderVenituri.setVisibility(View.VISIBLE);
			arrayListVenituri.clear();

			HashMap<String, String> temp;

			for (int i = 0; i < infoVenituriArray.size(); i++) {
				temp = new HashMap<String, String>(5, 0.75f);

				if (UserInfo.getInstance().getCodDepart().startsWith("04")) {
					temp.put("tipInterval", getIntervalDesc(Integer.valueOf(infoVenituriArray.get(i).getId())));
					temp.put("textVenitNetP", nf2.format(Double.parseDouble(infoVenituriArray.get(i).getVenitNetP040())));
					temp.put("MP", nf2.format(Double.parseDouble(infoVenituriArray.get(i).getmP040())));
					temp.put("textVenitNetP1", nf2.format(Double.parseDouble(infoVenituriArray.get(i).getVenitNetP041())));
					temp.put("MP1", nf2.format(Double.parseDouble(infoVenituriArray.get(i).getmP041())));
					temp.put(
							"textVenitNetPT",
							nf2.format(Double.parseDouble(infoVenituriArray.get(i).getVenitNetP040())
									+ Double.parseDouble(infoVenituriArray.get(i).getVenitNetP041())));
					temp.put("MPT",
							nf2.format(Double.parseDouble(infoVenituriArray.get(i).getmP040()) + Double.parseDouble(infoVenituriArray.get(i).getmP041())));

				} else {
					temp.put("tipInterval", getIntervalDesc(Integer.valueOf(infoVenituriArray.get(i).getId())));
					temp.put("textVenitNetP", nf2.format(Double.parseDouble(infoVenituriArray.get(i).getVenitNetP())));
					temp.put("MP", nf2.format(Double.parseDouble(infoVenituriArray.get(i).getmP())));
				}

				arrayListVenituri.add(temp);

			}

			listResultsVenituri.setAdapter(adapterVenituri);

		} else {
			arrayListVenituri.clear();
			listResultsVenituri.setAdapter(adapterVenituri);
			layoutHeaderVenituri.setVisibility(View.INVISIBLE);
			Toast toast = Toast.makeText(getApplicationContext(), "Nu exista informatii.", Toast.LENGTH_SHORT);
			toast.show();

		}

	}

	private void populateAgentiList(ArrayList<HashMap<String, String>> listAgenti) {

		SimpleAdapter adapterAgenti = new SimpleAdapter(this, listAgenti, R.layout.rowlayoutagenti, new String[] { "numeAgent", "codAgent" }, new int[] {
				R.id.textNumeAgent, R.id.textCodAgent });
		spinnerAgenti.setAdapter(adapterAgenti);

	}

	private String getIntervalDesc(int intervalId) {
		String retVal = "";

		switch (intervalId) {
		case 1:
			retVal = "1-7 an, luna crt / an, luna ant";
			break;

		case 2:
			retVal = "1-14 an, luna crt / an, luna ant";
			break;

		case 3:
			retVal = "1-21 an, luna crt / an, luna ant";
			break;

		case 4:
			retVal = "1-28 an, luna crt / an, luna ant";
			break;

		case 5:
			retVal = "Total / an, luna ant";
			break;

		}

		return retVal;

	}

	@Override
	public void onBackPressed() {
		returnToHome();
		return;
	}

	public void onTaskComplete(String methodName, Object result) {
		if (methodName.equals("getInfoVenituriData")) {
			populateResultList((String) result);
		}

	}

	@Override
	public void opAgentComplete(ArrayList<HashMap<String, String>> listAgenti) {
		populateAgentiList(listAgenti);

	}

}
