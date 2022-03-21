package my.logon.screen.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.listeners.OperatiiSalarizareListener;
import my.logon.screen.screens.AsyncTaskWSCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;
import my.logon.screen.beans.BeanSalarizareAgent;
import my.logon.screen.beans.BeanSalarizareAgentAfis;
import my.logon.screen.beans.BeanSalarizareSD;
import my.logon.screen.beans.SalarizareDatePrincipale;
import my.logon.screen.beans.SalarizareDetaliiBaza;
import my.logon.screen.beans.SalarizareDetaliiCVS;
import my.logon.screen.beans.SalarizareDetaliiCorectie;
import my.logon.screen.beans.SalarizareDetaliiInc08;
import my.logon.screen.beans.SalarizareDetaliiMalus;
import my.logon.screen.beans.SalarizareDetaliiTCF;
import my.logon.screen.enums.EnumOperatiiSalarizare;

public class OperatiiSalarizare implements AsyncTaskListener {

	private Context context;
	private EnumOperatiiSalarizare numeComanda;
	private HashMap<String, String> params;
	private OperatiiSalarizareListener listener;

	public OperatiiSalarizare(Context context) {
		this.context = context;
	}

	public void getSalarizareAgent(HashMap<String, String> params) {
		this.params = params;
		this.numeComanda = EnumOperatiiSalarizare.GET_SALARIZARE_AGENT;
		performOperation();
	}

	public void getSalarizareDepartament(HashMap<String, String> params) {
		this.params = params;
		this.numeComanda = EnumOperatiiSalarizare.GET_SALARIZARE_DEPART;
		performOperation();
	}

	public void getSalarizareSD(HashMap<String, String> params) {
		this.params = params;
		this.numeComanda = EnumOperatiiSalarizare.GET_SALARIZARE_SD;
		performOperation();
	}

	public void getSalarizareSDKA(HashMap<String, String> params) {
		this.params = params;
		this.numeComanda = EnumOperatiiSalarizare.GET_SALARIZARE_SDKA;
		performOperation();
	}

	public void getSalarizareKA(HashMap<String, String> params) {
		this.params = params;
		this.numeComanda = EnumOperatiiSalarizare.GET_SALARIZARE_KA;
		performOperation();
	}

	public void getSalarizareDepartamentKA(HashMap<String, String> params) {
		this.params = params;
		this.numeComanda = EnumOperatiiSalarizare.GET_SALARIZARE_DEPART_KA;
		performOperation();
	}

	private void performOperation() {
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getNumeComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResultsFromFragment();
	}

	public BeanSalarizareAgent deserializeSalarizareAgent(String result) {
		BeanSalarizareAgent salarizare = new BeanSalarizareAgent();

		try {
			JSONObject jsonObject = new JSONObject((String) result);

			SalarizareDatePrincipale datePrincipale = new SalarizareDatePrincipale();

			JSONObject jsonDatePrinc = new JSONObject(jsonObject.getString("datePrincipale"));
			datePrincipale.setVenitMJ_T1(Double.valueOf(jsonDatePrinc.getString("venitMJ_T1")));
			datePrincipale.setVenitTCF(Double.valueOf(jsonDatePrinc.getString("venitTCF")));
			datePrincipale.setCorectieIncasare(Double.valueOf(jsonDatePrinc.getString("corectieIncasare")));
			datePrincipale.setVenitFinal(Double.valueOf(jsonDatePrinc.getString("venitFinal")));

			salarizare.setDatePrincipale(datePrincipale);

			List<SalarizareDetaliiBaza> listDetaliiBaza = new ArrayList<SalarizareDetaliiBaza>();

			JSONArray jsonArray = new JSONArray(jsonObject.getString("detaliiBaza"));

			for (int i = 0; i < jsonArray.length(); i++) {

				JSONObject detObject = jsonArray.getJSONObject(i);
				SalarizareDetaliiBaza detaliiBaza = new SalarizareDetaliiBaza();

				detaliiBaza.setNumeClient(detObject.getString("numeClient"));
				detaliiBaza.setCodSintetic(detObject.getString("codSintetic"));
				detaliiBaza.setNumeSintetic(detObject.getString("numeSintetic"));
				detaliiBaza.setValoareNeta(Double.valueOf(detObject.getString("valoareNeta")));
				detaliiBaza.setT0(Double.valueOf(detObject.getString("T0")));
				detaliiBaza.setT1A(Double.valueOf(detObject.getString("T1A")));
				detaliiBaza.setT1D(Double.valueOf(detObject.getString("T1D")));
				detaliiBaza.setT1(Double.valueOf(detObject.getString("T1")));
				detaliiBaza.setVenitBaza(Double.valueOf(detObject.getString("venitBaza")));

				listDetaliiBaza.add(detaliiBaza);

			}

			salarizare.setDetaliiBaza(listDetaliiBaza);

			SalarizareDetaliiTCF detaliiTCF = new SalarizareDetaliiTCF();
			JSONObject jsonDetaliiTCF = new JSONObject(jsonObject.getString("detaliiTCF"));
			detaliiTCF.setVenitBaza(Double.valueOf(jsonDetaliiTCF.getString("venitBaza")));
			detaliiTCF.setClientiAnterior(jsonDetaliiTCF.getString("clientiAnterior"));
			detaliiTCF.setTarget(jsonDetaliiTCF.getString("target"));
			detaliiTCF.setClientiCurent(jsonDetaliiTCF.getString("clientiCurent"));
			detaliiTCF.setCoeficient(Double.valueOf(jsonDetaliiTCF.getString("coeficient")));
			detaliiTCF.setVenitTcf(Double.valueOf(jsonDetaliiTCF.getString("venitTcf")));

			salarizare.setDetaliiTCF(detaliiTCF);

			SalarizareDetaliiCorectie detaliiCorectie = new SalarizareDetaliiCorectie();
			JSONObject jsonDetaliiCorectie = new JSONObject(jsonObject.getString("detaliiCorectie"));
			detaliiCorectie.setVenitBaza(Double.valueOf(jsonDetaliiCorectie.getString("venitBaza")));
			detaliiCorectie.setIncasari08(Double.valueOf(jsonDetaliiCorectie.getString("incasari08")));
			detaliiCorectie.setMalus(Double.valueOf(jsonDetaliiCorectie.getString("malus")));
			detaliiCorectie.setVenitCorectat(Double.valueOf(jsonDetaliiCorectie.getString("venitCorectat")));

			salarizare.setDetaliiCorectie(detaliiCorectie);

			List<SalarizareDetaliiInc08> listDetaliiInc08 = new ArrayList<SalarizareDetaliiInc08>();
			JSONArray jsonDetaliiInc08 = new JSONArray(jsonObject.getString("detaliiIncasari08"));

			for (int i = 0; i < jsonDetaliiInc08.length(); i++) {

				SalarizareDetaliiInc08 detaliiInc08 = new SalarizareDetaliiInc08();
				JSONObject detObject08 = jsonDetaliiInc08.getJSONObject(i);

				detaliiInc08.setNumeClient(detObject08.getString("numeClient"));
				detaliiInc08.setValoareIncasare(Double.valueOf(detObject08.getString("valoareIncasare")));
				detaliiInc08.setVenitCorectat(Double.valueOf(detObject08.getString("venitCorectat")));
				listDetaliiInc08.add(detaliiInc08);

			}

			salarizare.setDetaliiInc08(listDetaliiInc08);

			List<SalarizareDetaliiMalus> listDetaliiMalus = new ArrayList<SalarizareDetaliiMalus>();
			JSONArray jsonDetaliiMalus = new JSONArray(jsonObject.getString("detaliiMalus"));

			for (int i = 0; i < jsonDetaliiMalus.length(); i++) {

				SalarizareDetaliiMalus detaliiMalus = new SalarizareDetaliiMalus();
				JSONObject detObjectMalus = jsonDetaliiMalus.getJSONObject(i);

				detaliiMalus.setNumeClient(detObjectMalus.getString("numeClient"));
				detaliiMalus.setCodClient(detObjectMalus.getString("codClient"));
				detaliiMalus.setValoareFactura(Double.valueOf(detObjectMalus.getString("valoareFactura")));
				detaliiMalus.setPenalizare(Double.valueOf(detObjectMalus.getString("penalizare")));

				detaliiMalus.setNrFactura(detObjectMalus.getString("nrFactura"));
				detaliiMalus.setDataFactura(detObjectMalus.getString("dataFactura"));
				detaliiMalus.setTpFact(Integer.parseInt(detObjectMalus.getString("tpFact")));
				detaliiMalus.setTpAgreat(Integer.parseInt(detObjectMalus.getString("tpAgreat")));
				detaliiMalus.setTpIstoric(Integer.parseInt(detObjectMalus.getString("tpIstoric")));
				detaliiMalus.setValIncasare(Double.parseDouble(detObjectMalus.getString("valIncasare")));
				detaliiMalus.setDataIncasare(detObjectMalus.getString("dataIncasare"));
				detaliiMalus.setZileIntarziere(Integer.parseInt(detObjectMalus.getString("zileIntarziere")));
				detaliiMalus.setCoefPenalizare(Double.parseDouble(detObjectMalus.getString("coefPenalizare")));

				listDetaliiMalus.add(detaliiMalus);
			}

			salarizare.setDetaliiMalus(listDetaliiMalus);

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return salarizare;
	}

	public List<BeanSalarizareAgentAfis> deserializeSalarizareDepartament(String result) {

		List<BeanSalarizareAgentAfis> listAgenti = new ArrayList<BeanSalarizareAgentAfis>();

		try {

			JSONArray jsonAgenti = new JSONArray(result);

			for (int i = 0; i < jsonAgenti.length(); i++) {

				BeanSalarizareAgentAfis salarizareAg = new BeanSalarizareAgentAfis();

				JSONObject objAgent = jsonAgenti.getJSONObject(i);

				salarizareAg.setCodAgent(objAgent.getString("codAgent"));
				salarizareAg.setNumeAgent(objAgent.getString("numeAgent"));

				SalarizareDatePrincipale datePrincipale = new SalarizareDatePrincipale();

				JSONObject jsonDatePrinc = new JSONObject(objAgent.getString("datePrincipale"));

				datePrincipale.setVenitMJ_T1(Double.valueOf(jsonDatePrinc.getString("venitMJ_T1")));
				datePrincipale.setVenitTCF(Double.valueOf(jsonDatePrinc.getString("venitTCF")));
				datePrincipale.setCorectieIncasare(Double.valueOf(jsonDatePrinc.getString("corectieIncasare")));
				datePrincipale.setVenitFinal(Double.valueOf(jsonDatePrinc.getString("venitFinal")));

				salarizareAg.setDatePrincipale(datePrincipale);
				listAgenti.add(salarizareAg);

			}

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return listAgenti;
	}

	public BeanSalarizareSD deserializeSalarizareSD(String result) {

		BeanSalarizareSD salarizareSD = new BeanSalarizareSD();
		BeanSalarizareAgent salarizareAgent = deserializeSalarizareAgent(result);
		List<SalarizareDetaliiCVS> listCVS = new ArrayList<SalarizareDetaliiCVS>();

		try {

			JSONObject jsonObject = new JSONObject((String) result);

			JSONObject jsonDatePrinc = new JSONObject(jsonObject.getString("datePrincipale"));

			SalarizareDatePrincipale datePrincipale = salarizareAgent.getDatePrincipale();
			datePrincipale.setVenitCVS(Double.valueOf(jsonDatePrinc.getString("venitCVS")));
			salarizareSD.setDatePrincipale(datePrincipale);

			JSONArray jsonCSV = new JSONArray(jsonObject.getString("detaliiCVS"));

			for (int i = 0; i < jsonCSV.length(); i++) {

				SalarizareDetaliiCVS detaliuCVS = new SalarizareDetaliiCVS();
				JSONObject objCsv = jsonCSV.getJSONObject(i);

				detaliuCVS.setAgent(objCsv.getString("agent"));
				detaliuCVS.setPondere(Double.valueOf(objCsv.getString("pondere")));
				detaliuCVS.setTargetValoric(Double.valueOf(objCsv.getString("targetValoric")));
				detaliuCVS.setValoareFTVA(Double.valueOf(objCsv.getString("valoareFTVA")));
				detaliuCVS.setValoareP6V(Double.valueOf(objCsv.getString("valoareP6V")));
				detaliuCVS.setVenitBaza(Double.valueOf(objCsv.getString("venitBaza")));
				detaliuCVS.setVenitCvs(Double.valueOf(objCsv.getString("venitCvs")));
				detaliuCVS.setCvs(Double.valueOf(objCsv.getString("cvs")));

				listCVS.add(detaliuCVS);

			}

			List<SalarizareDetaliiMalus> listDetaliiMalus = new ArrayList<SalarizareDetaliiMalus>();
			JSONArray jsonDetaliiMalus = new JSONArray(jsonObject.getString("detaliiMalus"));

			for (int i = 0; i < jsonDetaliiMalus.length(); i++) {

				SalarizareDetaliiMalus detaliiMalus = new SalarizareDetaliiMalus();
				JSONObject detObjectMalus = jsonDetaliiMalus.getJSONObject(i);

				detaliiMalus.setNumeClient(detObjectMalus.getString("numeClient"));
				detaliiMalus.setCodClient(detObjectMalus.getString("codClient"));
				detaliiMalus.setValoareFactura(Double.valueOf(detObjectMalus.getString("valoareFactura")));
				detaliiMalus.setPenalizare(Double.valueOf(detObjectMalus.getString("penalizare")));

				detaliiMalus.setNrFactura(detObjectMalus.getString("nrFactura"));
				detaliiMalus.setDataFactura(detObjectMalus.getString("dataFactura"));
				detaliiMalus.setTpFact(Integer.parseInt(detObjectMalus.getString("tpFact")));
				detaliiMalus.setTpAgreat(Integer.parseInt(detObjectMalus.getString("tpAgreat")));
				detaliiMalus.setTpIstoric(Integer.parseInt(detObjectMalus.getString("tpIstoric")));
				detaliiMalus.setValIncasare(Double.parseDouble(detObjectMalus.getString("valIncasare")));
				detaliiMalus.setDataIncasare(detObjectMalus.getString("dataIncasare"));
				detaliiMalus.setZileIntarziere(Integer.parseInt(detObjectMalus.getString("zileIntarziere")));
				detaliiMalus.setCoefPenalizare(Double.parseDouble(detObjectMalus.getString("coefPenalizare")));

				listDetaliiMalus.add(detaliiMalus);
			}

			salarizareSD.setDetaliiMalus(listDetaliiMalus);

			salarizareSD.setDatePrincipale(salarizareAgent.getDatePrincipale());
			salarizareSD.setDetaliiBaza(salarizareAgent.getDetaliiBaza());
			salarizareSD.setDetaliiCorectie(salarizareAgent.getDetaliiCorectie());
			salarizareSD.setDetaliiCvs(listCVS);
			salarizareSD.setDetaliiInc08(salarizareAgent.getDetaliiInc08());
			salarizareSD.setDetaliiTCF(salarizareAgent.getDetaliiTCF());

		} catch (JSONException e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
		}

		return salarizareSD;

	}

	public void setListener(OperatiiSalarizareListener listener) {
		this.listener = listener;
	}

	@Override
	public void onTaskComplete(String methodName, Object result) {
		if (listener != null)
			listener.operatiiSalarizareComplete(numeComanda, result);

	}

}
