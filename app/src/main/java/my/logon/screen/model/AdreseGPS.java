package my.logon.screen.model;

import java.util.ArrayList;
import java.util.HashMap;

import my.logon.screen.listeners.AdreseGPSListener;
import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.screens.AsyncTaskWSCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import my.logon.screen.utils.UtilsGeneral;

import android.content.Context;
import android.widget.Toast;
import my.logon.screen.beans.BeanClient;

public class AdreseGPS implements AsyncTaskListener {

	private AdreseGPSListener listener;
	private Context context;
	ArrayList<BeanClient> listObjClienti;
	ArrayList<HashMap<String, String>> listAdrese = new ArrayList<HashMap<String, String>>();;

	private AdreseGPS() {

	}

	public static AdreseGPS getInstance() {
		return new AdreseGPS();
	}

	public void getListAdreseGPS(String codAgent, String tipAdresa, String filiala, String departament, Context context) {

		this.context = context;

		HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
		params.put("codAgent", codAgent);
		params.put("tipAdresa", tipAdresa);
		params.put("filiala", filiala);
		params.put("departament", departament);

		AsyncTaskListener contextListener = (AsyncTaskListener) AdreseGPS.this;

		AsyncTaskWSCall call = new AsyncTaskWSCall(context, contextListener, "getListaAdreseGps", params);
		call.getCallResultsFromFragment();
	}

	private void deserializeAdreseData(String JSONString) {
		BeanClient unClient = null;
		listObjClienti = new ArrayList<BeanClient>();

		JSONArray jsonObject;
		try {

			Object json = new JSONTokener(JSONString).nextValue();

			if (json instanceof JSONArray) {
				jsonObject = new JSONArray(JSONString);

				for (int i = 0; i < jsonObject.length(); i++) {
					JSONObject clienObject = jsonObject.getJSONObject(i);

					unClient = new BeanClient();
					unClient.setNumeClient(clienObject.getString("numeClient"));
					unClient.setCodClient(clienObject.getString("codClient"));
					unClient.setTipClient(clienObject.getString("tipClient"));

					listObjClienti.add(unClient);

				}
			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	private void populateAdreseList() {

		listAdrese.clear();
		if (listObjClienti.size() > 0) {

			HashMap<String, String> temp;

			for (int i = 0; i < listObjClienti.size(); i++) {
				temp = new HashMap<String, String>();

				temp.put("numeClient", listObjClienti.get(i).getNumeClient());
				temp.put("codClient", listObjClienti.get(i).getCodClient());
				temp.put("tipClient", listObjClienti.get(i).getTipClient());
				listAdrese.add(temp);
			}

		}
	}

	public void setAdreseGPSListener(AdreseGPSListener listener) {
		this.listener = listener;
	}

	public void onTaskComplete(String methodName, Object result) {
		if (methodName.equals("getListaAdreseGps")) {

			deserializeAdreseData((String) result);
			populateAdreseList();

			if (listener != null) {
				listener.opAdreseGPSComplete(listAdrese);
			}

		}

	}

}
