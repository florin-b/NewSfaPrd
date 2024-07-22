package my.logon.screen.model;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.logon.screen.beans.BeanSalarizareAgent;
import my.logon.screen.beans.BeanSalarizareAgentAfis;
import my.logon.screen.beans.BeanSalarizareCVA;
import my.logon.screen.beans.BeanSalarizareSD;
import my.logon.screen.beans.BeanVanzariIncr;
import my.logon.screen.beans.BeanVanzariVS;
import my.logon.screen.beans.SalarizareCVABazaCL;
import my.logon.screen.beans.SalarizareDatePrincipale;
import my.logon.screen.beans.SalarizareDetaliiBaza;
import my.logon.screen.beans.SalarizareDetaliiCVS;
import my.logon.screen.beans.SalarizareDetaliiCorectie;
import my.logon.screen.beans.SalarizareDetaliiInc08;
import my.logon.screen.beans.SalarizareDetaliiMalus;
import my.logon.screen.beans.SalarizareDetaliiTCF;
import my.logon.screen.enums.EnumOperatiiSalarizare;
import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.listeners.OperatiiSalarizareListener;
import my.logon.screen.screens.AsyncTaskWSCall;

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

    public void getSalarizareCVA(HashMap<String, String> params) {
        this.params = params;
        this.numeComanda = EnumOperatiiSalarizare.GET_SALARIZARE_CVA;
        performOperation();
    }

    public void getSalarizareDepartCVA(HashMap<String, String> params) {
        this.params = params;
        this.numeComanda = EnumOperatiiSalarizare.GET_SALARIZARE_DEPART_CVA;
        performOperation();
    }

    public void getSalarizareCVIP(HashMap<String, String> params) {
        this.params = params;
        this.numeComanda = EnumOperatiiSalarizare.GET_SALARIZARE_CVIP;
        performOperation();
    }

    public void getSalarizareDepartCVIP(HashMap<String, String> params) {
        this.params = params;
        this.numeComanda = EnumOperatiiSalarizare.GET_SALARIZARE_DEPART_CVIP;
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

            if (jsonObject.getString("datePrincipale").equals("null"))
                return salarizare;

            SalarizareDatePrincipale datePrincipale = new SalarizareDatePrincipale();

            JSONObject jsonDatePrinc = new JSONObject(jsonObject.getString("datePrincipale"));
            datePrincipale.setVenitMJ_T1(Double.valueOf(jsonDatePrinc.getString("venitMJ_T1")));
            datePrincipale.setVenitTCF(Double.valueOf(jsonDatePrinc.getString("venitTCF")));
            datePrincipale.setCorectieIncasare(Double.valueOf(jsonDatePrinc.getString("corectieIncasare")));
            datePrincipale.setVenitFinal(Double.valueOf(jsonDatePrinc.getString("venitFinal")));
            datePrincipale.setVenitStocNociv(Double.valueOf(jsonDatePrinc.getString("venitStocNociv")));
            datePrincipale.setVenitIncrucisate(Double.valueOf(jsonDatePrinc.getString("venitIncrucisate")));

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
            salarizare.setDetaliiVS(getDetaliiVanzariVS(jsonObject.getString("detaliiVanzariVS")));
            salarizare.setDetaliiVanzariIncr(getDetaliiVanzariIncrucisate(jsonObject.getString("detaliiIncrAlocat")));

        } catch (JSONException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }

        return salarizare;
    }

    private List<BeanVanzariVS> getDetaliiVanzariVS(String jsonVanzariVS) {

        List<BeanVanzariVS> listVanzariVS = new ArrayList<>();

        if (jsonVanzariVS == null || jsonVanzariVS.equals("null"))
            return listVanzariVS;

        try {

            JSONArray jsonArray = new JSONArray(jsonVanzariVS);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject detObject = jsonArray.getJSONObject(i);
                BeanVanzariVS vanzariVS = new BeanVanzariVS();
                vanzariVS.setCoefSal(Double.valueOf(detObject.getString("COEF_SAL")));
                vanzariVS.setEname(detObject.getString("ENAME"));
                vanzariVS.setMatnr(detObject.getString("MATNR").replaceFirst("^0*", ""));
                vanzariVS.setPernr(detObject.getString("PERNR"));
                vanzariVS.setShortStr(detObject.getString("SHORT"));
                vanzariVS.setNetwrCalc(Double.valueOf(detObject.getString("NETWR_CALC")));
                vanzariVS.setVenitBaza(Double.valueOf(detObject.getString("VENIT_BAZA")));
                listVanzariVS.add(vanzariVS);

            }

        } catch (JSONException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }

        return listVanzariVS;
    }


    private List<BeanVanzariIncr> getDetaliiVanzariIncrucisate(String jsonVanzariIncr) {

        List<BeanVanzariIncr> listVanzariIncr = new ArrayList<>();

        if (jsonVanzariIncr == null || jsonVanzariIncr.equals("null"))
            return listVanzariIncr;

        try {

            JSONArray jsonArray = new JSONArray(jsonVanzariIncr);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject detObject = jsonArray.getJSONObject(i);
                BeanVanzariIncr vanzariIncr = new BeanVanzariIncr();
                vanzariIncr.setAnv(detObject.getString("Anv"));
                vanzariIncr.setLuna(detObject.getString("Luna"));
                vanzariIncr.setPernr(detObject.getString("Pernr"));
                vanzariIncr.setSpartAv(detObject.getString("SpartAv"));
                vanzariIncr.setSpartCl(detObject.getString("SpartCl"));
                vanzariIncr.setKunnr(detObject.getString("Kunnr"));
                vanzariIncr.setFacturat(detObject.getString("Facturat"));
                listVanzariIncr.add(vanzariIncr);

            }

        } catch (JSONException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }

        return listVanzariIncr;
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
                datePrincipale.setVenitStocNociv(Double.valueOf(jsonDatePrinc.getString("venitStocNociv")));
                datePrincipale.setVenitIncrucisate(Double.valueOf(jsonDatePrinc.getString("venitIncrucisate")));

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
                detaliuCVS.setVenitBaza(Double.valueOf(objCsv.getString("venitBaza")));
                detaliuCVS.setValTotal(Double.valueOf(objCsv.getString("valTotal")));
                detaliuCVS.setValNociv(Double.valueOf(objCsv.getString("valNociv")));
                detaliuCVS.setPrag(Double.valueOf(objCsv.getString("prag")));
                detaliuCVS.setProcent(Double.valueOf(objCsv.getString("procent")));
                detaliuCVS.setVenitCvs(Double.valueOf(objCsv.getString("venitCvs")));

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

            salarizareSD.setDetaliiVS(getDetaliiVanzariVS(jsonObject.getString("detaliiVanzariVS")));
            salarizareSD.setDetaliiVanzariIncr(getDetaliiVanzariIncrucisate(jsonObject.getString("detaliiIncrAlocat")));

        } catch (JSONException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }

        return salarizareSD;

    }


    public BeanSalarizareCVA deserializeSalarizareCVIP(String result) {
        BeanSalarizareCVA beanSalarizareCVA = new BeanSalarizareCVA();
        List<SalarizareCVABazaCL> listSalarizareBaza = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(result);


            JSONArray jsonGT_OUTTAB_AV = new JSONArray(jsonObject.getString("GtOuttabAv"));
            if (jsonGT_OUTTAB_AV.length() > 0) {
                beanSalarizareCVA.setVenitTcf(Double.parseDouble(jsonGT_OUTTAB_AV.getJSONObject(0).getString("Venittcf")));
                beanSalarizareCVA.setCorectIncas(Double.parseDouble(jsonGT_OUTTAB_AV.getJSONObject(0).getString("CorectIncas")));
                beanSalarizareCVA.setVenitFinal(Double.parseDouble(jsonGT_OUTTAB_AV.getJSONObject(0).getString("Venitfinal")));
                beanSalarizareCVA.setVenitStocNociv(Double.parseDouble(jsonGT_OUTTAB_AV.getJSONObject(0).getString("VenitVanzVs")));

                beanSalarizareCVA.setVenitBaza(Double.parseDouble(jsonGT_OUTTAB_AV.getJSONObject(0).getString("Baza")));
                beanSalarizareCVA.setVenitNruf(Double.parseDouble(jsonGT_OUTTAB_AV.getJSONObject(0).getString("VenitNruf")));
            }

            JSONArray jsonCVA = new JSONArray(jsonObject.getString("GtBazaclExp"));

            for (int i = 0; i < jsonCVA.length(); i++) {

                SalarizareCVABazaCL salCVABazaCL = new SalarizareCVABazaCL();
                JSONObject detBazaCl = jsonCVA.getJSONObject(i);

                salCVABazaCL.setKDGRP(detBazaCl.getString("Kdgrp"));
                salCVABazaCL.setKUNNR(detBazaCl.getString("Kunnr"));
                salCVABazaCL.setNAME1(detBazaCl.getString("Name1"));
                salCVABazaCL.setMATKL(detBazaCl.getString("Matkl"));
                salCVABazaCL.setWGBEZ(detBazaCl.getString("Wgbez"));
                salCVABazaCL.setVAL_NET(Double.parseDouble(detBazaCl.getString("ValNet")));
                salCVABazaCL.setT0(Double.parseDouble(detBazaCl.getString("t0")));
                salCVABazaCL.setT1A(Double.parseDouble(detBazaCl.getString("T1a")));
                salCVABazaCL.setT1(Double.parseDouble(detBazaCl.getString("t1")));
                salCVABazaCL.setVENIT_BAZA(Double.parseDouble(detBazaCl.getString("VenitBaza")));
                salCVABazaCL.setCOEF_X(detBazaCl.getString("CoefX").trim());
                salCVABazaCL.setT1A_PROC(detBazaCl.getString("T1aProc").trim());
                salCVABazaCL.setT1D_PROC(detBazaCl.getString("T1dProc").trim());
                listSalarizareBaza.add(salCVABazaCL);
            }

            beanSalarizareCVA.setDetaliiVS(getDetaliiVanzariVS(jsonObject.getString("GtVenVs")));

        } catch (JSONException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }


        beanSalarizareCVA.setListSalarizareCVABaza(listSalarizareBaza);
        return beanSalarizareCVA;
    }

    public List<BeanSalarizareCVA> deserializeSalarizareDepartCVA(String result) {

        List<BeanSalarizareCVA> listConsilieri = new ArrayList<>();

        try {

            JSONArray jsonAgenti = new JSONArray(result);

            for (int i = 0; i < jsonAgenti.length(); i++) {

                BeanSalarizareCVA unConsilier = new BeanSalarizareCVA();

                JSONObject objAgent = jsonAgenti.getJSONObject(i);

                unConsilier.setCodAgent(objAgent.getString("Pernr"));
                unConsilier.setNumeAgent(objAgent.getString("Ename"));
                unConsilier.setVenitBaza(Double.parseDouble(objAgent.getString("Baza")));
                unConsilier.setVenitTcf(Double.parseDouble(objAgent.getString("Venittcf")));
                unConsilier.setCorectIncas(Double.parseDouble(objAgent.getString("CorectIncas")));
                unConsilier.setVenitNruf(Double.parseDouble(objAgent.getString("VenitNruf")));
                unConsilier.setVenitStocNociv(Double.parseDouble(objAgent.getString("VenitVanzVs")));
                unConsilier.setVenitFinal(Double.parseDouble(objAgent.getString("Venitfinal")));
                listConsilieri.add(unConsilier);

            }

        } catch (JSONException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }

        return listConsilieri;



    }

    public BeanSalarizareCVA deserializeSalarizareCVA(String result) {
        BeanSalarizareCVA beanSalarizareCVA = new BeanSalarizareCVA();
        List<SalarizareCVABazaCL> listSalarizareBaza = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(result);

            JSONArray jsonGT_EXP = new JSONArray(jsonObject.getString("GtExp"));
            if (jsonGT_EXP.length() > 0) {
                beanSalarizareCVA.setVenitBaza(Double.parseDouble(jsonGT_EXP.getJSONObject(0).getString("VenitBaza")));
                beanSalarizareCVA.setNruf(Double.parseDouble(jsonGT_EXP.getJSONObject(0).getString("Nruf")));
                beanSalarizareCVA.setCoef(Double.parseDouble(jsonGT_EXP.getJSONObject(0).getString("Coef")));
                beanSalarizareCVA.setVenitNruf(Double.parseDouble(jsonGT_EXP.getJSONObject(0).getString("VenitNruf")));
            }

            JSONArray jsonGT_OUTTAB_AV = new JSONArray(jsonObject.getString("GtOuttabAv"));
            if (jsonGT_OUTTAB_AV.length() > 0) {
                beanSalarizareCVA.setVenitTcf(Double.parseDouble(jsonGT_OUTTAB_AV.getJSONObject(0).getString("Venittcf")));
                beanSalarizareCVA.setCorectIncas(Double.parseDouble(jsonGT_OUTTAB_AV.getJSONObject(0).getString("CorectIncas")));
                beanSalarizareCVA.setVenitFinal(Double.parseDouble(jsonGT_OUTTAB_AV.getJSONObject(0).getString("Venitfinal")));
                beanSalarizareCVA.setVenitStocNociv(Double.parseDouble(jsonGT_OUTTAB_AV.getJSONObject(0).getString("VenitVanzVs")));
            }

            JSONArray jsonCVA = new JSONArray(jsonObject.getString("GtBazaclExp"));

            for (int i = 0; i < jsonCVA.length(); i++) {

                SalarizareCVABazaCL salCVABazaCL = new SalarizareCVABazaCL();
                JSONObject detBazaCl = jsonCVA.getJSONObject(i);

                salCVABazaCL.setKDGRP(detBazaCl.getString("Kdgrp"));
                salCVABazaCL.setKUNNR(detBazaCl.getString("Kunnr"));
                salCVABazaCL.setNAME1(detBazaCl.getString("Name1"));
                salCVABazaCL.setMATKL(detBazaCl.getString("Matkl"));
                salCVABazaCL.setWGBEZ(detBazaCl.getString("Wgbez"));
                salCVABazaCL.setVAL_NET(Double.parseDouble(detBazaCl.getString("ValNet")));
                salCVABazaCL.setT0(Double.parseDouble(detBazaCl.getString("t0")));
                salCVABazaCL.setT1A(Double.parseDouble(detBazaCl.getString("T1a")));
                salCVABazaCL.setT1(Double.parseDouble(detBazaCl.getString("t1")));
                salCVABazaCL.setVENIT_BAZA(Double.parseDouble(detBazaCl.getString("VenitBaza")));
                salCVABazaCL.setCOEF_X(detBazaCl.getString("CoefX").trim());
                salCVABazaCL.setT1A_PROC(detBazaCl.getString("T1aProc").trim());
                salCVABazaCL.setT1D_PROC(detBazaCl.getString("T1dProc").trim());
                listSalarizareBaza.add(salCVABazaCL);
            }

            beanSalarizareCVA.setDetaliiVS(getDetaliiVanzariVS(jsonObject.getString("GtVenVs")));

        } catch (JSONException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }


        beanSalarizareCVA.setListSalarizareCVABaza(listSalarizareBaza);
        return beanSalarizareCVA;
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
