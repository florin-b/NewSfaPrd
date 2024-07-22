package my.logon.screen.model;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.logon.screen.beans.BeanAdresaLivrare;
import my.logon.screen.beans.BeanClient;
import my.logon.screen.beans.BeanDatePersonale;
import my.logon.screen.beans.ClientAlocat;
import my.logon.screen.beans.DetaliiClient;
import my.logon.screen.beans.InfoCredit;
import my.logon.screen.beans.PlatitorTva;
import my.logon.screen.enums.EnumClienti;
import my.logon.screen.enums.EnumTipClientIP;
import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.listeners.OperatiiClientListener;
import my.logon.screen.screens.AsyncTaskWSCall;

public class OperatiiClient implements AsyncTaskListener {

	Context context;
	OperatiiClientListener listener;
	EnumClienti numeComanda;

	public OperatiiClient(Context context) {
		this.context = context;
	}

	public void getListClienti(HashMap<String, String> params) {
		numeComanda = EnumClienti.GET_LISTA_CLIENTI;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();

	}

	public void getListMeseriasi(HashMap<String, String> params) {
		numeComanda = EnumClienti.GET_LISTA_MESERIASI;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();

	}

	public void getListClientiCV(HashMap<String, String> params) {
		numeComanda = EnumClienti.GET_LISTA_CLIENTI_CV;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();

	}

	public void getDetaliiClient(HashMap<String, String> params) {
		numeComanda = EnumClienti.GET_DETALII_CLIENT;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public void getAdreseLivrareClient(HashMap<String, String> params) {
		numeComanda = EnumClienti.GET_ADRESE_LIVRARE;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public void getStarePlatitorTva(HashMap<String, String> params) {
		numeComanda = EnumClienti.GET_STARE_TVA;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public void getMeseriasi(HashMap<String, String> params) {
		numeComanda = EnumClienti.GET_MESERIASI;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public void getCnpClient(HashMap<String, String> params) {
		numeComanda = EnumClienti.GET_CNP_CLIENT;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public void getClientiInstitPub(HashMap<String, String> params) {
		numeComanda = EnumClienti.GET_CLIENTI_INST_PUB;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public void getClientiAlocati(HashMap<String, String> params) {
		numeComanda = EnumClienti.GET_CLIENTI_ALOCATI;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public void getAgentComanda(HashMap<String, String> params) {
		numeComanda = EnumClienti.GET_AGENT_COMANDA;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public void getTermenPlata(HashMap<String, String> params) {
		numeComanda = EnumClienti.GET_TERMEN_PLATA;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public void getInfoCredit(HashMap<String, String> params) {
		numeComanda = EnumClienti.GET_INFO_CREDIT;
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public ArrayList<BeanClient> deserializeListClienti(String serializedListClienti) {
		BeanClient client = null;
		ArrayList<BeanClient> listClienti = new ArrayList<BeanClient>();

		try {
			Object json = new JSONTokener(serializedListClienti).nextValue();

			if (json instanceof JSONArray) {

				JSONArray jsonArray = new JSONArray(serializedListClienti);

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject object = jsonArray.getJSONObject(i);

					client = new BeanClient();
					client.setCodClient(object.getString("codClient"));
					client.setNumeClient(object.getString("numeClient"));
					client.setTipClient(object.getString("tipClient"));
					client.setAgenti(object.getString("agenti"));

					if (object.has("codCUI") && object.getString("codCUI") != "null")
						client.setCodCUI(object.getString("codCUI"));

					if (object.has("filiala") && object.getString("filiala") != "null")
						client.setFilialaClientIP(object.getString("filiala"));

					if (object.has("termenPlata") && object.getString("termenPlata") != "null") {
						JSONArray arrayPlata = new JSONArray(object.getString("termenPlata"));

						List<String> listPlata = new ArrayList<String>();

						for (int j = 0; j < arrayPlata.length(); j++) {
							listPlata.add(arrayPlata.getString(j));

						}
						client.setTermenPlata(listPlata);
					}

					if (object.has("tipClientIP") && object.getString("tipClientIP") != "null") {
						if (object.getString("tipClientIP").equals("CONSTR"))
							client.setTipClientIP(EnumTipClientIP.CONSTR);
						else
							client.setTipClientIP(EnumTipClientIP.NONCONSTR);
					} else
						client.setTipClientIP(null);

					if (object.has("clientBlocat") && object.getString("clientBlocat") != "null")
						client.setClientBlocat(Boolean.parseBoolean(object.getString("clientBlocat")));

					if (object.has("tipPlata") && object.getString("tipPlata") != "null")
						client.setTipPlata(object.getString("tipPlata"));

					if (object.has("localitate") && object.getString("localitate") != "null")
						client.setLocalitate(object.getString("localitate"));

					if (object.has("codJudet") && object.getString("codJudet") != "null")
						client.setCodJudet(object.getString("codJudet"));

					if (object.has("strada") && object.getString("strada") != "null")
						client.setStrada(object.getString("strada"));

					if (object.has("diviziiClient") && object.getString("diviziiClient") != "null")
						client.setDiviziiClient(object.getString("diviziiClient"));

					listClienti.add(client);
				}

			}
		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return listClienti;
	}

	public DetaliiClient deserializeDetaliiClient(String serializedDetaliiClient) {
		DetaliiClient detaliiClient = new DetaliiClient();

		try {
			JSONObject jsonObject = new JSONObject(serializedDetaliiClient);

			if (jsonObject instanceof JSONObject) {

				detaliiClient.setRegiune(jsonObject.getString("regiune"));
				detaliiClient.setOras(jsonObject.getString("oras"));
				detaliiClient.setStrada(jsonObject.getString("strada"));
				detaliiClient.setNrStrada(jsonObject.getString("nrStrada"));
				detaliiClient.setLimitaCredit(jsonObject.getString("limitaCredit"));
				detaliiClient.setRestCredit(jsonObject.getString("restCredit"));
				detaliiClient.setStare(jsonObject.getString("stare"));
				detaliiClient.setPersContact(jsonObject.getString("persContact"));
				detaliiClient.setTelefon(jsonObject.getString("telefon"));
				detaliiClient.setFiliala(jsonObject.getString("filiala"));
				detaliiClient.setMotivBlocare(jsonObject.getString("motivBlocare"));
				detaliiClient.setCursValutar(jsonObject.getString("cursValutar"));
				detaliiClient.setTermenPlata(jsonObject.getString("termenPlata"));
				detaliiClient.setTipClient(ClientiGenericiGedInfoStrings.getTipClient(jsonObject.getString("tipClient")));
				detaliiClient.setFurnizor(Boolean.valueOf(jsonObject.getString("isFurnizor")));
				detaliiClient.setDivizii(jsonObject.getString("divizii"));

				if (jsonObject.has("tipPlata"))
					detaliiClient.setTipPlata(jsonObject.getString("tipPlata"));

				if (jsonObject.has("errMsg"))
					detaliiClient.setErrMsg(jsonObject.getString("errMsg"));

				detaliiClient.setEmail(jsonObject.getString("email"));
			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return detaliiClient;

	}

	public List<BeanAdresaLivrare> deserializeAdreseLivrare(String adreseLivrare) {
		BeanAdresaLivrare oAdresa = null;
		ArrayList<BeanAdresaLivrare> objectsList = new ArrayList<BeanAdresaLivrare>();

		JSONArray jsonObject = null;

		try {

			Object json = new JSONTokener(adreseLivrare).nextValue();

			if (json instanceof JSONArray) {
				jsonObject = new JSONArray(adreseLivrare);

				for (int i = 0; i < jsonObject.length(); i++) {
					JSONObject clienObject = jsonObject.getJSONObject(i);

					oAdresa = new BeanAdresaLivrare();
					oAdresa.setOras(clienObject.getString("oras"));
					oAdresa.setStrada(clienObject.getString("strada"));
					oAdresa.setNrStrada(clienObject.getString("nrStrada"));
					oAdresa.setCodJudet(clienObject.getString("codJudet"));
					oAdresa.setCodAdresa(clienObject.getString("codAdresa"));

					objectsList.add(oAdresa);

				}
			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return objectsList;
	}

	public List<String> deserializeTermenPlata(String termenPlata) {

		ArrayList<String> listTermen = new ArrayList<String>();

		JSONArray jsonObject = null;

		try {
			Object json = new JSONTokener(termenPlata).nextValue();

			if (json instanceof JSONArray) {
				jsonObject = new JSONArray(termenPlata);

				for (int i = 0; i < jsonObject.length(); i++) {
					listTermen.add(jsonObject.getString(i));

				}
			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return listTermen;
	}

	public PlatitorTva deserializePlatitorTva(String result) {
		PlatitorTva platitorTva = new PlatitorTva();

		try {
			JSONObject jsonObject = new JSONObject(result);

			if (jsonObject instanceof JSONObject) {

				platitorTva.setPlatitor(Boolean.parseBoolean(jsonObject.getString("isPlatitor")));
				platitorTva.setNumeClient(jsonObject.getString("numeClient"));
				platitorTva.setNrInreg(jsonObject.getString("nrInreg"));
				platitorTva.setErrMessage(jsonObject.getString("errMessage") != "null" ? jsonObject.getString("errMessage") : "");
				platitorTva.setCodJudet(jsonObject.getString("codJudet"));
				platitorTva.setLocalitate(jsonObject.getString("localitate"));
				platitorTva.setStrada(jsonObject.getString("strada"));
				platitorTva.setStareInregistrare(jsonObject.getString("stareInregistrare"));
				platitorTva.setDiviziiClient(jsonObject.getString("diviziiClient"));

			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return platitorTva;
	}

	public List<BeanDatePersonale> deserializeDatePersonale(String result) {
		List<BeanDatePersonale> listDate = new ArrayList<BeanDatePersonale>();

		try {

			Object json = new JSONTokener(result).nextValue();

			if (json instanceof JSONArray) {
				JSONArray jsonObject = new JSONArray(result);

				for (int i = 0; i < jsonObject.length(); i++) {
					JSONObject dateObject = jsonObject.getJSONObject(i);

					BeanDatePersonale datePersonale = new BeanDatePersonale();

					datePersonale.setCnp(dateObject.getString("cnp"));
					datePersonale.setNume(dateObject.getString("nume"));
					datePersonale.setCodjudet(dateObject.getString("codjudet"));
					datePersonale.setLocalitate(dateObject.getString("localitate"));
					datePersonale.setStrada(dateObject.getString("strada"));

					if (dateObject.has("termenPlata") && dateObject.getString("termenPlata") != "null") {
						JSONArray arrayPlata = new JSONArray(dateObject.getString("termenPlata"));

						List<String> listPlata = new ArrayList<String>();

						for (int j = 0; j < arrayPlata.length(); j++) {
							listPlata.add(arrayPlata.getString(j));

						}
						datePersonale.setTermenPlata(listPlata);
					}

					if (dateObject.has("clientBlocat") && dateObject.getString("clientBlocat") != "null")
						datePersonale.setClientBlocat(Boolean.parseBoolean(dateObject.getString("clientBlocat")));

					if (dateObject.has("tipPlata") && dateObject.getString("tipPlata") != "null")
						datePersonale.setTipPlata(dateObject.getString("tipPlata"));

					if (dateObject.has("codClient") && dateObject.getString("codClient") != "null")
						datePersonale.setCodClient(dateObject.getString("codClient"));

					datePersonale.setDivizii(dateObject.getString("divizii"));

					listDate.add(datePersonale);

				}

			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return listDate;
	}

	public List<ClientAlocat> deserializeClientiAlocati(String result) {
		List<ClientAlocat> listClienti = new ArrayList<ClientAlocat>();

		try {
			Object json = new JSONTokener(result).nextValue();

			if (json instanceof JSONArray) {
				JSONArray jsonObject = new JSONArray(result);

				for (int i = 0; i < jsonObject.length(); i++) {
					JSONObject dateObject = jsonObject.getJSONObject(i);

					ClientAlocat client = new ClientAlocat();

					client.setNumeClient(dateObject.getString("numeClient"));
					client.setTipClient01(dateObject.getString("tipClient01"));
					client.setTipClient02(dateObject.getString("tipClient02"));
					client.setTipClient03(dateObject.getString("tipClient03"));
					client.setTipClient04(dateObject.getString("tipClient04"));
					client.setTipClient05(dateObject.getString("tipClient05"));
					client.setTipClient06(dateObject.getString("tipClient06"));
					client.setTipClient07(dateObject.getString("tipClient07"));
					client.setTipClient08(dateObject.getString("tipClient08"));
					client.setTipClient09(dateObject.getString("tipClient09"));

					listClienti.add(client);

				}

			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return listClienti;
	}

	public InfoCredit deserializeInfoCreditClient(String result) {
		InfoCredit infoCredit = new InfoCredit();

		try {
			JSONObject jsonObject = new JSONObject(result);

			if (jsonObject instanceof JSONObject) {

				infoCredit.setLimitaCredit(Double.parseDouble(jsonObject.getString("limitaCredit")));
				infoCredit.setRestCredit(Double.parseDouble(jsonObject.getString("restCredit")));
				infoCredit.setBlocat(Boolean.parseBoolean(jsonObject.getString("isBlocat")));
				infoCredit.setMotivBlocat(jsonObject.getString("motivBlocat"));
			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return infoCredit;

	}

	public void onTaskComplete(String methodName, Object result) {
		if (listener != null) {
			listener.operationComplete(numeComanda, result);
		}
	}

	public void setOperatiiClientListener(OperatiiClientListener listener) {
		this.listener = listener;
	}

}
