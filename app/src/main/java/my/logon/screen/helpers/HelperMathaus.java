package my.logon.screen.helpers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import my.logon.screen.beans.CostTransportMathaus;
import my.logon.screen.beans.RezumatComanda;
import my.logon.screen.model.ArticolComanda;
import my.logon.screen.model.HelperTranspBuc;
import my.logon.screen.model.ListaArticoleComanda;
import my.logon.screen.model.ListaArticoleComandaGed;
import my.logon.screen.utils.UtilsComenzi;

public class HelperMathaus {

    public static void adaugaArticolTransport(List<CostTransportMathaus> costTransport, String canalDistrib, List<ArticolComanda> listArticole) {

        List<ArticolComanda> listArticoleComanda;
        if (canalDistrib.equals("10"))
            listArticoleComanda = ListaArticoleComanda.getInstance().getListArticoleLivrare();
        else if (canalDistrib.equals("20"))
            listArticoleComanda = ListaArticoleComandaGed.getInstance().getListArticoleLivrare();
        else
            listArticoleComanda = listArticole;

        eliminaCostTransport(listArticoleComanda);
        eliminaTaxeTransport(listArticoleComanda);

        for (CostTransportMathaus cost : costTransport) {

            if (cost.getCodArtTransp().trim().isEmpty() || cost.getValTransp().equals("0"))
                continue;

            for (ArticolComanda articol : listArticoleComanda) {

                if (canalDistrib.equals("10")) {
                    if (cost.getFiliala().equals(articol.getFilialaSite()) && !cost.getValTransp().equals("0") && cost.getDepart().equals(articol.getDepart())) {
                        listArticoleComanda.add(genereazaArticolTransport(articol, cost, canalDistrib));
                        break;
                    }
                } else {

                    if (cost.getFiliala().equals(articol.getFilialaSite()) && !cost.getValTransp().equals("0")) {
                        listArticoleComanda.add(genereazaArticolTransport(articol, cost, canalDistrib));
                        break;
                    }
                }

            }

        }

    }


    public static void adaugaArticolTransportModificare(List<CostTransportMathaus> costTransport, List<ArticolComanda> listArticoleComanda) {

        eliminaCostTransport(listArticoleComanda, costTransport);

        for (CostTransportMathaus cost : costTransport) {

            for (ArticolComanda articol : listArticoleComanda) {

                if (!cost.getValTransp().equals("0") && cost.getDepart().equals(articol.getDepart())) {
                    listArticoleComanda.add(genereazaArticolTransport(articol, cost, ""));
                    break;
                }

            }

        }

    }

    private static boolean isArtTaxaTransp(String numeArticol) {
        return numeArticol != null && numeArticol.toUpperCase().contains("TAXA") && numeArticol.toUpperCase().contains("TONAJ");
    }

    private static boolean isArtCostTransp(String numeArticol) {
        return numeArticol != null && numeArticol.toUpperCase().contains("SERV") && numeArticol.toUpperCase().contains("TRANSP");
    }

    public static void eliminaTaxeTransport(List<ArticolComanda> listArticole) {

        Iterator<ArticolComanda> iterator = listArticole.iterator();
        while (iterator.hasNext()) {

            ArticolComanda articol = iterator.next();

            if (isArtTaxaTransp(articol.getNumeArticol())) {
                iterator.remove();
            }
        }
    }

    public static void eliminaCostTransport(List<ArticolComanda> listArticole) {

        Iterator<ArticolComanda> iterator = listArticole.iterator();
        while (iterator.hasNext()) {

            ArticolComanda articol = iterator.next();

            if (isArtCostTransp(articol.getNumeArticol())) {
                iterator.remove();
            }
        }
    }

    public static void eliminaCostTransport(List<ArticolComanda> listArticole, List<CostTransportMathaus> costTransport) {

        Iterator<ArticolComanda> iterator = listArticole.iterator();

        for (CostTransportMathaus cost : costTransport) {

            while (iterator.hasNext()) {

                ArticolComanda articol = iterator.next();

                if (cost.getCodArtTransp().replaceAll("^0+", "").equals(articol.getCodArticol().replaceAll("^0+", "")) && !articol.getNumeArticol().equals("Taxa verde")) {
                    iterator.remove();
                }

            }

            iterator = listArticole.iterator();
        }

    }

    private static ArticolComanda genereazaArticolTransport(ArticolComanda articol, CostTransportMathaus costTransport, String canal) {
        ArticolComanda articolComanda = new ArticolComanda();

        articolComanda.setCodArticol(costTransport.getCodArtTransp().replaceAll("^0+", ""));
        articolComanda.setNumeArticol(costTransport.getNumeCost());
        articolComanda.setCantitate(1);
        articolComanda.setCantUmb(1);
        articolComanda.setPretUnit(Double.valueOf(costTransport.getValTransp()));
        articolComanda.setPret(articolComanda.getPretUnit());
        articolComanda.setPretUnitarClient(articolComanda.getPretUnit());
        articolComanda.setPretUnitarGed(articolComanda.getPretUnit());
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
        articolComanda.setDepozit(getDepozitDescarcare(articol));
        articolComanda.setTipArt("");

        String departArt = articol.getDepart();
        if (canal != null && !canal.isEmpty() && canal.equals("20"))
            departArt = "11";

        articolComanda.setDepart(departArt);
        articolComanda.setDepartSintetic(departArt);
        articolComanda.setFilialaSite(costTransport.getFiliala());
        articolComanda.setTipTransport(costTransport.getTipTransp());

        return articolComanda;
    }

    private static String getDepozitDescarcare(ArticolComanda articol) {
        if (UtilsComenzi.isDespozitDeteriorate(articol.getDepozit()))
            return articol.getDepozit();
        else {
            if (articol.getDepart().substring(0, 2).equals("11"))
                return "MAV1";
            else
                return articol.getDepart().substring(0, 2) + "V1";
        }
    }

    public static List<RezumatComanda> getRezumatComanda(List<ArticolComanda> listArticole) {

        Set<String> filiale = getFilialeComanda(listArticole);

        List<RezumatComanda> listComenzi = new ArrayList<RezumatComanda>();

        for (String filiala : filiale) {

            RezumatComanda rezumat = new RezumatComanda();
            rezumat.setFilialaLivrare(filiala);
            List<ArticolComanda> listArtComanda = new ArrayList<ArticolComanda>();

            for (ArticolComanda articol : listArticole) {
                if (articol.getFilialaSite().equals(filiala)) {
                    listArtComanda.add(articol);
                }
            }

            rezumat.setListArticole(listArtComanda);
            listComenzi.add(rezumat);
        }

        return listComenzi;
    }

    private static Set<String> getFilialeComanda(List<ArticolComanda> listArticole) {

        Set<String> filiale = new HashSet<String>();
        for (final ArticolComanda articol : listArticole) {
            filiale.add(articol.getFilialaSite());
        }
        return filiale;

    }

    public static void trateazaTaxaBucuresti(List<ArticolComanda> listArticoleComanda) {

        String depozitArt = "";
        String tipTransp = "";

        for (ArticolComanda articolComanda : listArticoleComanda) {
            if (articolComanda.getDepozit() != null && !articolComanda.getDepozit().trim().isEmpty() && articolComanda.getTipTransport() != null
                    && !articolComanda.getTipTransport().trim().isEmpty()) {
                depozitArt = articolComanda.getDepozit();
                tipTransp = articolComanda.getTipTransport();
                break;
            }
        }

        for (ArticolComanda articolComanda : listArticoleComanda) {
            if (HelperTranspBuc.isTranspZonaBuc(articolComanda.getCodArticol())) {
                articolComanda.setDepozit(depozitArt);
                articolComanda.setTipTransport(tipTransp);
            }
        }

    }


}
