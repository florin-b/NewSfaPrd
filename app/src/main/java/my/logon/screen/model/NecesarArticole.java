package my.logon.screen.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.listeners.NecesarArticoleListener;
import my.logon.screen.model.HandleJSONData;
import my.logon.screen.screens.AsyncTaskWSCall;
import my.logon.screen.utils.UtilsGeneral;
import android.content.Context;

public class NecesarArticole implements AsyncTaskListener {

	private Context context;
	private NecesarArticoleListener listener;
	ArrayList<MaterialNecesar> materialeArray = new ArrayList<MaterialNecesar>();

	private NecesarArticole() {
	}

	public static NecesarArticole getInstance() {
		return new NecesarArticole();
	}

	public ArrayList<MaterialNecesar> getListaMateriale(String filiala, String departament, Context context) {
		ArrayList<MaterialNecesar> materiale = new ArrayList<MaterialNecesar>();

		HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
		params.put("filiala", filiala);
		params.put("departament", departament);

		AsyncTaskListener contextListener = (AsyncTaskListener) NecesarArticole.this;
		AsyncTaskWSCall call = new AsyncTaskWSCall(context, contextListener, "getListaMaterialeNecesar", params);
		call.getCallResultsFromFragment();

		return materiale;
	}

	public void setNecesarArticoleListener(NecesarArticoleListener listener) {
		this.listener = listener;
	}

	private ArrayList<MaterialNecesar> deserializeData(String result) {
		HandleJSONData objMaterialeList = new HandleJSONData(context, (String) result);
		materialeArray = objMaterialeList.decodeJSONNecesar();
		return materialeArray;
	}

	public ArrayList<MaterialNecesar> getListaMateriale() {
		return materialeArray;
	}

	public void onTaskComplete(String methodName, Object result) {
		if (listener != null) {

			if (((String) result).length() > 0)
				deserializeData((String) result);

			listener.onTaskComplete();
		}

	}

	public ArrayList<MaterialNecesar> sortByNumeArticol(ArrayList<MaterialNecesar> listaMateriale, boolean tipSort) {
		Collections.sort(listaMateriale, new MaterialNecesar.CompareNumeArt(tipSort));
		return listaMateriale;
	}

	public ArrayList<MaterialNecesar> sortByCodArticol(ArrayList<MaterialNecesar> listaMateriale, boolean tipSort) {
		Collections.sort(listaMateriale, new MaterialNecesar.CompareCodArt(tipSort));
		return listaMateriale;
	}

	public ArrayList<MaterialNecesar> sortByNumeSintetic(ArrayList<MaterialNecesar> listaMateriale, boolean tipSort) {
		Collections.sort(listaMateriale, new MaterialNecesar.CompareNumeSintetic(tipSort));
		return listaMateriale;
	}

	public ArrayList<MaterialNecesar> sortByCodSintetic(ArrayList<MaterialNecesar> listaMateriale, boolean tipSort) {
		Collections.sort(listaMateriale, new MaterialNecesar.CompareCodSintetic(tipSort));
		return listaMateriale;
	}

	public ArrayList<MaterialNecesar> sortByConsum30(ArrayList<MaterialNecesar> listaMateriale, boolean tipSort) {
		Collections.sort(listaMateriale, new MaterialNecesar.CompareCons30(tipSort));
		return listaMateriale;
	}

	public ArrayList<MaterialNecesar> sortByStoc(ArrayList<MaterialNecesar> listaMateriale, boolean tipSort) {
		Collections.sort(listaMateriale, new MaterialNecesar.CompareStoc(tipSort));
		return listaMateriale;
	}

	public ArrayList<MaterialNecesar> sortByPropunere(ArrayList<MaterialNecesar> listaMateriale, boolean tipSort) {
		Collections.sort(listaMateriale, new MaterialNecesar.ComparePropunere(tipSort));
		return listaMateriale;
	}

}
