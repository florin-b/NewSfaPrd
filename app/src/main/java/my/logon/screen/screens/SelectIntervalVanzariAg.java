/**
 * @author florinb
 *
 */
package my.logon.screen.screens;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import my.logon.screen.model.VanzariAgenti;
import my.logon.screen.R;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

public class SelectIntervalVanzariAg extends Fragment {

	TextView txtDataStart, txtDataStop;

	private int mYear, mMonth, mDay;

	public static final SelectIntervalVanzariAg newInstance() {

		SelectIntervalVanzariAg f = new SelectIntervalVanzariAg();
		Bundle bdl = new Bundle(1);
		f.setArguments(bdl);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.select_interval_vanz_ag, container, false);

		try {

			txtDataStart = (TextView) v.findViewById(R.id.txtDataStartRap);
			txtDataStop = (TextView) v.findViewById(R.id.txtDataStopRap);

			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.UK);
			String currentDate = sdf.format(new Date());

			txtDataStart.setText(currentDate);
			addtxtDataStartListener();

			txtDataStop.setText(currentDate);
			addtxtDataStopListener();

			fillInterval();

		} catch (Exception e) {
			Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
		}

		return v;

	}

	public void addtxtDataStartListener() {

		txtDataStart.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {

				try {
					VanzariAgenti.getInstance().startIntervalRap = txtDataStart.getText().toString().trim();

				} catch (Exception ex) {
					Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_LONG).show();
				}

			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

		});

		txtDataStart.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				try {
					Time today = new Time(Time.getCurrentTimezone());
					today.setToNow();
					DatePickerDialog dialog = new DatePickerDialog(getActivity(), datePickerStartListener, today.year,
							today.month, today.monthDay);
					dialog.show();
				} catch (Exception ex) {
					Toast toast = Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_LONG);
					toast.show();
				}

			}
		});

	}

	private DatePickerDialog.OnDateSetListener datePickerStartListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			mYear = selectedYear;
			mMonth = selectedMonth;
			mDay = selectedDay;

			SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.UK);
			Calendar clnd = new GregorianCalendar(mYear, mMonth, mDay);

			txtDataStart.setText(format.format(clnd.getTime()));
			VanzariAgenti.getInstance().startIntervalRap = txtDataStart.getText().toString();

		}
	};

	public void addtxtDataStopListener() {

		txtDataStop.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {

				try {
					VanzariAgenti.getInstance().stopIntervalRap = txtDataStop.getText().toString().trim();

				} catch (Exception ex) {
					Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_LONG).show();
				}

			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

		});

		txtDataStop.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				try {
					Time today = new Time(Time.getCurrentTimezone());
					today.setToNow();
					DatePickerDialog dialog = new DatePickerDialog(getActivity(), datePickerStopListener, today.year,
							today.month, today.monthDay);
					dialog.show();
				} catch (Exception ex) {
					Toast toast = Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_LONG);
					toast.show();
				}

			}
		});

	}

	private DatePickerDialog.OnDateSetListener datePickerStopListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			mYear = selectedYear;
			mMonth = selectedMonth;
			mDay = selectedDay;

			SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.UK);
			Calendar clnd = new GregorianCalendar(mYear, mMonth, mDay);

			txtDataStop.setText(format.format(clnd.getTime()));
			VanzariAgenti.getInstance().stopIntervalRap = txtDataStop.getText().toString();

		}
	};

	private void fillInterval() {

		VanzariAgenti vanzariInstance = VanzariAgenti.getInstance();

		txtDataStart.setText(vanzariInstance.startIntervalRap);
		txtDataStop.setText(vanzariInstance.stopIntervalRap);
	}

}
