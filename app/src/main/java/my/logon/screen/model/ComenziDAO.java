package my.logon.screen.model;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.logon.screen.beans.ArticolAmob;
import my.logon.screen.beans.ArticolCalculDesc;
import my.logon.screen.beans.ArticolSimulat;
import my.logon.screen.beans.BeanArticolComandaRetur;
import my.logon.screen.beans.BeanArticoleAfisare;
import my.logon.screen.beans.BeanClientBorderou;
import my.logon.screen.beans.BeanComandaCreata;
import my.logon.screen.beans.BeanComandaDeschisa;
import my.logon.screen.beans.BeanConditii;
import my.logon.screen.beans.BeanConditiiArticole;
import my.logon.screen.beans.BeanConditiiHeader;
import my.logon.screen.beans.BeanHeaderComandaRetur;
import my.logon.screen.beans.BeanStatusComandaRetur;
import my.logon.screen.beans.ComandaAmobAfis;
import my.logon.screen.beans.ComandaCalculDescarcare;
import my.logon.screen.beans.DateLivrareAfisare;
import my.logon.screen.beans.Delegat;
import my.logon.screen.beans.FurnizorComanda;
import my.logon.screen.enums.EnumComenziDAO;
import my.logon.screen.enums.EnumTipClientIP;
import my.logon.screen.listeners.AsyncTaskListener;
import my.logon.screen.listeners.ComenziDAOListener;
import my.logon.screen.screens.AsyncTaskWSCall;
import my.logon.screen.utils.UtilsGeneral;

public class ComenziDAO implements IComenziDAO, AsyncTaskListener {

    private Context context;
    ComenziDAOListener listener;
    private EnumComenziDAO numeComanda;
    private List<BeanComandaCreata> listComenziCreate = new ArrayList<BeanComandaCreata>();

    ComenziDAO(Context context) {
        this.context = context;
    }

    public static ComenziDAO getInstance(Context context) {
        return new ComenziDAO(context);
    }

    public void getListComenzi(HashMap<String, String> params) {
        numeComanda = EnumComenziDAO.GET_LIST_COMENZI;
        performOperation(params);
    }

    public void getArticoleComanda(HashMap<String, String> params) {
        numeComanda = EnumComenziDAO.GET_ARTICOLE_COMANDA;
        performOperation(params);
    }

    public void getArticoleComandaJSON(HashMap<String, String> params) {
        numeComanda = EnumComenziDAO.GET_ARTICOLE_COMANDA_JSON;
        performOperation(params);
    }

    public void salveazaConditiiComanda(HashMap<String, String> params) {
        numeComanda = EnumComenziDAO.SALVEAZA_CONDITII_COMANDA;
        performOperation(params);
    }

    public void salveazaConditiiComandaSer(HashMap<String, String> params) {
        numeComanda = EnumComenziDAO.SALVEAZA_CONDITII_COMANDA_SER;
        performOperation(params);
    }

    public void getMotiveRespingere(HashMap<String, String> params) {
        numeComanda = EnumComenziDAO.GET_LISTA_RESPINGERE;
        performOperation(params);
    }

    public void opereazaComanda(HashMap<String, String> params) {
        numeComanda = EnumComenziDAO.OPERATIE_COMANDA;
        performOperation(params);
    }

    public void salveazaComandaDistrib(HashMap<String, String> params) {
        numeComanda = EnumComenziDAO.SALVEAZA_COMANDA_DISTRIB;
        performOperation(params);
    }

    public void salveazaComandaGed(HashMap<String, String> params) {
        numeComanda = EnumComenziDAO.SALVEAZA_COMANDA_GED;
        performOperation(params);

    }

    public void sendOfertaGedMail(HashMap<String, String> params) {
        numeComanda = EnumComenziDAO.SEND_OFERTA_GED_MAIL;
        performOperation(params);
    }

    public void getComenziDeschise(HashMap<String, String> params) {
        numeComanda = EnumComenziDAO.GET_COMENZI_DESCHISE;
        performOperation(params);

    }

    public void getClientiBorderou(HashMap<String, String> params) {
        numeComanda = EnumComenziDAO.GET_CLIENTI_BORD;
        performOperation(params);

    }

    public void getPozitieMasina(HashMap<String, String> params) {
        numeComanda = EnumComenziDAO.GET_POZITIE_MASINA;
        performOperation(params);

    }

    public void getCostMacara(HashMap<String, String> params) {
        numeComanda = EnumComenziDAO.GET_COST_MACARA;
        performOperation(params);

    }

    public void getStareComanda(HashMap<String, String> params) {
        numeComanda = EnumComenziDAO.GET_STARE_COMANDA;
        performOperation(params);

    }

    public void salveazaLivrareCustodie(HashMap<String, String> params) {
        numeComanda = EnumComenziDAO.SALVEAZA_LIVRARE_CUSTODIE;
        performOperation(params);

    }

    public void getLivrariCustodie(HashMap<String, String> params) {
        numeComanda = EnumComenziDAO.GET_LIVRARI_CUSTODIE;
        performOperation(params);
    }

    public void getArticoleCustodie(HashMap<String, String> params) {
        numeComanda = EnumComenziDAO.GET_ARTICOLE_CUSTODIE;
        performOperation(params);
    }

    public void setCustodieDataLivrare(HashMap<String, String> params) {
        numeComanda = EnumComenziDAO.SET_CUSTODIE_DATA_LIVRARE;
        performOperation(params);

    }

    public void setCustodieAdresaLivrare(HashMap<String, String> params) {
        numeComanda = EnumComenziDAO.SET_CUSTODIE_ADRESA_LIVRARE;
        performOperation(params);

    }

    public void stergeLivrareCustodie(HashMap<String, String> params) {
        numeComanda = EnumComenziDAO.STERGE_LIVRARE_CUSTODIE;
        performOperation(params);

    }

    public void getComenziAmob(HashMap<String, String> params) {
        numeComanda = EnumComenziDAO.GET_COMENZI_AMOB_AFIS;
        performOperation(params);

    }

    public void getArticoleAmob(HashMap<String, String> params) {
        numeComanda = EnumComenziDAO.GET_ARTICOLE_AMOB;
        performOperation(params);

    }

    public void setCmdVanzDataLivrare(HashMap<String, String> params) {
        numeComanda = EnumComenziDAO.SET_CMD_VANZ_DATA_LIVRARE;
        performOperation(params);

    }

    public void updateComandaSimulata(HashMap<String, String> params) {
        numeComanda = EnumComenziDAO.UPDATE_COM_SIM;
        performOperation(params);

    }

    public void getLivrariMathaus(HashMap<String, String> params) {
        numeComanda = EnumComenziDAO.GET_LIVRARI_MATHAUS;
        performOperation(params);
    }

    public void getLivrariMathausTCLI(HashMap<String, String> params) {
        numeComanda = EnumComenziDAO.GET_LIVRARI_MATHAUS_TCLI;
        performOperation(params);
    }

    public void getCostMacaraComenzi(HashMap<String, String> params) {
        numeComanda = EnumComenziDAO.GET_COST_MACARA_COMENZI;
        performOperation(params);
    }

    public void getTotalComenziNumerar(HashMap<String, String> params) {
        numeComanda = EnumComenziDAO.GET_TOTAL_COMENZI_NUMERAR;
        performOperation(params);
    }

    public void getListComenziRetur(HashMap<String, String> params) {
        numeComanda = EnumComenziDAO.GET_LIST_DOCRETUR_STARE;
        performOperation(params);
    }

    public void getListaArticoleRetur(HashMap<String, String> params) {
        numeComanda = EnumComenziDAO.GET_LIST_ARTICOLE_RETUR;
        performOperation(params);
    }

    public void getStareReturComanda(HashMap<String, String> params) {
        numeComanda = EnumComenziDAO.GET_STATUS_COMANDA_RETUR;
        performOperation(params);
    }

    public void getProcMarjaIP(HashMap<String, String> params) {
        numeComanda = EnumComenziDAO.GET_PROC_MARJA_IP;
        performOperation(params);
    }

    private void performOperation(HashMap<String, String> params) {
        AsyncTaskListener contextListener = (AsyncTaskListener) ComenziDAO.this;
        AsyncTaskWSCall call = new AsyncTaskWSCall(context, contextListener, numeComanda.getComanda(), params);
        call.getCallResultsFromFragment();
    }

    public void setComenziDAOListener(ComenziDAOListener listener) {
        this.listener = listener;
    }

    public void onTaskComplete(String methodName, Object result) {
        if (listener != null) {

            Object resultObject = result;
            if (numeComanda.equals(EnumComenziDAO.GET_LIST_COMENZI)) {
                resultObject = deserializeListComenzi((String) result);

            }

            listener.operationComenziComplete(numeComanda, resultObject);
        }

    }

    public BeanArticoleAfisare deserializeArticoleComandaLight(String serializedResult) {
        BeanArticoleAfisare articoleComanda = new BeanArticoleAfisare();

        DateLivrareAfisare dateLivrare = null;
        ArrayList<ArticolSimulat> listArticole = new ArrayList<ArticolSimulat>();
        ArticolSimulat articol = null;

        try {

            JSONObject jsonObject = (JSONObject) new JSONTokener(serializedResult).nextValue();

            if (jsonObject instanceof JSONObject) {

                JSONObject jsonLivrare = jsonObject.getJSONObject("dateLivrare");

                dateLivrare = new DateLivrareAfisare();
                dateLivrare.setPersContact(jsonLivrare.getString("persContact"));
                dateLivrare.setNrTel(jsonLivrare.getString("nrTel"));
                dateLivrare.setDateLivrare(jsonLivrare.getString("dateLivrare"));
                dateLivrare.setTransport(jsonLivrare.getString("Transport"));
                dateLivrare.setTipPlata(jsonLivrare.getString("tipPlata"));
                dateLivrare.setOras(jsonLivrare.getString("Oras"));
                dateLivrare.setCodJudet(jsonLivrare.getString("codJudet"));
                dateLivrare.setNumeJudet(UtilsGeneral.getNumeJudet(jsonLivrare.getString("codJudet")));
                dateLivrare.setUnitLog(jsonLivrare.getString("unitLog"));
                dateLivrare.setNumeClient(jsonLivrare.getString("numeClient"));
                dateLivrare.setCnpClient(jsonLivrare.getString("cnpClient"));
                dateLivrare.setMail(jsonLivrare.getString("mail"));

                JSONArray jsonArticole = jsonObject.getJSONArray("articoleComanda");
                String subCmp = "";
                for (int i = 0; i < jsonArticole.length(); i++) {
                    JSONObject articolObject = jsonArticole.getJSONObject(i);

                    articol = new ArticolSimulat();
                    articol.setStatus(articolObject.getString("status"));
                    articol.setCodArticol(articolObject.getString("codArticol"));
                    articol.setNumeArticol(articolObject.getString("numeArticol"));
                    articol.setCantitate(Double.valueOf(articolObject.getString("cantitate")));
                    articol.setDepozit(articolObject.getString("depozit"));
                    articol.setPretUnit(Double.valueOf(articolObject.getString("pretUnit")));
                    articol.setPretUnitarClient(Double.valueOf(articolObject.getString("pretUnit")));
                    articol.setUm(articolObject.getString("um"));
                    articol.setProcent(Double.valueOf(articolObject.getString("procent")));
                    articol.setProcentFact(Double.valueOf(articolObject.getString("procentFact")));
                    articol.setMultiplu(Double.valueOf(articolObject.getString("multiplu")));
                    articol.setPret(Double.valueOf(articolObject.getString("pret")));
                    articol.setInfoArticol(articolObject.getString("infoArticol"));
                    articol.setCantUmb(Double.valueOf(articolObject.getString("cantUmb")));
                    articol.setUmb(articolObject.getString("Umb"));
                    articol.setUnitLogAlt(articolObject.getString("unitLogAlt"));
                    articol.setDepart(articolObject.getString("depart"));
                    articol.setTipArt(articolObject.getString("tipArt"));
                    articol.setConditii(false);

                    subCmp = "0";
                    if (articol.getPretUnit() < articol.getCmp())
                        subCmp = "1";
                    articol.setAlteValori(subCmp);
                    articol.setDepartSintetic(articolObject.getString("departSintetic"));
                    articol.setDepartAprob(articolObject.getString("departAprob"));

                    listArticole.add(articol);

                }

            }

        } catch (JSONException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }

        articoleComanda.setDateLivrare(dateLivrare);

        articoleComanda.setArticoleSimulate(listArticole);
        articoleComanda.setConditii(null);

        return articoleComanda;

    }

    public BeanArticoleAfisare deserializeArticoleComanda(String serializedResult) {

        BeanArticoleAfisare articoleComanda = new BeanArticoleAfisare();

        DateLivrareAfisare dateLivrare = null;
        ArrayList<ArticolComanda> listArticole = new ArrayList<ArticolComanda>();
        ArticolComanda articol = null;

        BeanConditii conditii = new BeanConditii();
        BeanConditiiHeader headerConditii = new BeanConditiiHeader();
        List<BeanConditiiArticole> listArticoleConditii = new ArrayList<BeanConditiiArticole>();

        try {

            JSONObject jsonObject = (JSONObject) new JSONTokener(serializedResult).nextValue();

            if (jsonObject instanceof JSONObject) {

                JSONObject jsonLivrare = jsonObject.getJSONObject("dateLivrare");

                dateLivrare = new DateLivrareAfisare();
                dateLivrare.setPersContact(jsonLivrare.getString("persContact"));
                dateLivrare.setNrTel(jsonLivrare.getString("nrTel"));
                dateLivrare.setDateLivrare(jsonLivrare.getString("dateLivrare"));
                dateLivrare.setTransport(jsonLivrare.getString("Transport"));
                dateLivrare.setTipPlata(jsonLivrare.getString("tipPlata"));
                dateLivrare.setCantar(jsonLivrare.getString("Cantar"));
                dateLivrare.setFactRed(jsonLivrare.getString("factRed"));
                dateLivrare.setRedSeparat(jsonLivrare.getString("factRed"));
                dateLivrare.setOras(jsonLivrare.getString("Oras"));
                dateLivrare.setCodJudet(jsonLivrare.getString("codJudet"));
                dateLivrare.setNumeJudet(UtilsGeneral.getNumeJudet(jsonLivrare.getString("codJudet")));
                dateLivrare.setUnitLog(jsonLivrare.getString("unitLog"));
                dateLivrare.setTermenPlata(jsonLivrare.getString("termenPlata"));
                dateLivrare.setObsLivrare(jsonLivrare.getString("obsLivrare"));
                dateLivrare.setTipPersClient(jsonLivrare.getString("tipPersClient"));
                dateLivrare.setMail(jsonLivrare.getString("mail"));
                dateLivrare.setObsPlata(jsonLivrare.getString("obsPlata"));
                dateLivrare.setDataLivrare(jsonLivrare.getString("dataLivrare"));
                dateLivrare.setAdresaD(jsonLivrare.getString("adresaD"));
                dateLivrare.setOrasD(jsonLivrare.getString("orasD"));
                dateLivrare.setCodJudetD(jsonLivrare.getString("codJudetD"));
                dateLivrare.setMacara(jsonLivrare.getString("macara"));
                dateLivrare.setNumeClient(jsonLivrare.getString("numeClient"));
                dateLivrare.setCnpClient(jsonLivrare.getString("cnpClient"));
                dateLivrare.setIdObiectiv(jsonLivrare.getString("idObiectiv"));
                dateLivrare.setAdresaObiectiv(Boolean.valueOf(jsonLivrare.getString("isAdresaObiectiv")));
                dateLivrare.setTipDocInsotitor(jsonLivrare.getString("tipDocInsotitor"));

                String[] coords = jsonLivrare.getString("coordonateGps").split(",");
                dateLivrare.setCoordonateAdresa(new LatLng(Double.valueOf(coords[0]), Double.valueOf(coords[1])));

                dateLivrare.setTonaj(jsonLivrare.getString("tonaj"));
                dateLivrare.setClientRaft(jsonLivrare.getString("clientRaft").equals("X") ? true : false);

                dateLivrare.setCodMeserias(jsonLivrare.getString("meserias"));
                dateLivrare.setFactPaletSeparat(Boolean.valueOf(jsonLivrare.getString("factPaletiSeparat")));

                FurnizorComanda furnizor = new FurnizorComanda();
                furnizor.setCodFurnizorMarfa(jsonLivrare.getString("furnizorMarfa"));
                furnizor.setCodFurnizorProduse(jsonLivrare.getString("furnizorProduse"));
                dateLivrare.setFurnizorComanda(furnizor);

                dateLivrare.setCamionDescoperit(Boolean.valueOf(jsonLivrare.getString("isCamionDescoperit")));
                dateLivrare.setDiviziiClient(jsonLivrare.getString("diviziiClient"));
                dateLivrare.setProgramLivrare(jsonLivrare.getString("programLivrare"));
                dateLivrare.setLivrareSambata(jsonLivrare.getString("livrareSambata"));
                dateLivrare.setBlocScara(jsonLivrare.getString("blocScara"));
                dateLivrare.setCodFilialaCLP(jsonLivrare.getString("filialaCLP"));

                Delegat delegat = new Delegat();
                delegat.setNume(jsonLivrare.getString("numeDelegat"));
                delegat.setSerieNumarCI(jsonLivrare.getString("ciDelegat"));
                delegat.setNrAuto(jsonLivrare.getString("autoDelegat"));
                dateLivrare.setDelegat(delegat);

                if (jsonLivrare.has("marjaT1"))
                    dateLivrare.setMarjaT1(Double.valueOf(jsonLivrare.getString("marjaT1")));

                if (jsonLivrare.has("procentT1"))
                    dateLivrare.setProcentT1(Double.valueOf(jsonLivrare.getString("procentT1")));

                if (jsonLivrare.has("marjaT1Tot"))
                    dateLivrare.setMarjaT1Tot(Double.valueOf(jsonLivrare.getString("marjaT1Tot")));

                if (jsonLivrare.has("procentT1Tot"))
                    dateLivrare.setProcentT1Tot(Double.valueOf(jsonLivrare.getString("procentT1Tot")));

                if (jsonLivrare.has("mCantCmd"))
                    dateLivrare.setmCantCmd(Double.valueOf(jsonLivrare.getString("mCantCmd")));

                if (jsonLivrare.has("mCant30"))
                    dateLivrare.setmCant30(Double.valueOf(jsonLivrare.getString("mCant30")));

                dateLivrare.setMarjaBrutaPalVal(Double.valueOf(jsonLivrare.getString("marjaBrutaPalVal")));
                dateLivrare.setMarjaBrutaCantVal(Double.valueOf(jsonLivrare.getString("marjaBrutaCantVal")));
                dateLivrare.setMarjaBrutaPalProc(Double.valueOf(jsonLivrare.getString("marjaBrutaPalProc")));
                dateLivrare.setMarjaBrutaCantProc(Double.valueOf(jsonLivrare.getString("marjaBrutaCantProc")));

                dateLivrare.setTipPersAgent(jsonLivrare.getString("tipPersAgent"));

                dateLivrare.setRefClient(jsonLivrare.getString("refClient"));

                if (jsonLivrare.has("isClientBlocat"))
                    dateLivrare.setClientBlocat(Boolean.valueOf(jsonLivrare.getString("isClientBlocat")));

                dateLivrare.setLimitaCredit(Double.valueOf(jsonLivrare.getString("limitaCredit")));

                if (jsonLivrare.has("nrCmdClp"))
                    dateLivrare.setNrCmdClp(jsonLivrare.getString("nrCmdClp"));
                else
                    dateLivrare.setNrCmdClp("");

                if (jsonLivrare.has("marjaBruta"))
                    dateLivrare.setMarjaBruta(Double.valueOf(jsonLivrare.getString("marjaBruta")));

                if (jsonLivrare.has("procMarjaBruta"))
                    dateLivrare.setProcMarjaBruta(Double.valueOf(jsonLivrare.getString("procMarjaBruta")));

                if (jsonLivrare.has("marjaBrutaTot"))
                    dateLivrare.setMarjaBrutaTot(Double.valueOf(jsonLivrare.getString("marjaBrutaTot")));

                if (jsonLivrare.has("procMarjaBrutaTot"))
                    dateLivrare.setProcMarjaBrutaTot(Double.valueOf(jsonLivrare.getString("procMarjaBrutaTot")));


                if (jsonLivrare.has("filialaPlata"))
                    dateLivrare.setFilialaPlata(jsonLivrare.getString("filialaPlata"));

                dateLivrare.setTonaj(jsonLivrare.getString("tonaj"));

                JSONArray jsonArticole = jsonObject.getJSONArray("articoleComanda");
                String tipAlert, subCmp;
                for (int i = 0; i < jsonArticole.length(); i++) {
                    JSONObject articolObject = jsonArticole.getJSONObject(i);

                    articol = new ArticolComanda();
                    articol.setStatus(articolObject.getString("status"));
                    articol.setCodArticol(articolObject.getString("codArticol"));
                    articol.setNumeArticol(articolObject.getString("numeArticol"));
                    articol.setCantitate(Double.valueOf(articolObject.getString("cantitate")));
                    articol.setDepozit(articolObject.getString("depozit"));
                    articol.setPretUnit(Double.valueOf(articolObject.getString("pretUnit")));
                    articol.setPretUnitarClient(Double.valueOf(articolObject.getString("pretUnit")));
                    articol.setUm(articolObject.getString("um"));
                    articol.setProcent(Double.valueOf(articolObject.getString("procent")));
                    articol.setProcentFact(Double.valueOf(articolObject.getString("procentFact")));
                    articol.setConditie(Boolean.valueOf(articolObject.getString("conditie")));
                    articol.setCmp(Double.valueOf(articolObject.getString("cmp")));
                    articol.setDiscClient(Double.valueOf(articolObject.getString("discClient")));
                    articol.setDiscountAg(Double.valueOf(articolObject.getString("discountAg")));
                    articol.setDiscountSd(Double.valueOf(articolObject.getString("discountSd")));
                    articol.setDiscountDv(Double.valueOf(articolObject.getString("discountDv")));
                    articol.setProcAprob(Double.valueOf(articolObject.getString("procAprob")));
                    articol.setPermitSubCmp(articolObject.getString("permitSubCmp"));
                    articol.setMultiplu(Double.valueOf(articolObject.getString("multiplu")));
                    articol.setPret(Double.valueOf(articolObject.getString("pret")));
                    articol.setInfoArticol(articolObject.getString("infoArticol"));
                    articol.setCantUmb(Double.valueOf(articolObject.getString("cantUmb")));
                    articol.setUmb(articolObject.getString("Umb"));
                    articol.setUnitLogAlt(articolObject.getString("unitLogAlt"));
                    articol.setDepart(articolObject.getString("depart"));
                    articol.setTipArt(articolObject.getString("tipArt"));
                    articol.setConditii(false);


                    tipAlert = " ";
                    if (UserInfo.getInstance().getTipAcces().equals("9")) {
                        if (articol.getProcAprob() > articol.getDiscountAg())
                            tipAlert = "SD";
                    }

                    if (articol.getProcAprob() > articol.getDiscountSd())
                        tipAlert = tipAlert.trim() + "DV";

                    articol.setTipAlert(tipAlert);

                    subCmp = "0";
                    if (articol.getPretUnit() < articol.getCmp())
                        subCmp = "1";
                    articol.setAlteValori(subCmp);

                    articol.setPonderare(Integer.valueOf(articolObject.getString("ponderare")));
                    articol.setPretMediu(Double.valueOf(articolObject.getString("pretMediu")));
                    articol.setAdaosMediu(Double.valueOf(articolObject.getString("adaosMediu")));
                    articol.setUnitMasPretMediu(articolObject.getString("unitMasPretMediu"));
                    articol.setDepartSintetic(articolObject.getString("departSintetic"));
                    articol.setCoefCorectie(Double.valueOf(articolObject.getString("coefCorectie")));
                    articol.setDepartAprob(articolObject.getString("departAprob"));
                    articol.setIstoricPret(articolObject.getString("istoricPret").trim());
                    articol.setVechime(articolObject.getString("vechime"));
                    articol.setMoneda(articolObject.getString("moneda"));
                    articol.setValTransport(Double.valueOf(articolObject.getString("valTransport")));
                    articol.setProcTransport(0);

                    if (articolObject.has("valT1"))
                        articol.setValT1(Double.valueOf(articolObject.getString("valT1")));
                    if (articolObject.has("procT1"))
                        articol.setProcT1(Double.valueOf(articolObject.getString("procT1")));

                    if (articolObject.has("filialaSite"))
                        articol.setFilialaSite(articolObject.getString("filialaSite"));

                    articol.setDataExpPret(articolObject.getString("dataExp"));
                    articol.setUmPalet(articolObject.getString("umPalet").equals("1") ? true : false);
                    articol.setListCabluri(new OperatiiArticolImpl(context).deserializeCantCabluri05(articolObject.getString("listCabluri")));

                    if (articolObject.has("aczcDeLivrat"))
                        articol.setAczcDeLivrat(Double.valueOf(articolObject.getString("aczcDeLivrat")));

                    if (articolObject.has("aczcLivrat"))
                        articol.setAczcLivrat(Double.valueOf(articolObject.getString("aczcLivrat")));

                    if (articolObject.has("greutate"))
                        articol.setGreutate(Double.valueOf(articolObject.getString("greutate")));

                    listArticole.add(articol);

                }

            }

            JSONObject jsonConditii = jsonObject.getJSONObject("conditii");

            JSONObject jsonHeader = null;
            if (!jsonConditii.isNull("header")) {
                jsonHeader = jsonConditii.getJSONObject("header");

                headerConditii.setId(Integer.parseInt(jsonHeader.getString("id")));
                headerConditii.setConditiiCalit(Double.parseDouble(jsonHeader.getString("conditiiCalit")));
                headerConditii.setNrFact(Integer.parseInt(jsonHeader.getString("nrFact")));
                headerConditii.setObservatii(jsonHeader.getString("observatii"));

            }

            JSONArray jsonArticole;
            if (!jsonConditii.isNull("articole")) {
                jsonArticole = jsonConditii.getJSONArray("articole");

                BeanConditiiArticole articolCond = null;

                for (int i = 0; i < jsonArticole.length(); i++) {

                    JSONObject articolObject = jsonArticole.getJSONObject(i);

                    articolCond = new BeanConditiiArticole();
                    articolCond.setCod(articolObject.getString("cod"));
                    articolCond.setNume(articolObject.getString("nume"));
                    articolCond.setCantitate(Double.valueOf(articolObject.getString("cantitate")));
                    articolCond.setUm(articolObject.getString("um"));
                    articolCond.setValoare(Double.valueOf(articolObject.getString("valoare")));
                    articolCond.setMultiplu(Double.valueOf(articolObject.getString("multiplu")));
                    listArticoleConditii.add(articolCond);

                }

            }

            conditii.setHeader(headerConditii);
            conditii.setArticole(listArticoleConditii);

        } catch (JSONException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }

        articoleComanda.setDateLivrare(dateLivrare);

        articoleComanda.setListArticole(listArticole);
        articoleComanda.setConditii(conditii);

        return articoleComanda;
    }

    public ArrayList<BeanComandaCreata> deserializeListComenzi(String serializedListComenzi) {

        BeanComandaCreata comanda = null;
        ArrayList<BeanComandaCreata> listComenzi = new ArrayList<BeanComandaCreata>();

        try {

            Object json = new JSONTokener(serializedListComenzi).nextValue();

            if (json instanceof JSONArray) {
                JSONArray jsonObject = new JSONArray(serializedListComenzi);

                for (int i = 0; i < jsonObject.length(); i++) {
                    JSONObject comandaObject = jsonObject.getJSONObject(i);

                    comanda = new BeanComandaCreata();
                    comanda.setId(comandaObject.getString("idComanda"));
                    comanda.setNumeClient(comandaObject.getString("numeClient"));
                    comanda.setCodClient(comandaObject.getString("codClient"));
                    comanda.setData(comandaObject.getString("dataComanda"));
                    comanda.setSuma(comandaObject.getString("sumaComanda"));
                    comanda.setStare(InfoStrings.statusAprobCmd(Integer.valueOf(comandaObject.getString("stareComanda"))));
                    comanda.setCodStare(comandaObject.getString("stareComanda"));
                    comanda.setMoneda(comandaObject.getString("monedaComanda"));
                    comanda.setSumaTva(comandaObject.getString("sumaTVA"));
                    comanda.setMonedaTva(comandaObject.getString("monedaTVA"));
                    comanda.setCmdSap(comandaObject.getString("cmdSap"));
                    comanda.setCanalDistrib(comandaObject.getString("canalDistrib"));
                    comanda.setTipClient(InfoStrings.getTipClient(comandaObject.getString("tipClient")));
                    comanda.setDivizieAgent(comandaObject.getString("divizieAgent"));
                    comanda.setFiliala(comandaObject.getString("filiala"));
                    comanda.setFactRed(comandaObject.getString("factRed"));
                    comanda.setAccept1(comandaObject.getString("accept1"));
                    comanda.setAccept2(comandaObject.getString("accept2"));
                    comanda.setNumeAgent(comandaObject.getString("numeAgent"));
                    comanda.setTermenPlata(comandaObject.getString("termenPlata"));
                    comanda.setCursValutar(comandaObject.getString("cursValutar"));
                    comanda.setDocInsotitor(comandaObject.getString("docInsotitor"));
                    comanda.setAdresaNoua(comandaObject.getString("adresaNoua"));
                    comanda.setAdresaLivrare(comandaObject.getString("adresaLivrare"));
                    comanda.setDivizieComanda(comandaObject.getString("divizieComanda"));
                    comanda.setPondere30(comandaObject.getString("pondere30"));
                    comanda.setAprobariNecesare(comandaObject.getString("aprobariNecesare"));
                    comanda.setAprobariPrimite(comandaObject.getString("aprobariPrimite"));
                    comanda.setCodClientGenericGed(comandaObject.getString("codClientGenericGed"));
                    comanda.setConditiiImpuse(comandaObject.getString("conditiiImpuse"));
                    comanda.setAvans(Double.valueOf(comandaObject.getString("avans")));
                    comanda.setClientRaft(comandaObject.getString("clientRaft").equals("X") ? true : false);

                    if (comandaObject.has("telAgent"))
                        comanda.setTelAgent(comandaObject.getString("telAgent"));

                    if (comandaObject.has("tipComanda"))
                        comanda.setTipComanda(comandaObject.getString("tipComanda"));

                    if (comandaObject.has("isCmdInstPublica"))
                        comanda.setCmdInstPublica(Boolean.valueOf(comandaObject.getString("isCmdInstPublica")));

                    if (comandaObject.has("bazaSalariala"))
                        comanda.setBazaSalariala(Double.valueOf(comandaObject.getString("bazaSalariala")));

                    if (comandaObject.has("tipClientInstPublica")) {
                        if (comandaObject.getString("tipClientInstPublica").equals("CONSTR"))
                            comanda.setTipClientInstPublica(EnumTipClientIP.CONSTR);
                        else
                            comanda.setTipClientInstPublica(EnumTipClientIP.NONCONSTR);
                    }

                    if (comandaObject.has("isAprobatDistrib"))
                        comanda.setAprobDistrib(Boolean.valueOf(comandaObject.getString("isAprobatDistrib")));

                    if (comandaObject.has("isComandaACZC"))
                        comanda.setComandaACZC(Boolean.valueOf(comandaObject.getString("isComandaACZC")));


                    listComenzi.add(comanda);

                }
            }

        } catch (JSONException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }

        listComenziCreate = listComenzi;
        return listComenzi;

    }

    public List<BeanComandaDeschisa> deserializeComenziDeschise(String result) {
        List<BeanComandaDeschisa> listComenzi = new ArrayList<BeanComandaDeschisa>();
        BeanComandaDeschisa comanda = null;
        try {
            Object json = new JSONTokener(result).nextValue();

            if (json instanceof JSONArray) {
                JSONArray jsonObject = new JSONArray(result);

                for (int i = 0; i < jsonObject.length(); i++) {
                    JSONObject comandaObject = jsonObject.getJSONObject(i);
                    comanda = new BeanComandaDeschisa();
                    comanda.setIdCmdSap(comandaObject.getString("idCmdSap"));
                    comanda.setNumeClient(comandaObject.getString("numeClient"));
                    comanda.setCodClient(comandaObject.getString("codClient"));
                    comanda.setValoare(comandaObject.getString("valoare"));
                    comanda.setLocalitate(comandaObject.getString("localitate"));
                    comanda.setStrada(comandaObject.getString("strada"));
                    comanda.setCodBorderou(comandaObject.getString("codBorderou"));
                    comanda.setNrMasina(comandaObject.getString("nrMasina"));
                    comanda.setCodJudet(comandaObject.getString("codJudet"));
                    comanda.setNumeSofer(comandaObject.getString("numeSofer"));
                    comanda.setTelSofer(comandaObject.getString("telSofer"));
                    comanda.setCodStareComanda(Integer.valueOf(comandaObject.getString("stareComanda")));
                    comanda.setTelClient(comandaObject.getString("telClient"));

                    listComenzi.add(comanda);

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return listComenzi;
    }

    public List<BeanClientBorderou> deserializeClientiBorderou(String result) {

        List<BeanClientBorderou> listClienti = new ArrayList<BeanClientBorderou>();
        BeanClientBorderou client = null;

        try {
            Object json = new JSONTokener(result).nextValue();

            if (json instanceof JSONArray) {
                JSONArray jsonObject = new JSONArray(result);

                for (int i = 0; i < jsonObject.length(); i++) {
                    JSONObject clientObject = jsonObject.getJSONObject(i);

                    client = new BeanClientBorderou();
                    client.setCodClient(clientObject.getString("codClient"));
                    client.setPozitie(Integer.valueOf(clientObject.getString("pozitie")));
                    client.setStareLivrare(clientObject.getString("dataEveniment"));
                    listClienti.add(client);

                }

            }

        } catch (JSONException e) {

        }

        return listClienti;
    }

    public List<ComandaAmobAfis> deserializeComenziAmobAfis(String result) {

        List<ComandaAmobAfis> listComenzi = new ArrayList<ComandaAmobAfis>();
        ComandaAmobAfis comanda = null;

        try {
            Object json = new JSONTokener(result).nextValue();

            if (json instanceof JSONArray) {
                JSONArray jsonObject = new JSONArray(result);

                for (int i = 0; i < jsonObject.length(); i++) {
                    JSONObject comandaObject = jsonObject.getJSONObject(i);

                    comanda = new ComandaAmobAfis();
                    comanda.setId(comandaObject.getString("idComanda"));
                    comanda.setIdAmob(comandaObject.getString("idAmob"));
                    comanda.setDataCreare(comandaObject.getString("dataCreare"));
                    comanda.setValoare(comandaObject.getString("valoare"));
                    comanda.setNumeClient(comandaObject.getString("numeClient"));
                    comanda.setMoneda("RON");

                    listComenzi.add(comanda);
                }

            }

        } catch (JSONException e) {

        }

        return listComenzi;
    }

    public List<ArticolAmob> deserializeArticoleAmob(String result) {

        List<ArticolAmob> listArticole = new ArrayList<ArticolAmob>();
        ArticolAmob articol = null;

        try {
            Object json = new JSONTokener(result).nextValue();

            if (json instanceof JSONArray) {
                JSONArray jsonObject = new JSONArray(result);

                for (int i = 0; i < jsonObject.length(); i++) {
                    JSONObject articolObject = jsonObject.getJSONObject(i);

                    articol = new ArticolAmob();
                    articol.setCodArticol(articolObject.getString("codArticol"));
                    articol.setDepozit(articolObject.getString("depozit"));
                    articol.setCantitate(Double.valueOf(articolObject.getString("cantitate")));
                    articol.setUm(articolObject.getString("um"));
                    articol.setPretUnitar(Double.valueOf(articolObject.getString("pretUnitar")));
                    articol.setProcentReducere(Double.valueOf(articolObject.getString("procentReducere")));
                    articol.setNumeArticol(articolObject.getString("numeArticol"));
                    articol.setUmVanz(articolObject.getString("umVanz"));
                    articol.setDepart(articolObject.getString("depart"));
                    articol.setTipAB(articolObject.getString("tipAB"));
                    listArticole.add(articol);

                }

            }

        } catch (JSONException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }

        return listArticole;
    }

    public String serializeArtCalcMacara(List<ArticolCalculDesc> artCalcul) {

        JSONArray jsonArrayArt = new JSONArray();

        try {

            for (ArticolCalculDesc art : artCalcul) {

                JSONObject jsonObjArt = new JSONObject();
                jsonObjArt.put("cod", art.getCod());
                jsonObjArt.put("um", art.getUm());
                jsonObjArt.put("cant", art.getCant());
                jsonObjArt.put("depoz", art.getDepoz());

                jsonArrayArt.put(jsonObjArt);

            }

        } catch (JSONException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }

        return jsonArrayArt.toString();
    }

    public String serializeCalcComenziMacara(List<ComandaCalculDescarcare> listComenzi) {

        JSONArray jsonArrayCom = new JSONArray();

        try {
            for (ComandaCalculDescarcare comanda : listComenzi) {
                JSONObject jsonObjCom = new JSONObject();
                jsonObjCom.put("filiala", comanda.getFiliala());
                jsonObjCom.put("listArticole", serializeArtCalcMacara(comanda.getListArticole()));
                jsonArrayCom.put(jsonObjCom);

            }
        } catch (JSONException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }

        return jsonArrayCom.toString();
    }

    public List<BeanComandaCreata> getComenziDivizie(String divizie) {
        CriteriulDivizie criteriu = new CriteriulDivizie();
        List<BeanComandaCreata> listCmd = criteriu.indeplinesteCriteriu(listComenziCreate, divizie);
        return listCmd;
    }


    public ArrayList<BeanHeaderComandaRetur> deserializeHeadeComandaRetur(String serializedResult) {
        BeanHeaderComandaRetur comandaRetur = null;
        ArrayList<BeanHeaderComandaRetur> listComenziRetur = new ArrayList<BeanHeaderComandaRetur>();
        try {
            Object json = new JSONTokener(serializedResult).nextValue();

            if (json instanceof JSONArray){
                JSONArray jsonObject = new JSONArray(serializedResult);
                for (int i=0;i<jsonObject.length();i++){
                    JSONObject comandaReturObject = jsonObject.getJSONObject(i);
                    comandaRetur = new BeanHeaderComandaRetur();
                    comandaRetur.setVbeln(comandaReturObject.getString("vbeln"));
                    comandaRetur.setKunnr(comandaReturObject.getString("kunnr"));
                    comandaRetur.setAuart(comandaReturObject.getString("auart"));
                    comandaRetur.setAudat(comandaReturObject.getString("audat"));
                    comandaRetur.setReferinta(comandaReturObject.getString("referinta"));
                    comandaRetur.setTraty(comandaReturObject.getString("traty"));
                    comandaRetur.setName1(comandaReturObject.getString("name1"));
                    comandaRetur.setTranspzone(comandaReturObject.getString("transpzone"));
                    comandaRetur.setPers_contact(comandaReturObject.getString("pers_contact"));
                    comandaRetur.setTel_contact(comandaReturObject.getString("tel_contact"));
                    comandaRetur.setCity1(comandaReturObject.getString("city1"));
                    comandaRetur.setStreet(comandaReturObject.getString("street"));

                    listComenziRetur.add(comandaRetur);
                }
            }

        } catch (JSONException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
        return listComenziRetur;
    }

    public ArrayList<BeanStatusComandaRetur> deserializeListaStatusRetur(String serializedResult) {
        BeanStatusComandaRetur statusComandaRetur = null;
        ArrayList<BeanStatusComandaRetur> listStatus = new ArrayList<BeanStatusComandaRetur>();
        try {
            Object json = new JSONTokener(serializedResult).nextValue();
            if (json instanceof JSONArray) {
                JSONArray jsonObject = new JSONArray(serializedResult);
                for (int i=0;i<jsonObject.length();i++) {
                    JSONObject statusReturObject = jsonObject.getJSONObject(i);
                    statusComandaRetur = new BeanStatusComandaRetur();
                    statusComandaRetur.setNrDocument(statusReturObject.getString("nrDocument"));
                    statusComandaRetur.setStare(statusReturObject.getString("stare"));

                    listStatus.add(statusComandaRetur);
                }
            }
        }
        catch (JSONException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
        return listStatus;
    }
    public ArrayList<BeanArticolComandaRetur> deserializeListaArticoleRetur(String serializedResult) {
        BeanArticolComandaRetur articolComandaRetur = null;
        ArrayList<BeanArticolComandaRetur> listArticoleRetur = new ArrayList<BeanArticolComandaRetur>();
        try {
            Object json = new JSONTokener(serializedResult).nextValue();
            if (json instanceof JSONArray) {
                JSONArray jsonObject = new JSONArray(serializedResult);
                for (int i=0;i<jsonObject.length();i++) {
                    JSONObject articolReturObject = jsonObject.getJSONObject(i);
                    articolComandaRetur = new BeanArticolComandaRetur();
                    articolComandaRetur.setMatnr(articolReturObject.getString("matnr"));
                    articolComandaRetur.setArktx(articolReturObject.getString("arktx"));
                    articolComandaRetur.setKwmeng(articolReturObject.getString("kwmeng"));
                    articolComandaRetur.setVrkme(articolReturObject.getString("vrkme"));
                    articolComandaRetur.setValoare_pozitie(articolReturObject.getString("valoare_pozitie"));

                    listArticoleRetur.add(articolComandaRetur);
                }
            }
        }
        catch (JSONException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
        return listArticoleRetur;
    }

}
