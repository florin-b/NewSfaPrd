package my.logon.screen.model;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import my.logon.screen.beans.AntetCmdMathaus;
import my.logon.screen.beans.ArticolCant;
import my.logon.screen.beans.ArticolDB;
import my.logon.screen.beans.BeanArticolCautare;
import my.logon.screen.beans.BeanArticolSimulat;
import my.logon.screen.beans.BeanArticolStoc;
import my.logon.screen.beans.BeanCablu05;
import my.logon.screen.beans.BeanGreutateArticol;
import my.logon.screen.beans.BeanParametruPretGed;
import my.logon.screen.beans.ComandaMathaus;
import my.logon.screen.beans.CostTransportMathaus;
import my.logon.screen.beans.DateArticolMathaus;
import my.logon.screen.beans.DatePoligonLivrare;
import my.logon.screen.beans.LivrareMathaus;
import my.logon.screen.beans.OptiuneCamion;
import my.logon.screen.beans.PretArticolGed;
import my.logon.screen.enums.EnumArticoleDAO;
import my.logon.screen.enums.EnumUnitMas;
import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.listeners.OperatiiArticolListener;
import my.logon.screen.screens.AsyncTaskWSCall;
import my.logon.screen.utils.UtilsGeneral;

public class OperatiiArticolImpl implements OperatiiArticol, AsyncTaskListener {

	private Context context;
	private OperatiiArticolListener listener;
	private EnumArticoleDAO numeComanda;
	private HashMap<String, String> params;

	public OperatiiArticolImpl(Context context) {
		this.context = context;
	}

	public void getPret(HashMap<String, String> params) {
		numeComanda = EnumArticoleDAO.GET_PRET;
		this.params = params;
		performOperation();
	}

	public void getPretGed(HashMap<String, String> params) {
		numeComanda = EnumArticoleDAO.GET_PRET_GED;
		this.params = params;
		performOperation();
	}

	public void getPretGedJson(HashMap<String, String> params) {
		numeComanda = EnumArticoleDAO.GET_PRET_GED_JSON;
		this.params = params;
		performOperation();

	}

	public void getInfoPretMathaus(HashMap<String, String> params) {
		numeComanda = EnumArticoleDAO.GET_INFOPRET_MATHAUS;
		this.params = params;
		performOperation();

	}

	public void getFactorConversie(HashMap<String, String> params) {
		numeComanda = EnumArticoleDAO.GET_FACTOR_CONVERSIE;
		this.params = params;
		performOperation();
	}

	public void getStocDepozit(HashMap<String, String> params) {
		numeComanda = EnumArticoleDAO.GET_STOC_DEPOZIT;
		this.params = params;
		performOperation();
	}

	public void getStocDisponibil(HashMap<String, String> params) {
		numeComanda = EnumArticoleDAO.GET_STOC_DISPONIBIL;
		this.params = params;
		performOperation();
	}

	public void getArticoleDistributie(HashMap<String, String> params) {
		numeComanda = EnumArticoleDAO.GET_ARTICOLE_DISTRIBUTIE;
		this.params = params;
		performOperation();
	}

	public void getArticoleFurnizor(HashMap<String, String> params) {
		numeComanda = EnumArticoleDAO.GET_ARTICOLE_FURNIZOR;
		this.params = params;
		performOperation();
	}

	public void getArticoleComplementare(List<ArticolComanda> listArticole) {
		numeComanda = EnumArticoleDAO.GET_ARTICOLE_COMPLEMENTARE;

		HashMap<String, String> params = UtilsGeneral.newHashMapInstance();
		params.put("listaArticoleComanda", serializeListArticole(listArticole));
		this.params = params;

		performOperation();
	}

	public void getSinteticeDistributie(HashMap<String, String> params) {
		numeComanda = EnumArticoleDAO.GET_SINTETICE_DISTRIBUTIE;
		this.params = params;
		performOperation();
	}

	public void getNivel1Distributie(HashMap<String, String> params) {
		numeComanda = EnumArticoleDAO.GET_NIVEL1_DISTRIBUTIE;
		this.params = params;
		performOperation();
	}

	@Override
	public void getStocArticole(HashMap<String, String> params) {
		numeComanda = EnumArticoleDAO.GET_STOC_ARTICOLE;
		this.params = params;
		performOperation();
	}

	@Override
	public void getCodBare(HashMap<String, String> params) {
		numeComanda = EnumArticoleDAO.GET_COD_BARE;
		this.params = params;
		performOperation();
	}

	@Override
	public void getArticoleStatistic(HashMap<String, String> params) {
		numeComanda = EnumArticoleDAO.GET_ARTICOLE_STATISTIC;
		this.params = params;
		performOperation();

	}

	@Override
	public void getArticoleCustodie(HashMap<String, String> params) {
		numeComanda = EnumArticoleDAO.GET_ARTICOLE_CUSTODIE;
		this.params = params;
		performOperation();

	}

	public void getStocCustodie(HashMap<String, String> params) {
		numeComanda = EnumArticoleDAO.GET_STOC_CUSTODIE;
		this.params = params;
		performOperation();
	}

	public void getStocMathaus(HashMap<String, String> params) {
		numeComanda = EnumArticoleDAO.GET_STOC_MATHAUS;
		this.params = params;
		performOperation();
	}

	public void getArticoleCant(HashMap<String, String> params) {
		numeComanda = EnumArticoleDAO.GET_ARTICOLE_CANT;
		this.params = params;
		performOperation();
	}

	public void getCabluri05(HashMap<String, String> params) {
		numeComanda = EnumArticoleDAO.GET_CABLURI_05;
		this.params = params;
		performOperation();
	}

	public void getArticoleACZC(HashMap<String, String> params) {
		numeComanda = EnumArticoleDAO.GET_ARTICOLE_ACZC;
		this.params = params;
		performOperation();
	}

	@Override
	public Object getDepartBV90(String codArticol) {
		numeComanda = EnumArticoleDAO.GET_DEP_BV90;

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("codArticol", codArticol);

		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getComanda(), params, (AsyncTaskListener) this, context);
		Object obj = call.getCallResultsSync();
		return obj;
	}

	private void performOperationSync() {
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResultsSync();
	}

	private void performOperation() {
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public ArrayList<BeanCablu05> deserializeCabluri05(String serListArticole) {

		ArrayList<BeanCablu05> listCabluri = new ArrayList<BeanCablu05>();

		try {

			JSONArray jsonArray = new JSONArray(serListArticole);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject articolObject = jsonArray.getJSONObject(i);

				BeanCablu05 cablu = new BeanCablu05();
				cablu.setCodBoxa(articolObject.getString("codBoxa"));
				cablu.setNumeBoxa(articolObject.getString("numeBoxa"));
				cablu.setStoc(articolObject.getString("stoc"));

				listCabluri.add(cablu);

			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return listCabluri;
	}


	public ArrayList<BeanArticolCautare> deserializeArtRecom(String serListArticole) {

		ArrayList<BeanArticolCautare> listArtRecom = new ArrayList<BeanArticolCautare>();

		try {

			JSONArray jsonArray = new JSONArray(serListArticole);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject articolObject = jsonArray.getJSONObject(i);
				BeanArticolCautare artRecom = new BeanArticolCautare();
				artRecom.setCod(articolObject.getString("cod"));
				artRecom.setNume(articolObject.getString("nume"));
				listArtRecom.add(artRecom);
			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return listArtRecom;
	}

	public ArrayList<BeanCablu05> deserializeCantCabluri05(String serListArticole) {

		ArrayList<BeanCablu05> listCabluri = new ArrayList<BeanCablu05>();

		try {

			JSONArray jsonArray = new JSONArray(serListArticole);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject articolObject = jsonArray.getJSONObject(i);

				BeanCablu05 cablu = new BeanCablu05();
				cablu.setCodBoxa(articolObject.getString("codBoxa"));
				cablu.setNumeBoxa(articolObject.getString("numeBoxa"));
				cablu.setCantitate(articolObject.getString("cantitate"));

				listCabluri.add(cablu);

			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return listCabluri;
	}

	public String serializeCabluri05(List<BeanCablu05> listCabluri) {

		JSONArray cabluArray = new JSONArray();

		if (listCabluri == null)
			return "";

		try {

			for (BeanCablu05 cablu : listCabluri) {
				JSONObject obj = new JSONObject();
				obj.put("numeBoxa", cablu.getNumeBoxa());
				obj.put("codBoxa", cablu.getCodBoxa());
				obj.put("cantitate", cablu.getCantitate());
				cabluArray.put(obj);

			}

		} catch (Exception ex) {
			Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
		}

		return cabluArray.toString();

	}

	public ArrayList<ArticolDB> deserializeArticoleVanzare(String serializedListArticole) {
		ArticolDB articol = null;
		ArrayList<ArticolDB> listArticole = new ArrayList<ArticolDB>();

		try {
			Object json = new JSONTokener(serializedListArticole).nextValue();

			if (json instanceof JSONArray) {

				JSONArray jsonArray = new JSONArray(serializedListArticole);

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject articolObject = jsonArray.getJSONObject(i);

					articol = new ArticolDB();
					articol.setCod(articolObject.getString("cod"));
					articol.setNume(articolObject.getString("nume"));
					articol.setSintetic(articolObject.getString("sintetic"));
					articol.setNivel1(articolObject.getString("nivel1"));
					articol.setUmVanz(articolObject.getString("umVanz"));
					articol.setUmVanz10(articolObject.getString("umVanz10"));
					articol.setTipAB(articolObject.getString("tipAB"));
					articol.setDepart(articolObject.getString("depart"));
					articol.setDepartAprob(articolObject.getString("departAprob"));
					articol.setUmPalet(articolObject.getString("umPalet").equals("1") ? true : false);
					articol.setStoc(articolObject.getString("stoc"));
					articol.setCategorie(articolObject.getString("categorie"));
					articol.setLungime(Double.valueOf(articolObject.getString("lungime")));
					listArticole.add(articol);

				}
			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return listArticole;

	}

	public String serializeParamPretGed(BeanParametruPretGed parametru) {

		JSONObject jsonParametru = new JSONObject();

		try {
			jsonParametru.put("client", parametru.getClient());
			jsonParametru.put("articol", parametru.getArticol());
			jsonParametru.put("cantitate", parametru.getCantitate());
			jsonParametru.put("depart", parametru.getDepart());
			jsonParametru.put("um", parametru.getUm());
			jsonParametru.put("ul", parametru.getUl());
			jsonParametru.put("depoz", parametru.getDepoz());
			jsonParametru.put("codUser", parametru.getCodUser());
			jsonParametru.put("tipUser", parametru.getTipUser());
			jsonParametru.put("canalDistrib", parametru.getCanalDistrib());
			jsonParametru.put("metodaPlata", parametru.getMetodaPlata());
			jsonParametru.put("codJudet", parametru.getCodJudet());
			jsonParametru.put("localitate", parametru.getLocalitate());
			jsonParametru.put("filialaAlternativa", parametru.getFilialaAlternativa());
			jsonParametru.put("codClientParavan", parametru.getCodClientParavan());
			jsonParametru.put("filialaClp", parametru.getFilialaClp());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonParametru.toString();

	}

	private String serializeListArticole(List<ArticolComanda> listArticole) {
		String serializedResult = "";

		JSONArray myArray = new JSONArray();
		JSONObject obj = null;

		Iterator<ArticolComanda> iterator = listArticole.iterator();
		ArticolComanda articol;

		while (iterator.hasNext()) {
			articol = iterator.next();

			try {
				obj = new JSONObject();
				obj.put("cod", articol.getCodArticol());
				myArray.put(obj);
			} catch (Exception ex) {
				Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
			}

		}

		serializedResult = myArray.toString();

		return serializedResult;
	}

	public String serializeArticolePretTransport(List<ArticolComanda> listArticole) {
		String serializedResult = "";

		JSONArray myArray = new JSONArray();
		JSONObject obj = null;

		Iterator<ArticolComanda> iterator = listArticole.iterator();
		ArticolComanda articol;
		String localCodArticol = "";

		while (iterator.hasNext()) {
			articol = iterator.next();

			try {
				obj = new JSONObject();

				if (articol.getCodArticol().length() == 8)
					localCodArticol = "0000000000" + articol.getCodArticol();
				else
					localCodArticol = articol.getCodArticol();

				obj.put("cod", localCodArticol);
				obj.put("valoare", Double.valueOf(articol.getPret()));
				obj.put("promo", articol.getPromotie() <= 0 ? " " : "X");

				myArray.put(obj);
			} catch (Exception ex) {
				Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
			}

		}

		serializedResult = myArray.toString();

		return serializedResult;
	}

	@Override
	public String serializeListArtStoc(List<BeanArticolStoc> listArticole) {

		JSONArray jsonArray = new JSONArray();
		JSONObject object = null;

		Iterator<BeanArticolStoc> iterator = listArticole.iterator();

		while (iterator.hasNext()) {
			BeanArticolStoc articol = iterator.next();

			object = new JSONObject();
			try {
				object.put("cod", articol.getCod());
				object.put("depozit", articol.getDepozit());
				object.put("depart", articol.getDepart());
				object.put("unitLog", articol.getUnitLog());

				jsonArray.put(object);

			} catch (JSONException e) {

				e.printStackTrace();
			}

		}

		return jsonArray.toString();
	}

	public String serializeCostTransportMathaus(List<CostTransportMathaus> costTransport) {

		JSONArray jsonArray = new JSONArray();

		if (costTransport == null)
			return jsonArray.toString();

		try {

			for (CostTransportMathaus cost : costTransport) {

				JSONObject object = new JSONObject();
				object.put("filiala", cost.getFiliala());
				object.put("tipTransp", cost.getTipTransp());
				object.put("valTransp", cost.getValTransp());
				object.put("codArtTransp", cost.getCodArtTransp());
				jsonArray.put(object);

			}
		} catch (JSONException e) {

			e.printStackTrace();
		}

		return jsonArray.toString();

	}

	@Override
	public String serializeListArtSim(List<BeanArticolSimulat> listArticole) {

		JSONArray jsonArray = new JSONArray();
		JSONObject object = null;

		Iterator<BeanArticolSimulat> iterator = listArticole.iterator();

		while (iterator.hasNext()) {
			BeanArticolSimulat articol = iterator.next();

			object = new JSONObject();
			try {
				object.put("cod", articol.getCod());
				object.put("depozit", articol.getDepozit());
				object.put("depart", articol.getDepart());
				object.put("unitLog", articol.getUnitLog());
				object.put("um", articol.getUm());

				jsonArray.put(object);

			} catch (JSONException e) {

				e.printStackTrace();
			}

		}

		return jsonArray.toString();
	}

	public void deserializeListArtStoc(String listArticole) {
		// Object json = new JSONTokener(serializedListArticole).nextValue();

	}

	public PretArticolGed deserializePretGed(Object result) {
		PretArticolGed pretArticol = new PretArticolGed();

		try {

			JSONObject jsonObject = new JSONObject((String) result);

			if (jsonObject instanceof JSONObject) {
				pretArticol.setPret(jsonObject.getString("pret"));
				pretArticol.setUm(jsonObject.getString("um"));
				pretArticol.setFaraDiscount(jsonObject.getString("faraDiscount"));
				pretArticol.setCodArticolPromo(jsonObject.getString("codArticolPromo"));
				pretArticol.setCantitateArticolPromo(jsonObject.getString("cantitateArticolPromo"));
				pretArticol.setPretArticolPromo(jsonObject.getString("pretArticolPromo"));
				pretArticol.setUmArticolPromo(jsonObject.getString("umArticolPromo"));
				pretArticol.setPretLista(jsonObject.getString("pretLista"));
				pretArticol.setCantitate(jsonObject.getString("cantitate"));
				pretArticol.setConditiiPret(jsonObject.getString("conditiiPret"));
				pretArticol.setMultiplu(jsonObject.getString("multiplu"));
				pretArticol.setCantitateUmBaza(jsonObject.getString("cantitateUmBaza"));
				pretArticol.setUmBaza(jsonObject.getString("umBaza"));
				pretArticol.setCmp(jsonObject.getString("cmp"));
				pretArticol.setPretMediu(jsonObject.getString("pretMediu"));
				pretArticol.setAdaosMediu(jsonObject.getString("adaosMediu"));
				pretArticol.setUmPretMediu(jsonObject.getString("umPretMediu"));
				pretArticol.setCoefCorectie(Double.valueOf(jsonObject.getString("coefCorectie") != "null" ? jsonObject.getString("coefCorectie") : "0"));
				pretArticol.setProcTransport(Double.valueOf(jsonObject.getString("procTransport") != "null" ? jsonObject.getString("procTransport") : "0"));
				pretArticol.setDiscMaxAV(Double.valueOf(jsonObject.getString("discMaxAV") != "null" ? jsonObject.getString("discMaxAV") : "0"));
				pretArticol.setDiscMaxSD(Double.valueOf(jsonObject.getString("discMaxSD") != "null" ? jsonObject.getString("discMaxSD") : "0"));
				pretArticol.setDiscMaxDV(Double.valueOf(jsonObject.getString("discMaxDV") != "null" ? jsonObject.getString("discMaxDV") : "0"));
				pretArticol.setDiscMaxKA(Double.valueOf(jsonObject.getString("discMaxKA") != "null" ? jsonObject.getString("discMaxKA") : "0"));
				pretArticol.setImpachetare(jsonObject.getString("impachetare"));
				pretArticol.setIstoricPret(jsonObject.getString("istoricPret"));
				pretArticol.setValTrap(Double.valueOf(jsonObject.getString("valTrap")));
				pretArticol.setErrMsg(jsonObject.getString("errMsg"));
				pretArticol.setProcReducereCmp(Double.valueOf(jsonObject.getString("procReducereCmp")));
				pretArticol.setPretFaraTva(Double.valueOf(jsonObject.getString("pretFaraTva")));
				pretArticol.setDataExp(jsonObject.getString("dataExp"));
				pretArticol.setGreutate(Double.valueOf(jsonObject.getString("greutate")));
				pretArticol.setArticoleRecomandate(deserializeArtRecom(jsonObject.getString("articoleRecomandate")));
				pretArticol.setTipMarfa(jsonObject.getString("tipMarfa"));
				pretArticol.setGreutateBruta(Double.valueOf(jsonObject.getString("greutateBruta")));
				pretArticol.setLungimeArt(jsonObject.getString("lungime"));


			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return pretArticol;
	}

	public void onTaskComplete(String methodName, Object result) {
		if (listener != null) {
			listener.operationComplete(numeComanda, result);
		}
	}

	public void setListener(OperatiiArticolListener listener) {
		this.listener = listener;
	}

	public BeanGreutateArticol deserializeGreutateArticol(Object result) {

		BeanGreutateArticol greutateArticol = new BeanGreutateArticol();

		try {
			JSONObject jsonObject = new JSONObject((String) result);

			if (jsonObject instanceof JSONObject) {
				greutateArticol.setCodArticol(jsonObject.getString("codArticol"));
				greutateArticol.setGreutate(Double.valueOf(jsonObject.getString("greutate")));
				greutateArticol.setUnitMas(EnumUnitMas.valueOf(jsonObject.getString("um")));
				greutateArticol.setUnitMasCantiate(jsonObject.getString("umCantitate"));

			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return greutateArticol;
	}

	public List<BeanArticolStoc> derializeListArtStoc(String serializedResult) {

		List<BeanArticolStoc> listArticole = new ArrayList<BeanArticolStoc>();

		try {
			Object jsonObject = new JSONTokener(serializedResult).nextValue();

			if (jsonObject instanceof JSONArray) {
				JSONArray jsonArray = new JSONArray(serializedResult);

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject articolObject = jsonArray.getJSONObject(i);

					BeanArticolStoc articol = new BeanArticolStoc();

					articol.setCod(articolObject.getString("cod"));

					articol.setDepozit(articolObject.getString("depozit"));
					articol.setDepart(articolObject.getString("depart"));
					articol.setUnitLog(articolObject.getString("unitLog"));
					articol.setStoc(Double.valueOf(articolObject.getString("stoc")));

					listArticole.add(articol);

				}

			}

		} catch (JSONException e) {

			e.printStackTrace();
		}

		return listArticole;
	}

	public ArrayList<ArticolCant> deserializeArticoleCant(String listArticoleSer) {
		ArticolCant articol = null;
		ArrayList<ArticolCant> listArticole = new ArrayList<ArticolCant>();

		try {
			Object json = new JSONTokener(listArticoleSer).nextValue();

			if (json instanceof JSONArray) {

				JSONArray jsonArray = new JSONArray(listArticoleSer);

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject articolObject = jsonArray.getJSONObject(i);

					articol = new ArticolCant();

					articol.setCod(articolObject.getString("cod"));
					articol.setNume(articolObject.getString("denumire"));
					articol.setSintetic(articolObject.getString("sintetic"));
					articol.setDimensiuni(articolObject.getString("dimensiuni"));
					articol.setCaract(articolObject.getString("caract"));
					articol.setStoc(articolObject.getString("stoc"));
					articol.setUmVanz(articolObject.getString("um"));
					articol.setTipCant(articolObject.getString("tipCant"));
					articol.setUlStoc(articolObject.getString("ulStoc"));
					articol.setNivel1(articolObject.getString("nivel1"));
					articol.setUmVanz(articolObject.getString("umVanz"));
					articol.setUmVanz10(articolObject.getString("umVanz10"));
					articol.setDepart(articolObject.getString("depart"));
					articol.setDepartAprob(articolObject.getString("departAprob"));
					articol.setTipAB(articolObject.getString("tipAB"));
					articol.setUmPalet(articolObject.getString("umPalet").equals("1") ? true : false);
					articol.setCategorie(articolObject.getString("categorie"));
					articol.setLungime(Double.valueOf(articolObject.getString("lungime")));
					articol.setDepozit(articolObject.getString("depozit"));
					listArticole.add(articol);

				}
			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return listArticole;

	}

	public LivrareMathaus deserializeLivrareMathaus(String result) {

		LivrareMathaus livrareMathaus = new LivrareMathaus();

		try {

			JSONObject jsonObject = new JSONObject((String) result);

			livrareMathaus.setComandaMathaus(deserializeStocMathaus(jsonObject.getString("comandaMathaus")));

			List<CostTransportMathaus> listCostTransport = new ArrayList<CostTransportMathaus>();

			JSONArray jsonArrayTransp = new JSONArray(jsonObject.getString("costTransport"));

			for (int i = 0; i < jsonArrayTransp.length(); i++) {

				JSONObject transpObject = jsonArrayTransp.getJSONObject(i);

				CostTransportMathaus cost = new CostTransportMathaus();
				cost.setFiliala(transpObject.getString("filiala"));
				cost.setTipTransp(transpObject.getString("tipTransp"));
				cost.setValTransp(transpObject.getString("valTransp").equals("null") ? "0" : transpObject.getString("valTransp"));
				cost.setCodArtTransp(transpObject.getString("codArtTransp").equals("null") ? "0" : transpObject.getString("codArtTransp"));
				cost.setDepart(transpObject.getString("depart"));
				cost.setNumeCost(transpObject.getString("numeCost"));
				listCostTransport.add(cost);

			}

			livrareMathaus.setCostTransport(listCostTransport);

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return livrareMathaus;
	}

	public ComandaMathaus deserializeStocMathaus(String result) {

		ComandaMathaus comandaMathaus = new ComandaMathaus();
		List<DateArticolMathaus> listArticole = new ArrayList<DateArticolMathaus>();

		try {

			JSONObject jsonObject = new JSONObject((String) result);
			comandaMathaus.setSellingPlant(jsonObject.getString("sellingPlant"));

			JSONArray jsonArrayLoc = new JSONArray(jsonObject.getString("deliveryEntryDataList"));

			for (int i = 0; i < jsonArrayLoc.length(); i++) {

				JSONObject articolObject = jsonArrayLoc.getJSONObject(i);

				DateArticolMathaus articol = new DateArticolMathaus();
				articol.setDeliveryWarehouse(articolObject.getString("deliveryWarehouse"));
				articol.setProductCode(articolObject.getString("productCode"));
				articol.setQuantity(Double.parseDouble(articolObject.getString("quantity")));
				articol.setUnit(articolObject.getString("unit"));
				articol.setDepozit(articolObject.getString("depozit"));

				listArticole.add(articol);
			}

			comandaMathaus.setDeliveryEntryDataList(listArticole);

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return comandaMathaus;
	}

	public String serializeAntetCmdMathaus(AntetCmdMathaus antetComanda) {

		JSONObject jsonAntet = new JSONObject();

		try {
			jsonAntet.put("localitate", antetComanda.getLocalitate());
			jsonAntet.put("codJudet", antetComanda.getCodJudet());
			jsonAntet.put("codClient", antetComanda.getCodClient());
			jsonAntet.put("tipPers", antetComanda.getTipPers());
			jsonAntet.put("depart", antetComanda.getDepart());
			jsonAntet.put("codPers", antetComanda.getCodPers());
			jsonAntet.put("tipTransp", antetComanda.getTipTransp());

			jsonAntet.put("camionDescoperit", antetComanda.isCamionDescoperit());
			jsonAntet.put("macara", antetComanda.isMacara());

			if (antetComanda.getOptiuniCamion() == null)
				jsonAntet.put("tipCamion", " ");
			else
				jsonAntet.put("tipCamion", serializeOptiuniCamion(antetComanda.getOptiuniCamion()));

			jsonAntet.put("greutateComanda", antetComanda.getGreutateComanda());
			jsonAntet.put("tipComandaCamion", antetComanda.getTipComandaCamion());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonAntet.toString();

	}

	public String serializeOptiuniCamion(List<OptiuneCamion> listOptiuni) {

		JSONArray jsonArray = new JSONArray();
		JSONObject object = null;

		Iterator<OptiuneCamion> iterator = listOptiuni.iterator();

		while (iterator.hasNext()) {
			OptiuneCamion optiune = iterator.next();

			object = new JSONObject();
			try {
				object.put("nume", optiune.getNume());
				object.put("exista", optiune.isExista());
				object.put("selectat", optiune.isSelectat());
				jsonArray.put(object);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return jsonArray.toString();
	}

	public String serializeComandaMathaus(ComandaMathaus comandaMathaus) {

		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = null;
		JSONObject jsonComanda = new JSONObject();

		try {

			jsonComanda.put("sellingPlant", comandaMathaus.getSellingPlant());

			for (DateArticolMathaus articol : comandaMathaus.getDeliveryEntryDataList()) {
				jsonObject = new JSONObject();
				jsonObject.put("productCode", articol.getProductCode());
				jsonObject.put("quantity", articol.getQuantity());
				jsonObject.put("unit", articol.getUnit());
				jsonObject.put("valPoz", String.valueOf(articol.getValPoz()));
				jsonObject.put("tip2", articol.getTip2());
				jsonObject.put("ulStoc", articol.getUlStoc());
				jsonObject.put("depozit", articol.getDepozit());
				jsonObject.put("greutate", articol.getGreutate());
				jsonArray.put(jsonObject);
			}

			jsonComanda.put("deliveryEntryDataList", jsonArray);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonComanda.toString();

	}



	public String serializeDatePoligon(DatePoligonLivrare datePoligonLivrare) {

		JSONObject jsonPoligon = new JSONObject();

		try {

			if (datePoligonLivrare != null) {
				jsonPoligon.put("filialaPrincipala", datePoligonLivrare.getFilialaPrincipala());
				jsonPoligon.put("filialaSecundara", datePoligonLivrare.getFilialaSecundara());
				jsonPoligon.put("tipZona", datePoligonLivrare.getTipZona());
				jsonPoligon.put("limitareTonaj", datePoligonLivrare.getLimitareTonaj());
				jsonPoligon.put("nume", datePoligonLivrare.getNume());
			} else {
				jsonPoligon.put("filialaPrincipala", "");
				jsonPoligon.put("filialaSecundara", "");
				jsonPoligon.put("tipZona", "");
				jsonPoligon.put("limitareTonaj", "");
				jsonPoligon.put("nume", "");
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonPoligon.toString();

	}

}
