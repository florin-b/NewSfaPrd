package my.logon.screen.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.listeners.OperatiiConcurentaListener;
import my.logon.screen.screens.AsyncTaskWSCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.widget.Toast;
import my.logon.screen.beans.BeanArticolConcurenta;
import my.logon.screen.beans.BeanCompanieConcurenta;
import my.logon.screen.beans.BeanNewPretConcurenta;
import my.logon.screen.beans.BeanPretConcurenta;
import my.logon.screen.enums.EnumOperatiiConcurenta;

public class OperatiiConcurentaImpl implements OperatiiConcurenta, AsyncTaskListener {

	private Context context;
	private OperatiiConcurentaListener listener;
	private EnumOperatiiConcurenta numeComanda;
	private HashMap<String, String> params;

	public OperatiiConcurentaImpl(Context context) {
		this.context = context;
	}

	public void getArticoleConcurenta(HashMap<String, String> params) {
		numeComanda = EnumOperatiiConcurenta.GET_ARTICOLE_CONCURENTA;
		this.params = params;
		performOperation();

	}

	public void getArticoleConcurentaBulk(HashMap<String, String> params) {
		numeComanda = EnumOperatiiConcurenta.GET_ARTICOLE_CONCURENTA_BULK;
		this.params = params;
		performOperation();

	}

	public void getCompaniiConcurente(HashMap<String, String> params) {
		numeComanda = EnumOperatiiConcurenta.GET_COMPANII_CONCURENTE;
		this.params = params;
		performOperation();
	}

	public void getPretConcurenta(HashMap<String, String> params) {
		numeComanda = EnumOperatiiConcurenta.GET_PRET_CONCURENTA;
		this.params = params;
		performOperation();
	}

	public void addPretConcurenta(HashMap<String, String> params) {
		numeComanda = EnumOperatiiConcurenta.ADD_PRET_CONCURENTA;
		this.params = params;
		performOperation();
	}

	public void saveListPreturi(HashMap<String, String> params) {
		numeComanda = EnumOperatiiConcurenta.SAVE_LIST_PRETURI;
		this.params = params;
		performOperation();
	}

	public String serializePreturi(List<BeanNewPretConcurenta> listPreturi) {

		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = null;

		try {

			for (BeanNewPretConcurenta pret : listPreturi) {
				jsonObject = new JSONObject();
				jsonObject.put("cod", pret.getCod());
				jsonObject.put("concurent", pret.getConcurent());
				jsonObject.put("valoare", pret.getValoare());
				jsonObject.put("observatii", pret.getObservatii());
				jsonArray.put(jsonObject);

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonArray.toString();
	}

	public List<BeanCompanieConcurenta> deserializeCompConcurente(Object resultList) {
		BeanCompanieConcurenta companie = null;
		ArrayList<BeanCompanieConcurenta> listCompanii = new ArrayList<BeanCompanieConcurenta>();

		try {
			Object json = new JSONTokener((String) resultList).nextValue();

			if (json instanceof JSONArray) {
				JSONArray jsonArray = new JSONArray((String) resultList);

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject compObject = jsonArray.getJSONObject(i);
					companie = new BeanCompanieConcurenta();
					companie.setCod(compObject.getString("cod"));
					companie.setNume(compObject.getString("nume"));
					listCompanii.add(companie);

				}
			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return listCompanii;
	}

	public List<BeanPretConcurenta> deserializePreturiConcurenta(Object resultList) {
		BeanPretConcurenta unPret = null;
		ArrayList<BeanPretConcurenta> preturiList = new ArrayList<BeanPretConcurenta>();

		try {

			Object json = new JSONTokener((String) resultList).nextValue();

			if (json instanceof JSONArray) {
				JSONArray jsonObject = new JSONArray((String) resultList);

				for (int i = 0; i < jsonObject.length(); i++) {
					JSONObject pretObject = jsonObject.getJSONObject(i);

					unPret = new BeanPretConcurenta();
					unPret.setData(pretObject.getString("data"));
					unPret.setValoare(pretObject.getString("valoare"));
					preturiList.add(unPret);

				}
			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return preturiList;
	}

	public ArrayList<BeanArticolConcurenta> deserializeArticoleConcurenta(String serializedListArticole) {
		BeanArticolConcurenta articol = null;
		ArrayList<BeanArticolConcurenta> listArticole = new ArrayList<BeanArticolConcurenta>();

		try {
			Object json = new JSONTokener(serializedListArticole).nextValue();

			if (json instanceof JSONArray) {

				JSONArray jsonArray = new JSONArray(serializedListArticole);

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject articolObject = jsonArray.getJSONObject(i);

					articol = new BeanArticolConcurenta();
					articol.setCod(articolObject.getString("cod"));
					articol.setNume(articolObject.getString("nume"));
					articol.setUmVanz(articolObject.getString("umVanz"));
					articol.setValoare(articolObject.getString("valoare"));
					articol.setDataValoare(articolObject.getString("dataValoare"));
					articol.setObservatii((articolObject.getString("observatii")));

					listArticole.add(articol);

				}
			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return listArticole;

	}

	private void performOperation() {
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public void setListener(OperatiiConcurentaListener listener) {
		this.listener = listener;
	}

	public void onTaskComplete(String methodName, Object result) {
		if (listener != null) {
			listener.operationComplete(numeComanda, result);
		}

	}

}
