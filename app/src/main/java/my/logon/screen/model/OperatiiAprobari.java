package my.logon.screen.model;
//Petru
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import my.logon.screen.beans.Aprobare;
import my.logon.screen.enums.EnumAprobari;
import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.listeners.OperatiiAprobariListener;
import my.logon.screen.screens.AsyncTaskWSCall;

public class OperatiiAprobari implements AsyncTaskListener {
	Context context;
	OperatiiAprobariListener listener;
	EnumAprobari numeAprobari;

	ArrayList<Aprobare> listaAprobari;
	Object json;

	public OperatiiAprobari(Context context) {
		this.context = context;

	}

	public void getAprobariComenziKA(HashMap<String, String> params) {
		numeAprobari = EnumAprobari.GET_APROBARI_COMENZI_KA;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeAprobari.getAprobari(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();

	}

	@Override
	public void onTaskComplete(String methodName, Object result) {
		if (listener != null) {
			listener.operationComplete(numeAprobari, result);
		}

	}

	public void setOperatiiAprobariListener(OperatiiAprobariListener listener) {
		this.listener = listener;
	}

	public ArrayList<Aprobare> deserializeAprobariKA(String serializedArpobariKa) {

		listaAprobari = new ArrayList<Aprobare>();
		try {
			json = new JSONObject(serializedArpobariKa);

			if (json instanceof JSONObject) {

				if (((JSONObject) json).get("aprobSD").equals(null) && !((JSONObject) json).get("aprobDV").equals(null)) {
					getAprobareDV();
				}

				else if (!((JSONObject) json).get("aprobSD").equals(null) && ((JSONObject) json).get("aprobDV").equals(null)) {
					getAprobareSD();

				} else if (((JSONObject) json).get("aprobSD").equals(null) && ((JSONObject) json).get("aprobDV").equals(null)) {
					listaAprobari.isEmpty();
				}

				else {
					getAprobareSD();
					getAprobareDV();
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return listaAprobari;

	}

	private void getAprobareDV() throws JSONException {

		Aprobare aprobareDV = new Aprobare();
		JSONArray jsonObject1 = new JSONArray(((JSONObject) json).getString("aprobDV"));

		for (int i = 0; i < jsonObject1.length(); i++) {
			JSONObject persObject2 = jsonObject1.getJSONObject(i);
			aprobareDV.setTip("DV");
			aprobareDV.setNume(persObject2.getString("nume"));
			aprobareDV.setNrTelefon(persObject2.getString("telefon"));
			listaAprobari.add(aprobareDV);
		}
	}

	private void getAprobareSD() throws JSONException {

		Aprobare aprobareSD = new Aprobare();
		JSONArray jsonObject = new JSONArray(((JSONObject) json).getString("aprobSD"));

		for (int i = 0; i < jsonObject.length(); i++) {
			JSONObject persObject = jsonObject.getJSONObject(i);
			aprobareSD.setTip("SD");
			aprobareSD.setNume(persObject.getString("nume"));
			aprobareSD.setNrTelefon(persObject.getString("telefon"));
			listaAprobari.add(aprobareSD);

		}

	}
//End Petru 
}
