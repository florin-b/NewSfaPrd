package my.logon.screen.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.listeners.ObiectiveListener;
import my.logon.screen.model.UserInfo;
import my.logon.screen.screens.AsyncTaskWSCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import my.logon.screen.utils.UtilsGeneral;
import android.content.Context;
import android.widget.Toast;
import my.logon.screen.beans.BeanObiectivAfisare;
import my.logon.screen.beans.BeanObiectivDepartament;
import my.logon.screen.beans.BeanObiectivHarta;
import my.logon.screen.beans.BeanObiectiveConstructori;
import my.logon.screen.beans.BeanObiectiveGenerale;
import my.logon.screen.beans.BeanStadiuObiectiv;
import my.logon.screen.beans.BeanUrmarireEveniment;
import my.logon.screen.beans.BeanUrmarireObiectiv;
import my.logon.screen.beans.ObiectivConsilier;
import my.logon.screen.enums.EnumOperatiiObiective;
import my.logon.screen.enums.EnumStadiuSubantrep;

public class OperatiiObiective implements AsyncTaskListener {

	private Context context;
	private ObiectiveListener listener;
	private EnumOperatiiObiective numeComanda;

	public OperatiiObiective(Context context) {
		this.context = context;
	}

	public void saveObiectiv(HashMap<String, String> params) {
		numeComanda = EnumOperatiiObiective.SALVEAZA_OBIECTIV;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getNume(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public void getListObiective(HashMap<String, String> params) {
		numeComanda = EnumOperatiiObiective.GET_LIST_OBIECTIVE;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getNume(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public void getListObiectiveAV(HashMap<String, String> params) {
		numeComanda = EnumOperatiiObiective.GET_LIST_OBIECTIVE_AV;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getNume(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public void getDetaliiObiectiv(HashMap<String, String> params) {
		numeComanda = EnumOperatiiObiective.GET_DETALII_OBIECTIV;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getNume(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public void getStareClient(HashMap<String, String> params) {
		numeComanda = EnumOperatiiObiective.GET_STARE_CLIENT;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getNume(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public void getClientiObiectiv(HashMap<String, String> params) {
		numeComanda = EnumOperatiiObiective.GET_CLIENTI_OBIECTIV;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getNume(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public void salveazaEvenimentObiectiv(HashMap<String, String> params) {
		numeComanda = EnumOperatiiObiective.SALVEAZA_EVENIMENT;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getNume(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public void getEvenimenteClient(HashMap<String, String> params) {
		numeComanda = EnumOperatiiObiective.GET_EVENIMENTE_CLIENT;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getNume(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public void getObiectiveDepartament(HashMap<String, String> params) {
		numeComanda = EnumOperatiiObiective.GET_OBIECTIVE_DEPARTAMENT;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getNume(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public void getObiectiveHarta(HashMap<String, String> params) {
		numeComanda = EnumOperatiiObiective.GET_OBIECTIVE_HARTA;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getNume(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public void getObiectiveConsilieri(HashMap<String, String> params) {
		numeComanda = EnumOperatiiObiective.GET_OBIECTIVE_CONSILIERI;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getNume(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public List<BeanObiectivAfisare> deserializeListaObiective(String result) {
		List<BeanObiectivAfisare> listaObiective = new ArrayList<BeanObiectivAfisare>();
		BeanObiectivAfisare obiectiv = null;

		try {
			Object json = new JSONTokener(result).nextValue();

			if (json instanceof JSONArray) {

				JSONArray jsonArray = new JSONArray(result);

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject object = jsonArray.getJSONObject(i);

					obiectiv = new BeanObiectivAfisare();
					obiectiv.setId(object.getString("id"));
					obiectiv.setNume(object.getString("nume"));
					obiectiv.setData(object.getString("data"));
					obiectiv.setBeneficiar(object.getString("beneficiar"));
					obiectiv.setCodStatus(object.getString("codStatus"));
					obiectiv.setNumeAgent(object.getString("numeAgent"));
					listaObiective.add(obiectiv);
				}

			}
		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return listaObiective;
	}

	public List<BeanObiectivHarta> deserializeListaObiectiveHarta(String result) {
		List<BeanObiectivHarta> listaObiective = new ArrayList<BeanObiectivHarta>();
		BeanObiectivHarta obiectiv = null;

		try {
			Object json = new JSONTokener(result).nextValue();

			if (json instanceof JSONArray) {

				JSONArray jsonArray = new JSONArray(result);

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject object = jsonArray.getJSONObject(i);

					obiectiv = new BeanObiectivHarta();
					obiectiv.setId(object.getString("id"));
					obiectiv.setNume(object.getString("nume"));
					obiectiv.setData(object.getString("data"));
					obiectiv.setBeneficiar(object.getString("beneficiar"));
					obiectiv.setCodStatus(object.getString("codStatus"));
					obiectiv.setAddress(object.getString("adresa"));
					obiectiv.setNumeAgent(object.getString("numeAgent"));
					listaObiective.add(obiectiv);
				}

			}
		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return listaObiective;
	}

	public BeanObiectiveGenerale deserializeDetaliiObiectiv(String detaliiObiectiv) {

		BeanObiectiveGenerale.getInstance().clearInstanceData();
		BeanObiectiveGenerale obiectiv = BeanObiectiveGenerale.getInstance();

		try {
			JSONObject jsonObject = new JSONObject(detaliiObiectiv);
			obiectiv.setId(jsonObject.getString("id"));
			obiectiv.setNumeObiectiv(jsonObject.getString("numeObiectiv"));
			obiectiv.setStadiuObiectiv(jsonObject.getString("codStadiuObiectiv"));
			obiectiv.setCodMotivSuspendare(jsonObject.getString("codMotivSuspendare"));
			obiectiv.setDataCreare(jsonObject.getString("dataCreare"));
			obiectiv.setAdresaObiectiv(jsonObject.getString("adresaObiectiv"));
			obiectiv.setNumeBeneficiar(jsonObject.getString("numeBeneficiar"));
			obiectiv.setNumeAntreprenorGeneral(jsonObject.getString("numeAntreprenorGeneral"));
			obiectiv.setCodAntreprenorGeneral(jsonObject.getString("codAntreprenorGeneral"));
			obiectiv.setNumeArhitect(jsonObject.getString("numeArhitect"));
			obiectiv.setCategorieObiectiv(jsonObject.getString("codCategorieObiectiv"));
			obiectiv.setValoareObiectiv(jsonObject.getString("valoareObiectiv"));
			obiectiv.setNrAutorizatieConstructie(jsonObject.getString("nrAutorizatieConstructie"));
			obiectiv.setDataEmitereAutorizatie(jsonObject.getString("dataEmitereAutorizatie"));
			obiectiv.setDataExpirareAutorizatie(jsonObject.getString("dataExpirareAutorizatie"));
			obiectiv.setPrimariaEmitenta(jsonObject.getString("primariaEmitenta"));
			obiectiv.setValoareFundatie(jsonObject.getString("valoareFundatie"));

			JSONArray jsonArray = new JSONArray(jsonObject.getString("constructori"));

			List<BeanStadiuObiectiv> listStadii = new ArrayList<BeanStadiuObiectiv>();
			BeanStadiuObiectiv stadiu = null;

			List<BeanObiectiveConstructori> listConstructori = new ArrayList<BeanObiectiveConstructori>();
			BeanObiectiveConstructori constructor = null;

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject object = jsonArray.getJSONObject(i);

				constructor = new BeanObiectiveConstructori();
				constructor.setCodClient(object.getString("codClient"));
				constructor.setNumeClient(object.getString("numeClient"));
				constructor.setCodDepart(object.getString("codDepart"));
				constructor.setStare(object.getString("stare"));
				listConstructori.add(constructor);

				stadiu = new BeanStadiuObiectiv();
				stadiu.setCodDepart(object.getString("codDepart"));
				stadiu.setCodStadiu(EnumStadiuSubantrep.IN_CONSTRUCTIE.getCodStadiu());
				stadiu.setNumeStadiu(EnumStadiuSubantrep.IN_CONSTRUCTIE.getNumeStadiu());
				addListStadiu(listStadii, stadiu);

			}

			obiectiv.setListConstructori(listConstructori);
			obiectiv.setListStadii(listStadii);

			jsonArray = new JSONArray(jsonObject.getString("stadii"));

			BeanStadiuObiectiv stadiuDepart = null;
			List<BeanStadiuObiectiv> listStadiiDepart = new ArrayList<BeanStadiuObiectiv>();

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject object = jsonArray.getJSONObject(i);

				stadiuDepart = new BeanStadiuObiectiv();
				stadiuDepart.setCodDepart(object.getString("codDepart"));
				stadiuDepart.setCodStadiu(Integer.valueOf(object.getString("codStadiu")));
				stadiuDepart.setNumeStadiu(EnumStadiuSubantrep.getNumeStadiu(stadiuDepart.getCodStadiu()));
				listStadiiDepart.add(stadiuDepart);

			}

			obiectiv.setStadiiDepart(listStadiiDepart);

			jsonArray = new JSONArray(jsonObject.getString("evenimente"));

			obiectiv.setListEvenimente(deserializeListEvenimente(String.valueOf(jsonArray)));

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return obiectiv;
	}

	private void addListStadiu(List<BeanStadiuObiectiv> listStadii, BeanStadiuObiectiv stadiu) {

		boolean stadiuExists = false;
		for (BeanStadiuObiectiv unStadiu : listStadii) {
			if (unStadiu.getCodDepart().equals(stadiu.getCodDepart())) {
				stadiuExists = true;
				break;
			}
		}

		if (!stadiuExists)
			listStadii.add(stadiu);

	}

	public String serializeEveniment(BeanUrmarireObiectiv obiectiv) {
		JSONObject jsonObiectiv = new JSONObject();

		try {
			jsonObiectiv.put("idObiectiv", obiectiv.getIdObiectiv());
			jsonObiectiv.put("codClient", obiectiv.getCodClient());
			jsonObiectiv.put("codDepart", obiectiv.getCodDepart());
			jsonObiectiv.put("codEveniment", obiectiv.getCodEveniment());
			jsonObiectiv.put("data", obiectiv.getData());
			jsonObiectiv.put("observatii", obiectiv.getObservatii());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonObiectiv.toString();
	}

	public String serializeObiectiv(BeanObiectiveGenerale obiectiv) {

		JSONObject jsonObiectiv = new JSONObject();

		try {
			jsonObiectiv.put("id", obiectiv.getId());
			jsonObiectiv.put("codAgent", UserInfo.getInstance().getCod());
			jsonObiectiv.put("unitLog", UserInfo.getInstance().getUnitLog());
			jsonObiectiv.put("numeObiectiv", obiectiv.getNumeObiectiv());
			jsonObiectiv.put("codStadiuObiectiv", obiectiv.getStadiuObiectiv());
			jsonObiectiv.put("codMotivSuspendare", obiectiv.getCodMotivSuspendare());
			jsonObiectiv.put("dataCreare", obiectiv.getDataCreare());
			jsonObiectiv.put("numeBeneficiar", obiectiv.getNumeBeneficiar());
			jsonObiectiv.put("codAntreprenorGeneral", obiectiv.getCodAntreprenorGeneral());
			jsonObiectiv.put("numeArhitect", obiectiv.getNumeArhitect());
			jsonObiectiv.put("valoareObiectiv", obiectiv.getValoareObiectiv());
			jsonObiectiv.put("nrAutorizatieConstructie", obiectiv.getNrAutorizatieConstructie());
			jsonObiectiv.put("dataEmitereAutorizatie", obiectiv.getDataEmitereAutorizatie());
			jsonObiectiv.put("dataExpirareAutorizatie", obiectiv.getDataExpirareAutorizatie());
			jsonObiectiv.put("valoareFundatie", obiectiv.getValoareFundatie());
			jsonObiectiv.put("codCategorieObiectiv", obiectiv.getCategorieObiectiv().getCodCategorie());
			jsonObiectiv.put("adresaObiectiv", obiectiv.getAdresaSer());
			jsonObiectiv.put("primariaEmitenta", obiectiv.getPrimariaEmitentaSer());
			jsonObiectiv.put("inchis", obiectiv.isInchis());

			JSONObject jsonObjConstr = null;
			JSONArray jsonArrayConstr = new JSONArray();
			Iterator<BeanObiectiveConstructori> iterator = obiectiv.getListConstructori().iterator();
			BeanObiectiveConstructori constructor = null;
			while (iterator.hasNext()) {
				constructor = iterator.next();

				if (!constructor.getCodDepart().equals("00")) {
					jsonObjConstr = new JSONObject();
					jsonObjConstr.put("codClient", constructor.getCodClient());
					jsonObjConstr.put("codDepart", constructor.getCodDepart());
					jsonArrayConstr.put(jsonObjConstr);
				}
			}

			jsonObiectiv.put("constructori", jsonArrayConstr.toString());

			JSONArray jsonArrayStadiu = new JSONArray();
			JSONObject jsonObjStadiu = null;
			Iterator<BeanStadiuObiectiv> iteratorStadiu = obiectiv.getStadiiDepart().iterator();
			BeanStadiuObiectiv stadiuObiectiv = null;

			while (iteratorStadiu.hasNext()) {
				stadiuObiectiv = iteratorStadiu.next();
				jsonObjStadiu = new JSONObject();
				jsonObjStadiu.put("codDepart", stadiuObiectiv.getCodDepart());
				jsonObjStadiu.put("codStadiu", stadiuObiectiv.getCodStadiu());
				jsonArrayStadiu.put(jsonObjStadiu);

			}

			jsonObiectiv.put("stadii", jsonArrayStadiu.toString());

		} catch (JSONException e) {

			e.printStackTrace();
		}

		return jsonObiectiv.toString();

	}

	public BeanObiectiveConstructori deserializeStareConstructor(String stareConstructor) {
		BeanObiectiveConstructori constructor = new BeanObiectiveConstructori();

		try {
			JSONObject jsonObject = new JSONObject(stareConstructor);

			if (jsonObject instanceof JSONObject) {
				constructor.setCodClient(jsonObject.getString("codClient"));
				constructor.setCodDepart(jsonObject.getString("codDepart"));
				constructor.setStare(jsonObject.getString("stare"));

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return constructor;
	}

	public List<BeanUrmarireEveniment> deserializeListEvenimente(String resultList) {
		List<BeanUrmarireEveniment> listEvenimente = new ArrayList<BeanUrmarireEveniment>();
		BeanUrmarireEveniment eveniment = null;

		try {
			Object json = new JSONTokener(resultList).nextValue();

			if (json instanceof JSONArray) {

				JSONArray jsonArray = new JSONArray(resultList);

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject evenimentObject = jsonArray.getJSONObject(i);

					eveniment = new BeanUrmarireEveniment();
					eveniment.setIdEveniment(Integer.parseInt(evenimentObject.getString("codEveniment")));
					eveniment.setData(UtilsGeneral.getFormattedDate(evenimentObject.getString("data")));
					eveniment.setObservatii(evenimentObject.getString("observatii"));
					eveniment.setCodClient(evenimentObject.getString("codClient"));
					eveniment.setCodDepart(evenimentObject.getString("codDepart"));
					listEvenimente.add(eveniment);

				}
			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return listEvenimente;
	}

	public List<BeanObiectiveConstructori> deserializeListConstructori(String resultList) {
		List<BeanObiectiveConstructori> listConstructori = new ArrayList<BeanObiectiveConstructori>();
		BeanObiectiveConstructori constructor;

		try {
			Object json = new JSONTokener(resultList).nextValue();

			if (json instanceof JSONArray) {

				JSONArray jsonArray = new JSONArray(resultList);

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject constructorObject = jsonArray.getJSONObject(i);

					constructor = new BeanObiectiveConstructori();
					constructor.setCodClient(constructorObject.getString("codClient"));
					constructor.setNumeClient(constructorObject.getString("numeClient"));
					constructor.setCodDepart(constructorObject.getString("codDepart"));
					listConstructori.add(constructor);

				}
			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return listConstructori;

	}

	public List<BeanObiectivDepartament> deserializeObiectiveDepart(String obiective) {

		List<BeanObiectivDepartament> listObiective = new ArrayList<BeanObiectivDepartament>();
		BeanObiectivDepartament obiectiv;

		try {
			Object json = new JSONTokener(obiective).nextValue();

			if (json instanceof JSONArray) {

				JSONArray jsonArray = new JSONArray(obiective);

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject obiectivObject = jsonArray.getJSONObject(i);

					obiectiv = new BeanObiectivDepartament();
					obiectiv.setId(obiectivObject.getString("id"));
					obiectiv.setNume(obiectivObject.getString("nume"));
					obiectiv.setBeneficiar(obiectivObject.getString("beneficiar"));
					obiectiv.setDataCreare(obiectivObject.getString("dataCreare"));
					obiectiv.setAdresa(obiectivObject.getString("adresa"));
					listObiective.add(obiectiv);

				}
			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return listObiective;
	}

	public List<ObiectivConsilier> deserializeObiectiveConsilieri(String result) {
		List<ObiectivConsilier> listaObiective = new ArrayList<ObiectivConsilier>();
		ObiectivConsilier obiectiv = null;

		try {
			Object json = new JSONTokener(result).nextValue();

			if (json instanceof JSONArray) {

				JSONArray jsonArray = new JSONArray(result);

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject object = jsonArray.getJSONObject(i);

					obiectiv = new ObiectivConsilier();
					obiectiv.setId(object.getString("id"));
					obiectiv.setBeneficiar(object.getString("beneficiar"));
					obiectiv.setDataCreare(object.getString("dataCreare"));
					obiectiv.setAdresa(object.getString("adresa"));
					obiectiv.setCodJudet(object.getString("codJudet"));
					obiectiv.setCoordGps(object.getString("coordGps"));
					listaObiective.add(obiectiv);
				}

			}
		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return listaObiective;
	}

	public void setObiectiveListener(ObiectiveListener listener) {
		this.listener = listener;
	}

	public void onTaskComplete(String methodName, Object result) {
		if (listener != null) {
			listener.operationObiectivComplete(numeComanda, result);
		}

	}

}
