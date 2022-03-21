/**
 * @author florinb
 *
 */
package my.logon.screen.screens;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import my.logon.screen.R;
import my.logon.screen.listeners.AgentSelection;
import my.logon.screen.listeners.AgentSelectionListener;
import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.listeners.FilialaSelection;
import my.logon.screen.listeners.FilialaSelectionListener;
import my.logon.screen.model.UserInfo;

public class SelectAgentClientiSemiactivi extends Fragment implements AsyncTaskListener, AgentSelectionListener,
		FilialaSelectionListener {

	Spinner spinnerFiliale, spinnerAgenti;

	String[] numeFiliale = { "Andronache", "Bacau", "Baia-Mare", "Brasov", "Constanta", "Cluj", "Craiova", "Focsani",
			"Galati", "Glina", "Iasi", "Militari", "Oradea", "Otopeni", "Piatra-Neamt", "Pitesti", "Ploiesti",
			"Timisoara", "Tg. Mures" };

	String[] codFiliale = { "BU13", "BC10", "MM10", "BV10", "CT10", "CJ10", "DJ10", "VN10", "GL10", "BU10", "IS10",
			"BU11", "BH10", "BU12", "NT10", "AG10", "PH10", "TM10", "MS10" };

	private static ArrayList<HashMap<String, String>> listFiliale = new ArrayList<HashMap<String, String>>();
	public SimpleAdapter adapterFiliale, adapterAgenti;
	private static ArrayList<HashMap<String, String>> listAgenti = new ArrayList<HashMap<String, String>>();

	String localFiliala = "-1";
	TextView textAgentiVanzAg;

	private AgentSelection agentSelected = new AgentSelection();
	private FilialaSelection filialaSelected = new FilialaSelection();

	public static final SelectAgentClientiSemiactivi newInstance() {

		SelectAgentClientiSemiactivi f = new SelectAgentClientiSemiactivi();
		Bundle bdl = new Bundle(1);
		f.setArguments(bdl);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.select_agent_cl_inactivi, container, false);

		try {

			spinnerFiliale = (Spinner) v.findViewById(R.id.spinnerFilialaVanzAg);
			// spinnerFiliale.setVisibility(View.INVISIBLE);

			adapterAgenti = new SimpleAdapter(getActivity(), listAgenti, R.layout.rowlayoutagenti, new String[] {
					"numeAgent", "codAgent" }, new int[] { R.id.textNumeAgent, R.id.textCodAgent });
			spinnerAgenti = (Spinner) v.findViewById(R.id.spinnerAgentiVanzAg);

			textAgentiVanzAg = (TextView) v.findViewById(R.id.textAgentiVanzAg);
			textAgentiVanzAg.setVisibility(View.VISIBLE);

			agentSelected.setAgentListener(this);
			filialaSelected.setFilialaListener(this);

			if (UserInfo.getInstance().getTipAcces().equals("9") || UserInfo.getInstance().getTipAcces().equals("27")
					|| UserInfo.getInstance().getTipAcces().equals("17")) // ag,
			// ka,
			// cv
			{
				textAgentiVanzAg.setText(UserInfo.getInstance().getNume() + ", " + UserInfo.getInstance().getCod());
				spinnerAgenti.setVisibility(View.GONE);
				spinnerFiliale.setVisibility(View.GONE);
			} else {
				if (UserInfo.getInstance().getTipAcces().equals("10")
						|| UserInfo.getInstance().getTipAcces().equals("18")) // sd,
				// sm
				{
					spinnerAgenti.setVisibility(View.VISIBLE);
					spinnerFiliale.setVisibility(View.GONE);
					ClientiSemiactivi.selectedFiliala = UserInfo.getInstance().getUnitLog();
					performGetAgenti();
				} else if (UserInfo.getInstance().getTipAcces().equals("12") // dv,
																				// dd
						|| UserInfo.getInstance().getTipAcces().equals("14")) {
					spinnerAgenti.setVisibility(View.VISIBLE);
					spinnerFiliale.setVisibility(View.VISIBLE);
					populateListFiliale();
				}

			}

		} catch (Exception e) {
			Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
		}

		return v;

	}

	private void populateListFiliale() {

		spinnerFiliale.setOnItemSelectedListener(filialaSelected);

		listFiliale = new ArrayList<HashMap<String, String>>();
		adapterFiliale = new SimpleAdapter(getActivity(), listFiliale, R.layout.rowlayoutagenti, new String[] {
				"numeFiliala", "codFiliala" }, new int[] { R.id.textNumeAgent, R.id.textCodAgent });

		HashMap<String, String> temp;
		temp = new HashMap<String, String>();
		temp.put("numeFiliala", "Filiala");
		temp.put("codFiliala", " ");
		listFiliale.add(temp);

		int selectedJudIndex = -1;
		for (int i = 0; i < numeFiliale.length; i++) {
			temp = new HashMap<String, String>();

			temp.put("numeFiliala", numeFiliale[i]);
			temp.put("codFiliala", codFiliale[i]);

			if (codFiliale[i].equals(ClientiSemiactivi.selectedFiliala)) {
				selectedJudIndex = i;
			}

			listFiliale.add(temp);
		}

		spinnerFiliale.setAdapter(adapterFiliale);

		if (selectedJudIndex != -1)
			spinnerFiliale.setSelection(selectedJudIndex + 1);

	}

	public class OnSelectFiliala implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {

			String[] tokenFiliala = spinnerFiliale.getSelectedItem().toString().split(",");
			String filNr = tokenFiliala[1].substring(tokenFiliala[1].indexOf('=') + 1, tokenFiliala[1].length() - 1);

			if (filNr.trim().equals(""))
				filNr = "-1"; // fara selectie
			if (filNr.equals("00000000"))
				filNr = "0";

			localFiliala = filNr;
			ClientiSemiactivi.selectedFiliala = filNr;

			listAgenti.clear();
			adapterAgenti.notifyDataSetChanged();

			if (!filNr.equals("-1")) {
				performGetAgenti();

			}

		}

		public void onNothingSelected(AdapterView<?> parent) {
			// TODO
		}
	}

	private void performGetAgenti() {
		try {

			HashMap<String, String> params = new HashMap<String, String>();

			String localDepart = UserInfo.getInstance().getCodDepart();

			// director, keyacc
			if (UserInfo.getInstance().getTipAcces().equals("35")) {
				localDepart = "10";
			}

			// sef magazin
			if (UserInfo.getInstance().getTipAcces().equals("18")) {
				localDepart = "11";
			}

			params.put("filiala", ClientiSemiactivi.selectedFiliala);
			params.put("depart", localDepart);

			AsyncTaskListener contextListener = (AsyncTaskListener) SelectAgentClientiSemiactivi.this;
			AsyncTaskWSCall call = new AsyncTaskWSCall(getActivity(), contextListener, "getListAgenti", params);
			call.getCallResultsFromFragment();

		} catch (Exception e) {
			Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	protected void populateAgentiList(String agentiList) {

		if (!agentiList.equals("-1") && agentiList.length() > 0) {

			listAgenti.clear();

			spinnerAgenti.setOnItemSelectedListener(agentSelected);

			HashMap<String, String> temp;
			String[] tokenLinie = agentiList.split("@@");
			String[] tokenClient;
			String client = "";

			temp = new HashMap<String, String>();
			temp.put("numeAgent", "Agent");
			temp.put("codAgent", " ");
			listAgenti.add(temp);

			int selectedAgentIndex = -1;
			for (int i = 0; i < tokenLinie.length; i++) {
				temp = new HashMap<String, String>();
				client = tokenLinie[i];
				tokenClient = client.split("#");

				temp.put("numeAgent", tokenClient[0]);
				temp.put("codAgent", tokenClient[1]);

				if (tokenClient[1].equals(ClientiSemiactivi.selectedAgent)) {
					selectedAgentIndex = i;
				}

				listAgenti.add(temp);
			}

			spinnerAgenti.setAdapter(adapterAgenti);

			if (ClientiSemiactivi.selectedAgent.equals("0")) {
				spinnerAgenti.setSelection(1);
			} else {
				if (selectedAgentIndex != -1) {
					spinnerAgenti.setSelection(selectedAgentIndex + 1);
				}
			}

		} else {
			Toast.makeText(getActivity(), "Nu exista agenti!", Toast.LENGTH_SHORT).show();
		}

	}

	public void onTaskComplete(String methodName, Object result) {
		if (methodName.equals("getListAgenti")) {
			populateAgentiList((String)result);
		}

	}

	public void selectedAgent(String agentCod) {
		ClientiSemiactivi.selectedAgent = agentCod;
	}

	public void selectedFiliala(String filialaCod) {

		ClientiSemiactivi.selectedFiliala = filialaCod;
		listAgenti.clear();
		adapterAgenti.notifyDataSetChanged();

		if (!filialaCod.equals("-1")) {
			performGetAgenti();

		}

	}

}
