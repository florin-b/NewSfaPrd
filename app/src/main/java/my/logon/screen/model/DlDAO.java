package my.logon.screen.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.listeners.DlDAOListener;
import my.logon.screen.screens.AsyncTaskWSCall;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.widget.Toast;
import my.logon.screen.beans.ArticolCLP;
import my.logon.screen.beans.ComandaCLP;
import my.logon.screen.beans.DateLivrareCLP;
import my.logon.screen.enums.EnumDlDAO;

public class DlDAO implements IDlDAO, AsyncTaskListener {

	private Context context;
	private EnumDlDAO numeComanda;
	private DlDAOListener listener;

	public DlDAO(Context context) {
		this.context = context;
	}

	public void getListComenzi(HashMap<String, String> params) {
		numeComanda = EnumDlDAO.GET_LIST_COMENZI;
		performOperation(params);
	}

	public void getArticoleComanda(HashMap<String, String> params) {
		numeComanda = EnumDlDAO.GET_ARTICOLE_COMANDA;
		performOperation(params);
	}

	public void operatiiComanda(HashMap<String, String> params) {
		numeComanda = EnumDlDAO.OPERATIE_COMANDA;
		performOperation(params);
	}

	public void salveazaComanda(HashMap<String, String> params) {
		numeComanda = EnumDlDAO.SALVEAZA_COMANDA;
		performOperation(params);
	}

	public void getArticoleComandaJSON(HashMap<String, String> params) {
		numeComanda = EnumDlDAO.GET_ARTICOLE_COMANDA_JSON;
		performOperation(params);

	}

	private void performOperation(HashMap<String, String> params) {
		AsyncTaskListener contextListener = (AsyncTaskListener) DlDAO.this;
		AsyncTaskWSCall call = new AsyncTaskWSCall(context, contextListener, numeComanda.getComanda(), params);
		call.getCallResultsFromFragment();
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
				dateLivrare.setNrCT(jsonLivrare.getString("nrCT"));
				

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

	public void setDlDAOListener(DlDAOListener listener) {
		this.listener = listener;
	}

	public void onTaskComplete(String methodName, Object result) {
		if (listener != null) {
			listener.operationDlComplete(numeComanda, result);
		}

	}

}
