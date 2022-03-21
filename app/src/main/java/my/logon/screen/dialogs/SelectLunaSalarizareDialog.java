package my.logon.screen.dialogs;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import my.logon.screen.listeners.IntervalSalarizareListener;
import my.logon.screen.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

public class SelectLunaSalarizareDialog extends Dialog {

	private Context context;

	private String[] month = { "Ianuarie", "Februarie", "Martie", "Aprilie", "Mai", "Iunie", "Iulie", "August", "Septembrie", "Octombrie", "Noiembrie",
			"Decembrie" };

	private String[] years;
	private IntervalSalarizareListener listener;

	private Button btnOkInterval;
	private Spinner spinnerLunaSalarizare;
	private Spinner spinnerAnSalarizare;

	public SelectLunaSalarizareDialog(Context context) {
		super(context);
		this.context = context;

		setContentView(R.layout.sel_luna_salarizare);
		setTitle("Selectati luna si anul");
		setCancelable(true);

		setUpLayout();

	}

	public void showDialog() {
		this.show();
	}

	private void dismissCustomDialog() {
		this.dismiss();
	}

	public void setIntervalDialogListener(IntervalSalarizareListener listener) {
		this.listener = listener;
	}

	private void setUpLayout() {

		HashMap<String, String> temp;

		spinnerLunaSalarizare = (Spinner) findViewById(R.id.lunaSalarizare);
		spinnerAnSalarizare = (Spinner) findViewById(R.id.anSalarizare);

		ArrayList<HashMap<String, String>> listIntervalMonth = new ArrayList<HashMap<String, String>>();
		SimpleAdapter adapterMonth = new SimpleAdapter(context, listIntervalMonth, R.layout.customrowintervalstart, new String[] { "startInterval" },
				new int[] { R.id.textStartInterval });

		for (int ii = 0; ii < month.length; ii++) {
			temp = new HashMap<String, String>();
			temp.put("startInterval", month[ii]);
			listIntervalMonth.add(temp);
		}

		spinnerLunaSalarizare.setAdapter(adapterMonth);

		ArrayList<HashMap<String, String>> listIntervalYear = new ArrayList<HashMap<String, String>>();
		SimpleAdapter adapterYear = new SimpleAdapter(context, listIntervalYear, R.layout.customrowintervalstart, new String[] { "startInterval" },
				new int[] { R.id.textStartInterval });

		years = getYears();

		for (int ii = 0; ii < years.length; ii++) {
			temp = new HashMap<String, String>();
			temp.put("startInterval", years[ii]);
			listIntervalYear.add(temp);
		}

		spinnerAnSalarizare.setAdapter(adapterYear);

		Date dt = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(dt);
		c.add(Calendar.MONTH, -1);

		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);

		spinnerLunaSalarizare.setSelection(mMonth);

		setInitYear(mYear);

		btnOkInterval = (Button) findViewById(R.id.btnOkInterval);
		setBtnOkListener();

	}

	private String[] getYears() {
		String[] years;

		int lYear = Calendar.getInstance().get(Calendar.YEAR);
		int fYear = 2018;
		int nrYears = lYear - fYear;

		List<String> lYears = new ArrayList<String>();

		for (int i = 0; i <= nrYears; i++) {
			lYears.add(String.valueOf(fYear + i));
		}

		years = lYears.toArray(new String[0]);
		return years;
	}

	private void setInitYear(int currentYear) {

		for (int i = 0; i < spinnerAnSalarizare.getAdapter().getCount(); i++) {
			if (spinnerAnSalarizare.getAdapter().getItem(i).toString().contains(String.valueOf(currentYear))) {
				spinnerAnSalarizare.setSelection(i);
				break;
			}
		}

	}

	private void setBtnOkListener() {
		btnOkInterval.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				NumberFormat nf3 = new DecimalFormat("00");

				if (listener != null) {

					String strLunaSel = nf3.format(spinnerLunaSalarizare.getSelectedItemPosition() + 1);

					@SuppressWarnings("unchecked")
					HashMap<String, String> artMap = (HashMap<String, String>) spinnerAnSalarizare.getSelectedItem();
					String strAnSel = artMap.get("startInterval");
					listener.intervalSalarizareSelected(strLunaSel, strAnSel);
					dismissCustomDialog();
				}

			}
		});
	}

}
