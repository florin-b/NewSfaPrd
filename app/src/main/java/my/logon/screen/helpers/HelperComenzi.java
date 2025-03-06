package my.logon.screen.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.logon.screen.beans.ArticolDB;
import my.logon.screen.model.ArticolComanda;
import my.logon.screen.model.DateLivrare;

public class HelperComenzi {

    public static ArticolDB getArticolDB(ArticolComanda articolComanda) {

        ArticolDB articolDB = new ArticolDB();
        articolDB.setCod(articolComanda.getCodArticol());
        articolDB.setDepart(articolComanda.getDepart());
        articolDB.setDepartAprob(articolComanda.getDepartAprob());
        articolDB.setNume(articolComanda.getNumeArticol());
        articolDB.setStoc("-1");
        articolDB.setUmPalet(articolComanda.isUmPalet());
        articolDB.setUmVanz(articolComanda.getUm());
        articolDB.setUmVanz10(articolComanda.getUm());
        articolDB.setLungime(articolComanda.getLungime());
        articolDB.setCategorie(" ");
        articolDB.setTipAB(" ");
        articolDB.setSintetic(articolComanda.getSintetic());

        return articolDB;

    }

    public static ArticolComanda getArticolModifCmd(HashMap<String, String> paramsStocDepozit) {

        ArticolComanda articolModifCmd = null;

        if (DateLivrare.getInstance().getComandaInit() == null)
            return null;

        for (ArticolComanda articolComanda : DateLivrare.getInstance().getComandaInit()) {

            if (articolExist(articolComanda, paramsStocDepozit)) {
                articolModifCmd = articolComanda;
                break;
            }

        }

        return articolModifCmd;

    }

    public static boolean articolExist(ArticolComanda articolComanda, HashMap<String, String> paramsStocDepozit) {

        return articolComanda.getCodArticol().replaceFirst("^0*", "").equals(paramsStocDepozit.get("codArt").replaceFirst("^0*", "")) &&
                articolComanda.getFilialaSite().equals(paramsStocDepozit.get("filiala")) &&
                articolComanda.getDepozit().equals(paramsStocDepozit.get("depozit"));

    }


    public static List<ArticolComanda> getComandaInit(List<ArticolComanda> listArticoleInit) {

        List<ArticolComanda> comandaInit = new ArrayList<>();

        for (ArticolComanda articolInit : listArticoleInit) {
            ArticolComanda articol = new ArticolComanda();
            articol.setCodArticol(articolInit.getCodArticol());
            articol.setCantitate(articolInit.getCantitate());
            articol.setUm(articolInit.getUm());
            articol.setFilialaSite(articolInit.getFilialaSite());
            articol.setDepozit(articolInit.getDepozit());
            comandaInit.add(articol);
        }

        return comandaInit;

    }
}
