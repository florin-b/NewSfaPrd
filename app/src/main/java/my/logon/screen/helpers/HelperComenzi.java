package my.logon.screen.helpers;

import my.logon.screen.beans.ArticolDB;
import my.logon.screen.model.ArticolComanda;

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
}
