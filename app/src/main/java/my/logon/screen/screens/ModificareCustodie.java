/**
 * @author florinb
 * 
 */
package my.logon.screen.screens;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import my.logon.screen.R;
import my.logon.screen.adapters.ArticolCustodieAdapter;
import my.logon.screen.adapters.CustodiiAdapter;
import my.logon.screen.adapters.DLExpirateAdapter;
import my.logon.screen.beans.BeanArticoleAfisare;
import my.logon.screen.beans.BeanComandaCreata;
import my.logon.screen.beans.DLExpirat;
import my.logon.screen.beans.DateLivrareAfisare;
import my.logon.screen.beans.StatusIntervalLivrare;
import my.logon.screen.dialogs.SelectDateDialog;
import my.logon.screen.enums.EnumComenziDAO;
import my.logon.screen.listeners.ComenziDAOListener;
import my.logon.screen.model.ArticolComanda;
import my.logon.screen.model.ComenziDAO;
import my.logon.screen.model.HandleJSONData;
import my.logon.screen.model.UserInfo;
import my.logon.screen.utils.UtilsDates;
import my.logon.screen.utils.UtilsFormatting;

public class ModificareCustodie extends Activity implements ComenziDAOListener {

	Spinner spinnerCmdClp;

	private static String selectedCmd = "";

	private TextView textDataLivrare;

	private LinearLayout layoutCustodii;

	private static int intervalAfisare = 0;

	private ComenziDAO operatiiComenzi;
	private Button btnDataLivrare;
	private Button btnSalveaza;
	private Button btnStergeLivrare;

	private ListView listViewArticole;
	private Spinner spinnerCustodii;
	public static String codClient = "";
	public static String codAdresa = "";
	public static String idComanda = "";
	public static DateLivrareAfisare dateLivrare;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.modificare_custodie);

		operatiiComenzi = ComenziDAO.getInstance(this);
		operatiiComenzi.setComenziDAOListener(this);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Modificare livrare custodie");
		actionBar.setDisplayHomeAsUpEnabled(true);

		spinnerCustodii = (Spinner) findViewById(R.id.spinnerCustodii);

		spinnerCustodii.setVisibility(View.INVISIBLE);
		addSpinnerListener();

		listViewArticole = (ListView) findViewById(R.id.listArtCustodii);

		listViewArticole.setVisibility(View.INVISIBLE);

		textDataLivrare = (TextView) findViewById(R.id.textDataLivrare);

		layoutCustodii = (LinearLayout) findViewById(R.id.layoutCustodii);
		layoutCustodii.setVisibility(View.INVISIBLE);

		btnDataLivrare = (Button) findViewById(R.id.btnDataLivrare);
		setListenerDataLivrare();

		btnSalveaza = (Button) findViewById(R.id.btnSalveaza);
		setListenerBtnSalveaza();

		btnStergeLivrare = (Button) findViewById(R.id.btnStergeLivrare);
		setListenerBtnStergeLivrare();

		getLivrariCustodie();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		CreateMenu(menu);
		return true;
	}

	private void CreateMenu(Menu menu) {

		MenuItem mnu1 = menu.add(0, 0, 0, "Interval");
		mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		
		MenuItem mnu2 = menu.add(1, 1, 1, "Date livrare");
		mnu2.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case 0:

			final CharSequence[] constrCmd = { "Astazi", "In ultimele 7 zile", "In ultimele 30 de zile" };
			AlertDialog.Builder builderConstr = new AlertDialog.Builder(this);
			AlertDialog alertConstr;
			builderConstr.setTitle("Afiseaza comenzile create");
			builderConstr.setCancelable(true);
			builderConstr.setSingleChoiceItems(constrCmd, intervalAfisare, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {

					intervalAfisare = which;

					getLivrariCustodie();
					dialog.cancel();

				}
			});

			alertConstr = builderConstr.create();
			alertConstr.setCancelable(true);
			alertConstr.show();

			return true;
			
		case 1:
			
			if (spinnerCustodii.getAdapter() == null)
				return true;
			
			Intent nextScreenAdr = new Intent(getApplicationContext(), AdrLivrCustodie.class);
			startActivity(nextScreenAdr);
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

	public void getLivrariCustodie() {

		HashMap<String, String> params = new HashMap<String, String>();

		params.put("codAgent", UserInfo.getInstance().getCod());
		params.put("interval", String.valueOf(intervalAfisare));
		params.put("tipCmd", "1");

		operatiiComenzi.getLivrariCustodie(params);

	}

	private void showListComenzi(List<BeanComandaCreata> listComenziCreate) {

		listViewArticole.setAdapter(null);

		if (listComenziCreate.size() > 0) {

			spinnerCustodii.setVisibility(View.VISIBLE);
			layoutCustodii.setVisibility(View.VISIBLE);

			NumberFormat nf2 = NumberFormat.getInstance();
			nf2.setMinimumFractionDigits(2);
			nf2.setMaximumFractionDigits(2);

			CustodiiAdapter adapterComenzi = new CustodiiAdapter(listComenziCreate, this);
			spinnerCustodii.setAdapter(adapterComenzi);

			selectedCmd = adapterComenzi.getItem(0).getId();

		} else {
			Toast.makeText(getApplicationContext(), "Nu exista livrari.", Toast.LENGTH_LONG).show();
			noComenziLayout();
		}

	}

	void noComenziLayout() {

		spinnerCustodii.setVisibility(View.INVISIBLE);
		selectedCmd = "-1";

		spinnerCustodii.setAdapter(null);
		listViewArticole.setAdapter(null);

		((LinearLayout) findViewById(R.id.layoutCustodii)).setVisibility(View.INVISIBLE);

	}

	private void populateArtCmdList(BeanArticoleAfisare articoleComanda) {

		List<ArticolComanda> listArticole = articoleComanda.getListArticole();
		codAdresa = articoleComanda.getDateLivrare().getAdresaD();
		dateLivrare = articoleComanda.getDateLivrare();
		listViewArticole.setVisibility(View.VISIBLE);
		ArticolCustodieAdapter adapter = new ArticolCustodieAdapter(this, listArticole);
		listViewArticole.setAdapter(adapter);

	}

	void populateCmdList(String cmdList) {

		HandleJSONData objDocList = new HandleJSONData(this, cmdList);
		ArrayList<DLExpirat> listDocumente = objDocList.decodeDLExpirat();

		DLExpirateAdapter adapterComenziClp = new DLExpirateAdapter(this, listDocumente);
		spinnerCmdClp.setAdapter(adapterComenziClp);

		if (listDocumente.size() > 0) {
			spinnerCmdClp.setVisibility(View.VISIBLE);
			listViewArticole.setVisibility(View.VISIBLE);
			layoutCustodii.setVisibility(View.VISIBLE);

		} else {
			layoutCustodii.setVisibility(View.INVISIBLE);
			spinnerCmdClp.setVisibility(View.INVISIBLE);
			listViewArticole.setVisibility(View.INVISIBLE);
			Toast.makeText(getApplicationContext(), "Nu exista comenzi!", Toast.LENGTH_SHORT).show();
		}

	}

	private void performArtCmd() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("nrCmd", selectedCmd);
		operatiiComenzi.getArticoleCustodie(params);

	}

	private void setListenerDataLivrare() {
		btnDataLivrare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Locale.setDefault(new Locale("ro"));

				int year = Calendar.getInstance().get(Calendar.YEAR);
				int month = Calendar.getInstance().get(Calendar.MONTH);
				int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
				SelectDateDialog datePickerDialog = new SelectDateDialog(ModificareCustodie.this, datePickerListener, year, month, day);
				datePickerDialog.setTitle("Data livrare");

				datePickerDialog.show();

			}
		});
	}

	private void setListenerBtnSalveaza() {

		btnSalveaza.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				setCmdDataLivrare();

			}
		});

	}

	private void setListenerBtnStergeLivrare() {
		btnStergeLivrare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stergeLivrareCustodie();

			}
		});
	}

	private void setCmdDataLivrare() {

		String[] dataLivrare = textDataLivrare.getText().toString().split("\\-");

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("idComanda", selectedCmd);
		params.put("dataLivrare", dataLivrare[2] + dataLivrare[1] + dataLivrare[0]);
		operatiiComenzi.setCustodieDataLivrare(params);
	}

	private void stergeLivrareCustodie() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("idComanda", selectedCmd);
		operatiiComenzi.stergeLivrareCustodie(params);
	}

	private void operatieLivrareStatus(String result) {
		
		Toast.makeText(getApplicationContext(), result.split("#")[1], Toast.LENGTH_LONG).show();
		
		if (result.split("#")[0].equals("0")) {
			getLivrariCustodie();
		} 
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

			if (view.isShown()) {

				SimpleDateFormat displayFormat = new SimpleDateFormat("dd-MM-yyyy");
				Calendar calendar = new GregorianCalendar(selectedYear, selectedMonth, selectedDay);

				StatusIntervalLivrare statusInterval = UtilsDates.getStatusIntervalLivrare(calendar.getTime());

				if (statusInterval.isValid()) {
					textDataLivrare.setText(displayFormat.format(calendar.getTime()));
					btnSalveaza.setVisibility(View.VISIBLE);
				} else
					Toast.makeText(getApplicationContext(), statusInterval.getMessage(), Toast.LENGTH_LONG).show();
			}

		}
	};

	private void clearAllData() {

		selectedCmd = "";
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
		spinnerCustodii.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

				BeanComandaCreata custodie = (BeanComandaCreata) parent.getAdapter().getItem(position);
				idComanda = custodie.getId();
				codClient = custodie.getCodClient();
				selectedCmd = custodie.getId();
				textDataLivrare.setText(UtilsFormatting.formatDateFromSap(custodie.getData().split("#")[1]));
				performArtCmd();

			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	@Override
	public void operationComenziComplete(EnumComenziDAO methodName, Object result) {
		switch (methodName) {
		case GET_LIVRARI_CUSTODIE:
			showListComenzi(operatiiComenzi.deserializeListComenzi((String) result));
			break;
		case GET_ARTICOLE_CUSTODIE:
			populateArtCmdList(operatiiComenzi.deserializeArticoleComanda((String) result));
			break;
		case SET_CUSTODIE_DATA_LIVRARE:
		case STERGE_LIVRARE_CUSTODIE:
			operatieLivrareStatus((String) result);
			break;
		default:
			break;
		}

	}
}
