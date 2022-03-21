/**
 * @author florinb
 *
 */
package my.logon.screen.screens;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import my.logon.screen.R;

public class SelectTipReducere extends Activity {

	private Button saveAntetReducere;

	private static ArrayList<HashMap<String, String>> arraylistTipReducere = null, arraylistFrecventaReducere = null;
	private SimpleAdapter adapterTipReducere, adapterFrecventaReducere;
	private Spinner spinnerTipReducere, spinnerFrecventaReducere;
	private TextView textDataStart, textDataStop, textProcRedB5, textCoefCalitSelect, textValDepart;
	private int mYear;
	private int mMonth;
	private int mDay;
	private int dataInt = 0;
	static final int DATE_DIALOG_ID = 1;
	ToggleButton tglTotDepart;
	TableLayout tblTotDepart;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		setTheme(R.style.LRTheme);
		setContentView(R.layout.selecttipreducereheader);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Date antet reduceri ulterioare");
		actionBar.setDisplayHomeAsUpEnabled(true);

		ReduceriUlterioare.tipReducere = "";
		// ReduceriUlterioare.frecventaRedCod = "";
		ReduceriUlterioare.frecventaRedNume = "";
		ReduceriUlterioare.startValabil = "";
		ReduceriUlterioare.stopValabil = "";
		ReduceriUlterioare.selTotDepart = false;

		this.textProcRedB5 = (TextView) findViewById(R.id.textProcRedB5);
		this.textCoefCalitSelect = (TextView) findViewById(R.id.textCoefCalitSelect);
		this.textValDepart = (TextView) findViewById(R.id.textValDepart);

		this.tglTotDepart = (ToggleButton) findViewById(R.id.tglTotDepart);
		addListenerTglTotDepart();

		tblTotDepart = (TableLayout) findViewById(R.id.tableTotDepart);
		tblTotDepart.setVisibility(View.INVISIBLE);

		this.saveAntetReducere = (Button) findViewById(R.id.saveAntetReducere);
		addListenerSaveAntetRed();

		spinnerTipReducere = (Spinner) findViewById(R.id.spinnerTipReducere);

		arraylistTipReducere = new ArrayList<HashMap<String, String>>();
		adapterTipReducere = new SimpleAdapter(this, arraylistTipReducere, R.layout.rowlayouttipreducere,
				new String[] { "textNumeReducere", "textCodReducere" }, new int[] { R.id.textNumeReducere, R.id.textCodReducere });

		HashMap<String, String> temp;

		temp = new HashMap<String, String>();
		temp.put("textNumeReducere", "Reducere cantitativ/valorica");
		temp.put("textCodReducere", "B5");
		arraylistTipReducere.add(temp);

		spinnerTipReducere.setAdapter(adapterTipReducere);

		spinnerTipReducere.setEnabled(false);

		spinnerTipReducere.setSelection(0);
		addListenerSpinnerReducere();

		spinnerFrecventaReducere = (Spinner) findViewById(R.id.spinnerFrecventaReducere);

		arraylistFrecventaReducere = new ArrayList<HashMap<String, String>>();
		adapterFrecventaReducere = new SimpleAdapter(this, arraylistFrecventaReducere, R.layout.rowlayoutfrecventareducere, new String[] { "textNumeFrecventa",
				"textCodFrecventa" }, new int[] { R.id.textNumeFrecventa, R.id.textCodFrecventa });

		spinnerFrecventaReducere.setEnabled(false);

		HashMap<String, String> hashFrecvRed;
		hashFrecvRed = new HashMap<String, String>();
		hashFrecvRed.put("textNumeFrecventa", "Fara frecventa");
		hashFrecvRed.put("textCodFrecventa", "F");
		arraylistFrecventaReducere.add(hashFrecvRed);

		hashFrecvRed = new HashMap<String, String>();
		hashFrecvRed.put("textNumeFrecventa", "Zilnica");
		hashFrecvRed.put("textCodFrecventa", "Z");
		arraylistFrecventaReducere.add(hashFrecvRed);

		hashFrecvRed = new HashMap<String, String>();
		hashFrecvRed.put("textNumeFrecventa", "Saptamanala");
		hashFrecvRed.put("textCodFrecventa", "S");
		arraylistFrecventaReducere.add(hashFrecvRed);

		hashFrecvRed = new HashMap<String, String>();
		hashFrecvRed.put("textNumeFrecventa", "Lunara");
		hashFrecvRed.put("textCodFrecventa", "L");
		arraylistFrecventaReducere.add(hashFrecvRed);

		hashFrecvRed = new HashMap<String, String>();
		hashFrecvRed.put("textNumeFrecventa", "Anuala");
		hashFrecvRed.put("textCodFrecventa", "A");
		arraylistFrecventaReducere.add(hashFrecvRed);

		hashFrecvRed = new HashMap<String, String>();
		hashFrecvRed.put("textNumeFrecventa", "Punctuala");
		hashFrecvRed.put("textCodFrecventa", "P");
		arraylistFrecventaReducere.add(hashFrecvRed);

		spinnerFrecventaReducere.setAdapter(adapterFrecventaReducere);

		this.textDataStart = (TextView) findViewById(R.id.textDataStart);
		addListenerDataStart();

		this.textDataStop = (TextView) findViewById(R.id.textDataStop);
		addListenerDataStop();

		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		c.set(Calendar.DAY_OF_MONTH, 1);

		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.UK);
		textDataStart.setText(format.format(c.getTime()));

		int lastDate = c.getActualMaximum(Calendar.DATE);
		c.set(Calendar.DATE, lastDate);

		textDataStop.setText(format.format(c.getTime()));

		ReduceriUlterioare.procRedB5 = "-1";
		ReduceriUlterioare.codReducere = "-1";
		ReduceriUlterioare.valTotDepart = "-1";
		ReduceriUlterioare.selTipClient = false;

	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case android.R.id.home:
			finish();
			return true;
		}
		return false;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
		}
		return null;
	}

	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case DATE_DIALOG_ID:
			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
			break;
		}
	}

	public void addListenerDataStart() {
		textDataStart.setOnClickListener(new View.OnClickListener() {
			@SuppressWarnings("deprecation")
			public void onClick(View v) {

				dataInt = 1;
				showDialog(DATE_DIALOG_ID);

			}
		});

	}

	public void addListenerTglTotDepart() {
		tglTotDepart.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (tglTotDepart.isChecked()) {
					ReduceriUlterioare.selTotDepart = true;
					tblTotDepart.setVisibility(View.VISIBLE);
				} else {

					ReduceriUlterioare.selTotDepart = false;
					ReduceriUlterioare.valTotDepart = "-1";
					tblTotDepart.setVisibility(View.INVISIBLE);

				}
			}
		});

	}

	public void addListenerDataStop() {
		textDataStop.setOnClickListener(new View.OnClickListener() {
			@SuppressWarnings("deprecation")
			public void onClick(View v) {
				dataInt = 2;
				showDialog(DATE_DIALOG_ID);
			}
		});

	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;

			SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.UK);
			Calendar clnd = new GregorianCalendar(mYear, mMonth, mDay);

			if (dataInt == 1) {
				textDataStart.setText(format.format(clnd.getTime()));
			}

			if (dataInt == 2) {
				textDataStop.setText(format.format(clnd.getTime()));
			}
		}
	};

	public void addListenerSaveAntetRed() {
		saveAntetReducere.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				ReduceriUlterioare.procRedB5 = textProcRedB5.getText().toString().trim();

				if (ReduceriUlterioare.selTotDepart) {
					if (textValDepart.getText().toString().trim().equals("")) {
						Toast.makeText(getApplicationContext(), "Completati valoarea pe departament!", Toast.LENGTH_SHORT).show();
						return;
					}
				}

				Object obj = spinnerTipReducere.getSelectedItem();
				String[] token = obj.toString().split(",");
				if (token[0].toLowerCase().contains("codreducere"))
					ReduceriUlterioare.tipReducere = token[0].substring(token[0].indexOf('=') + 1, token[0].length());
				else
					ReduceriUlterioare.tipReducere = token[1].substring(token[1].indexOf('=') + 1, token[1].length() - 1);

				obj = spinnerFrecventaReducere.getSelectedItem();
				token = obj.toString().split(",");

				ReduceriUlterioare.frecventaRedNume = token[0].substring(token[0].indexOf('=') + 1, token[0].length());

				ReduceriUlterioare.frecventaRedCod = token[1].substring(token[1].indexOf('=') + 1, token[1].length() - 1);

				ReduceriUlterioare.startValabil = textDataStart.getText().toString();
				ReduceriUlterioare.stopValabil = textDataStop.getText().toString();
				ReduceriUlterioare.coefCalit = textCoefCalitSelect.getText().toString();

				if (!ReduceriUlterioare.selTotDepart)
					ReduceriUlterioare.valTotDepart = "-1";
				else
					ReduceriUlterioare.valTotDepart = textValDepart.getText().toString();

				finish();
			}
		});

	}

	public void addListenerSpinnerReducere() {

		spinnerTipReducere.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

			}

			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});

	}

	@Override
	public void onBackPressed() {
		finish();
		return;
	}

}