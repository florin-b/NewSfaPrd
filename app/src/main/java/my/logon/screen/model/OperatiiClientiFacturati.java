package my.logon.screen.model;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;

import my.logon.screen.beans.BeanClientiFacturati;
import my.logon.screen.beans.BeanListaFacturati;
import my.logon.screen.enums.EnumClientiFacturati;
import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.listeners.ClientiFacturatiListener;
import my.logon.screen.screens.AsyncTaskWSCall;

public class OperatiiClientiFacturati implements AsyncTaskListener {
	
	private Context context;
	private ClientiFacturatiListener listener;
	private EnumClientiFacturati numeWebService;
	
	public OperatiiClientiFacturati(Context context) {
		this.context = context;
	}
	
	public void getListClientiFacturatiKA(HashMap<String, String> params) {
		numeWebService = EnumClientiFacturati.GETLISTA_CLIENTIFACTURATIKA;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeWebService.getNume(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}
	
	public void getListFacturiClient(HashMap<String, String> params) {
		numeWebService = EnumClientiFacturati.GETLISTA_FACTURICLIENT;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeWebService.getNume(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}
	
	public ArrayList<BeanClientiFacturati> deserializeClientiFacturatiKA(String result) {
		ArrayList<BeanClientiFacturati> objectsList = new ArrayList<BeanClientiFacturati>();
		BeanClientiFacturati clientFacturat = null;
		
		try {
			Object json = new JSONTokener(result).nextValue();
			if (json instanceof JSONArray){
				
				JSONArray jsonObject = new JSONArray(result);
				
				for (int i = 0; i< jsonObject.length(); i++) {
					JSONObject clientObject = jsonObject.getJSONObject(i);
					
					clientFacturat = new BeanClientiFacturati();
					clientFacturat.setCodClient(clientObject.getString("cod"));
					clientFacturat.setNumeClient(clientObject.getString("nume"));
					clientFacturat.setLuna1(clientObject.getString("luna1"));
					clientFacturat.setLuna2(clientObject.getString("luna2"));
					clientFacturat.setLuna3(clientObject.getString("luna3"));
					clientFacturat.setLuna4(clientObject.getString("luna4"));
					clientFacturat.setLuna5(clientObject.getString("luna5"));
					clientFacturat.setLuna6(clientObject.getString("luna6"));
					clientFacturat.setLuna7(clientObject.getString("luna7"));
					objectsList.add(clientFacturat);
				}
			}
			
		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}
		
		return objectsList;
	}
	
	public ArrayList<BeanListaFacturati> deserializeListaFacturi(String result) {
		ArrayList<BeanListaFacturati> objectsListFacturi = new ArrayList<BeanListaFacturati>();
		BeanListaFacturati factura = null;
		
		try {
			Object json = new JSONTokener(result).nextValue();
			if (json instanceof JSONArray){
				
				JSONArray jsonObject = new JSONArray(result);
				
				for (int i = 0; i< jsonObject.length(); i++) {
					JSONObject clientObject = jsonObject.getJSONObject(i);
					
					factura = new BeanListaFacturati();
					factura.setNrFactura(clientObject.getString("nrComanda"));
					factura.setDataEmitere(clientObject.getString("dataEmitere"));
					factura.setValoare(clientObject.getString("valoare"));
					
					objectsListFacturi.add(factura);
				}
			}
			
		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}
		
		return objectsListFacturi;
	}
	
	public void setClientiFacturatiListener(ClientiFacturatiListener listener) {
		this.listener = listener;
	}

	
	public void onTaskComplete(String methodName, Object result) {

		if (listener != null) {
			listener.operationClientiFacturatiComplete(numeWebService, result);			
		}
		else {
			
			Toast.makeText(context, "listener empty", Toast.LENGTH_SHORT).show();
		}
		
	}

}
