package my.logon.screen.screens;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import my.logon.screen.listeners.InputTextDialogListener;
import my.logon.screen.listeners.ObiectiveListener;
import my.logon.screen.model.OperatiiObiective;
import my.logon.screen.adapters.AdapterClientiObiective;
import my.logon.screen.adapters.AdapterObiectiveUrmarire;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import my.logon.screen.beans.BeanObiectiveConstructori;
import my.logon.screen.beans.BeanObiectiveGenerale;
import my.logon.screen.beans.BeanUrmarireEveniment;
import my.logon.screen.beans.BeanUrmarireObiectiv;
import my.logon.screen.dialogs.InputTextDialog;
import my.logon.screen.dialogs.SelectDateDialog;
import my.logon.screen.enums.EnumOperatiiObiective;
import my.logon.screen.enums.EnumUrmarireObiective;
import my.logon.screen.R;

public class UrmarireObiective extends Fragment implements InputTextDialogListener, ObiectiveListener {

	private TextView textCurrentPage, textObservatii, textDataEveniment;
	private Spinner spinnerOptiuniUrmarire;
	private Spinner spinnerClienti;
	private ImageButton btnModificaData, btnModificaObservatii;
	private BeanUrmarireObiectiv evenimentObiectiv;
	private List<BeanUrmarireEveniment> listEvenimente;
	private Button btnSaveUrmarire;
	private AdapterObiectiveUrmarire adapterObiective;
	private LinearLayout layoutOptiuniClient, layoutOptiuniEveniment;
	private TableLayout layoutSalvareEveniment;
	private RadioGroup radioEvenimentProdus;
	private RadioButton radioEvenimentDa;
	private boolean isEvenimentProdus = false;
	private OperatiiObiective operatiiObiective;
	private ProgressBar progressSaveEveniment;
	private Timer myTimer;
	private int progressVal = 0;
	private Handler saveHandler = new Handler();

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.urmarire_obiective_main, container, false);

		textCurrentPage = (TextView) v.findViewById(R.id.textCurrentPage);
		textCurrentPage.setText("Urmarire obiectiv " + BeanObiectiveGenerale.getInstance().getNumeObiectiv());

		spinnerClienti = (Spinner) v.findViewById(R.id.spinnerClienti);
		AdapterClientiObiective adapter = new AdapterClientiObiective(getActivity(), BeanObiectiveGenerale.getInstance().getListConstructori());
		spinnerClienti.setAdapter(adapter);
		setSpinnerClientiListener();

		listEvenimente = new ArrayList<BeanUrmarireEveniment>();

		adapterObiective = new AdapterObiectiveUrmarire(getActivity(), listEvenimente);

		loadListEvenimente(new ArrayList<BeanUrmarireEveniment>());
		spinnerOptiuniUrmarire = (Spinner) v.findViewById(R.id.spinnerUrmarireObiective);
		spinnerOptiuniUrmarire.setAdapter(adapterObiective);
		setListenerSpinnerUrmarire();

		textDataEveniment = (TextView) v.findViewById(R.id.textDataEveniment);

		btnModificaData = (ImageButton) v.findViewById(R.id.btnModificaData);
		btnModificaData.setVisibility(View.INVISIBLE);
		setCurrentDate(textDataEveniment);
		setListenerBtnModificaData();

		btnModificaObservatii = (ImageButton) v.findViewById(R.id.btnModificaObservatii);
		setListenerModificaObservatii();

		textObservatii = (TextView) v.findViewById(R.id.textObservatii);

		textObservatii.setHint("Adaugati observatii");
		textObservatii.setMovementMethod(new ScrollingMovementMethod());
		setListenerTextObservatii();

		radioEvenimentDa = (RadioButton) v.findViewById(R.id.radioDa);

		radioEvenimentProdus = (RadioGroup) v.findViewById(R.id.radioEveniment);
		setListenerRadioEveniment();

		btnSaveUrmarire = (Button) v.findViewById(R.id.btnSaveUrmarire);
		setListenerBtnSaveUrmarire();

		layoutOptiuniClient = (LinearLayout) v.findViewById(R.id.layoutOptiuniClient);
		layoutOptiuniClient.setVisibility(View.INVISIBLE);

		layoutOptiuniEveniment = (LinearLayout) v.findViewById(R.id.layoutOptiuniEveniment);
		layoutOptiuniEveniment.setVisibility(View.INVISIBLE);

		layoutSalvareEveniment = (TableLayout) v.findViewById(R.id.layoutSalvareEveniment);
		layoutSalvareEveniment.setVisibility(View.INVISIBLE);

		operatiiObiective = new OperatiiObiective(getActivity());
		operatiiObiective.setObiectiveListener(this);

		progressSaveEveniment = (ProgressBar) v.findViewById(R.id.progressSaveUrmarire);
		progressSaveEveniment.setVisibility(View.INVISIBLE);

		return v;
	}

	private void setSpinnerClientiListener() {
		spinnerClienti.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (arg2 == 0) {
					layoutOptiuniClient.setVisibility(View.INVISIBLE);
					layoutOptiuniEveniment.setVisibility(View.INVISIBLE);
				} else {
					layoutOptiuniClient.setVisibility(View.VISIBLE);
					setCurrentDate(textDataEveniment);
					getEvenimenteClient();
				}

			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	private void getEvenimenteClient() {
		BeanObiectiveConstructori client = (BeanObiectiveConstructori) spinnerClienti.getItemAtPosition(spinnerClienti.getSelectedItemPosition() - 1);

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("idObiectiv", BeanObiectiveGenerale.getInstance().getId());
		params.put("codClient", client.getCodClient());
		params.put("codDepart", client.getCodDepart());

		operatiiObiective.getEvenimenteClient(params);
	}

	private void setListenerSpinnerUrmarire() {
		spinnerOptiuniUrmarire.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (arg2 == 0) {
					layoutOptiuniEveniment.setVisibility(View.INVISIBLE);
					layoutSalvareEveniment.setVisibility(View.INVISIBLE);
				} else {
					layoutOptiuniEveniment.setVisibility(View.VISIBLE);
					layoutSalvareEveniment.setVisibility(View.VISIBLE);
					setCurrentDate(textDataEveniment);
					setEvenimentValues(spinnerOptiuniUrmarire.getSelectedItem());
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	private void setEvenimentValues(Object evenimentValues) {
		BeanUrmarireEveniment eveniment = (BeanUrmarireEveniment) evenimentValues;
		if (eveniment.getData().length() > 1) {
			radioEvenimentDa.setChecked(true);
			textDataEveniment.setText(eveniment.getData());

			if (eveniment.getObservatii().trim().length() > 0)
				textObservatii.setText(eveniment.getObservatii());
		} else {
			radioEvenimentDa.setChecked(true);
			setCurrentDate(textDataEveniment);
			textObservatii.setText("");
		}

	}

	private void setListenerRadioEveniment() {
		radioEvenimentProdus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.radioDa)
					isEvenimentProdus = true;
				else
					isEvenimentProdus = false;

			}
		});
	}

	private void setListenerBtnSaveUrmarire() {

		btnSaveUrmarire.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					progressSaveEveniment.setVisibility(View.VISIBLE);
					progressSaveEveniment.setProgress(0);
					progressVal = 0;
					myTimer = new Timer();
					myTimer.schedule(new UpdateProgress(), 40, 15);
					return true;

				case MotionEvent.ACTION_UP:
					if (myTimer != null)
						myTimer.cancel();
					progressSaveEveniment.setVisibility(View.INVISIBLE);
					return true;

				}
				return false;
			}

		});

	}

	class UpdateProgress extends TimerTask {
		public void run() {
			progressVal++;
			if (progressSaveEveniment.getProgress() == 50) {
				saveHandler.post(new Runnable() {
					public void run() {
						prepareParamsSaveEveniment();

					}
				});

				myTimer.cancel();
			} else {
				progressSaveEveniment.setProgress(progressVal);
			}

		}
	}

	private void prepareParamsSaveEveniment() {
		evenimentObiectiv = new BeanUrmarireObiectiv();
		evenimentObiectiv.setIdObiectiv(BeanObiectiveGenerale.getInstance().getId());

		BeanObiectiveConstructori client = (BeanObiectiveConstructori) spinnerClienti.getItemAtPosition(spinnerClienti.getSelectedItemPosition() - 1);

		evenimentObiectiv.setCodClient(client.getCodClient());
		evenimentObiectiv.setCodDepart(client.getCodDepart());
		evenimentObiectiv.setData(textDataEveniment.getText().toString());
		evenimentObiectiv.setObservatii(textObservatii.getText().toString());

		BeanUrmarireEveniment eveniment = (BeanUrmarireEveniment) spinnerOptiuniUrmarire.getSelectedItem();
		evenimentObiectiv.setCodEveniment(eveniment.getIdEveniment());

		saveEveniment();
	}

	private void saveEveniment() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("eveniment", operatiiObiective.serializeEveniment(evenimentObiectiv));
		operatiiObiective.salveazaEvenimentObiectiv(params);

	}

	private void updateEvenimentUrmarit(BeanUrmarireObiectiv obiectivUrmarit) {
		for (BeanUrmarireEveniment linie : listEvenimente) {
			if (linie.getIdEveniment() == obiectivUrmarit.getCodEveniment()) {
				if (isEvenimentProdus) {
					linie.setData(obiectivUrmarit.getData());
					linie.setObservatii(obiectivUrmarit.getObservatii());
				} else {
					linie.setData("");
					linie.setObservatii("");
				}
				break;
			}
		}
	}

	private void setCurrentDate(TextView textData) {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

		String formattedData = String.format("%02d", day) + "." + String.format("%02d", month) + "." + String.format("%02d", year);

		textData.setText(formattedData);
	}

	private void setListenerBtnModificaData() {
		btnModificaData.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDataDialog();

			}
		});
	}

	private void showDataDialog() {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH);
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

		SelectDateDialog dialogDate = new SelectDateDialog(getActivity(), datePickerListener, year, month, day);
		dialogDate.show();
	}

	private void setListenerTextObservatii() {
		textObservatii.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showInputTextDialog();
			}
		});
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			int mYear = selectedYear;
			int mMonth = selectedMonth;
			int mDay = selectedDay;

			SimpleDateFormat displayFormat = new SimpleDateFormat("dd.MM.yyyy");
			Calendar calendar = new GregorianCalendar(mYear, mMonth, mDay);
			textDataEveniment.setText(displayFormat.format(calendar.getTime()));

		}
	};

	private void setListenerModificaObservatii() {
		btnModificaObservatii.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showInputTextDialog();

			}
		});
	}

	private void showInputTextDialog() {
		InputTextDialog dialog = new InputTextDialog(EnumUrmarireObiective.getNumeEveniment(spinnerOptiuniUrmarire.getSelectedItemPosition() - 1),
				textObservatii.getText().toString(), getActivity());
		dialog.setInputTextListener(UrmarireObiective.this);
		dialog.show();
	}

	public static UrmarireObiective newInstance() {
		UrmarireObiective obGen = new UrmarireObiective();
		Bundle bdl = new Bundle();
		obGen.setArguments(bdl);

		return obGen;
	}

	public void textSaved(String textValue) {
		textObservatii.setText(textValue);

	}

	private void updateSavedData(String result) {
		if (result.equals("0")) {
			updateEvenimentUrmarit(evenimentObiectiv);
			adapterObiective.notifyDataSetChanged();
			textObservatii.setText("");
			Toast.makeText(getActivity(), "Date salvate.", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getActivity(), "Eroare salvare date.", Toast.LENGTH_SHORT).show();
		}

	}

	private void loadListEvenimente(List<BeanUrmarireEveniment> evenimente) {

		listEvenimente.clear();
		BeanUrmarireEveniment obiectiv = null;

		obiectiv = new BeanUrmarireEveniment();
		obiectiv.setIdEveniment(-1);
		obiectiv.setNumeEveniment("Selectati un eveniment");
		obiectiv.setData("");
		listEvenimente.add(obiectiv);

		for (EnumUrmarireObiective nume : EnumUrmarireObiective.values()) {
			obiectiv = new BeanUrmarireEveniment();
			obiectiv.setIdEveniment(nume.getCod());
			obiectiv.setNumeEveniment(nume.getNume());

			obiectiv.setData("");
			obiectiv.setObservatii("");

			for (BeanUrmarireEveniment ev : evenimente) {
				if (ev.getIdEveniment() == nume.getCod()) {
					obiectiv.setData(ev.getData());
					obiectiv.setObservatii(ev.getObservatii());
				}
			}

			listEvenimente.add(obiectiv);

		}

		if (spinnerOptiuniUrmarire != null)
			spinnerOptiuniUrmarire.setSelection(0);

	}

	public void operationObiectivComplete(EnumOperatiiObiective numeComanda, Object result) {
		switch (numeComanda) {
		case SALVEAZA_EVENIMENT:
			updateSavedData((String) result);
			break;
		case GET_EVENIMENTE_CLIENT:
			loadListEvenimente(operatiiObiective.deserializeListEvenimente((String) result));
			break;
		default:
			break;
		}

	}

}
