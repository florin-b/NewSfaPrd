package my.logon.screen.screens;

import java.util.ArrayList;
import java.util.HashMap;

import my.logon.screen.listeners.AdreseGPSListener;
import my.logon.screen.listeners.CustomSpinnerClass;
import my.logon.screen.listeners.CustomSpinnerListener;
import my.logon.screen.listeners.OperatiiAgentListener;
import my.logon.screen.model.AdreseGPS;
import my.logon.screen.model.OperatiiAgent;
import my.logon.screen.model.OperatiiFiliala;
import my.logon.screen.model.UserInfo;
import my.logon.screen.R;
import my.logon.screen.utils.UtilsGeneral;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import my.logon.screen.enums.EnumTipAlert;

public class AdresaGpsClientiActivity extends Fragment implements CustomSpinnerListener, OperatiiAgentListener,
		AdreseGPSListener {

	private Spinner spinnerFiliala, spinnerAgenti;
	private ListView listViewAdrese;
	public SimpleAdapter adapterFiliale, adapterAgenti, adapterAdrese;
	private String selectedFiliala = "";
	private TextView textAgent;

	private CustomSpinnerClass spinnerListener = new CustomSpinnerClass();
	private String selectedAgent = "";
	private RadioButton radioNec, radioCom;
	private String tipAdresa = "1";
	private View v;
	private OperatiiAgent agent;
	private AdreseGPS adreseGPS;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		v = inflater.inflate(R.layout.selectii_adrese, container, false);

		spinnerListener.setListener(this);

		agent = OperatiiAgent.getInstance();
		agent.setOperatiiAgentListener(this);

		adreseGPS = AdreseGPS.getInstance();
		adreseGPS.setAdreseGPSListener(this);

		addLayoutComponents();
		return v;

	}

	private void addLayoutComponents() {

		spinnerFiliala = (Spinner) v.findViewById(R.id.spinnerFilialaAdrese);
		populateSpinnerFiliale();

		spinnerAgenti = (Spinner) v.findViewById(R.id.spinnerAgentiAdrese);

		textAgent = (TextView) v.findViewById(R.id.textAgent);

		if (UserInfo.getInstance().getTipAcces().equals("12") || UserInfo.getInstance().getTipAcces().equals("14")
				|| UserInfo.getInstance().getTipAcces().equals("35")) {
			spinnerFiliala.setVisibility(View.VISIBLE);
			spinnerAgenti.setVisibility(View.INVISIBLE);
			textAgent.setVisibility(View.INVISIBLE);
		} else if (UserInfo.getInstance().getTipAcces().equals("10")) {
			spinnerFiliala.setVisibility(View.GONE);
			spinnerAgenti.setVisibility(View.VISIBLE);
			selectedFiliala = UserInfo.getInstance().getUnitLog();
			performGetAgenti();
			textAgent.setVisibility(View.INVISIBLE);
		} else if (UserInfo.getInstance().getTipAcces().equals("9")
				|| UserInfo.getInstance().getTipAcces().equals("27")) {
			spinnerFiliala.setVisibility(View.GONE);
			spinnerAgenti.setVisibility(View.GONE);
			textAgent.setVisibility(View.VISIBLE);
			textAgent.setText(UserInfo.getInstance().getNume());
			selectedAgent = UserInfo.getInstance().getCod();
			getListaAdreseGps(selectedAgent, tipAdresa);
		}

		listViewAdrese = (ListView) v.findViewById(R.id.listAdrese);
		listViewAdrese.setVisibility(View.INVISIBLE);

		radioNec = (RadioButton) v.findViewById(R.id.radioNecompletate);
		radioNec.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (radioNec.isChecked())
					tipAdresa = "1";

				if (!selectedAgent.equals("0")) {
					getListaAdreseGps(selectedAgent, tipAdresa);
				}
			}
		});

		radioCom = (RadioButton) v.findViewById(R.id.radioCompletate);
		radioCom.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (radioCom.isChecked())
					tipAdresa = "2";

				if (!selectedAgent.equals("0")) {
					getListaAdreseGps(selectedAgent, tipAdresa);
				}
			}
		});

	}

	private void populateSpinnerFiliale() {

		OperatiiFiliala filiala = OperatiiFiliala.getInstance();
		adapterFiliale = new SimpleAdapter(getActivity(), filiala.getListToateFiliale(), R.layout.rowlayoutagenti,
				new String[] { "numeFiliala", "codFiliala" }, new int[] { R.id.textNumeAgent, R.id.textCodAgent });

		spinnerFiliala.setOnItemSelectedListener(spinnerListener);
		spinnerFiliala.setAdapter(adapterFiliale);

	}

	private void getListaAdreseGps(String codAgent, String tipAdresa) {

		String localFiliala = UserInfo.getInstance().getUnitLog();

		if (selectedAgent.equals("00000000")) {
			localFiliala = selectedFiliala;
		}

		adreseGPS.getListAdreseGPS(selectedAgent, tipAdresa, localFiliala, UserInfo.getInstance().getCodDepart(),
				getActivity());

	}

	public void onSelectedSpinnerItem(int spinnerId, HashMap<String, String> map, int position) {
		if (spinnerId == R.id.spinnerFilialaAdrese) {
			String filNr = map.get("codFiliala");
			if (filNr.trim().equals(""))
				filNr = "-1";
			if (filNr.equals("00000000"))
				filNr = "0";
			selectedFiliala = filNr;
			if (!filNr.equals("-1")) {
				performGetAgenti();

			}
		} else if (spinnerId == R.id.spinnerAgentiAdrese) {
			String agentNr = map.get("codAgent");
			if (agentNr.trim().equals(""))
				agentNr = "0";
			selectedAgent = agentNr;
			if (!selectedAgent.equals("0")) {
				getListaAdreseGps(selectedAgent, tipAdresa);
			}
		}

	}

	private void performGetAgenti() {

		String localDepart = UserInfo.getInstance().getCodDepart();

		// dka
		if (UserInfo.getInstance().getTipAcces().equals("35")) {
			localDepart = "10";
		}

		// sm
		if (UserInfo.getInstance().getTipAcces().equals("18")) {
			localDepart = "11";
		}

		agent.getListaAgenti(selectedFiliala, localDepart, getActivity(), true, null);

	}

	private void fillListAgenti(ArrayList<HashMap<String, String>> listAgenti) {

		adapterAgenti = new SimpleAdapter(getActivity(), listAgenti, R.layout.rowlayoutagenti, new String[] {
				"numeAgent", "codAgent" }, new int[] { R.id.textNumeAgent, R.id.textCodAgent });
		spinnerAgenti.setAdapter(adapterAgenti);

		if (listAgenti.size() > 0) {
			spinnerAgenti.setVisibility(View.VISIBLE);
			spinnerAgenti.setOnItemSelectedListener(spinnerListener);
		} else {
			UtilsGeneral.showCustomToast(getActivity(), "Nu exista inregistrari", EnumTipAlert.Info);
		}

	}

	private void fillListAdrese(ArrayList<HashMap<String, String>> listAdrese) {

		adapterAdrese = new SimpleAdapter(getActivity(), listAdrese, R.layout.customrownumeclient_gps, new String[] {
				"numeClient", "codClient", "tipClient" }, new int[] { R.id.textNumeClient, R.id.textCodClient,
				R.id.textTipClient });
		listViewAdrese.setAdapter(adapterAdrese);

		if (listAdrese.size() > 0) {
			listViewAdrese.setVisibility(View.VISIBLE);

			int nrRec = listAdrese.size();

			if (nrRec == 1) {
				UtilsGeneral.showCustomToast(getActivity(), "1 inregistrare", EnumTipAlert.Info);
			} else {
				UtilsGeneral.showCustomToast(getActivity(), String.valueOf(nrRec) + " inregistrari", EnumTipAlert.Info);
			}

		} else {
			UtilsGeneral.showCustomToast(getActivity(), "Nu exista inregistrari", EnumTipAlert.Info);
		}

	}

	public void opAgentComplete(ArrayList<HashMap<String, String>> listAgenti) {
		fillListAgenti(listAgenti);

	}

	public void opAdreseGPSComplete(ArrayList<HashMap<String, String>> listAdrese) {
		fillListAdrese(listAdrese);

	}

}
