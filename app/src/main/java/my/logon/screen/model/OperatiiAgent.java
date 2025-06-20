package my.logon.screen.model;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.listeners.OperatiiAgentListener;
import my.logon.screen.screens.AsyncTaskWSCall;
import my.logon.screen.utils.UtilsGeneral;

public class OperatiiAgent implements AsyncTaskListener {

	private Context context;
	private ArrayList<Agent> listObjAgenti;
	private ArrayList<HashMap<String, String>> listAgenti = new ArrayList<HashMap<String, String>>();
	private boolean optTotiAgentii;
	private OperatiiAgentListener listener;

	private OperatiiAgent() {

	}

	public static OperatiiAgent getInstance() {
		return new OperatiiAgent();
	}

	public void getListaAgenti(String filiala, String departament, Context context, boolean optTotiAgentii, String tipAgent) {

		this.optTotiAgentii = optTotiAgentii;
		this.context = context;

		HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
		params.put("filiala", filiala);
		params.put("depart", departament);

		if (tipAgent != null)
			params.put("tipAgent", tipAgent);

		params.put("codAgent", UserInfo.getInstance().getCod());

		AsyncTaskListener contextListener = OperatiiAgent.this;
		AsyncTaskWSCall call = new AsyncTaskWSCall(context, contextListener, "getListAgentiJSON", params);
		call.getCallResultsFromFragment();
	}

	private void deserializeAgentiData(String JSONString) {
		Agent unAgent = null;
		JSONArray jsonObject;
		try {

			Object json = new JSONTokener(JSONString).nextValue();
			listObjAgenti = new ArrayList<>();

			if (json instanceof JSONArray) {
				jsonObject = new JSONArray(JSONString);

				for (int i = 0; i < jsonObject.length(); i++) {
					JSONObject agentObject = jsonObject.getJSONObject(i);
					unAgent = new Agent();
					unAgent.setNume(agentObject.getString("nume"));
					unAgent.setCod(agentObject.getString("cod"));
					unAgent.setDepart(agentObject.getString("depart"));
					listObjAgenti.add(unAgent);

				}
			}

		} catch (JSONException e) {
			Toast.makeText(context, "JSON: " + e.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	private void populateListAgenti() {

		listAgenti.clear();
		if (listObjAgenti.size() > 0) {

			HashMap<String, String> temp;

			temp = new HashMap<String, String>();
			temp.put("numeAgent", "Agent");
			temp.put("codAgent", " ");
			temp.put("depart", " ");
			listAgenti.add(temp);

			if (optTotiAgentii) {
				temp = new HashMap<String, String>();
				temp.put("numeAgent", "Toti agentii");
				temp.put("codAgent", "00000000");
				temp.put("depart", " ");
				listAgenti.add(temp);
			}

			for (int i = 0; i < listObjAgenti.size(); i++) {
				temp = new HashMap<String, String>();
				temp.put("numeAgent", listObjAgenti.get(i).getNume());
				temp.put("codAgent", listObjAgenti.get(i).getCod());
				temp.put("depart", listObjAgenti.get(i).getDepart());
				listAgenti.add(temp);
			}

		}

	}

	public void setOperatiiAgentListener(OperatiiAgentListener listener) {
		this.listener = listener;
	}

	public int getItemPosition(String agentId) {
		int position = -1;

		for (int i = 0; i < listObjAgenti.size(); i++) {
			if (listObjAgenti.get(i).getCod().equals(agentId)) {
				position = i;
				break;
			}
		}

		if (optTotiAgentii)
			position++;

		return position;
	}

	public List<Agent> getListObjAgenti() {
		List<Agent> localList = new ArrayList<Agent>(listObjAgenti);
		localList.add(0, new Agent("Selectati un agent", "-1"));

		return localList;
	}

	public void onTaskComplete(String methodName, Object result) {
		if (methodName.equals("getListAgentiJSON")) {
			deserializeAgentiData((String) result);
			populateListAgenti();

			if (listener != null) {
				listener.opAgentComplete(listAgenti);
			}

		}

	}

}
