package my.logon.screen.screens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import my.logon.screen.listeners.CautaClientDialogListener;
import my.logon.screen.listeners.ObiectiveListener;
import my.logon.screen.model.OperatiiObiective;
import my.logon.screen.model.ValidareObiective;
import my.logon.screen.adapters.AdapterObiectiveConstructori;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import my.logon.screen.beans.BeanClient;
import my.logon.screen.beans.BeanObiectiveConstructori;
import my.logon.screen.beans.BeanObiectiveGenerale;
import my.logon.screen.beans.BeanStadiuObiectiv;
import my.logon.screen.dialogs.CautaClientDialog;
import my.logon.screen.enums.EnumDepartFinisaje;
import my.logon.screen.enums.EnumDepartInterioare;
import my.logon.screen.enums.EnumOperatiiObiective;
import my.logon.screen.R;

public class CreareObiectiveFundatie extends Fragment implements ObiectiveListener, CautaClientDialogListener {

	private TextView textCurrentPage;
	private ListView listObiectiveFundatie;
	private Spinner spinnerFinisaj, spinnerInterior;
	private RadioButton radioFundatie, radioFinisaje;
	private RadioGroup radioGroup;
	private AdapterObiectiveConstructori adapter;
	private Button btnSaveObiective;
	private List<BeanObiectiveConstructori> listConstructori;
	private String selectedDepartament = "00";
	private ImageButton buttonAdaugaClient;
	private ProgressBar progressSaveObiective;
	private Timer myTimer;
	private int progressVal = 0;
	private Handler saveHandler = new Handler();
	private CheckBox checkInchideOb;
	private OperatiiObiective operatiiObiective;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.obiective_fundatie_main, container, false);

		textCurrentPage = (TextView) v.findViewById(R.id.textCurrentPage);
		textCurrentPage.setText("CONSTRUCTORI SI SUBANTREPRENORI");

		btnSaveObiective = (Button) v.findViewById(R.id.btnSaveObiective);
		setBtnSaveListener();

		listObiectiveFundatie = (ListView) v.findViewById(R.id.listObiectiveFundatie);
		listConstructori = BeanObiectiveGenerale.getInstance().getListConstructori();

		adapter = new AdapterObiectiveConstructori(listConstructori, getActivity());
		listObiectiveFundatie.setAdapter(adapter);
		listObiectiveFundatie.setVisibility(View.INVISIBLE);

		buttonAdaugaClient = (ImageButton) v.findViewById(R.id.buttonAdaugaClient);
		setBtnAdaugaClientListener();

		spinnerFinisaj = (Spinner) v.findViewById(R.id.spinnerFinisaj);
		spinnerInterior = (Spinner) v.findViewById(R.id.spinnerInterior);
		setSpinnersVisibility(false);

		radioGroup = (RadioGroup) v.findViewById(R.id.radioConstructori);
		radioFundatie = (RadioButton) v.findViewById(R.id.radioFundatie);
		radioFinisaje = (RadioButton) v.findViewById(R.id.radioFinisaje);

		checkInchideOb = (CheckBox) v.findViewById(R.id.checkInchideOb);
		setListenerInchideOb();

		setRadioFundatieListener();
		setRadioFinisajeListener();

		setSpinnerFinisajeListener();
		setSpinnerInterioareListener();

		progressSaveObiective = (ProgressBar) v.findViewById(R.id.progressSaveObiective);
		progressSaveObiective.setVisibility(View.INVISIBLE);

		operatiiObiective = new OperatiiObiective(getActivity());
		operatiiObiective.setObiectiveListener(this);

		return v;
	}

	private void setBtnSaveListener() {

		btnSaveObiective.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:

					if (!isObiectiveValid())
						return true;

					progressSaveObiective.setVisibility(View.VISIBLE);
					progressSaveObiective.setProgress(0);
					progressVal = 0;
					myTimer = new Timer();
					myTimer.schedule(new UpdateProgress(), 40, 15);
					return true;

				case MotionEvent.ACTION_UP:
					if (myTimer != null)
						myTimer.cancel();
					progressSaveObiective.setVisibility(View.INVISIBLE);
					return true;

				}
				return false;
			}

		});

	}

	private void setListenerInchideOb() {
		checkInchideOb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				BeanObiectiveGenerale.getInstance().setInchis(checkInchideOb.isChecked());
			}
		});
	}

	private boolean isObiectiveValid() {
		return new ValidareObiective().valideaza(getActivity());
	}

	private void saveObiective() {
		OperatiiObiective opObiective = new OperatiiObiective(getActivity());
		opObiective.setObiectiveListener(CreareObiectiveFundatie.this);

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("dateObiective", opObiective.serializeObiectiv(BeanObiectiveGenerale.getInstance()));
		opObiective.saveObiectiv(params);
	}

	class UpdateProgress extends TimerTask {
		public void run() {
			progressVal++;
			if (progressSaveObiective.getProgress() == 50) {
				saveHandler.post(new Runnable() {
					public void run() {
						saveObiective();

					}
				});

				myTimer.cancel();
			} else {
				progressSaveObiective.setProgress(progressVal);
			}

		}
	}

	private void setBtnAdaugaClientListener() {
		buttonAdaugaClient.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				CautaClientDialog dialogClient = new CautaClientDialog(getActivity());
				dialogClient.setClientSelectedListener(CreareObiectiveFundatie.this);
				dialogClient.setClientObiectivKA(true);
				dialogClient.show();

			}
		});
	}

	private void setSpinnersVisibility(boolean isVisible) {
		if (isVisible) {
			spinnerInterior.setVisibility(View.VISIBLE);
			spinnerFinisaj.setVisibility(View.VISIBLE);
			buttonAdaugaClient.setVisibility(View.VISIBLE);
		} else {
			spinnerInterior.setVisibility(View.INVISIBLE);
			spinnerFinisaj.setVisibility(View.INVISIBLE);
			buttonAdaugaClient.setVisibility(View.INVISIBLE);
		}
	}

	private void setRadioButtonsVisibility() {

		List<BeanStadiuObiectiv> listStadii = BeanObiectiveGenerale.getInstance().getStadiiDepart();

		boolean isFundatieVisible = false;
		boolean isFinisajeVisible = false;

		for (BeanStadiuObiectiv stadiu : listStadii) {
			if (stadiu.getCodDepart().equals("04") && (stadiu.getCodStadiu() == 1 || stadiu.getCodStadiu() == 2)) {
				isFundatieVisible = true;
				break;
			}
		}

		for (BeanStadiuObiectiv stadiu : listStadii) {
			if (!stadiu.getCodDepart().equals("04") && (stadiu.getCodStadiu() == 1 || stadiu.getCodStadiu() == 2)) {
				isFinisajeVisible = true;
				break;
			}
		}

		if (isFundatieVisible || isFinisajeVisible) {
			radioGroup.setVisibility(View.VISIBLE);

			if (!isFundatieVisible)
				radioFundatie.setEnabled(false);
			else {
				radioFundatie.setEnabled(true);
				radioFundatie.setChecked(true);
				radioFundatie.performClick();
			}

			if (!isFinisajeVisible)
				radioFinisaje.setEnabled(false);
			else {
				radioFinisaje.setEnabled(true);

				if (!isFundatieVisible) {
					radioFinisaje.setChecked(true);
					radioFinisaje.performClick();
				}
			}

			listObiectiveFundatie.setVisibility(View.VISIBLE);

		} else {
			radioGroup.setVisibility(View.INVISIBLE);
			setSpinnersVisibility(false);
			listObiectiveFundatie.setVisibility(View.INVISIBLE);
		}

	}

	private void addSpinnerFinisajValues() {
		List<String> finisajValues = new ArrayList<String>();

		List<BeanStadiuObiectiv> listStadii = BeanObiectiveGenerale.getInstance().getStadiiDepart();

		for (EnumDepartFinisaje departFin : EnumDepartFinisaje.values()) {
			for (BeanStadiuObiectiv stadiu : listStadii) {
				if (stadiu.getCodDepart().equals(departFin.getCodDepart()) && (stadiu.getCodStadiu() == 1 || stadiu.getCodStadiu() == 2)) {
					finisajValues.add(departFin.name());
					break;
				}

			}

		}

		for (BeanStadiuObiectiv stadiu : listStadii) {
			if (stadiu.getCodDepart().equals("03") && (stadiu.getCodStadiu() == 1 || stadiu.getCodStadiu() == 2)) {
				finisajValues.add(EnumDepartFinisaje.Interioare.name());
				break;
			}

		}

		if (finisajValues.size() == 0) {
			spinnerFinisaj.setVisibility(View.INVISIBLE);
			return;
		}

		spinnerFinisaj.setVisibility(View.VISIBLE);

		ArrayAdapter<String> finisajAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, finisajValues);

		finisajAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinnerFinisaj.setAdapter(finisajAdapter);

	}

	public void setupScreen() {
		setRadioButtonsVisibility();

	}

	private void setRadioFundatieListener() {
		radioFundatie.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (radioFundatie.isChecked()) {
					setSpinnersVisibility(false);
					adapter.setCodDepart("04");
					selectedDepartament = "04";
					buttonAdaugaClient.setVisibility(View.VISIBLE);
				}

			}
		});
	}

	private void setRadioFinisajeListener() {
		radioFinisaje.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (radioFinisaje.isChecked()) {
					addSpinnerFinisajValues();
					buttonAdaugaClient.setVisibility(View.VISIBLE);
				}

			}
		});
	}

	private void addSpinnerInterioareValues() {
		List<String> interioareValues = new ArrayList<String>();

		for (EnumDepartInterioare depInt : EnumDepartInterioare.values()) {
			interioareValues.add(depInt.name());
		}

		ArrayAdapter<String> interioareAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, interioareValues);

		interioareAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerInterior.setAdapter(interioareAdapter);

	}

	private void setSpinnerFinisajeListener() {
		spinnerFinisaj.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				if (spinnerFinisaj.getSelectedItem().toString().equals("Interioare")) {
					dealWithFinisaje();
				} else {
					spinnerInterior.setVisibility(View.INVISIBLE);
					EnumDepartFinisaje enumDepartFin = EnumDepartFinisaje.valueOf(spinnerFinisaj.getSelectedItem().toString());
					adapter.setCodDepart(enumDepartFin.getCodDepart());
					selectedDepartament = enumDepartFin.getCodDepart();

				}

			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	private void dealWithFinisaje() {
		addSpinnerInterioareValues();
		spinnerInterior.setVisibility(View.VISIBLE);
		spinnerInterior.setSelection(0);
		adapter.setCodDepart(null);
	}

	private void setSpinnerInterioareListener() {
		spinnerInterior.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				if (spinnerFinisaj.getSelectedItem() != null && spinnerFinisaj.getSelectedItem().toString().equalsIgnoreCase("interioare")) {
					EnumDepartInterioare enumInterior = EnumDepartInterioare.valueOf(spinnerInterior.getSelectedItem().toString());
					adapter.setCodDepart(enumInterior.getCodDepart());
					selectedDepartament = enumInterior.getCodDepart();
				}

			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	public static CreareObiectiveFundatie newInstance() {
		CreareObiectiveFundatie obConstr = new CreareObiectiveFundatie();
		Bundle bdl = new Bundle();
		obConstr.setArguments(bdl);
		return obConstr;
	}

	public void operationObiectivComplete(EnumOperatiiObiective numeComanda, Object result) {

		switch (numeComanda) {
		case SALVEAZA_OBIECTIV:
			if (((String) result).equals("0")) {
				Toast.makeText(getActivity(), "Date salvate cu succes.", Toast.LENGTH_SHORT).show();
				clearInputData();
			} else
				Toast.makeText(getActivity(), "Datele nu au fost salvate.", Toast.LENGTH_SHORT).show();
			break;
		case GET_STARE_CLIENT:
			setStareClient(operatiiObiective.deserializeStareConstructor((String) result));
			break;
		default:
			break;
		}

		System.out.println((String) result);

	}

	private void clearInputData() {
		BeanObiectiveGenerale.getInstance().clearInstanceData();
		listConstructori.clear();
		adapter.notifyDataSetChanged();
	}

	private void getStareClient(BeanClient client) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("codClient", client.getCodClient());
		params.put("codDepartament", selectedDepartament);
		operatiiObiective.getStareClient(params);
	}

	private void setStareClient(BeanObiectiveConstructori client) {

		Iterator<BeanObiectiveConstructori> iterator = listConstructori.iterator();

		BeanObiectiveConstructori constructor;
		while (iterator.hasNext()) {
			constructor = iterator.next();
			if (constructor.equals(client)) {
				constructor.setStare(client.getStare());
				break;
			}

		}

		adapter.notifyDataSetChanged();

	}

	private void updateClientsList(BeanClient client) {
		BeanObiectiveConstructori constructor = new BeanObiectiveConstructori();
		constructor.setCodClient(client.getCodClient());
		constructor.setNumeClient(client.getNumeClient());
		constructor.setCodDepart(selectedDepartament);
		addToConstructoriList(constructor);
		adapter.notifyDataSetChanged();
		getStareClient(client);

	}

	private void addToConstructoriList(BeanObiectiveConstructori constructor) {

		int indexPos = 0;

		if (listConstructori.contains(constructor)) {
			indexPos = listConstructori.indexOf(constructor);
			listConstructori.remove(constructor);
		}

		if (indexPos == 0)
			listConstructori.add(0, constructor);
		else
			listConstructori.add(indexPos, constructor);
	}

	public void clientSelected(BeanClient client) {
		updateClientsList(client);

	}

}
