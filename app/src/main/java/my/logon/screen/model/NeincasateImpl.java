package my.logon.screen.model;

import java.util.ArrayList;
import java.util.HashMap;

import my.logon.screen.listeners.ArticolNeincasatListener;
import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.model.INeincasate;
import my.logon.screen.screens.AsyncTaskWSCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.widget.Toast;
import my.logon.screen.beans.BeanArticolVanzari;
import my.logon.screen.enums.EnumNeincasate;

public class NeincasateImpl implements INeincasate, AsyncTaskListener {

	private Context context;
	private ArticolNeincasatListener listener;
	private EnumNeincasate numeComanda;
	private HashMap<String, String> params;

	public NeincasateImpl(Context context) {
		this.context = context;
	}

	
	public void getArtDocNeincasat(HashMap<String, String> params) {
		numeComanda = EnumNeincasate.GET_ART_DOC_NEINCASAT;
		this.params = params;
		performOperation();
	}

	public void setArtNeincasatListener(ArticolNeincasatListener listener) {
		this.listener = listener;
	}

	private void performOperation() {
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getNumeComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();

	}

	public void onTaskComplete(String methodName, Object result) {
		if (listener != null) {
			listener.articolNeincasatComplete(numeComanda, result);

		}

	}
	
	
	public ArrayList<BeanArticolVanzari> getArticole(Object result) {
		BeanArticolVanzari unArticol = null;
		ArrayList<BeanArticolVanzari> objectsList = new ArrayList<BeanArticolVanzari>();

		try {

			Object json = new JSONTokener((String)result).nextValue();

			if (json instanceof JSONArray) {
				JSONArray jsonObject = new JSONArray((String)result);

				for (int i = 0; i < jsonObject.length(); i++) {
					JSONObject clienObject = jsonObject.getJSONObject(i);

					unArticol = new BeanArticolVanzari();
					unArticol.setCodArticol(clienObject.getString("codArticol"));
					unArticol.setNumeArticol(clienObject.getString("numeArticol"));
					unArticol.setCantitateArticol(clienObject.getString("cantitateArticol"));
					unArticol.setValoareArticol(clienObject.getString("valoareArticol"));

					objectsList.add(unArticol);

				}
			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return objectsList;
	}

	

}
