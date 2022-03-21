package my.logon.screen.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.listeners.OperatiiVenitListener;
import my.logon.screen.model.CalculVenit;
import my.logon.screen.screens.AsyncTaskWSCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import my.logon.screen.beans.VenitAG;
import my.logon.screen.beans.VenitTCF;
import my.logon.screen.beans.VenitTPR;
import my.logon.screen.beans.VenituriNTCF;
import my.logon.screen.enums.EnumOperatiiVenit;

public class CalculVenitImpl implements CalculVenit, AsyncTaskListener {

	private EnumOperatiiVenit numeComanda;
	private HashMap<String, String> params;
	private Context context;
	private OperatiiVenitListener listener;

	public CalculVenitImpl(Context context) {
		this.context = context;
	}

	
	public void getVenitTPR_TCF(HashMap<String, String> params) {
		numeComanda = EnumOperatiiVenit.GET_VENIT_AG;
		this.params = params;
		performOperation();
	}

	@Override
	public void getVenitNTCF(HashMap<String, String> params) {
		numeComanda = EnumOperatiiVenit.GET_VENIT_NTCF;
		this.params = params;
		performOperation();

	}	
	
	private void performOperation() {
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getNumeComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public void setOperatiiVenitListener(OperatiiVenitListener listener) {
		this.listener = listener;
	}

	
	public void onTaskComplete(String methodName, Object result) {
		if (listener != null)
			listener.operatiiVenitComplete(numeComanda, result);
	}

	
	public VenitAG getVenit(Object venitData) {

		VenitAG venitAG = new VenitAG();

		try {
			JSONObject jsonObject = new JSONObject((String) venitData);

			JSONArray arrayTcf = new JSONArray(jsonObject.getString("venitTcf"));

			List<VenitTCF> listTCF = new ArrayList<VenitTCF>();
			

			for (int i = 0; i < arrayTcf.length(); i++) {
				JSONObject object = arrayTcf.getJSONObject(i);
				
				VenitTCF venitTCF = new VenitTCF();
				venitTCF.setVenitGrInc(object.getString("venitGrInc"));
				venitTCF.setTargetPropus(object.getString("targetPropus"));
				venitTCF.setTargetRealizat(object.getString("targetRealizat"));
				venitTCF.setCoefAfectare(object.getString("coefAfectare"));
				venitTCF.setVenitTcf(object.getString("venitTcf"));
				listTCF.add(venitTCF);
			}

			JSONArray arrayTpr = new JSONArray(jsonObject.getString("venitTpr"));

			List<VenitTPR> listTPR = new ArrayList<VenitTPR>();
			

			for (int i = 0; i < arrayTpr.length(); i++) {
				JSONObject object = arrayTpr.getJSONObject(i);

				VenitTPR venitTPR = new VenitTPR();
				venitTPR.setCodN2(object.getString("codN2"));
				venitTPR.setNumeN2(object.getString("numeN2"));
				venitTPR.setVenitGrInc(object.getString("venitGrInc"));
				venitTPR.setPondere(object.getString("pondere"));
				venitTPR.setTargetPropCant(object.getString("targetPropCant"));
				venitTPR.setTargetRealCant(object.getString("targetRealCant"));
				venitTPR.setUm(object.getString("um"));
				venitTPR.setTargetPropVal(object.getString("targetPropVal"));
				venitTPR.setTargetRealVal(object.getString("targetRealVal"));
				venitTPR.setRealizareTarget(object.getString("realizareTarget"));
				venitTPR.setTargetPonderat(object.getString("targetPonderat"));

				listTPR.add(venitTPR);

			}
			
			
			venitAG.setVenitTcf(listTCF);
			venitAG.setVenitTpr(listTPR);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return venitAG;
	}

	public VenituriNTCF deserializeDateNTCF(Object dateNTCF) {

		VenituriNTCF venituriNTCF = new VenituriNTCF();

		try {

			JSONObject jsonObject = new JSONObject((String) dateNTCF);

			JSONObject clientFactAnAnteriorObject = jsonObject.getJSONObject("clientFactAnAnterior");
			HashMap<String, Object> clientFactAnAnterior = (HashMap<String, Object>) toMap(clientFactAnAnteriorObject);
			venituriNTCF.setClientFactAnAnterior(clientFactAnAnterior);

			JSONObject targetAnCurentObject = jsonObject.getJSONObject("targetAnCurent");
			HashMap<String, Object> targetAnCurent = (HashMap<String, Object>) toMap(targetAnCurentObject);
			venituriNTCF.setTargetAnCurent(targetAnCurent);

			JSONObject clientFactAnCurentObject = jsonObject.getJSONObject("clientFactAnCurent");
			HashMap<String, Object> clientFactAnCurent = (HashMap<String, Object>) toMap(clientFactAnCurentObject);
			venituriNTCF.setClientFactAnCurent(clientFactAnCurent);

			JSONObject coefAfectareObject = jsonObject.getJSONObject("coefAfectare");
			HashMap<String, Object> coefAfectare = (HashMap<String, Object>) toMap(coefAfectareObject);
			venituriNTCF.setCoefAfectare(coefAfectare);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return venituriNTCF;

	}

	public static Map<String, Object> toMap(JSONObject object) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();

		Iterator<String> keysItr = object.keys();
		while (keysItr.hasNext()) {
			String key = keysItr.next();
			Object value = object.get(key);

			if (value instanceof JSONArray) {
				value = toList((JSONArray) value);
			}

			else if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			map.put(key, value);
		}
		return map;
	}

	public static List<Object> toList(JSONArray array) throws JSONException {
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < array.length(); i++) {
			Object value = array.get(i);
			if (value instanceof JSONArray) {
				value = toList((JSONArray) value);
			}

			else if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			list.add(value);
		}
		return list;
	}	
	
	
	
}
