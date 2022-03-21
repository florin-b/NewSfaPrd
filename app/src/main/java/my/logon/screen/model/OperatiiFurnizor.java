package my.logon.screen.model;

import java.util.ArrayList;
import java.util.HashMap;

import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.listeners.OperatiiFurnizorListener;
import my.logon.screen.screens.AsyncTaskWSCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.widget.Toast;
import my.logon.screen.beans.BeanFurnizor;
import my.logon.screen.beans.BeanFurnizorProduse;
import my.logon.screen.enums.EnumOperatiiFurnizor;

public class OperatiiFurnizor implements AsyncTaskListener{

	private EnumOperatiiFurnizor numeComanda;
	private OperatiiFurnizorListener listener;
	private Context context;

	public OperatiiFurnizor(Context context) {
		this.context = context;
	}

	public void getFurnizoriMarfa(HashMap<String, String> params) {
		numeComanda = EnumOperatiiFurnizor.GET_FURNIZORI_MARFA;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();

	}

	public void getFurnizoriProduse(HashMap<String, String> params) {
		numeComanda = EnumOperatiiFurnizor.GET_FURNIZORI_PRODUSE;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();

	}

	
	public ArrayList<BeanFurnizor> deserializeListFurnizori(String serializedListFurnizori) {
		BeanFurnizor furnizor = null;
		ArrayList<BeanFurnizor> listFurnizori = new ArrayList<BeanFurnizor>();

		try {
			Object json = new JSONTokener(serializedListFurnizori).nextValue();

			if (json instanceof JSONArray) {

				JSONArray jsonArray = new JSONArray(serializedListFurnizori);

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject object = jsonArray.getJSONObject(i);

					furnizor = new BeanFurnizor();
					furnizor.setCodFurnizor(object.getString("codFurnizor"));
					furnizor.setNumeFurnizor(object.getString("numeFurnizor"));
					
					listFurnizori.add(furnizor);

				}

			}
		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return listFurnizori;
	}	
	
	
	public ArrayList<BeanFurnizorProduse> deserializeListFurnizoriProduse(String serializedListFurnizori) {
		BeanFurnizorProduse furnizor = null;
		ArrayList<BeanFurnizorProduse> listFurnizori = new ArrayList<BeanFurnizorProduse>();

		try {
			Object json = new JSONTokener(serializedListFurnizori).nextValue();

			if (json instanceof JSONArray) {

				JSONArray jsonArray = new JSONArray(serializedListFurnizori);

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject object = jsonArray.getJSONObject(i);

					furnizor = new BeanFurnizorProduse();
					furnizor.setCodFurnizorProduse(object.getString("codFurnizorProduse"));
					furnizor.setNumeFurnizorProduse(object.getString("numeFurnizorProduse"));
					
					listFurnizori.add(furnizor);

				}

			}
		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return listFurnizori;
	}	
	
	
	@Override
	public void onTaskComplete(String methodName, Object result) {
		if (listener != null) {
			listener.operationComplete(numeComanda, result);
		}
	}

	public void setOperatiiFurnizorListener(OperatiiFurnizorListener listener) {
		this.listener = listener;
	}

}
