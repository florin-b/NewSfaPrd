/**
 * @author florinb
 *
 */
package my.logon.screen.screens;


import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import my.logon.screen.R;
import my.logon.screen.adapters.AdapterAdreseLivrare;
import my.logon.screen.beans.Address;
import my.logon.screen.beans.BeanAdresaLivrare;
import my.logon.screen.beans.BeanAdreseJudet;
import my.logon.screen.beans.GeocodeAddress;
import my.logon.screen.beans.StatusIntervalLivrare;
import my.logon.screen.dialogs.MapAddressDialog;
import my.logon.screen.dialogs.SelectDateDialog;
import my.logon.screen.enums.EnumComenziDAO;
import my.logon.screen.enums.EnumLocalitate;
import my.logon.screen.enums.EnumOperatiiAdresa;
import my.logon.screen.helpers.HelperAdreseLivrare;
import my.logon.screen.listeners.AutocompleteDialogListener;
import my.logon.screen.listeners.ComenziDAOListener;
import my.logon.screen.listeners.MapListener;
import my.logon.screen.listeners.OperatiiAdresaListener;
import my.logon.screen.model.ComenziDAO;
import my.logon.screen.model.DateLivrare;
import my.logon.screen.model.HandleJSONData;
import my.logon.screen.model.ListaArticoleComanda;
import my.logon.screen.model.OperatiiAdresa;
import my.logon.screen.model.OperatiiAdresaImpl;
import my.logon.screen.model.UserInfo;
import my.logon.screen.utils.MapUtils;
import my.logon.screen.utils.UtilsAddress;
import my.logon.screen.utils.UtilsDates;
import my.logon.screen.utils.UtilsGeneral;

public class AdrLivrCustodie extends AppCompatActivity implements OnTouchListener, OnItemClickListener, OperatiiAdresaListener, MapListener, AutocompleteDialogListener,
		ComenziDAOListener {

	private Button saveAdrLivrBtn;
	private EditText txtPers, txtTel, txtObservatii, txtValoareIncasare;

	public SimpleAdapter adapterJudete, adapterAdreseLivrare;

	private Spinner spinnerPlata, spinnerTransp, spinnerJudet, spinnerTermenPlata, spinnerAdreseLivrare, spinnerDocInsot, spinnerTipReducere,
			spinnerResponsabil;
	private static ArrayList<HashMap<String, String>> listJudete = null, listAdreseLivrare = null;

	private LinearLayout layoutAdrese, layoutAdr1, layoutAdr2, layoutValoareIncasare;
	private String globalCodClient = "";

	private RadioButton radioLista, radioText;
	private boolean adrNouaModifCmd = false;
	private int selectedAddrModifCmd = -1;

	CheckBox checkModifValInc;
	NumberFormat nf2;
	private OperatiiAdresa operatiiAdresa;

	private AutoCompleteTextView textLocalitate, textStrada;
	private Button btnPozitieAdresa;
	private EditText textNrStr;
	private Spinner spinnerTonaj, spinnerIndoire;
	private ArrayList<BeanAdresaLivrare> adreseList;

	private LinearLayout layoutHarta;

	private static final int LIST_LOCALITATI = 1;
	private static final int LIST_ADRESE = 2;
	private TextView textDataLivrare;
	private Button btnDataLivrare;

	private ComenziDAO operatiiComenzi;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.adr_livr_custodie_header);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Date livrare");
		actionBar.setDisplayHomeAsUpEnabled(true);

		operatiiAdresa = new OperatiiAdresaImpl(this);
		operatiiAdresa.setOperatiiAdresaListener(this);

		this.saveAdrLivrBtn = (Button) findViewById(R.id.saveAdrLivrBtn);
		addListenerSaveAdr();

		this.layoutAdrese = (LinearLayout) findViewById(R.id.layoutListAdrese);
		layoutAdrese.setVisibility(View.GONE);

		spinnerTonaj = (Spinner) findViewById(R.id.spinnerTonaj);
		setupSpinnerTonaj();

		this.layoutAdr1 = (LinearLayout) findViewById(R.id.layoutAdr1);
		this.layoutAdr2 = (LinearLayout) findViewById(R.id.layoutAdr2);

		layoutHarta = (LinearLayout) findViewById(R.id.layoutHarta);
		layoutHarta.setVisibility(View.GONE);

		textLocalitate = (AutoCompleteTextView) findViewById(R.id.autoCompleteLocalitate);
		textLocalitate.setVisibility(View.INVISIBLE);

		textNrStr = (EditText) findViewById(R.id.textNrStr);

		btnPozitieAdresa = (Button) findViewById(R.id.btnPozitieAdresa);
		setListnerBtnPozitieAdresa();

		operatiiComenzi = ComenziDAO.getInstance(this);
		operatiiComenzi.setComenziDAOListener(this);

		textStrada = (AutoCompleteTextView) findViewById(R.id.autoCompleteStrada);

		nf2 = NumberFormat.getInstance();
		nf2.setMinimumFractionDigits(2);
		nf2.setMaximumFractionDigits(2);

		spinnerJudet = (Spinner) findViewById(R.id.spinnerJudet);
		spinnerJudet.setOnItemSelectedListener(new regionSelectedListener());

		listJudete = new ArrayList<HashMap<String, String>>();
		adapterJudete = new SimpleAdapter(this, listJudete, R.layout.rowlayoutjudete, new String[] { "numeJudet", "codJudet" }, new int[] { R.id.textNumeJudet,
				R.id.textCodJudet });

		spinnerAdreseLivrare = (Spinner) findViewById(R.id.spinnerAdreseLivrare);

		setListenerSpinnerAdreseLivrare();

		listAdreseLivrare = new ArrayList<HashMap<String, String>>();
		selectedAddrModifCmd = -1;

		String numeJudSel = "";
		HashMap<String, String> temp;
		int i = 0;

		for (i = 0; i < UtilsGeneral.numeJudete.length; i++) {
			temp = new HashMap<String, String>(50, 0.75f);
			temp.put("numeJudet", UtilsGeneral.numeJudete[i]);
			temp.put("codJudet", UtilsGeneral.codJudete[i]);
			listJudete.add(temp);

			if (DateLivrare.getInstance().getCodJudet().equals(UtilsGeneral.codJudete[i])) {
				numeJudSel = UtilsGeneral.numeJudete[i];
			}

		}

		DateLivrare.getInstance().setNumeJudet(numeJudSel);
		spinnerJudet.setAdapter(adapterJudete);

		radioLista = (RadioButton) findViewById(R.id.radioLista);
		addListenerRadioLista();

		radioText = (RadioButton) findViewById(R.id.radioText);
		addListenerRadioText();

		layoutAdrese.setVisibility(View.VISIBLE);
		layoutAdr1.setVisibility(View.GONE);
		layoutAdr2.setVisibility(View.GONE);

		textDataLivrare = (TextView) findViewById(R.id.textDataLivrare);

		if (!DateLivrare.getInstance().getDataLivrare().isEmpty())
			textDataLivrare.setText(DateLivrare.getInstance().getDataLivrare());

		btnDataLivrare = (Button) findViewById(R.id.btnDataLivrare);
		addListenerDataLivrare();

		globalCodClient = ModificareCustodie.codClient;

		if (!ModificareCustodie.codAdresa.trim().isEmpty())
			performGetAdreseLivrare();
		else
			setDateLivrareComanda();

	}

	private void addListenerDataLivrare() {
		btnDataLivrare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Locale.setDefault(new Locale("ro"));

				int year = Calendar.getInstance().get(Calendar.YEAR);
				int month = Calendar.getInstance().get(Calendar.MONTH);
				int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
				SelectDateDialog datePickerDialog = new SelectDateDialog(AdrLivrCustodie.this, datePickerListener, year, month, day);
				datePickerDialog.setTitle("Data livrare");

				datePickerDialog.show();

			}
		});
	}

	private void setupSpinnerTonaj() {

		String[] tonajValues = { "Selectati tonajul", "3.5 T", "10 T", "Fara restrictie de tonaj" };

		ArrayAdapter<String> adapterTonaj = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tonajValues);
		adapterTonaj.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerTonaj.setAdapter(adapterTonaj);

	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

			if (view.isShown()) {

				SimpleDateFormat displayFormat = new SimpleDateFormat("dd.MM.yyyy");
				Calendar calendar = new GregorianCalendar(selectedYear, selectedMonth, selectedDay);

				StatusIntervalLivrare statusInterval = UtilsDates.getStatusIntervalLivrare(calendar.getTime());

				if (statusInterval.isValid()) {
					textDataLivrare.setText(displayFormat.format(calendar.getTime()));
					DateLivrare.getInstance().setDataLivrare(displayFormat.format(calendar.getTime()));
				} else
					Toast.makeText(getApplicationContext(), statusInterval.getMessage(), Toast.LENGTH_LONG).show();
			}

		}
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void addListenerRadioLista() {

		radioLista.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				

			}
		});

		radioLista.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

				if (arg1) {

					layoutAdrese.setVisibility(View.VISIBLE);
					layoutAdr1.setVisibility(View.GONE);
					layoutAdr2.setVisibility(View.GONE);
					layoutHarta.setVisibility(View.GONE);
					((LinearLayout) findViewById(R.id.layoutTonaj)).setVisibility(View.INVISIBLE);
					performGetAdreseLivrare();

				}

			}
		});
	}

	public void addListenerRadioText() {

		radioText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clearAdresaLivrare();
				((LinearLayout) findViewById(R.id.layoutTonaj)).setVisibility(View.VISIBLE);
				spinnerTonaj.setEnabled(true);
				spinnerTonaj.setSelection(0);
				setDateLivrareComanda();

			}
		});

		radioText.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

				if (arg1) {
					layoutAdrese.setVisibility(View.GONE);
					layoutAdr1.setVisibility(View.VISIBLE);
					layoutAdr2.setVisibility(View.VISIBLE);
					layoutHarta.setVisibility(View.VISIBLE);
					

				}

			}
		});
	}

	protected void performGetAdreseLivrare() {

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("codClient", globalCodClient);

		operatiiAdresa.getAdreseLivrareClient(params);

	}

	private void fillListAdrese(String adrese) {

		HandleJSONData objClientList = new HandleJSONData(this, adrese);
		adreseList = objClientList.decodeJSONAdresaLivrare();

		if (!adreseList.isEmpty()) {

			AdapterAdreseLivrare adapterAdrese = new AdapterAdreseLivrare(this, adreseList);
			spinnerAdreseLivrare.setAdapter(adapterAdrese);

			String codAdresaCmd = ModificareCustodie.codAdresa;

			if (!codAdresaCmd.trim().isEmpty()) {

				radioLista.setChecked(true);

				int nrAdrese = spinnerAdreseLivrare.getAdapter().getCount();

				for (int j = 0; j < nrAdrese; j++) {

					BeanAdresaLivrare adresaLivrare = (BeanAdresaLivrare) spinnerAdreseLivrare.getAdapter().getItem(j);

					String adrNumber = adresaLivrare.getCodAdresa();

					if (codAdresaCmd.equals(adrNumber)) {
						spinnerAdreseLivrare.setSelection(j);
						break;
					}

				}
			}

		}

	}

	private void setDateLivrareComanda() {

		if (ModificareCustodie.codAdresa.trim().isEmpty()) {

			radioText.setChecked(true);

			int nrJudete = spinnerJudet.getAdapter().getCount();

			String codJudet;

			for (int j = 0; j < nrJudete; j++) {
				@SuppressWarnings("unchecked")
				HashMap<String, String> artMap = (HashMap<String, String>) this.adapterJudete.getItem(j);
				codJudet = artMap.get("codJudet").toString();

				if (ModificareCustodie.dateLivrare.getCodJudet().equals(codJudet)) {
					spinnerJudet.setSelection(j);
					break;
				}

			}

			textLocalitate.setText(ModificareCustodie.dateLivrare.getOras());
			textStrada.setText(ModificareCustodie.dateLivrare.getDateLivrare());
			
			
			
			
		}
	}

	private void getCmdDateLivrare() {

		HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
		params.put("idCmd", ModificareComanda.selectedCmd);
		operatiiAdresa.getDateLivrare(params);

	}

	@SuppressWarnings("unchecked")
	private void fillDateLivrare(String dateLivrare) {

		try {
			// incarcare date livrare comanda modificata
			String[] tokLivrare = dateLivrare.split("#");
			HashMap<String, String> artMapLivr = null;

			// adresa de livrare
			if (tokLivrare[13].equals("X")) // adresa noua
			{
				radioText.setChecked(true);
				int nrJudete = listJudete.size(), j = 0;
				String codJudet = "";

				for (j = 0; j < nrJudete; j++) {
					artMapLivr = (HashMap<String, String>) this.adapterJudete.getItem(j);
					codJudet = artMapLivr.get("codJudet").toString();

					if (tokLivrare[8].equals(codJudet)) {
						spinnerJudet.setSelection(j);
						break;
					}

				}

				DateLivrare.getInstance().setOras(tokLivrare[7]);

				textLocalitate.setText(tokLivrare[7]);

				textStrada.setText(tokLivrare[4]);
				adrNouaModifCmd = true;

			} else {
				radioLista.setChecked(true);

				int nrAdrese = spinnerAdreseLivrare.getAdapter().getCount();

				int j = 0;
				String adr_number = "";

				for (j = 0; j < nrAdrese; j++) {

					BeanAdresaLivrare adresaLivrare = (BeanAdresaLivrare) spinnerAdreseLivrare.getAdapter().getItem(j);

					adr_number = adresaLivrare.getCodAdresa();

					if (tokLivrare[15].equals(adr_number)) {
						spinnerAdreseLivrare.setSelection(j);
						selectedAddrModifCmd = j;
						break;
					}

				}

			}

			// pers. contact
			txtPers.setText(tokLivrare[2]);

			// telefon
			txtTel.setText(tokLivrare[3]);

			// fact.red. separat
			if (tokLivrare[6].equals("X"))
				spinnerTipReducere.setSelection(1);
			if (tokLivrare[6].equals("R"))
				spinnerTipReducere.setSelection(2);
			if (tokLivrare[6].equals(" "))
				spinnerTipReducere.setSelection(0);

			// doc. insot
			if (tokLivrare[12].equals("1"))
				spinnerDocInsot.setSelection(0);
			else
				spinnerDocInsot.setSelection(1);

			// tip plata
			if (tokLivrare[12].equals("B"))
				spinnerPlata.setSelection(0);
			if (tokLivrare[12].equals("C"))
				spinnerPlata.setSelection(1);
			if (tokLivrare[12].equals("E"))
				spinnerPlata.setSelection(2);
			if (tokLivrare[12].equals("O"))
				spinnerPlata.setSelection(3);

			// obs livrare
			txtObservatii.setText(tokLivrare[10]);

			// obs. plata (responsabil livrare)
			spinnerResponsabil.setSelection(0);

			if (tokLivrare[14].equals("AV")) {
				spinnerResponsabil.setSelection(0);
			} else if (tokLivrare[14].equals("SO")) {
				spinnerResponsabil.setSelection(1);
			} else if (tokLivrare[14].equals("OC")) {
				spinnerResponsabil.setSelection(2);
			}

			DateLivrare.getInstance().setValoareIncasare(tokLivrare[16]);
			if (tokLivrare[17].equals("X")) {
				DateLivrare.getInstance().setValIncModif(true);
				checkModifValInc.setChecked(true);
			} else {
				DateLivrare.getInstance().setValIncModif(false);
				checkModifValInc.setChecked(false);
			}

			DateLivrare.getInstance().setPrelucrare(tokLivrare[18]);
			for (int i = 0; i < spinnerIndoire.getCount(); i++)
				if (spinnerIndoire.getItemAtPosition(i).toString().toUpperCase().contains(tokLivrare[18])) {
					spinnerIndoire.setSelection(i);
					break;
				}

			DateLivrare.getInstance().setClientRaft(tokLivrare[20].equals("X") ? true : false);

		} catch (Exception ex) {
			Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	public class regionSelectedListener implements OnItemSelectedListener {
		@SuppressWarnings("unchecked")
		public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {

			if (spinnerJudet.getSelectedItemPosition() > 0) {

				HashMap<String, String> tempMap = (HashMap<String, String>) spinnerJudet.getSelectedItem();
				DateLivrare.getInstance().setNumeJudet(tempMap.get("numeJudet"));
				DateLivrare.getInstance().setCodJudet(tempMap.get("codJudet"));

				HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
				params.put("codJudet", DateLivrare.getInstance().getCodJudet());
				operatiiAdresa.getAdreseJudet(params, null);

			}

		}

		public void onNothingSelected(AdapterView<?> parent) {
			return;
		}
	}

	private void populateListLocalitati(BeanAdreseJudet listAdrese) {

		textLocalitate.setVisibility(View.VISIBLE);
		textLocalitate.setText(DateLivrare.getInstance().getOras());

		String[] arrayLocalitati = listAdrese.getListLocalitati().toArray(new String[listAdrese.getListLocalitati().size()]);
		ArrayAdapter<String> adapterLoc = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayLocalitati);

		textLocalitate.setThreshold(0);
		textLocalitate.setAdapter(adapterLoc);

		String[] arrayStrazi = listAdrese.getListStrazi().toArray(new String[listAdrese.getListStrazi().size()]);
		ArrayAdapter<String> adapterStrazi = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayStrazi);

		textStrada.setThreshold(0);
		textStrada.setAdapter(adapterStrazi);

		setListenerTextLocalitate();

	}

	private void setListenerTextLocalitate() {

		textLocalitate.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				DateLivrare.getInstance().setOras(textLocalitate.getText().toString().trim());

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

	}

	private void setListnerBtnPozitieAdresa() {
		btnPozitieAdresa.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				FragmentManager fm = getSupportFragmentManager();

				Address address = new Address();

				if (radioText.isChecked()) {
					address.setCity(textLocalitate.getText().toString().trim());
					address.setStreet(textStrada.getText().toString().trim());
					address.setSector(UtilsGeneral.getNumeJudet(DateLivrare.getInstance().getCodJudet()));

					if (!isAdresaComplet())
						return;

					String nrStrada = " ";

					if (textNrStr.getText().toString().trim().length() > 0)
						nrStrada = " nr " + textNrStr.getText().toString().trim();

					DateLivrare.getInstance().setOras(address.getCity());
					DateLivrare.getInstance().setStrada(address.getStreet() + nrStrada);

				} else {
					address.setCity(DateLivrare.getInstance().getOras());
					address.setStreet(DateLivrare.getInstance().getStrada());
					address.setSector(UtilsGeneral.getNumeJudet(DateLivrare.getInstance().getCodJudet()));
				}

				MapAddressDialog mapDialog = new MapAddressDialog(address, AdrLivrCustodie.this, fm);
				mapDialog.setCoords(DateLivrare.getInstance().getCoordonateAdresa());
				mapDialog.setMapListener(AdrLivrCustodie.this);

			}
		});
	}

	private boolean isAdresaComplet() {
		if (spinnerJudet.getSelectedItemPosition() == 0) {
			Toast.makeText(this, "Selectati judetul", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (textLocalitate.getText().toString().trim().equals("")) {
			Toast.makeText(this, "Completati localitatea", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	public void addListenerSaveAdr() {
		saveAdrLivrBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				if (radioText.isChecked())
					valideazaAdresaLivrare();
				else
					valideazaDateLivrare();

			}
		});

	}

	private void valideazaDateLivrare() {
		String adresa = "";
		String pers = "";
		String telefon = "";
		String observatii = "", obsPlata = " ";

		DateLivrare dateLivrareInstance = DateLivrare.getInstance();

		if (layoutAdrese.getVisibility() == View.VISIBLE) {

			if (listAdreseLivrare.size() > 0) {
				BeanAdresaLivrare adresaLivrare = (BeanAdresaLivrare) spinnerAdreseLivrare.getSelectedItem();
				setAdresaLivrareFromList(adresaLivrare);

			}

		} else {

			String nrStrada = "";

			if (textNrStr.getText().toString().trim().length() > 0)
				nrStrada = " NR " + textNrStr.getText().toString().trim();

			dateLivrareInstance.setStrada(textStrada.getText().toString().trim() + nrStrada);
			dateLivrareInstance.setAdrLivrNoua(true);
			dateLivrareInstance.setAddrNumber(" ");

		}

		if (DateLivrare.getInstance().getOras().trim().equals("")) {
			Toast.makeText(getApplicationContext(), "Selectati localitatea!", Toast.LENGTH_LONG).show();
			return;
		}

		if (dateLivrareInstance.getCodJudet().equals("")) {
			Toast.makeText(getApplicationContext(), "Selectati judetul!", Toast.LENGTH_LONG).show();
			return;
		}

		if (((LinearLayout) findViewById(R.id.layoutTonaj)).getVisibility() == View.VISIBLE && spinnerTonaj.getSelectedItemPosition() == 0) {
			Toast.makeText(getApplicationContext(), "Selectati tonajul!", Toast.LENGTH_LONG).show();
			return;
		}

		if (!isAdresaCorecta()) {
			Toast.makeText(getApplicationContext(), "Completati adresa corect sau pozitionati adresa pe harta.", Toast.LENGTH_LONG).show();
			return;
		}

		String cantar = "NU";
		dateLivrareInstance.setCantar("NU");

		adresa = dateLivrareInstance.getNumeJudet() + " " + dateLivrareInstance.getOras() + " " + dateLivrareInstance.getStrada();

		dateLivrareInstance.setDateLivrare(adresa + "#" + pers + "#" + telefon + "#" + cantar + "#" + dateLivrareInstance.getTipPlata() + "#"
				+ dateLivrareInstance.getTransport() + "#" + dateLivrareInstance.getRedSeparat() + "#");

		if (isConditiiTonaj(spinnerTransp, spinnerTonaj)) {
			dateLivrareInstance.setTonaj(HelperAdreseLivrare.getTonajSpinnerValue(spinnerTonaj.getSelectedItemPosition()));
		} else
			dateLivrareInstance.setTonaj("-1");

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("idComanda", ModificareCustodie.idComanda);
		params.put("dateLivrare", serializeDateLivrare());

		operatiiComenzi.setCustodieAdresaLivrare(params);

	}

	private boolean isAdresaCorecta() {
		if (isAdresaText())
			return isAdresaGoogleOk();
		else
			return true;

	}

	private boolean isAdresaText() {
		return radioText != null && radioText.isChecked();
	}

	private boolean hasCoordinates() {
		if (DateLivrare.getInstance().getCoordonateAdresa() == null)
			return false;
		else if (DateLivrare.getInstance().getCoordonateAdresa().latitude == 0)
			return false;

		return true;
	}

	private boolean isConditiiTonaj(Spinner spinnerTransp, Spinner spinnerTonaj) {
		return spinnerTonaj.getSelectedItemPosition() > 0;

	}

	private boolean isAdresaGoogleOk() {
		GeocodeAddress geoAddress = MapUtils.geocodeAddress(getAddressFromForm(), getApplicationContext());

		DateLivrare.getInstance().setCoordonateAdresa(geoAddress.getCoordinates());

		return geoAddress.isAdresaValida();

	}

	private Address getAddressFromForm() {
		Address address = new Address();

		address.setCity(DateLivrare.getInstance().getOras());
		address.setStreet(UtilsAddress.getStreetNoNumber(DateLivrare.getInstance().getStrada()));
		address.setSector(UtilsGeneral.getNumeJudet(DateLivrare.getInstance().getCodJudet()));

		return address;
	}

	private void setAdresaLivrareFromList(BeanAdresaLivrare adresaLivrare) {

		DateLivrare.getInstance().setAddrNumber(adresaLivrare.getCodAdresa());
		DateLivrare.getInstance().setNumeJudet(UtilsGeneral.getNumeJudet(adresaLivrare.getCodJudet()));

		if (!adresaLivrare.getOras().trim().equals(""))
			DateLivrare.getInstance().setOras(adresaLivrare.getOras());
		else
			DateLivrare.getInstance().setOras("-");

		if (!adresaLivrare.getStrada().trim().equals("")) {
			DateLivrare.getInstance().setStrada(adresaLivrare.getStrada() + " " + adresaLivrare.getNrStrada());

		} else {
			DateLivrare.getInstance().setStrada("-");

		}

		DateLivrare.getInstance().setCodJudet(adresaLivrare.getCodJudet());
		DateLivrare.getInstance().setAdrLivrNoua(false);

		String[] tokenCoords = adresaLivrare.getCoords().split(",");

		DateLivrare.getInstance().setCoordonateAdresa(new LatLng(Double.valueOf(tokenCoords[0]), Double.valueOf(tokenCoords[1])));
		DateLivrare.getInstance().setTonaj(adresaLivrare.getTonaj());

		if (adresaLivrare.getTonaj().equals("0")) {
			((LinearLayout) findViewById(R.id.layoutTonaj)).setVisibility(View.VISIBLE);
			spinnerTonaj.setSelection(0);
		} else {
			((LinearLayout) findViewById(R.id.layoutTonaj)).setVisibility(View.INVISIBLE);
		}

	}

	private void setAdresaLivrare(Address address) {

		textLocalitate.getText().clear();
		textStrada.getText().clear();
		textNrStr.getText().clear();

		int nrJudete = spinnerJudet.getAdapter().getCount();

		for (int j = 0; j < nrJudete; j++) {
			HashMap<String, String> artMapLivr = (HashMap<String, String>) this.adapterJudete.getItem(j);
			String numeJudet = artMapLivr.get("numeJudet").toString();

			if (address.getSector().equals(numeJudet)) {
				spinnerJudet.setSelection(j);
				break;
			}

		}

		if (address.getCity() != null && !address.getCity().isEmpty())
			textLocalitate.setText(address.getCity());

		if (address.getStreet() != null && !address.getStreet().isEmpty() && !address.getStreet().toUpperCase().contains("UNNAMED"))
			textStrada.setText(address.getStreet().trim());

		if (address.getNumber() != null && address.getNumber().length() > 0)
			textNrStr.setText(address.getNumber());

	}

	private void valideazaAdresaLivrare() {

		HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
		params.put("codJudet", DateLivrare.getInstance().getCodJudet());
		params.put("localitate", DateLivrare.getInstance().getOras());

		operatiiAdresa.isAdresaValida(params, EnumLocalitate.LOCALITATE_SEDIU);

	}

	private void setListenerSpinnerAdreseLivrare() {
		spinnerAdreseLivrare.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				BeanAdresaLivrare adresaLivrare = (BeanAdresaLivrare) spinnerAdreseLivrare.getAdapter().getItem(position);
				setAdresaLivrareFromList(adresaLivrare);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	private String serializeDateLivrare() {

		JSONObject obj = new JSONObject();

		try {

			obj.put("codJudet", DateLivrare.getInstance().getCodJudet());
			obj.put("numeJudet", DateLivrare.getInstance().getNumeJudet());
			obj.put("Oras", DateLivrare.getInstance().getOras());
			obj.put("Strada", DateLivrare.getInstance().getStrada() + " ");
			obj.put("persContact", DateLivrare.getInstance().getPersContact());
			obj.put("nrTel", DateLivrare.getInstance().getNrTel());
			obj.put("redSeparat", DateLivrare.getInstance().getRedSeparat());
			obj.put("Cantar", DateLivrare.getInstance().getCantar());
			obj.put("tipPlata", DateLivrare.getInstance().getTipPlata());
			obj.put("Transport", DateLivrare.getInstance().getTransport());
			obj.put("dateLivrare", DateLivrare.getInstance().getDateLivrare());
			obj.put("termenPlata", DateLivrare.getInstance().getTermenPlata());
			obj.put("obsLivrare", DateLivrare.getInstance().getObsLivrare());
			obj.put("dataLivrare", DateLivrare.getInstance().getDataLivrare());
			obj.put("adrLivrNoua", DateLivrare.getInstance().isAdrLivrNoua());
			obj.put("tipDocInsotitor", DateLivrare.getInstance().getTipDocInsotitor());
			obj.put("obsPlata", DateLivrare.getInstance().getObsPlata());
			obj.put("addrNumber", DateLivrare.getInstance().getAddrNumber());
			obj.put("valoareIncasare", DateLivrare.getInstance().getValoareIncasare());
			obj.put("isValIncModif", DateLivrare.getInstance().isValIncModif());
			obj.put("mail", DateLivrare.getInstance().getMail());
			obj.put("totalComanda", ListaArticoleComanda.getInstance().getTotalComanda());
			obj.put("unitLog", DateLivrare.getInstance().getUnitLog());
			obj.put("codAgent", DateLivrare.getInstance().getCodAgent());
			obj.put("factRed", DateLivrare.getInstance().getFactRed());
			obj.put("macara", DateLivrare.getInstance().isMasinaMacara() ? "X" : " ");
			obj.put("idObiectiv", DateLivrare.getInstance().getIdObiectiv());
			obj.put("isAdresaObiectiv", DateLivrare.getInstance().isAdresaObiectiv());
			obj.put("coordonateGps", getCoordAdresa());
			obj.put("tonaj", DateLivrare.getInstance().getTonaj());
			obj.put("prelucrare", DateLivrare.getInstance().getPrelucrare());
			obj.put("clientRaft", DateLivrare.getInstance().isClientRaft());
			obj.put("factPaletiSeparat", DateLivrare.getInstance().isFactPaletSeparat());

			String codFurnizorMarfa = DateLivrare.getInstance().getFurnizorComanda() == null ? " " : DateLivrare.getInstance().getFurnizorComanda()
					.getCodFurnizorMarfa();
			obj.put("furnizorMarfa", codFurnizorMarfa);

			String codFurnizorProduse = DateLivrare.getInstance().getFurnizorComanda() == null ? " " : DateLivrare.getInstance().getFurnizorComanda()
					.getCodFurnizorProduse();
			obj.put("furnizorProduse", codFurnizorProduse);

			obj.put("isCamionDescoperit", DateLivrare.getInstance().isCamionDescoperit());
			obj.put("codSuperAgent", UserInfo.getInstance().getCodSuperUser());

		} catch (JSONException ex) {
			Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
		} catch (Exception ex) {
			Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
		}

		return obj.toString();

	}

	private String getCoordAdresa() {
		if (DateLivrare.getInstance().getCoordonateAdresa() != null)
			return DateLivrare.getInstance().getCoordonateAdresa().latitude + "#" + DateLivrare.getInstance().getCoordonateAdresa().longitude;
		else
			return "0#0";
	}

	private void clearAdresaLivrare() {
		DateLivrare.getInstance().setOras("");
		DateLivrare.getInstance().setStrada("");
		DateLivrare.getInstance().setCodJudet("");
		DateLivrare.getInstance().setCoordonateAdresa(null);

		spinnerJudet.setSelection(0);
		textLocalitate.setText("");
		textNrStr.setText("");
		textStrada.setText("");

	}

	@Override
	public void onBackPressed() {
		finish();
		return;
	}

	private void valideazaAdresaResponse(String result) {
		valideazaDateLivrare();

	}

	public void operatiiAdresaComplete(EnumOperatiiAdresa numeComanda, Object result, EnumLocalitate tipLocalitate) {

		switch (numeComanda) {
		case GET_ADRESE_JUDET:
			populateListLocalitati(operatiiAdresa.deserializeListAdrese(result));
			break;
		case IS_ADRESA_VALIDA:
			valideazaAdresaResponse((String) result);
			break;
		case GET_DATE_LIVRARE:
			fillDateLivrare((String) result);
			break;
		case GET_ADRESE_LIVR_CLIENT:
			fillListAdrese((String) result);
			break;

		default:
			break;
		}

	}

	@Override
	public void addressSelected(LatLng coord, android.location.Address address) {
		DateLivrare.getInstance().setCoordonateAdresa(coord);
		setAdresaLivrare(MapUtils.getAddress(address));
		// valideazaTonajAdresaNoua();

	}

	@Override
	public void selectionComplete(String selectedItem, int actionId) {

		switch (actionId) {
		case LIST_LOCALITATI:
			textLocalitate.setText(selectedItem);
			break;
		case LIST_ADRESE:
			textStrada.setText(selectedItem);
			break;
		default:
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		return false;
	}

	@Override
	public void operationComenziComplete(EnumComenziDAO methodName, Object result) {
		Toast.makeText(getApplicationContext(), ((String)result).split("#")[1], Toast.LENGTH_LONG).show();

	}

}
