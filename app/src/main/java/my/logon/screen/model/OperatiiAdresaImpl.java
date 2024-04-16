package my.logon.screen.model;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import my.logon.screen.beans.BeanAdreseJudet;
import my.logon.screen.beans.BeanDateLivrareClient;
import my.logon.screen.beans.BeanFilialaLivrare;
import my.logon.screen.beans.BeanLocalitate;
import my.logon.screen.beans.DatePoligonLivrare;
import my.logon.screen.enums.EnumLocalitate;
import my.logon.screen.enums.EnumOperatiiAdresa;
import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.listeners.OperatiiAdresaListener;
import my.logon.screen.screens.AsyncTaskWSCall;
import my.logon.screen.utils.UtilsFormatting;

public class OperatiiAdresaImpl implements OperatiiAdresa, AsyncTaskListener {

	private Context context;
	private EnumOperatiiAdresa numeComanda;
	private HashMap<String, String> params;
	private OperatiiAdresaListener listener;
	private EnumLocalitate tipLocalitate;

	public OperatiiAdresaImpl(Context context) {
		this.context = context;
	}

	public void getLocalitatiJudet(HashMap<String, String> params, EnumLocalitate tipLocalitate) {

		numeComanda = EnumOperatiiAdresa.GET_LOCALITATI_JUDET;
		this.params = params;
		this.tipLocalitate = tipLocalitate;
		performOperation();

	}

	public void getAdreseJudet(HashMap<String, String> params, EnumLocalitate tipLocalitate) {

		numeComanda = EnumOperatiiAdresa.GET_ADRESE_JUDET;
		this.params = params;
		this.tipLocalitate = tipLocalitate;
		performOperation();

	}

	public void isAdresaValida(HashMap<String, String> params, EnumLocalitate tipLocalitate) {
		numeComanda = EnumOperatiiAdresa.IS_ADRESA_VALIDA;
		this.params = params;
		this.tipLocalitate = tipLocalitate;
		performOperation();

	}

	@Override
	public void getDateLivrare(HashMap<String, String> params) {
		numeComanda = EnumOperatiiAdresa.GET_DATE_LIVRARE;
		this.params = params;
		performOperation();

	}

	@Override
	public void getAdreseLivrareClient(HashMap<String, String> params) {
		numeComanda = EnumOperatiiAdresa.GET_ADRESE_LIVR_CLIENT;
		this.params = params;
		performOperation();

	}

	@Override
	public void getLocalitatiLivrareRapida() {
		numeComanda = EnumOperatiiAdresa.GET_LOCALITATI_LIVRARE_RAPIDA;
		performOperation();

	}

	public void getDateLivrareClient(HashMap<String, String> params) {
		this.params = params;
		numeComanda = EnumOperatiiAdresa.GET_DATE_LIVRARE_CLIENT;
		performOperation();

	}
	
	public void getFilialaLivrareMathaus(HashMap<String, String> params) {
		this.params = params;
		numeComanda = EnumOperatiiAdresa.GET_FILIALA_MATHAUS;
		performOperation();

	}

	public void getAdresaFiliala(HashMap<String, String> params) {
		this.params = params;
		numeComanda = EnumOperatiiAdresa.GET_ADRESA_FILIALA;
		performOperation();

	}

	public void getDatePoligonLivrare(HashMap<String, String> params){
		this.params = params;
		numeComanda = EnumOperatiiAdresa.GET_DATE_POLIGON_LIVRARE;
		performOperation();
	}

	public void getFilialeComenziTCLI(){
		numeComanda = EnumOperatiiAdresa.GET_FILIALE_TCLI;
		performOperation();
	}

	public List<String> deserializeListLocalitati(Object resultList) {

		List<String> listLocalitati = new ArrayList<String>();

		try {
			Object json = new JSONTokener((String) resultList).nextValue();

			if (json instanceof JSONArray) {
				JSONArray jsonObject = new JSONArray((String) resultList);

				for (int i = 0; i < jsonObject.length(); i++) {
					listLocalitati.add(jsonObject.get(i).toString());
				}
			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return listLocalitati;
	}

	public List<BeanFilialaLivrare> deserializeFilialeLivrare(String resultList){

		//{"nume":"Bacau Depozit Materiale grele","werks":"BC10","depozite":["01V2","02V2","03V2","04V2","04V3","05V2","06V2","07V2","09V2"]}

		List<BeanFilialaLivrare> listFilialeLivrare = new ArrayList<>();

		try {
			Object json = new JSONTokener((String) resultList).nextValue();

			if (json instanceof JSONArray) {
				JSONArray jsonObject = new JSONArray((String) resultList);

				for (int i = 0; i < jsonObject.length(); i++) {

					BeanFilialaLivrare filialaLivrare = new BeanFilialaLivrare();
					JSONObject filialaObject = jsonObject.getJSONObject(i);

					if (!filialaObject.toString().contains("nume"))
						break;

					filialaLivrare.setNumeFiliala(filialaObject.getString("nume"));
					filialaLivrare.setUnitLog(filialaObject.getString("werks"));
					String listDepozite[] = filialaObject.getString("depozite").replace("[","").replace("]","").replace("\"","").split(",");
					filialaLivrare.setDepozite(Arrays.asList(listDepozite));
					listFilialeLivrare.add(filialaLivrare);
				}
			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return listFilialeLivrare;
	}

	public BeanAdreseJudet deserializeListAdrese(Object resultList) {

		BeanAdreseJudet adreseJudet = new BeanAdreseJudet();
		List<BeanLocalitate> listLocalitati = new ArrayList<BeanLocalitate>();
		List<String> listStrazi = new ArrayList<String>();

		adreseJudet.setListLocalitati(listLocalitati);
		adreseJudet.setListStrazi(listStrazi);

		if (((String) resultList).length() == 2) {
			return adreseJudet;
		}

		try {
			JSONObject jsonObject = new JSONObject((String) resultList);

			if (!jsonObject.toString().contains("listLocalitati"))
				return adreseJudet;

			JSONArray jsonArrayLoc = new JSONArray(jsonObject.getString("listLocalitati"));

			for (int i = 0; i < jsonArrayLoc.length(); i++) {

				JSONObject articolObject = jsonArrayLoc.getJSONObject(i);

				BeanLocalitate loc = new BeanLocalitate();
				loc.setLocalitate(UtilsFormatting.flattenToAscii(articolObject.getString("localitate")));
				loc.setOras(Boolean.parseBoolean(articolObject.getString("isOras")));
				loc.setRazaKm(Integer.parseInt(articolObject.getString("razaKm")));
				loc.setCoordonate(articolObject.getString("coordonate"));

				listLocalitati.add(loc);
			}

			if (!jsonObject.toString().contains("listStrazi"))
				return adreseJudet;

			JSONArray jsonArrayStrazi = new JSONArray(jsonObject.getString("listStrazi"));

			for (int i = 0; i < jsonArrayStrazi.length(); i++) {
				listStrazi.add(UtilsFormatting.flattenToAscii(jsonArrayStrazi.getString(i).toString()));
			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		adreseJudet.setListLocalitati(listLocalitati);
		adreseJudet.setListStrazi(listStrazi);

		return adreseJudet;
	}

	public BeanDateLivrareClient deserializeDateLivrareClient(String result) {
		BeanDateLivrareClient dateLivrare = new BeanDateLivrareClient();

		try {

			JSONObject jsonObject = new JSONObject((String) result);

			if (jsonObject instanceof JSONObject) {

				dateLivrare.setCodJudet(jsonObject.getString("codJudet"));
				dateLivrare.setLocalitate(jsonObject.getString("localitate"));
				dateLivrare.setStrada(jsonObject.getString("strada"));
				dateLivrare.setNrStrada(jsonObject.getString("nrStrada"));
				dateLivrare.setNumePersContact(jsonObject.getString("numePersContact"));
				dateLivrare.setTelPersContact(jsonObject.getString("telPersContact"));
				dateLivrare.setTermenPlata(jsonObject.getString("termenPlata"));

			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}
		return dateLivrare;
	}

	public DatePoligonLivrare deserializePoligonLivrare(String result){
		DatePoligonLivrare poligonLivrare = new DatePoligonLivrare();

		try {

			JSONObject jsonObject = new JSONObject((String) result);
			poligonLivrare.setFilialaPrincipala(jsonObject.getString("filialaPrincipala"));
			poligonLivrare.setFilialaSecundara(jsonObject.getString("filialaSecundara"));
			poligonLivrare.setTipZona(jsonObject.getString("tipZona"));
			poligonLivrare.setLimitareTonaj(jsonObject.getString("limitareTonaj"));
			poligonLivrare.setNume(jsonObject.getString("nume"));
			poligonLivrare.setRestrictionat(Boolean.parseBoolean(jsonObject.getString("isRestrictionat")));

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return poligonLivrare;
	}

	public void setOperatiiAdresaListener(OperatiiAdresaListener listener) {
		this.listener = listener;
	}

	private void performOperation() {
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getNumeComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public void onTaskComplete(String methodName, Object result) {

		if (listener != null) {
			listener.operatiiAdresaComplete(numeComanda, result, tipLocalitate);
		}

	}

}
