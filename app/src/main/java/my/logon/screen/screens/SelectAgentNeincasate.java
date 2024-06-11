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
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import my.logon.screen.R;
import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.listeners.CustomSpinnerClass;
import my.logon.screen.listeners.CustomSpinnerListener;
import my.logon.screen.model.UserInfo;
import my.logon.screen.utils.UtilsGeneral;

public class SelectAgentNeincasate extends Fragment implements CustomSpinnerListener, AsyncTaskListener {

	Spinner spinnerFiliale, spinnerAgenti;

	private static ArrayList<HashMap<String, String>> listFiliale = new ArrayList<HashMap<String, String>>();
	public SimpleAdapter adapterFiliale, adapterAgenti;
	private static ArrayList<HashMap<String, String>> listAgenti = new ArrayList<HashMap<String, String>>();

	String localFiliala = "-1";
	TextView textAgentiVanzAg;

	private CustomSpinnerClass spinnerListener = new CustomSpinnerClass();

	public static final SelectAgentNeincasate newInstance() {

		SelectAgentNeincasate f = new SelectAgentNeincasate();
		Bundle bdl = new Bundle(1);
		f.setArguments(bdl);

		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.select_agent_vanz_ag, container, false);

		spinnerListener.setListener(this);

		try {

			spinnerFiliale = (Spinner) v.findViewById(R.id.spinnerFilialaVanzAg);

			adapterAgenti = new SimpleAdapter(getActivity(), listAgenti, R.layout.rowlayoutagenti, new String[] { "numeAgent", "codAgent" },
					new int[] { R.id.textNumeAgent, R.id.textCodAgent });
			spinnerAgenti = (Spinner) v.findViewById(R.id.spinnerAgentiVanzAg);

			textAgentiVanzAg = (TextView) v.findViewById(R.id.textAgentiVanzAg);
			textAgentiVanzAg.setVisibility(View.VISIBLE);

			if (UserInfo.getInstance().getTipAcces().equals("9") || UserInfo.getInstance().getTipAcces().equals("27")
					|| UserInfo.getInstance().getTipAcces().equals("17") || UserInfo.getInstance().getTipAcces().equals("41")) // ag,
			// ka,
			// cv
			{
				textAgentiVanzAg.setText(UserInfo.getInstance().getNume() + ", " + UserInfo.getInstance().getCod());
				spinnerAgenti.setVisibility(View.GONE);
				spinnerFiliale.setVisibility(View.GONE);
			} else {
				if (UserInfo.getInstance().getTipAcces().equals("10") || UserInfo.getInstance().getTipAcces().equals("18")
						|| UserInfo.getInstance().getTipAcces().equals("32") || UserInfo.getInstance().getTipAcces().equals("39")) // sd,
				// sm
				{
					spinnerAgenti.setVisibility(View.VISIBLE);
					spinnerFiliale.setVisibility(View.GONE);
					Neincasate.selectedFiliala = UserInfo.getInstance().getUnitLog();
					performGetAgenti();
				} else {
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

		spinnerFiliale.setOnItemSelectedListener(spinnerListener);

		listFiliale = new ArrayList<HashMap<String, String>>();
		adapterFiliale = new SimpleAdapter(getActivity(), listFiliale, R.layout.rowlayoutagenti, new String[] { "numeFiliala", "codFiliala" },
				new int[] { R.id.textNumeAgent, R.id.textCodAgent });

		HashMap<String, String> temp;
		temp = new HashMap<String, String>();
		temp.put("numeFiliala", "Filiala");
		temp.put("codFiliala", " ");
		listFiliale.add(temp);

		int selectedJudIndex = -1;
		for (int i = 0; i < UtilsGeneral.numeFiliale.length; i++) {
			temp = new HashMap<String, String>();

			temp.put("numeFiliala", UtilsGeneral.numeFiliale[i]);
			temp.put("codFiliala", UtilsGeneral.codFiliale[i]);

			if (UtilsGeneral.codFiliale[i].equals(Neincasate.selectedFiliala)) {
				selectedJudIndex = i;
			}

			listFiliale.add(temp);
		}

		spinnerFiliale.setAdapter(adapterFiliale);

		if (selectedJudIndex != -1)
			spinnerFiliale.setSelection(selectedJudIndex + 1);

	}

	private void performGetAgenti() {
		try {

			HashMap<String, String> params = new HashMap<String, String>();

			String localDepart = UserInfo.getInstance().getCodDepart();

			if (UserInfo.getInstance().getTipAcces().equals("35") || UserInfo.getInstance().getTipAcces().equals("32")) // director
																	// keyacc
			{
				localDepart = "10";
			}

			if (UserInfo.getInstance().getTipAcces().equals("18") || UserInfo.getInstance().getTipAcces().equals("39")) // sef
																	// magazin
			{
				localDepart = "11";
			}
			
			if (UserInfo.getInstance().getCod().equals("00010281") || UserInfo.getInstance().getCod().equals("00018768")
					|| UserInfo.getInstance().getCod().equals("00086469"))
				localDepart = "11";
			
			String filialaNeincasate = Neincasate.selectedFiliala;

			if (UserInfo.getInstance().getTipUserSap().equals("SDIP"))
				filialaNeincasate = UserInfo.getInstance().getInitUnitLog();

			params.put("filiala", filialaNeincasate);
			params.put("depart", localDepart);
			params.put("tipUserSap", UserInfo.getInstance().getTipUserSap());
			params.put("codAgent", UserInfo.getInstance().getCod());

			AsyncTaskListener contextListener = (AsyncTaskListener) SelectAgentNeincasate.this;
			AsyncTaskWSCall call = new AsyncTaskWSCall(getActivity(), contextListener, "getListAgenti", params);
			call.getCallResultsFromFragment();

		} catch (Exception e) {
			Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	protected void populateAgentiList(String agentiList) {

		if (!agentiList.equals("-1") && agentiList.length() > 0) {

			listAgenti.clear();

			spinnerAgenti.setOnItemSelectedListener(spinnerListener);

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

				if (tokenClient[1].equals(Neincasate.selectedAgent)) {
					selectedAgentIndex = i;
				}

				listAgenti.add(temp);
			}

			spinnerAgenti.setAdapter(adapterAgenti);

			if (selectedAgentIndex != -1) {
				spinnerAgenti.setSelection(selectedAgentIndex + 1);
			}

		} else {
			Toast.makeText(getActivity(), "Nu exista agenti!", Toast.LENGTH_SHORT).show();
		}

	}

	public void onSelectedSpinnerItem(int spinnerId, HashMap<String, String> map, int position) {
		if (spinnerId == R.id.spinnerFilialaVanzAg) {
			String filNr = map.get("codFiliala");
			if (filNr.trim().equals(""))
				filNr = "-1"; // fara selectie
			if (filNr.equals("00000000"))
				filNr = "0";
			localFiliala = filNr;
			Neincasate.selectedFiliala = filNr;
			listAgenti.clear();
			adapterAgenti.notifyDataSetChanged();
			if (!filNr.equals("-1")) {
				performGetAgenti();

			}
		} else if (spinnerId == R.id.spinnerAgentiVanzAg) {
			String agentNr = map.get("codAgent");
			if (agentNr.trim().equals(""))
				agentNr = "0"; // fara selectie
			if (agentNr.equals("00000000"))
				agentNr = "0";
			Neincasate.selectedAgent = agentNr;
		}

	}

	public void selectedAgent(String agentCod) {
		// TODO Auto-generated method stub

	}

	public void onTaskComplete(String methodName, Object result) {
		if (methodName.equals("getListAgenti")) {
			populateAgentiList((String) result);
		}

	}

}
