package my.logon.screen.helpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import my.logon.screen.beans.ArticolCalculDesc;
import my.logon.screen.beans.ArticolDescarcare;
import my.logon.screen.beans.ArticolPalet;
import my.logon.screen.beans.BeanArticolRetur;
import my.logon.screen.beans.CantitateFiliala;
import my.logon.screen.beans.CostDescarcare;
import my.logon.screen.enums.TipCmdDistrib;
import my.logon.screen.enums.TipCmdGed;
import my.logon.screen.model.ArticolComanda;
import my.logon.screen.model.Constants;
import my.logon.screen.model.DateLivrare;

public class HelperCostDescarcare {

    public static List<ArticolComanda> getArticoleDescarcare(CostDescarcare costDescarcare, double valoareCost,
                                                             List<ArticolComanda> articoleComanda) {


        List<ArticolComanda> listArticole = new ArrayList<ArticolComanda>();

        for (ArticolDescarcare artDesc : costDescarcare.getArticoleDescarcare()) {

            if (artDesc.getCantitate() == 0)
                continue;

            ArticolComanda articolComanda = new ArticolComanda();

            articolComanda.setCodArticol(artDesc.getCod().replaceFirst("^0+(?!$)", ""));
            articolComanda.setNumeArticol(Constants.NUME_SERV_DESC_PALET);

            if (DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.LIVRARE_CUSTODIE)) {
                articolComanda.setCantitate(1);
                articolComanda.setCantUmb(1);
            } else {
                articolComanda.setCantitate(artDesc.getCantitate());
                articolComanda.setCantUmb(artDesc.getCantitate());
            }

            articolComanda.setPretUnit(artDesc.getValoare());
            articolComanda.setPret(artDesc.getValoare() * artDesc.getCantitate());
            articolComanda.setPretUnitarClient(artDesc.getValoare());
            articolComanda.setPretUnitarGed(artDesc.getValoare());


            if (DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.LIVRARE_CUSTODIE)) {
                articolComanda.setPretUnit(articolComanda.getPretUnit() * artDesc.getCantitate());
                articolComanda.setPretUnitarClient(articolComanda.getPretUnitarClient() * artDesc.getCantitate());
                articolComanda.setPretUnitarGed(articolComanda.getPretUnitarGed() * artDesc.getCantitate());
            }

            articolComanda.setProcent(0);
            articolComanda.setUm("BUC");
            articolComanda.setUmb("BUC");
            articolComanda.setDiscClient(0);
            articolComanda.setProcentFact(0);
            articolComanda.setMultiplu(1);
            articolComanda.setConditie(false);
            articolComanda.setProcAprob(0);
            articolComanda.setInfoArticol(" ");
            articolComanda.setObservatii("");
            articolComanda.setDepartAprob("");
            articolComanda.setIstoricPret("");
            articolComanda.setAlteValori("");
            articolComanda.setDepozit(getDepozitDescarcare(artDesc.getDepart(), articoleComanda));
            articolComanda.setTipArt("");
            articolComanda.setDepart(artDesc.getDepart());
            articolComanda.setDepartSintetic(artDesc.getDepart());
            articolComanda.setFilialaSite(artDesc.getFiliala());

            listArticole.add(articolComanda);
        }

        return listArticole;

    }


    public static List<ArticolComanda> getArticoleDescarcareDistrib(CostDescarcare costDescarcare, double valoareCost,
                                                                    List<ArticolComanda> articoleComanda) {

        List<ArticolComanda> listArticole = new ArrayList<ArticolComanda>();

        for (ArticolDescarcare artDesc : costDescarcare.getArticoleDescarcare()) {

            if (artDesc.getCantitate() == 0)
                continue;

            ArticolComanda articolComanda = new ArticolComanda();
            articolComanda.setCodArticol(artDesc.getCod().replaceFirst("^0+(?!$)", ""));
            articolComanda.setNumeArticol(Constants.NUME_SERV_DESC_PALET);

            if (DateLivrare.getInstance().getTipComandaDistrib().equals(TipCmdDistrib.LIVRARE_CUSTODIE)) {
                articolComanda.setCantitate(1);
                articolComanda.setCantUmb(1);
            } else {
                articolComanda.setCantitate(artDesc.getCantitate());
                articolComanda.setCantUmb(artDesc.getCantitate());
            }

            articolComanda.setPret(artDesc.getValoare() * artDesc.getCantitate());
            articolComanda.setPretUnit(artDesc.getValoare());
            articolComanda.setPretUnitarClient(artDesc.getValoare());
            articolComanda.setPretUnitarGed(artDesc.getValoare());

            if (DateLivrare.getInstance().getTipComandaDistrib().equals(TipCmdDistrib.LIVRARE_CUSTODIE)) {
                articolComanda.setPretUnit(articolComanda.getPretUnit() * artDesc.getCantitate());
                articolComanda.setPretUnitarClient(articolComanda.getPretUnitarClient() * artDesc.getCantitate());
                articolComanda.setPretUnitarGed(articolComanda.getPretUnitarGed() * artDesc.getCantitate());
            }


            articolComanda.setProcent(0);
            articolComanda.setUm("BUC");
            articolComanda.setUmb("BUC");
            articolComanda.setDiscClient(0);
            articolComanda.setProcentFact(0);
            articolComanda.setMultiplu(1);
            articolComanda.setConditie(false);
            articolComanda.setProcAprob(0);
            articolComanda.setInfoArticol(" ");
            articolComanda.setObservatii("");
            articolComanda.setDepartAprob("");
            articolComanda.setIstoricPret("");
            articolComanda.setAlteValori("");
            articolComanda.setDepozit(getDepozitDescarcare(artDesc.getDepart(), articoleComanda));
            articolComanda.setTipArt("");
            articolComanda.setDepart(artDesc.getDepart());
            articolComanda.setDepartSintetic(artDesc.getDepart());
            articolComanda.setFilialaSite(artDesc.getFiliala());

            listArticole.add(articolComanda);
        }

        return listArticole;

    }

    private static String getDepozitDescarcare(String depart, List<ArticolComanda> articoleComanda) {
        if (depart.substring(0, 2).equals("11"))
            return getDepozitComandaGed(articoleComanda);
        else
            return depart.substring(0, 2) + "V1";
    }

    private static String getDepozitComandaGed(List<ArticolComanda> articoleComanda) {
        String depozit = "";

        for (ArticolComanda articol : articoleComanda) {
            if (articol.getDepozit() != null && !articol.getDepozit().isEmpty()) {
                depozit = articol.getDepozit();
                break;
            }

        }

        return depozit;
    }

    public static void eliminaCostDescarcare(List<ArticolComanda> listArticole) {

        Iterator<ArticolComanda> iterator = listArticole.iterator();

        while (iterator.hasNext()) {

            ArticolComanda articol = iterator.next();

            if (articol.getNumeArticol() != null && articol.getNumeArticol().toUpperCase().contains("PREST.SERV.DESCARCARE PALET"))
                iterator.remove();

        }

    }

    public static ArticolComanda getArticolPalet(ArticolPalet articolPalet, String depozit, String unitLog) {

        ArticolComanda articolComanda = new ArticolComanda();

        articolComanda.setCodArticol(articolPalet.getCodPalet());
        articolComanda.setNumeArticol(articolPalet.getNumePalet());

        CantitateFiliala cantPaletFiliala = getCantPaletFiliala(articolPalet, unitLog);

        articolComanda.setCantitate(cantPaletFiliala.getCantitate());
        articolComanda.setCantUmb(cantPaletFiliala.getCantitate());
        articolComanda.setPretUnit(cantPaletFiliala.getPretUnitPalet());
        articolComanda.setPret(cantPaletFiliala.getPretUnitPalet() * cantPaletFiliala.getCantitate());
        articolComanda.setPretUnitarClient(cantPaletFiliala.getPretUnitPalet());
        articolComanda.setPretUnitarGed(cantPaletFiliala.getPretUnitPalet());
        articolComanda.setProcent(0);
        articolComanda.setUm("BUC");
        articolComanda.setUmb("BUC");
        articolComanda.setDiscClient(0);
        articolComanda.setProcentFact(0);
        articolComanda.setMultiplu(1);
        articolComanda.setConditie(false);
        articolComanda.setProcAprob(0);
        articolComanda.setInfoArticol(" ");
        articolComanda.setObservatii("");
        articolComanda.setDepartAprob("");
        articolComanda.setIstoricPret("");
        articolComanda.setAlteValori("");
        articolComanda.setDepozit(depozit);
        articolComanda.setTipArt("");
        articolComanda.setDepart(articolPalet.getDepart());
        articolComanda.setDepartSintetic(articolPalet.getDepart());
        articolComanda.setFilialaSite(unitLog);
        articolComanda.setUmPalet(true);

        return articolComanda;

    }

    private static CantitateFiliala getCantPaletFiliala(ArticolPalet articolPalet, String filiala) {

        CantitateFiliala retCantFiliala = new CantitateFiliala();

        if (articolPalet.getCantFiliala() == null || articolPalet.getCantFiliala().isEmpty()) {
            retCantFiliala.setCantitate(articolPalet.getCantitate());
            retCantFiliala.setPretUnitPalet(articolPalet.getPretUnit());
            return retCantFiliala;
        }

        int fltCantPaletFiliala = 0;
        for (CantitateFiliala cantFiliala : articolPalet.getCantFiliala()) {
            if (cantFiliala.getFiliala().equals(filiala)) {

                retCantFiliala.setPretUnitPalet(cantFiliala.getPretUnitPalet());

                fltCantPaletFiliala += cantFiliala.getCantitate();
                if (articolPalet.getCantitate() == cantFiliala.getCantTotal())
                    retCantFiliala.setCantitate(fltCantPaletFiliala);
                else
                    retCantFiliala.setCantitate(Math.round(articolPalet.getCantitate() * ((float) fltCantPaletFiliala / cantFiliala.getCantTotal())));
            }

        }

        return retCantFiliala;

    }


    public static List<ArticolCalculDesc> getDateCalculDescarcareRetur(List<BeanArticolRetur> listArticole) {

        List<ArticolCalculDesc> articoleCalcul = new ArrayList<ArticolCalculDesc>();

        for (BeanArticolRetur artCmd : listArticole) {

            if (artCmd.getCantitateRetur() > 0) {
                ArticolCalculDesc articol = new ArticolCalculDesc();
                articol.setCod(artCmd.getCod());
                articol.setCant(artCmd.getCantitateRetur());
                articol.setUm(artCmd.getUm());
                articol.setDepoz(" ");
                articoleCalcul.add(articol);
            }
        }

        return articoleCalcul;

    }

    public static CostDescarcare deserializeCostMacara(String dateCost) {

        CostDescarcare costDescarcare = new CostDescarcare();
        List<ArticolDescarcare> listArticole = new ArrayList<ArticolDescarcare>();
        List<ArticolPalet> listPaleti = new ArrayList<ArticolPalet>();

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(dateCost);

            costDescarcare.setSePermite(Boolean.valueOf(jsonObject.getString("sePermite")));

            JSONArray jsonArray = new JSONArray(jsonObject.getString("articoleDescarcare"));

            for (int i = 0; i < jsonArray.length(); i++) {
                ArticolDescarcare articol = new ArticolDescarcare();

                JSONObject object = jsonArray.getJSONObject(i);

                articol.setCod(object.getString("cod"));
                articol.setDepart(object.getString("depart"));
                articol.setValoare(Double.valueOf(object.getString("valoare")));
                articol.setCantitate(Double.valueOf(object.getString("cantitate")));
                articol.setValoareMin(Double.valueOf(object.getString("valoareMin")));
                listArticole.add(articol);

            }

            costDescarcare.setArticoleDescarcare(listArticole);

            JSONArray jsonPaleti = new JSONArray(jsonObject.getString("articolePaleti"));

            for (int i = 0; i < jsonPaleti.length(); i++) {
                ArticolPalet articol = new ArticolPalet();

                JSONObject object = jsonPaleti.getJSONObject(i);

                articol.setCodPalet(object.getString("codPalet"));
                articol.setNumePalet(object.getString("numePalet"));
                articol.setDepart(object.getString("depart"));
                articol.setCantitate(Integer.valueOf(object.getString("cantitate")));
                articol.setPretUnit(Double.valueOf(object.getString("pretUnit")));
                articol.setFurnizor(object.getString("furnizor"));
                articol.setCodArticol(object.getString("codArticol"));
                articol.setNumeArticol(object.getString("numeArticol"));
                articol.setCantArticol(object.getString("cantArticol"));
                articol.setUmArticol(object.getString("umArticol"));

                listPaleti.add(articol);

            }

            costDescarcare.setArticolePaleti(listPaleti);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return costDescarcare;
    }

    public static String getDepozitPalet(List<ArticolComanda> listArticole, String codArticolPalet) {
        String depozit = "";

        for (ArticolComanda art : listArticole) {
            if (art.getCodArticol().contains(codArticolPalet)) {
                depozit = art.getDepozit().replace("040", "04").replace("041", "04");
                break;
            }
        }

        return depozit;
    }


}
