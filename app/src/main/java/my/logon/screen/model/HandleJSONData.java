package my.logon.screen.model;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

import my.logon.screen.beans.BeanAdresaGpsClient;
import my.logon.screen.beans.BeanAdresaLivrare;
import my.logon.screen.beans.BeanArticolVanzari;
import my.logon.screen.beans.BeanClient;
import my.logon.screen.beans.BeanClientInactiv;
import my.logon.screen.beans.BeanClientSemiactiv;
import my.logon.screen.beans.BeanDocumentCLP;
import my.logon.screen.beans.BeanFacturaNeincasata;
import my.logon.screen.beans.BeanFurnizor;
import my.logon.screen.beans.BeanFurnizorProduse;
import my.logon.screen.beans.BeanInfoVenituri;
import my.logon.screen.beans.BeanIstoricSemiactiv;
import my.logon.screen.beans.BeanObiectivKA;
import my.logon.screen.beans.DLExpirat;

public class HandleJSONData {

	private String JSONString;
	private JSONArray jsonObject;
	private Context context;

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public HandleJSONData(Context context, String JSONString) {
		this.context = context;
		this.JSONString = JSONString;
	}

	public ArrayList<BeanDocumentCLP> decodeJSONDocumentCLP() {
		BeanDocumentCLP unDocumentCLP = null;
		ArrayList<BeanDocumentCLP> objectsList = new ArrayList<BeanDocumentCLP>();

		try {

			Object json = new JSONTokener(JSONString).nextValue();

			if (json instanceof JSONArray) {
				jsonObject = new JSONArray(JSONString);

				for (int i = 0; i < jsonObject.length(); i++) {
					JSONObject documentCLPObject = jsonObject.getJSONObject(i);

					unDocumentCLP = new BeanDocumentCLP();
					unDocumentCLP.setNrDocument(documentCLPObject.getString("nrDocument"));
					unDocumentCLP.setNumeClient(documentCLPObject.getString("numeClient"));
					unDocumentCLP.setNumeAgent(documentCLPObject.getString("numeAgent"));
					unDocumentCLP.setDataDocument(documentCLPObject.getString("dataDocument"));
					unDocumentCLP.setUnitLog(documentCLPObject.getString("unitLog"));
					unDocumentCLP.setNrDocumentSap(documentCLPObject.getString("nrDocumentSap"));
					unDocumentCLP.setStatusDocument(documentCLPObject.getString("statusDocument"));
					unDocumentCLP.setDepozit(documentCLPObject.getString("depozit"));
					unDocumentCLP.setFurnizor(documentCLPObject.getString("furnizor"));
					unDocumentCLP.setObservatii(documentCLPObject.getString("observatii"));

					objectsList.add(unDocumentCLP);

				}
			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return objectsList;
	}

	public ArrayList<DLExpirat> decodeDLExpirat() {
		DLExpirat dlExpirat = null;
		ArrayList<DLExpirat> objectsList = new ArrayList<DLExpirat>();

		try {

			Object json = new JSONTokener(JSONString).nextValue();

			if (json instanceof JSONArray) {
				jsonObject = new JSONArray(JSONString);

				for (int i = 0; i < jsonObject.length(); i++) {
					JSONObject documentCLPObject = jsonObject.getJSONObject(i);

					dlExpirat = new DLExpirat();
					dlExpirat.setNrDocument(documentCLPObject.getString("nrDocument"));
					dlExpirat.setNumeClient(documentCLPObject.getString("numeClient"));
					dlExpirat.setDataDocument(documentCLPObject.getString("dataDocument"));
					dlExpirat.setNrDocumentSap(documentCLPObject.getString("nrDocumentSap"));
					dlExpirat.setDataLivrare(documentCLPObject.getString("dataLivrare"));
					dlExpirat.setFurnizor(documentCLPObject.getString("furnizor"));

					objectsList.add(dlExpirat);

				}
			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return objectsList;
	}
	
	
	public ArrayList<BeanObiectivKA> decodeJSONObiectivKA() {
		BeanObiectivKA unObiectivKA = null;
		ArrayList<BeanObiectivKA> objectsList = new ArrayList<BeanObiectivKA>();

		try {

			Object json = new JSONTokener(JSONString).nextValue();

			if (json instanceof JSONArray) {
				jsonObject = new JSONArray(JSONString);

				for (int i = 0; i < jsonObject.length(); i++) {
					JSONObject obiectivKAObject = jsonObject.getJSONObject(i);

					unObiectivKA = new BeanObiectivKA();
					unObiectivKA.setIdObiectiv(obiectivKAObject.getString("idObiectiv"));
					unObiectivKA.setNumeObiectiv(obiectivKAObject.getString("numeObiectiv"));
					unObiectivKA.setDataObiectiv(obiectivKAObject.getString("dataObiectiv"));
					unObiectivKA.setStatusObiectiv(obiectivKAObject.getString("statusObiectiv"));
					unObiectivKA.setOrasObiectiv(obiectivKAObject.getString("orasObiectiv"));
					unObiectivKA.setStradaObiectiv(obiectivKAObject.getString("stradaObiectiv"));
					unObiectivKA.setAgentObiectiv(obiectivKAObject.getString("agentObiectiv"));

					objectsList.add(unObiectivKA);

				}
			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return objectsList;
	}

	public ArrayList<BeanClient> decodeJSONClientList() {
		BeanClient unClient = null;
		ArrayList<BeanClient> objectsList = new ArrayList<BeanClient>();

		try {

			Object json = new JSONTokener(JSONString).nextValue();

			if (json instanceof JSONArray) {
				jsonObject = new JSONArray(JSONString);

				for (int i = 0; i < jsonObject.length(); i++) {
					JSONObject clienObject = jsonObject.getJSONObject(i);

					unClient = new BeanClient();
					unClient.setNumeClient(clienObject.getString("numeClient"));
					unClient.setCodClient(clienObject.getString("codClient"));
					unClient.setTipClient(clienObject.getString("tipClient"));
					unClient.setAgenti(clienObject.getString("agenti"));
					objectsList.add(unClient);

				}
			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return objectsList;
	}

	public ArrayList<BeanFurnizor> decodeJSONFurnizorList() {
		BeanFurnizor unFurnizor = null;
		ArrayList<BeanFurnizor> objectsList = new ArrayList<BeanFurnizor>();

		try {

			Object json = new JSONTokener(JSONString).nextValue();

			if (json instanceof JSONArray) {
				jsonObject = new JSONArray(JSONString);

				for (int i = 0; i < jsonObject.length(); i++) {
					JSONObject clienObject = jsonObject.getJSONObject(i);

					unFurnizor = new BeanFurnizor();
					unFurnizor.setNumeFurnizor(clienObject.getString("numeFurnizor"));
					unFurnizor.setCodFurnizor(clienObject.getString("codFurnizor"));

					objectsList.add(unFurnizor);

				}
			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return objectsList;
	}

	public ArrayList<BeanFurnizorProduse> decodeJSONFurnizorProduseList() {
		BeanFurnizorProduse unFurnizorProduse = null;
		ArrayList<BeanFurnizorProduse> objectsList = new ArrayList<BeanFurnizorProduse>();

		try {

			Object json = new JSONTokener(JSONString).nextValue();

			if (json instanceof JSONArray) {
				jsonObject = new JSONArray(JSONString);

				for (int i = 0; i < jsonObject.length(); i++) {
					JSONObject clienObject = jsonObject.getJSONObject(i);

					unFurnizorProduse = new BeanFurnizorProduse();
					unFurnizorProduse.setNumeFurnizorProduse(clienObject.getString("numeFurnizorProduse"));
					unFurnizorProduse.setCodFurnizorProduse(clienObject.getString("codFurnizorProduse"));

					objectsList.add(unFurnizorProduse);

				}
			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return objectsList;
	}

	public ArrayList<BeanAdresaLivrare> decodeJSONAdresaLivrare() {
		BeanAdresaLivrare oAdresa = null;
		ArrayList<BeanAdresaLivrare> objectsList = new ArrayList<BeanAdresaLivrare>();

		try {

			Object json = new JSONTokener(JSONString).nextValue();

			if (json instanceof JSONArray) {
				jsonObject = new JSONArray(JSONString);

				for (int i = 0; i < jsonObject.length(); i++) {
					JSONObject clienObject = jsonObject.getJSONObject(i);

					oAdresa = new BeanAdresaLivrare();
					oAdresa.setOras(clienObject.getString("oras"));
					oAdresa.setStrada(clienObject.getString("strada"));
					oAdresa.setNrStrada(clienObject.getString("nrStrada"));
					oAdresa.setCodJudet(clienObject.getString("codJudet"));
					oAdresa.setCodAdresa(clienObject.getString("codAdresa"));
					oAdresa.setTonaj(clienObject.getString("tonaj"));
					
					oAdresa.setOras(Boolean.parseBoolean(clienObject.getString("isOras")));
					oAdresa.setRazaKm(Integer.parseInt(clienObject.getString("razaKm")));
					oAdresa.setCoordsCentru(clienObject.getString("coordsCentru"));
					

					if (clienObject.getString("coords") != null && !clienObject.getString("coords").equals("null"))
						oAdresa.setCoords(clienObject.getString("coords"));

					oAdresa.setCodPostal(clienObject.getString("codPostal"));

					objectsList.add(oAdresa);

				}
			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return objectsList;
	}

	public ArrayList<BeanArticolVanzari> decodeJSONArticolVanzari() {
		BeanArticolVanzari unArticol = null;
		ArrayList<BeanArticolVanzari> objectsList = new ArrayList<BeanArticolVanzari>();

		try {

			Object json = new JSONTokener(JSONString).nextValue();

			if (json instanceof JSONArray) {
				jsonObject = new JSONArray(JSONString);

				for (int i = 0; i < jsonObject.length(); i++) {
					JSONObject clienObject = jsonObject.getJSONObject(i);

					unArticol = new BeanArticolVanzari();
					unArticol.setCodArticol(clienObject.getString("codArticol"));
					unArticol.setNumeArticol(clienObject.getString("numeArticol"));
					unArticol.setCantitateArticol(clienObject.getString("cantitateArticol"));
					unArticol.setValoareArticol(clienObject.getString("valoareArticol"));

					objectsList.add(unArticol);

				}
			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return objectsList;
	}

	public ArrayList<BeanFacturaNeincasata> decodeJSONFacturiNeincasate() {
		BeanFacturaNeincasata oFactura = null;
		ArrayList<BeanFacturaNeincasata> objectsList = new ArrayList<BeanFacturaNeincasata>();

		try {

			Object json = new JSONTokener(JSONString).nextValue();

			if (json instanceof JSONArray) {
				jsonObject = new JSONArray(JSONString);

				for (int i = 0; i < jsonObject.length(); i++) {
					JSONObject clienObject = jsonObject.getJSONObject(i);

					oFactura = new BeanFacturaNeincasata();
					oFactura.setNumeClient(clienObject.getString("numeClient"));
					oFactura.setCodClient(clienObject.getString("codClient"));
					oFactura.setReferinta(clienObject.getString("referinta"));
					oFactura.setEmitere(clienObject.getString("emitere"));
					oFactura.setScadenta(clienObject.getString("scadenta"));
					oFactura.setValoare(clienObject.getString("valoare"));
					oFactura.setIncasat(clienObject.getString("incasat"));
					oFactura.setRest(clienObject.getString("rest"));
					oFactura.setAcoperit(clienObject.getString("acoperit"));
					oFactura.setScadentaBO(clienObject.getString("scadentaBO"));
					oFactura.setTipPlata(clienObject.getString("tipPlata"));
					oFactura.setNumeAgent(clienObject.getString("numeAgent"));

					objectsList.add(oFactura);

				}
			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return objectsList;
	}

	public ArrayList<BeanClientInactiv> decodeJSONClientiInactivi() {
		BeanClientInactiv unClient = null;
		ArrayList<BeanClientInactiv> objectsList = new ArrayList<BeanClientInactiv>();

		try {

			Object json = new JSONTokener(JSONString).nextValue();

			if (json instanceof JSONArray) {
				jsonObject = new JSONArray(JSONString);

				for (int i = 0; i < jsonObject.length(); i++) {
					JSONObject clienObject = jsonObject.getJSONObject(i);

					unClient = new BeanClientInactiv();
					unClient.setNumeClient(clienObject.getString("numeClient"));
					unClient.setCodClient(clienObject.getString("codClient"));
					unClient.setCodAgent(clienObject.getString("codAgent"));
					unClient.setStareClient(clienObject.getString("stareClient"));
					unClient.setTipClient(clienObject.getString("tipClient"));

					objectsList.add(unClient);

				}
			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return objectsList;
	}

	public ArrayList<BeanClientSemiactiv> decodeJSONClientiSemiactivi() {
		BeanClientSemiactiv unClient = null;
		ArrayList<BeanClientSemiactiv> objectsList = new ArrayList<BeanClientSemiactiv>();

		try {

			Object json = new JSONTokener(JSONString).nextValue();

			if (json instanceof JSONArray) {
				jsonObject = new JSONArray(JSONString);

				for (int i = 0; i < jsonObject.length(); i++) {
					JSONObject clienObject = jsonObject.getJSONObject(i);

					unClient = new BeanClientSemiactiv();
					unClient.setNumeClient(clienObject.getString("numeClient"));
					unClient.setCodClient(clienObject.getString("codClient"));
					unClient.setJudet(clienObject.getString("judet"));
					unClient.setLocalitate(clienObject.getString("localitate"));
					unClient.setStrada(clienObject.getString("strada"));
					unClient.setNumePersContact(clienObject.getString("numePersContact"));
					unClient.setTelPersContact(clienObject.getString("telPersContact"));
					unClient.setVanzMedie(clienObject.getString("vanzMedie"));
					unClient.setVanz03(clienObject.getString("vanz03"));
					unClient.setVanz06(clienObject.getString("vanz06"));
					unClient.setVanz07(clienObject.getString("vanz07"));
					unClient.setVanz09(clienObject.getString("vanz09"));
					unClient.setVanz040(clienObject.getString("vanz040"));
					unClient.setVanz041(clienObject.getString("vanz041"));

					objectsList.add(unClient);

				}
			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return objectsList;
	}

	public ArrayList<BeanInfoVenituri> decodeJSONInfoVenituri() {
		BeanInfoVenituri unVenit = null;
		ArrayList<BeanInfoVenituri> objectsList = new ArrayList<BeanInfoVenituri>();

		try {

			Object json = new JSONTokener(JSONString).nextValue();

			if (json instanceof JSONArray) {
				jsonObject = new JSONArray(JSONString);

				for (int i = 0; i < jsonObject.length(); i++) {
					JSONObject clienObject = jsonObject.getJSONObject(i);

					unVenit = new BeanInfoVenituri();
					unVenit.setId(clienObject.getString("id"));
					unVenit.setVenitNetP(clienObject.getString("venitNetP"));
					unVenit.setmP(clienObject.getString("mP"));
					unVenit.setVenitNetP040(clienObject.getString("venitNetP040"));
					unVenit.setmP040(clienObject.getString("mP040"));
					unVenit.setVenitNetP041(clienObject.getString("venitNetP041"));
					unVenit.setmP041(clienObject.getString("mP041"));

					objectsList.add(unVenit);

				}
			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return objectsList;
	}

	public ArrayList<BeanAdresaGpsClient> decodeJSONAdreseGpsClient() {
		BeanAdresaGpsClient oAdresaGps = null;
		ArrayList<BeanAdresaGpsClient> adreseList = new ArrayList<BeanAdresaGpsClient>();

		try {

			Object json = new JSONTokener(JSONString).nextValue();

			if (json instanceof JSONArray) {
				jsonObject = new JSONArray(JSONString);

				for (int i = 0; i < jsonObject.length(); i++) {
					JSONObject clienObject = jsonObject.getJSONObject(i);

					oAdresaGps = new BeanAdresaGpsClient();
					oAdresaGps.setId(String.valueOf(i + 1));
					oAdresaGps.setCodClient(clienObject.getString("codClient"));
					oAdresaGps.setCodAgent(clienObject.getString("codAgent"));
					oAdresaGps.setTipLocatie(clienObject.getString("tipLocatie"));
					oAdresaGps.setDateGps(clienObject.getString("dateGps"));
					oAdresaGps.setData(clienObject.getString("data"));
					oAdresaGps.setOra(clienObject.getString("ora"));
					oAdresaGps.setAdresa(clienObject.getString("adresa"));

					adreseList.add(oAdresaGps);

				}
			}

		} catch (JSONException e) {
			Toast.makeText(context, "JSON: " + e.toString(), Toast.LENGTH_SHORT).show();
		}

		return adreseList;
	}

	public ArrayList<MaterialNecesar> decodeJSONNecesar() {
		MaterialNecesar unMaterial = null;
		ArrayList<MaterialNecesar> materialeList = new ArrayList<MaterialNecesar>();

		try {

			Object json = new JSONTokener(JSONString).nextValue();

			if (json instanceof JSONArray) {
				jsonObject = new JSONArray(JSONString);

				for (int i = 0; i < jsonObject.length(); i++) {
					JSONObject necesarObject = jsonObject.getJSONObject(i);

					unMaterial = new MaterialNecesar();
					unMaterial.setCodArticol(necesarObject.getString("codArticol"));
					unMaterial.setNumeArticol(necesarObject.getString("numeArticol"));
					unMaterial.setCodSintetic(necesarObject.getString("codSintetic"));
					unMaterial.setNumeSintetic(necesarObject.getString("numeSintetic"));
					unMaterial.setConsum30(necesarObject.getString("consum30"));
					unMaterial.setStoc(necesarObject.getString("stoc"));
					unMaterial.setPropunereNecesar(necesarObject.getString("propunereNecesar"));
					unMaterial.setCA(necesarObject.getString("CA"));
					unMaterial.setInterval1(necesarObject.getString("interval1"));
					unMaterial.setInterval2(necesarObject.getString("interval2"));
					unMaterial.setInterval3(necesarObject.getString("interval3"));

					materialeList.add(unMaterial);

				}
			}

		} catch (JSONException e) {
			Toast.makeText(context, "JSON: " + e.toString(), Toast.LENGTH_SHORT).show();
		}

		return materialeList;
	}

	public ArrayList<BeanIstoricSemiactiv> decodeIstoricSemiactivi() {
		BeanIstoricSemiactiv istoric = null;
		ArrayList<BeanIstoricSemiactiv> istoricList = new ArrayList<BeanIstoricSemiactiv>();

		try {

			Object json = new JSONTokener(JSONString).nextValue();

			if (json instanceof JSONArray) {
				jsonObject = new JSONArray(JSONString);

				for (int i = 0; i < jsonObject.length(); i++) {
					JSONObject istoricObject = jsonObject.getJSONObject(i);

					istoric = new BeanIstoricSemiactiv();
					istoric.setCodClient(istoricObject.getString("codClient"));
					istoric.setAn(istoricObject.getString("an"));
					istoric.setLuna(istoricObject.getString("luna"));
					istoric.setVanz03(istoricObject.getString("vanz03"));
					istoric.setVanz06(istoricObject.getString("vanz06"));
					istoric.setVanz07(istoricObject.getString("vanz07"));
					istoric.setVanz09(istoricObject.getString("vanz09"));
					istoric.setVanz040(istoricObject.getString("vanz040"));
					istoric.setVanz041(istoricObject.getString("vanz041"));

					istoricList.add(istoric);

				}
			}

		} catch (JSONException e) {
			Toast.makeText(context, "JSON: " + e.toString(), Toast.LENGTH_SHORT).show();
		}

		return istoricList;
	}

}
