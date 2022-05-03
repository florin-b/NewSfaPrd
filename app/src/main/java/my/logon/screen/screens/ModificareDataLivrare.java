/**
 * @author florinb
 */
package my.logon.screen.screens;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import my.logon.screen.R;
import my.logon.screen.adapters.ArticolModifDataLivrAdapter;
import my.logon.screen.adapters.ComandaModificareAdapter;
import my.logon.screen.beans.BeanArticoleAfisare;
import my.logon.screen.beans.BeanComandaCreata;
import my.logon.screen.beans.DateLivrareAfisare;
import my.logon.screen.beans.StatusIntervalLivrare;
import my.logon.screen.dialogs.SelectDateDialog;
import my.logon.screen.enums.EnumComenziDAO;
import my.logon.screen.listeners.ComenziDAOListener;
import my.logon.screen.model.ArticolComanda;
import my.logon.screen.model.ComenziDAO;
import my.logon.screen.model.UserInfo;
import my.logon.screen.utils.UtilsDates;

public class ModificareDataLivrare extends Activity implements ComenziDAOListener {

	Spinner spinnerCmdClp;

	private static String selectedCmd = "";

	private TextView textDataLivrare;

	private LinearLayout layoutDataLivrare;

	private ComenziDAO operatiiComenzi;
	private Button btnDataLivrare;
	private Button btnSalveaza;

	private ListView listViewArticole;
	private Spinner spinnerComenzi;
	public static String codClient = "";
	public static String codAdresa = "";
	public static String idComanda = "";
	public static DateLivrareAfisare dateLivrare;

	private List<BeanComandaCreata> listComenzi;
	private BeanComandaCreata comandaSelectata;
	private LinearLayout livrPartLayout, livrPartLayout1;
	private String[] livrPartOpt = {"Selectati una din optiunile:", "Livrare partiala",
			"Livrare finala"};
	private Spinner spinnerLivrPart;
	private StatusIntervalLivrare statusInterval;
	private boolean isLivrPartACZC;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.modificare_data_livrare);

		operatiiComenzi = ComenziDAO.getInstance(this);
		operatiiComenzi.setComenziDAOListener(this);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Modificare data livrare");
		actionBar.setDisplayHomeAsUpEnabled(true);

		spinnerComenzi = (Spinner) findViewById(R.id.spinnerComenzi);


		livrPartLayout = (LinearLayout) findViewById(R.id.livrPartLayout);
		livrPartLayout1 = (LinearLayout) findViewById(R.id.livrPartLayout1);
		spinnerLivrPart = (Spinner) findViewById(R.id.spinnerLivrPart);
		ArrayAdapter livrPartAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, livrPartOpt);
		spinnerLivrPart.setAdapter(livrPartAdapter);
		setSpinnerLivrPartListener();

		spinnerComenzi.setVisibility(View.INVISIBLE);
		addSpinnerListener();

		listViewArticole = (ListView) findViewById(R.id.listArtComenzi);

		listViewArticole.setVisibility(View.INVISIBLE);

		textDataLivrare = (TextView) findViewById(R.id.textDataLivrare);

		layoutDataLivrare = (LinearLayout) findViewById(R.id.layoutDataLivrare);
		layoutDataLivrare.setVisibility(View.INVISIBLE);

		btnDataLivrare = (Button) findViewById(R.id.btnDataLivrare);
		setListenerDataLivrare();

		btnSalveaza = (Button) findViewById(R.id.btnSalveaza);
		setListenerBtnSalveaza();

		getListComenzi();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

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

	private void getListComenzi() {
		HashMap<String, String> params = new HashMap<String, String>();

		params.put("filiala", UserInfo.getInstance().getUnitLog());
		params.put("codUser", UserInfo.getInstance().getCod());
		params.put("tipCmd", "1");
		params.put("depart", UserInfo.getInstance().getCodDepart());
		params.put("tipUser", UserInfo.getInstance().getTipUser());
		params.put("tipUserSap", UserInfo.getInstance().getTipUserSap());

		operatiiComenzi.getListComenzi(params);

	}

	void noComenziLayout() {

		spinnerComenzi.setVisibility(View.INVISIBLE);
		selectedCmd = "-1";

		spinnerComenzi.setAdapter(null);
		listViewArticole.setAdapter(null);

		((LinearLayout) findViewById(R.id.layoutCustodii)).setVisibility(View.INVISIBLE);

	}

	private void setListenerDataLivrare() {
		btnDataLivrare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Locale.setDefault(new Locale("ro"));

				int year = Calendar.getInstance().get(Calendar.YEAR);
				int month = Calendar.getInstance().get(Calendar.MONTH);
				int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
				SelectDateDialog datePickerDialog = new SelectDateDialog(ModificareDataLivrare.this, datePickerListener, year, month, day);
				datePickerDialog.setTitle("Data livrare");

				datePickerDialog.show();

			}
		});
	}

	private void setListenerBtnSalveaza() {

		btnSalveaza.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (isLivrPartACZC && spinnerLivrPart.getSelectedItemPosition() == 0)
					Toast.makeText(getApplicationContext(), "Selectati tipul de livrare.", Toast.LENGTH_LONG).show();
				else
					setCmdDataLivrare();

			}
		});

	}

	private void setCmdDataLivrare() {

		String[] dataLivrare = textDataLivrare.getText().toString().split("\\-");

		String strLivrFinala = " ";

		if (isLivrPartACZC) {
			if (spinnerLivrPart.getSelectedItem().toString().contains("finala"))
				strLivrFinala = "X";
		}


		HashMap<String, String> params = new HashMap<String, String>();
		params.put("idComanda", selectedCmd);
		params.put("dataLivrare", dataLivrare[2] + "-" + dataLivrare[1] + "-" + dataLivrare[0]);
		params.put("livrareFinala", strLivrFinala);

		operatiiComenzi.setCmdVanzDataLivrare(params);


	}

	private void operatieLivrareStatus(String result) {
		Toast.makeText(getApplicationContext(), result.split("#")[1], Toast.LENGTH_LONG).show();

	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

			if (view.isShown()) {

				SimpleDateFormat displayFormat = new SimpleDateFormat("dd-MM-yyyy");
				Calendar calendar = new GregorianCalendar(selectedYear, selectedMonth, selectedDay);

				statusInterval = UtilsDates.getStatusIntervalLivrare(calendar.getTime());

				if (statusInterval.isValid()) {
					textDataLivrare.setText(displayFormat.format(calendar.getTime()));
					btnSalveaza.setVisibility(View.VISIBLE);
				} else
					Toast.makeText(getApplicationContext(), statusInterval.getMessage(), Toast.LENGTH_LONG).show();
			}

		}
	};

	private void setSpinnerLivrPartListener() {
		spinnerLivrPart.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

				if (statusInterval != null && statusInterval.isValid() && position > 0)
					btnSalveaza.setVisibility(View.VISIBLE);
				else
					btnSalveaza.setVisibility(View.INVISIBLE);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}


	private boolean isCmdACZCToSave() {
		if (!comandaSelectata.isComandaACZC())
			return true;
		else if (isLivrPartACZC && spinnerLivrPart.getSelectedItemPosition() == 0)
			return false;

		return true;

	}

	private void verificaLivrareACZC(List<ArticolComanda> listArticole) {

		isLivrPartACZC = false;

		for (ArticolComanda articol : listArticole) {

			if (articol.getCantitate() != articol.getAczcDeLivrat()) {
				isLivrPartACZC = true;
				break;
			}

		}


	}

	private void clearAllData() {

		selectedCmd = "";

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
		spinnerComenzi.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				comandaSelectata = listComenzi.get(position);
				getArticoleComanda(listComenzi.get(position).getId());
				selectedCmd = listComenzi.get(position).getCmdSap();
				statusInterval = null;
				btnSalveaza.setVisibility(View.INVISIBLE);
				isLivrPartACZC = false;

			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}


	private void setLivrPartVisibility(boolean isVisible) {

		if (isVisible) {
			livrPartLayout.setVisibility(View.VISIBLE);
			livrPartLayout1.setVisibility(View.VISIBLE);
			spinnerLivrPart.setSelection(0);
		} else {
			livrPartLayout.setVisibility(View.GONE);
			livrPartLayout1.setVisibility(View.GONE);
		}
	}

	private void getArticoleComanda(String idComanda) {

		HashMap<String, String> params = new HashMap<String, String>();

		params.put("nrCmd", idComanda);
		params.put("afisCond", "1");
		params.put("tipUser", UserInfo.getInstance().getTipUser());

		operatiiComenzi.getArticoleComandaJSON(params);

	}

	private void afiseazaListaComenzi(List<BeanComandaCreata> listComenzi) {

		if (!listComenzi.isEmpty()) {
			ComandaModificareAdapter adapter = new ComandaModificareAdapter(listComenzi, this);
			spinnerComenzi.setAdapter(adapter);
			spinnerComenzi.setVisibility(View.VISIBLE);
			layoutDataLivrare.setVisibility(View.VISIBLE);

			selectedCmd = adapter.getItem(0).getCmdSap();

		} else {
			spinnerComenzi.setVisibility(View.INVISIBLE);
			layoutDataLivrare.setVisibility(View.INVISIBLE);
			Toast.makeText(getApplicationContext(), "Nu exista comenzi.", Toast.LENGTH_SHORT).show();
		}

	}

	private void afiseazaArticoleComanda(BeanArticoleAfisare articoleComanda) {

		ArticolModifDataLivrAdapter adapterArticole = new ArticolModifDataLivrAdapter(this, articoleComanda.getListArticole(), articoleComanda.getConditii()
				.getArticole(), comandaSelectata);

		textDataLivrare.setText(articoleComanda.getDateLivrare().getDataLivrare().replace(".", "-"));

		listViewArticole.setAdapter(adapterArticole);
		listViewArticole.setVisibility(View.VISIBLE);

		if (comandaSelectata.isComandaACZC())
			verificaLivrareACZC(articoleComanda.getListArticole());

		setLivrPartVisibility(isLivrPartACZC);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void operationComenziComplete(EnumComenziDAO methodName, Object result) {
		switch (methodName) {

			case SET_CMD_VANZ_DATA_LIVRARE:
				operatieLivrareStatus((String) result);
				break;
			case GET_LIST_COMENZI:
				this.listComenzi = (List<BeanComandaCreata>) result;
				afiseazaListaComenzi(this.listComenzi);
				break;
			case GET_ARTICOLE_COMANDA_JSON:
				afiseazaArticoleComanda(operatiiComenzi.deserializeArticoleComanda((String) result));
				break;

			default:
				break;
		}

	}
}
