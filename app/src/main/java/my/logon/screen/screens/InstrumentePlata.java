package my.logon.screen.screens;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import my.logon.screen.listeners.FacturiNeincasateListener;
import my.logon.screen.listeners.OperatiiClientListener;
import my.logon.screen.model.OperatiiClient;
import my.logon.screen.model.OperatiiDocumente;
import my.logon.screen.model.UserInfo;
import my.logon.screen.R;
import my.logon.screen.utils.UtilsGeneral;
import my.logon.screen.adapters.CautareClientiAdapter;
import my.logon.screen.adapters.FacturaNeincasataAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import my.logon.screen.beans.BeanClient;
import my.logon.screen.beans.FacturaNeincasataLite;
import my.logon.screen.beans.IncasareDocument;
import my.logon.screen.beans.PlataNeincasata;
import my.logon.screen.dialogs.SelectDateDialog;
import my.logon.screen.enums.EnumClienti;
import my.logon.screen.enums.EnumNeincasate;

public class InstrumentePlata extends Activity implements OperatiiClientListener, FacturiNeincasateListener {

	private Button btnCautaClient, btnSchimbaClient, btnFacturi, btnDataEmitere, btnDataScadenta, btnSalveazaPlata;
	private EditText textClient, txtSuma, txtSerie;
	private OperatiiClient operatiiClient;
	private ListView listViewClienti, listViewNeincasate;
	private LinearLayout layoutClient, layoutSelectieClient;
	private TextView labelNumeClient, txtScadent, txtEmitere, txtGirant;
	private BeanClient clientSelectat;
	private OperatiiDocumente operatiiDocumente;
	private RadioButton radioBO, radioCEC;
	private List<FacturaNeincasataLite> listFacturi;
	private FacturaNeincasataAdapter adapter;
	private NumberFormat numberFormat = new DecimalFormat("#0.00");

	private enum EnumTipData {
		DATA_EMITERE("Data emitere"), DATA_SCADENTA("Data scadenta");

		private String tipData;

		EnumTipData(String tipData) {
			this.tipData = tipData;
		}

		public String getTipData() {
			return tipData;
		}

	}

	private EnumTipData tipData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTheme(R.style.LRTheme);

		setContentView(R.layout.instrumente_plata);

		operatiiDocumente = new OperatiiDocumente(this);
		operatiiDocumente.setFacturiNeincasateListener(this);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Plati CEC/BO");
		actionBar.setDisplayHomeAsUpEnabled(true);

		operatiiClient = new OperatiiClient(this);
		operatiiClient.setOperatiiClientListener(this);

		textClient = (EditText) findViewById(R.id.textNumeClient);
		textClient.setHint("Introduceti nume client");
		listViewClienti = (ListView) findViewById(R.id.listClienti);
		setListenerListaClienti();

		txtSuma = (EditText) findViewById(R.id.txtSuma);
		setListenerTxtSuma();

		txtScadent = (TextView) findViewById(R.id.txtScadent);
		txtEmitere = (TextView) findViewById(R.id.txtEmitere);
		txtSerie = (EditText) findViewById(R.id.txtSerie);
		txtGirant = (EditText) findViewById(R.id.txtGirant);

		listViewNeincasate = (ListView) findViewById(R.id.listNeincasate);

		btnCautaClient = (Button) findViewById(R.id.cautaClientBtn);
		setListenerCautaClient();

		btnDataEmitere = (Button) findViewById(R.id.dataEmitereBtn);
		setListenerDataEmitereBtn();

		btnDataScadenta = (Button) findViewById(R.id.dataScadentaBtn);
		setListenerDataScadentaBtn();

		btnSchimbaClient = (Button) findViewById(R.id.schimbaClientBtn);
		setListenerSchimbaClient();

		labelNumeClient = (TextView) findViewById(R.id.labelNumeClient);
		layoutClient = (LinearLayout) findViewById(R.id.layoutClient);
		layoutSelectieClient = (LinearLayout) findViewById(R.id.layoutSelectieClient);

		btnFacturi = (Button) findViewById(R.id.facturiBtn);
		setListenerFacturiBtn();

		radioBO = (RadioButton) findViewById(R.id.radioBO);
		radioCEC = (RadioButton) findViewById(R.id.radioCEC);

		btnSalveazaPlata = (Button) findViewById(R.id.salveazaPlataBtn);
		setListenerSalveazaPlataBtn();

		showPlataFields(false);

	}

	private void setListenerTxtSuma() {

		txtSuma.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String strSuma = txtSuma.getText().toString().trim();

				if (!strSuma.isEmpty() && adapter != null) {
					adapter.setSumaPlata(Double.parseDouble(strSuma));
					adapter.notifyDataSetChanged();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

	}

	private void setListenerCautaClient() {

		btnCautaClient.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cautaClient();
			}
		});

	}

	private void setListenerSchimbaClient() {

		btnSchimbaClient.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				schimbaClient();
				showPlataFields(false);
				InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
			}
		});

	}

	private void setListenerListaClienti() {

		listViewClienti.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				clearOldData();
				showPlataFields(true);
				clientSelectat = (BeanClient) parent.getAdapter().getItem(position);
				showClientLayout();

			}
		});

	}

	private void setListenerDataEmitereBtn() {

		btnDataEmitere.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tipData = EnumTipData.DATA_EMITERE;
				showSelectDataDialog();

			}
		});

	}

	private void setListenerDataScadentaBtn() {
		btnDataScadenta.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tipData = EnumTipData.DATA_SCADENTA;
				showSelectDataDialog();

			}
		});

	}

	private void showSelectDataDialog() {

		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH);
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		SelectDateDialog datePickerDialog = new SelectDateDialog(InstrumentePlata.this, datePickerListener, year, month, day);
		datePickerDialog.setTitle(tipData.getTipData());
		datePickerDialog.show();

	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

			if (view.isShown()) {

				SimpleDateFormat displayFormat = new SimpleDateFormat("dd.MM.yyyy");
				Calendar calendar = new GregorianCalendar(selectedYear, selectedMonth, selectedDay);

				if (tipData == EnumTipData.DATA_EMITERE)
					txtEmitere.setText(displayFormat.format(calendar.getTime()));
				if (tipData == EnumTipData.DATA_SCADENTA)
					txtScadent.setText(displayFormat.format(calendar.getTime()));

			}

		}
	};

	private void setListenerFacturiBtn() {
		btnFacturi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getFacturiNeincasate();

			}
		});

	}

	private void getFacturiNeincasate() {

		if (clientSelectat == null) {
			Toast.makeText(getApplicationContext(), "Selectati clientul", Toast.LENGTH_LONG).show();
			return;
		}

		if (txtSuma.getText().toString().isEmpty()) {
			Toast.makeText(getApplicationContext(), "Completati suma de plata", Toast.LENGTH_LONG).show();
			return;
		}

		InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(txtSuma.getWindowToken(), 0);

		HashMap<String, String> params = new HashMap<String, String>();

		params.put("codClient", clientSelectat.getCodClient());
		params.put("codDepart", UserInfo.getInstance().getCodDepart());
		params.put("codAgent", UserInfo.getInstance().getCod());
		params.put("unitLog", UserInfo.getInstance().getUnitLog());

		operatiiDocumente.getFacturiNeincasate(params);

	}

	private void showClientLayout() {

		labelNumeClient.setText(clientSelectat.getNumeClient());
		layoutClient.setVisibility(View.VISIBLE);
		layoutSelectieClient.setVisibility(View.GONE);
		listViewClienti.setVisibility(View.GONE);

	}

	private void clearOldData() {

		clientSelectat = null;
		txtSuma.setText("");
		labelNumeClient.setText("");

		adapter = new FacturaNeincasataAdapter(new ArrayList<FacturaNeincasataLite>(), getApplicationContext());
		listViewNeincasate.setAdapter(adapter);

		radioBO.setChecked(false);
		radioCEC.setChecked(false);
		txtSerie.setText("");
		txtEmitere.setText("");
		txtScadent.setText("");
		txtGirant.setText("");

	}

	private void cautaClient() {

		String numeClient = textClient.getText().toString().trim().replace('*', '%');

		if (numeClient.isEmpty())
			return;

		HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
		params.put("numeClient", numeClient);
		params.put("depart", "00");
		params.put("departAg", UserInfo.getInstance().getCodDepart());
		params.put("unitLog", UserInfo.getInstance().getUnitLog());

		operatiiClient.getListClienti(params);

		InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

	}

	private void schimbaClient() {

		textClient.setText("");
		textClient.setFocusableInTouchMode(true);
		textClient.requestFocus();

		CautareClientiAdapter adapterClienti = new CautareClientiAdapter(this, new ArrayList<BeanClient>());
		listViewClienti.setAdapter(adapterClienti);

		layoutClient.setVisibility(View.GONE);
		layoutSelectieClient.setVisibility(View.VISIBLE);
		listViewClienti.setVisibility(View.VISIBLE);
	}

	private void setListenerSalveazaPlataBtn() {
		btnSalveazaPlata.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				salveazaPlata();

			}
		});
	}

	private void salveazaPlata() {

		if (!isDataValid())
			return;

		PlataNeincasata plataNeincasata = new PlataNeincasata();

		plataNeincasata.setCodClient(clientSelectat.getCodClient());
		plataNeincasata.setSumaPlata(Double.parseDouble(txtSuma.getText().toString()));

		String tipDocument;
		if (radioBO.isChecked())
			tipDocument = "B";
		else
			tipDocument = "C";

		plataNeincasata.setTipDocument(tipDocument);
		plataNeincasata.setSeriaDocument(txtSerie.getText().toString().trim());
		plataNeincasata.setDataEmitere(txtEmitere.getText().toString().trim());
		plataNeincasata.setDataScadenta(txtScadent.getText().toString().trim());
		plataNeincasata.setCodAgent(UserInfo.getInstance().getCod());
		plataNeincasata.setGirant(txtGirant.getText().toString().trim());
		List<IncasareDocument> listDocumente = new ArrayList<IncasareDocument>();

		for (FacturaNeincasataLite factura : listFacturi) {
			IncasareDocument document = new IncasareDocument();

			if (factura.getPlatit() > 0) {
				document.setNrDocument(factura.getNrFactura());
				document.setSumaIncasata(factura.getPlatit());
				document.setAnDocument(factura.getAnDocument());
				listDocumente.add(document);
			}

		}

		plataNeincasata.setListaDocumente(listDocumente);

		callWSPlata(plataNeincasata);

	}

	private boolean isDataValid() {
		if (clientSelectat == null || clientSelectat.getCodClient() == null) {
			Toast.makeText(getApplicationContext(), "Selectati clientul.", Toast.LENGTH_LONG).show();
			return false;
		}

		if (txtSuma.getText().toString().isEmpty()) {
			Toast.makeText(getApplicationContext(), "Completati suma.", Toast.LENGTH_LONG).show();
			return false;
		}

		if (adapter == null || adapter.getCount() == 0) {
			Toast.makeText(getApplicationContext(), "Selectati documentele de plata.", Toast.LENGTH_LONG).show();
			return false;
		}

		if (!radioBO.isChecked() && !radioCEC.isChecked()) {
			Toast.makeText(getApplicationContext(), "Selectati tipul de document.", Toast.LENGTH_LONG).show();
			return false;
		}

		boolean platit = false;

		BigDecimal sumaPlataInit = BigDecimal.valueOf(Double.parseDouble(txtSuma.getText().toString().trim()));
		BigDecimal sumaAcoperitFacturi = BigDecimal.valueOf(0.0);

		for (FacturaNeincasataLite factura : listFacturi) {
			if (factura.getPlatit() > 0) {
				platit = true;
				sumaAcoperitFacturi = sumaAcoperitFacturi.add(BigDecimal.valueOf(factura.getPlatit()));

			}

		}

		if (!platit) {
			Toast.makeText(getApplicationContext(), "Trebuie sa platiti cel putin o factura.", Toast.LENGTH_LONG).show();
			return false;
		}

		if (sumaPlataInit.compareTo(sumaAcoperitFacturi) > 0) {
			BigDecimal restSuma = sumaPlataInit.subtract(sumaAcoperitFacturi);
			Toast.makeText(getApplicationContext(), "Au ramas " + numberFormat.format(restSuma) + " RON nepunctati. ", Toast.LENGTH_LONG).show();
			return false;
		}

		if (txtSerie.getText().toString().isEmpty()) {
			Toast.makeText(getApplicationContext(), "Completati seria sau numarul documentului.", Toast.LENGTH_LONG).show();
			return false;
		}

		if (txtEmitere.getText().toString().isEmpty() && radioCEC.isChecked()) {
			Toast.makeText(getApplicationContext(), "Completati data emiterii documentului.", Toast.LENGTH_LONG).show();
			return false;
		}

		if (txtScadent.getText().toString().isEmpty()) {
			Toast.makeText(getApplicationContext(), "Completati data scadenta a documentului.", Toast.LENGTH_LONG).show();
			return false;
		}

		return true;
	}

	private void showPlataFields(boolean isVisible) {
		if (isVisible) {

			((LinearLayout) findViewById(R.id.layoutClient)).setVisibility(View.VISIBLE);
			((LinearLayout) findViewById(R.id.layoutSuma)).setVisibility(View.VISIBLE);
			((LinearLayout) findViewById(R.id.layoutFacturi)).setVisibility(View.VISIBLE);
			((LinearLayout) findViewById(R.id.layoutEmitere)).setVisibility(View.VISIBLE);
			((LinearLayout) findViewById(R.id.layoutTipPlata)).setVisibility(View.VISIBLE);
			((LinearLayout) findViewById(R.id.layoutSerie)).setVisibility(View.VISIBLE);
			((LinearLayout) findViewById(R.id.layoutScadenta)).setVisibility(View.VISIBLE);
			((LinearLayout) findViewById(R.id.layoutGirant)).setVisibility(View.VISIBLE);
			((Button) findViewById(R.id.salveazaPlataBtn)).setVisibility(View.VISIBLE);

		} else {
			((LinearLayout) findViewById(R.id.layoutClient)).setVisibility(View.INVISIBLE);
			((LinearLayout) findViewById(R.id.layoutSuma)).setVisibility(View.INVISIBLE);
			((LinearLayout) findViewById(R.id.layoutFacturi)).setVisibility(View.INVISIBLE);
			((LinearLayout) findViewById(R.id.layoutTipPlata)).setVisibility(View.INVISIBLE);
			((LinearLayout) findViewById(R.id.layoutEmitere)).setVisibility(View.INVISIBLE);
			((LinearLayout) findViewById(R.id.layoutSerie)).setVisibility(View.INVISIBLE);
			((LinearLayout) findViewById(R.id.layoutScadenta)).setVisibility(View.INVISIBLE);
			((LinearLayout) findViewById(R.id.layoutGirant)).setVisibility(View.INVISIBLE);
			((Button) findViewById(R.id.salveazaPlataBtn)).setVisibility(View.INVISIBLE);
		}
	}

	private void callWSPlata(PlataNeincasata plataNeincasata) {
		HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
		params.put("plataNeincasata", operatiiDocumente.serializePlata(plataNeincasata));
		operatiiDocumente.salveazaPlataNeincasata(params);

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

	private void populateListClienti(String resultClienti) {
		List<BeanClient> listClienti = operatiiClient.deserializeListClienti(resultClienti);
		CautareClientiAdapter adapterClienti = new CautareClientiAdapter(this, listClienti);
		listViewClienti.setAdapter(adapterClienti);

		listViewClienti.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});

	}

	private void getOperationStatus(String result) {
		if (result.contains("#")) {
			String[] msg = result.split("#");

			Toast.makeText(getApplicationContext(), msg[0], Toast.LENGTH_LONG).show();

			if (msg.length > 1 && msg[1].equals("X")) {
				clearOldData();
			}
		}

	}

	private void populateListFacturi(String strFacturi) {

		listFacturi = operatiiDocumente.deserializeFacturi(strFacturi);

		if (listFacturi.isEmpty()) {
			Toast.makeText(getApplicationContext(), "Nu exista facturi", Toast.LENGTH_LONG).show();
			return;
		}

		double sumaPlata = Double.parseDouble(txtSuma.getText().toString());

		adapter = new FacturaNeincasataAdapter(listFacturi, getApplicationContext());
		adapter.setSumaPlata(sumaPlata);

		listViewNeincasate.setAdapter(adapter);
		adapter.sortByData();

		listViewNeincasate.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});

	}

	@Override
	public void operationComplete(EnumClienti methodName, Object result) {

		switch (methodName) {
		case GET_LISTA_CLIENTI:
			populateListClienti((String) result);
			break;
		default:
			break;

		}

	}

	@Override
	public void operationComplete(EnumNeincasate methodName, Object result) {

		switch (methodName) {
		case GET_FACTURI_NEINCASATE:
			populateListFacturi((String) result);
			break;
		case SALVEAZA_PLATA:
			getOperationStatus((String) result);
			break;
		default:
			break;
		}

	}

}
