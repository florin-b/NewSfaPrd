package my.logon.screen.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.listeners.FacturiNeincasateListener;
import my.logon.screen.screens.AsyncTaskWSCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.widget.Toast;
import my.logon.screen.beans.FacturaNeincasataLite;
import my.logon.screen.beans.IncasareDocument;
import my.logon.screen.beans.PlataNeincasata;
import my.logon.screen.enums.EnumNeincasate;

public class OperatiiDocumente implements AsyncTaskListener {

	private EnumNeincasate numeComanda;
	private Context context;
	private FacturiNeincasateListener listener;

	public OperatiiDocumente(Context context) {
		this.context = context;
	}

	public void getFacturiNeincasate(HashMap<String, String> params) {
		numeComanda = EnumNeincasate.GET_FACTURI_NEINCASATE;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getNumeComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();

	}

	public void salveazaPlataNeincasata(HashMap<String, String> params) {
		numeComanda = EnumNeincasate.SALVEAZA_PLATA;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getNumeComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();

	}

	public List<FacturaNeincasataLite> deserializeFacturi(String strFacturi) {

		List<FacturaNeincasataLite> listFacturi = new ArrayList<FacturaNeincasataLite>();

		try {
			Object json = new JSONTokener(strFacturi).nextValue();

			if (json instanceof JSONArray) {

				JSONArray jsonArray = new JSONArray(strFacturi);

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject object = jsonArray.getJSONObject(i);

					FacturaNeincasataLite factura = new FacturaNeincasataLite();
					factura.setNrFactura(object.getString("nrFactura"));
					factura.setRestPlata(Double.valueOf(object.getString("restPlata")));
					factura.setDataEmitere(object.getString("dataEmitere"));
					factura.setAnDocument(object.getString("anDocument"));
					factura.setNrDocument(object.getString("nrDocument"));

					listFacturi.add(factura);

				}

			}
		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return listFacturi;
	}

	public String serializePlata(PlataNeincasata plataNeincasata) {

		JSONObject jsonPlata = new JSONObject();
		try {
			jsonPlata.put("tipDocument", plataNeincasata.getTipDocument());
			jsonPlata.put("codClient", plataNeincasata.getCodClient());
			jsonPlata.put("sumaPlata", plataNeincasata.getSumaPlata());
			jsonPlata.put("seriaDocument", plataNeincasata.getSeriaDocument());
			jsonPlata.put("dataEmitere", plataNeincasata.getDataEmitere());
			jsonPlata.put("dataScadenta", plataNeincasata.getDataScadenta());
			jsonPlata.put("codAgent", plataNeincasata.getCodAgent());
			jsonPlata.put("girant", plataNeincasata.getGirant());
			

			JSONArray jsonArrayDocs = new JSONArray();
			for (IncasareDocument incasare : plataNeincasata.getListaDocumente()) {
				JSONObject jsonObjDoc = new JSONObject();
				jsonObjDoc.put("nrDocument", incasare.getNrDocument());
				jsonObjDoc.put("sumaIncasata", incasare.getSumaIncasata());
				jsonObjDoc.put("anDocument", incasare.getAnDocument());
				jsonArrayDocs.put(jsonObjDoc);
			}

			jsonPlata.put("listaDocumente", jsonArrayDocs.toString());

		} catch (JSONException e) {

			e.printStackTrace();
		}

		return jsonPlata.toString();

	}

	public void setFacturiNeincasateListener(FacturiNeincasateListener listener) {
		this.listener = listener;
	}

	@Override
	public void onTaskComplete(String methodName, Object result) {
		if (listener != null)
			listener.operationComplete(numeComanda, result);

	}

}
