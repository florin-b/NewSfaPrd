package my.logon.screen.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.listeners.ClpDAOListener;
import my.logon.screen.screens.AsyncTaskWSCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.widget.Toast;
import my.logon.screen.beans.AntetComandaCLP;
import my.logon.screen.beans.ArticolCLP;
import my.logon.screen.beans.ComandaCLP;
import my.logon.screen.beans.ComandaCreataCLP;
import my.logon.screen.beans.DateLivrareCLP;
import my.logon.screen.enums.EnumClpDAO;

public class ClpDAO implements IClpDAO, AsyncTaskListener {

	private Context context;
	private EnumClpDAO numeComanda;
	private ClpDAOListener listener;

	public ClpDAO(Context context) {
		this.context = context;
	}

	public void getListComenzi(HashMap<String, String> params) {
		numeComanda = EnumClpDAO.GET_LIST_COMENZI;
		performOperation(params);
	}

	public void getArticoleComanda(HashMap<String, String> params) {
		numeComanda = EnumClpDAO.GET_ARTICOLE_COMANDA;
		performOperation(params);
	}

	public void getArticoleComandaJSON(HashMap<String, String> params) {
		numeComanda = EnumClpDAO.GET_ARTICOLE_COMANDA_JSON;
		performOperation(params);
	}

	public void operatiiComanda(HashMap<String, String> params) {
		numeComanda = EnumClpDAO.OPERATIE_COMANDA;
		performOperation(params);
	}

	public void salveazaComanda(HashMap<String, String> params) {
		numeComanda = EnumClpDAO.SALVEAZA_COMANDA;
		performOperation(params);
	}

	private void performOperation(HashMap<String, String> params) {
		AsyncTaskListener contextListener = (AsyncTaskListener) ClpDAO.this;
		AsyncTaskWSCall call = new AsyncTaskWSCall(context, contextListener, numeComanda.getComanda(), params);
		call.getCallResultsFromFragment();
	}

	public void setClpDAOListener(ClpDAOListener listener) {
		this.listener = listener;
	}

	public void onTaskComplete(String methodName, Object result) {
		if (listener != null) {
			listener.operationClpComplete(numeComanda, result);
		}

	}

	public ComandaCLP decodeArticoleComanda(String JSONString) {

		ComandaCLP comanda = new ComandaCLP();
		DateLivrareCLP dateLivrare = new DateLivrareCLP();
		List<ArticolCLP> listArticole = new ArrayList<ArticolCLP>();

		try {

			JSONObject json = (JSONObject) new JSONTokener(JSONString).nextValue();

			if (json instanceof JSONObject) {

				JSONObject jsonLivrare = json.getJSONObject("dateLivrare");
				dateLivrare.setPersContact(jsonLivrare.getString("persContact"));
				dateLivrare.setTelefon(jsonLivrare.getString("telefon"));
				dateLivrare.setAdrLivrare(jsonLivrare.getString("adrLivrare"));
				dateLivrare.setOras(jsonLivrare.getString("oras"));
				dateLivrare.setJudet(ClientiGenericiGedInfoStrings.numeJudet(jsonLivrare.getString("codJudet")));
				dateLivrare.setData(jsonLivrare.getString("data"));
				dateLivrare.setTipMarfa(jsonLivrare.getString("tipMarfa"));
				dateLivrare.setMasa(jsonLivrare.getString("masa"));
				dateLivrare.setTipCamion(jsonLivrare.getString("tipCamion"));
				dateLivrare.setTipIncarcare(jsonLivrare.getString("tipIncarcare"));
				dateLivrare.setTipPlata(ClientiGenericiGedInfoStrings.getTipPlata(jsonLivrare.getString("tipPlata")));
				dateLivrare.setMijlocTransport(ClientiGenericiGedInfoStrings.getTipTransport(jsonLivrare.getString("mijlocTransport")));
				dateLivrare.setAprobatOC(ClientiGenericiGedInfoStrings.getTipAprobare(jsonLivrare.getString("aprobatOC")));
				dateLivrare.setDeSters(jsonLivrare.getString("deSters"));
				dateLivrare.setStatusAprov(jsonLivrare.getString("statusAprov"));
				dateLivrare.setValComanda(jsonLivrare.getString("valComanda"));
				dateLivrare.setObsComanda(jsonLivrare.getString("obsComanda"));
				dateLivrare.setValTransp(jsonLivrare.getString("valTransp"));
				dateLivrare.setProcTransp(jsonLivrare.getString("procTransp"));
				dateLivrare.setAcceptDV(jsonLivrare.getString("acceptDV"));
				dateLivrare.setDataIncarcare(jsonLivrare.getString("dataIncarcare"));

				ArticolCLP articol;
				JSONArray jsonArticole = json.getJSONArray("articole");

				for (int i = 0; i < jsonArticole.length(); i++) {
					JSONObject articolObject = jsonArticole.getJSONObject(i);

					articol = new ArticolCLP();
					articol.setCod(articolObject.getString("cod"));
					articol.setNume(articolObject.getString("nume"));
					articol.setCantitate(articolObject.getString("cantitate"));
					articol.setUmBaza(articolObject.getString("umBaza"));
					articol.setDepozit(articolObject.getString("depozit"));

					if (articolObject.getString("status").equals("9"))
						articol.setStatus("Stoc insuficient");
					else
						articol.setStatus("");

					listArticole.add(articol);

				}

			}

			comanda.setDateLivrare(dateLivrare);
			comanda.setArticole(listArticole);

		} catch (Exception ex) {
			Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();
		}

		return comanda;

	}

	public String serializeComandaClp(ComandaCreataCLP comandaCLP) {

		JSONObject jsonComanda = new JSONObject();

		JSONObject jsonAntet = new JSONObject();

		AntetComandaCLP antentComanda = comandaCLP.getAntetComandaCLP();

		try {

			jsonAntet.put("codClient", antentComanda.getCodClient());
			jsonAntet.put("codJudet", antentComanda.getCodJudet());
			jsonAntet.put("localitate", antentComanda.getLocalitate());
			jsonAntet.put("strada", antentComanda.getStrada());
			jsonAntet.put("persCont", antentComanda.getPersCont());
			jsonAntet.put("telefon", antentComanda.getTelefon());
			jsonAntet.put("codFilialaDest", antentComanda.getCodFilialaDest());
			jsonAntet.put("dataLivrare", antentComanda.getDataLivrare());
			jsonAntet.put("tipPlata", antentComanda.getTipPlata());
			jsonAntet.put("tipTransport", antentComanda.getTipTransport());
			jsonAntet.put("depozDest", antentComanda.getDepozDest());
			jsonAntet.put("selectedAgent", antentComanda.getSelectedAgent());
			jsonAntet.put("cmdFasonate", antentComanda.isCmdFasonate());
			jsonAntet.put("numeClientCV", antentComanda.getNumeClientCV());
			jsonAntet.put("observatii", antentComanda.getObservatiiCLP());
			jsonAntet.put("tipMarfa", antentComanda.getTipMarfa());
			jsonAntet.put("masaMarfa", antentComanda.getMasaMarfa());
			jsonAntet.put("tipCamion", antentComanda.getTipCamion());
			jsonAntet.put("tipIncarcare", antentComanda.getTipIncarcare());
			jsonAntet.put("tonaj", antentComanda.getTonaj());
			jsonAntet.put("observatiiCLP", antentComanda.getObservatiiCLP());
			jsonAntet.put("prelucrare", antentComanda.getPrelucrare());

			JSONArray jsonArray = new JSONArray();
			JSONObject jsonObject = null;

			for (ArticolCLP articol : comandaCLP.getListaArticoleComanda()) {
				jsonObject = new JSONObject();
				jsonObject.put("cod", articol.getCod());
				jsonObject.put("cantitate", articol.getCantitate());
				jsonObject.put("umBaza", articol.getUmBaza());
				jsonObject.put("depozit", articol.getDepozit());
				jsonObject.put("depart", articol.getDepart());
				jsonArray.put(jsonObject);

			}

			jsonComanda.put("antetComanda", jsonAntet.toString());
			jsonComanda.put("listArticole", jsonArray.toString());

		} catch (JSONException e) {

			e.printStackTrace();
		}

		return jsonComanda.toString();
	}

}
