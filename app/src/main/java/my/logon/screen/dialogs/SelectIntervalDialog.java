package my.logon.screen.dialogs;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import my.logon.screen.listeners.IntervalDialogListener;
import my.logon.screen.R;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class SelectIntervalDialog extends Dialog {

	private Context context;

	String[] options = { "Astazi", "In ultimele 7 zile", "In ultimele 30 de zile", "In intervalul" };

	String[] days = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23",
			"24", "25", "26", "27", "28", "29", "30", "31" };

	String[] month = { "Ianuarie", "Februarie", "Martie", "Aprilie", "Mai", "Iunie", "Iulie", "August", "Septembrie", "Octombrie", "Noiembrie", "Decembrie" };

	String[] years = { "2012", "2013", "2014", "2015", "2016", "2017", "2018" };
	Spinner spinnerSelInterval;
	LinearLayout layoutSelInterval;
	IntervalDialogListener listener;

	private String intervalAfisare = "0", dataSelStart = "", dataSelStop = "";

	Spinner dayStart, monthStart, yearStart, dayStop, monthStop, yearStop;
	Button btnOkInterval;

	public SelectIntervalDialog(Context context) {
		super(context);
		this.context = context;

		setContentView(R.layout.selintervaldialogafiscmd);
		setTitle("Afiseaza comenzile create");
		setCancelable(true);

		setUpLayout();

	}

	public void showDialog() {
		this.show();
	}

	private void dismissCustomDialog() {
		this.dismiss();
	}

	public void setIntervalDialogListener(IntervalDialogListener listener) {
		this.listener = listener;
	}

	private void setUpLayout() {
		layoutSelInterval = (LinearLayout) findViewById(R.id.layoutSelInterval);
		layoutSelInterval.setVisibility(View.INVISIBLE);

		spinnerSelInterval = (Spinner) findViewById(R.id.spinnerSelInterval);

		ArrayList<HashMap<String, String>> listOptInterval = new ArrayList<HashMap<String, String>>();
		SimpleAdapter adapterOptions = new SimpleAdapter(context, listOptInterval, R.layout.customrowselinterval, new String[] { "optInterval" },
				new int[] { R.id.textTipInterval });

		HashMap<String, String> temp;

		for (int ii = 0; ii < options.length; ii++) {
			temp = new HashMap<String, String>();
			temp.put("optInterval", options[ii]);
			listOptInterval.add(temp);
		}

		spinnerSelInterval.setAdapter(adapterOptions);
		setSpinnerIntervalListener();
		spinnerSelInterval.setSelection(Integer.valueOf(intervalAfisare));

		dayStart = (Spinner) findViewById(R.id.dayStart);
		monthStart = (Spinner) findViewById(R.id.monthStart);
		yearStart = (Spinner) findViewById(R.id.yearStart);
		dayStop = (Spinner) findViewById(R.id.dayStop);
		monthStop = (Spinner) findViewById(R.id.monthStop);
		yearStop = (Spinner) findViewById(R.id.yearStop);

		ArrayList<HashMap<String, String>> listIntervalDay = new ArrayList<HashMap<String, String>>();
		SimpleAdapter adapterDay = new SimpleAdapter(context, listIntervalDay, R.layout.customrowintervalstart, new String[] { "startInterval" },
				new int[] { R.id.textStartInterval });

		for (int ii = 0; ii < days.length; ii++) {
			temp = new HashMap<String, String>();
			temp.put("startInterval", days[ii]);
			listIntervalDay.add(temp);
		}

		dayStart.setAdapter(adapterDay);
		dayStop.setAdapter(adapterDay);

		ArrayList<HashMap<String, String>> listIntervalMonth = new ArrayList<HashMap<String, String>>();
		SimpleAdapter adapterMonth = new SimpleAdapter(context, listIntervalMonth, R.layout.customrowintervalstart, new String[] { "startInterval" },
				new int[] { R.id.textStartInterval });

		for (int ii = 0; ii < month.length; ii++) {
			temp = new HashMap<String, String>();
			temp.put("startInterval", month[ii]);
			listIntervalMonth.add(temp);
		}

		monthStart.setAdapter(adapterMonth);
		monthStop.setAdapter(adapterMonth);

		ArrayList<HashMap<String, String>> listIntervalYear = new ArrayList<HashMap<String, String>>();
		SimpleAdapter adapterYear = new SimpleAdapter(context, listIntervalYear, R.layout.customrowintervalstart, new String[] { "startInterval" },
				new int[] { R.id.textStartInterval });

		years = getYears();
		
		for (int ii = 0; ii < years.length; ii++) {
			temp = new HashMap<String, String>();
			temp.put("startInterval", years[ii]);
			listIntervalYear.add(temp);
		}

		yearStart.setAdapter(adapterYear);
		yearStop.setAdapter(adapterYear);

		final Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);

		dayStart.setSelection(0);
		monthStart.setSelection(mMonth);

		dayStop.setSelection(mDay - 1);
		monthStop.setSelection(mMonth);

		setInitYear(mYear);

		btnOkInterval = (Button) findViewById(R.id.btnOkInterval);
		setBtnOkListener();

	}

	private String[] getYears() {
		String[] years;

		int lYear = Calendar.getInstance().get(Calendar.YEAR);
		int fYear = 2012;
		int nrYears = lYear - fYear;

		List<String> lYears = new ArrayList<String>();

		for (int i = 0; i <= nrYears; i++) {
			lYears.add(String.valueOf(fYear + i));
		}

		years = lYears.toArray(new String[0]);
		return years;
	}	
	
	private void setInitYear(int currentYear) {

		for (int i = 0; i < yearStop.getAdapter().getCount(); i++) {
			if (yearStop.getAdapter().getItem(i).toString().contains(String.valueOf(currentYear))) {
				yearStart.setSelection(i);
				yearStop.setSelection(i);
				break;
			}
		}

	}

	private void setSpinnerIntervalListener() {
		spinnerSelInterval.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				if (pos == 0) {
					layoutSelInterval.setVisibility(View.INVISIBLE);
					intervalAfisare = "0";
					dataSelStart = dataSelStop = "";
				}

				if (pos == 1) {
					layoutSelInterval.setVisibility(View.INVISIBLE);
					intervalAfisare = "1";
					dataSelStart = dataSelStop = "";
				}

				if (pos == 2) {
					layoutSelInterval.setVisibility(View.INVISIBLE);
					intervalAfisare = "2";
					dataSelStart = dataSelStop = "";
				}

				if (pos == 3) {
					layoutSelInterval.setVisibility(View.VISIBLE);
					intervalAfisare = "3";
				}

			}

			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	private void setBtnOkListener() {
		btnOkInterval.setOnClickListener(new View.OnClickListener() {

			@SuppressWarnings("unchecked")
			public void onClick(View v) {
				NumberFormat nf3 = new DecimalFormat("00");

				HashMap<String, String> artMap = (HashMap<String, String>) dayStart.getSelectedItem();
				String strDayStart = artMap.get("startInterval");

				artMap = (HashMap<String, String>) yearStart.getSelectedItem();
				String strYearStart = artMap.get("startInterval");

				artMap = (HashMap<String, String>) dayStop.getSelectedItem();
				String strDayStop = artMap.get("startInterval");

				artMap = (HashMap<String, String>) yearStop.getSelectedItem();
				String strYearStop = artMap.get("startInterval");

				dataSelStart = strDayStart + "#" + nf3.format(monthStart.getSelectedItemPosition() + 1) + "#" + strYearStart;
				dataSelStop = strDayStop + "#" + nf3.format(monthStop.getSelectedItemPosition() + 1) + "#" + strYearStop;

				if (!isDateValid(strDayStart + "." + nf3.format(monthStart.getSelectedItemPosition() + 1) + "." + strYearStart)) {
					Toast.makeText(context, "Data inceput interval este invalida!", Toast.LENGTH_SHORT).show();
					return;
				}

				if (!isDateValid(strDayStop + "." + nf3.format(monthStop.getSelectedItemPosition() + 1) + "." + strYearStop)) {
					Toast.makeText(context, "Data sfarsit interval este invalida!", Toast.LENGTH_SHORT).show();
					return;
				}

				if (listener != null) {
					listener.operationIntervalComplete(intervalAfisare, dataSelStart, dataSelStop);
					dismissCustomDialog();
				}

			}
		});
	}

	private boolean isDateValid(String date) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
			df.setLenient(false);
			df.parse(date);
			return true;
		} catch (Exception e) {
			Log.e("Error", e.toString());
			return false;
		}
	}

}
