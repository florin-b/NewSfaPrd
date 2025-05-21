package my.logon.screen.helpers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import my.logon.screen.beans.ArticolDescarcare;
import my.logon.screen.beans.ArticolPalet;
import my.logon.screen.beans.BeanStocTCLI;
import my.logon.screen.beans.BeanTaxaCamion;
import my.logon.screen.beans.CostDescarcare;
import my.logon.screen.beans.CostTransportMathaus;
import my.logon.screen.beans.DateArticolMathaus;
import my.logon.screen.beans.LivrareMathaus;
import my.logon.screen.beans.RezumatComanda;
import my.logon.screen.beans.TaxaMasina;
import my.logon.screen.beans.TaxaTransport;
import my.logon.screen.enums.EnumTipCamion;
import my.logon.screen.enums.TipCmdDistrib;
import my.logon.screen.enums.TipCmdGed;
import my.logon.screen.model.ArticolComanda;
import my.logon.screen.model.Constants;
import my.logon.screen.model.DateLivrare;
import my.logon.screen.model.ListaArticoleComanda;
import my.logon.screen.model.ListaArticoleComandaGed;
import my.logon.screen.utils.UtilsArticole;
import my.logon.screen.utils.UtilsComenzi;
import my.logon.screen.utils.UtilsFormatting;
import my.logon.screen.utils.UtilsGeneral;

public class HelperMathaus {

    private static NumberFormat nf2 = new DecimalFormat("#0.00");

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

    public static boolean isArtTaxaTransp(String numeArticol) {
        return numeArticol != null && numeArticol.toUpperCase().contains("TAXA") && numeArticol.toUpperCase().contains("TONAJ");
    }

    private static boolean isArtTaxaMetro(String numeArticol) {
        return numeArticol != null && numeArticol.toUpperCase().contains("EXTRA") && numeArticol.toUpperCase().contains("METRO");
    }

    public static boolean isArtCostTransp(String numeArticol) {
        return numeArticol != null && numeArticol.toUpperCase().contains("SERV") && numeArticol.toUpperCase().contains("TRANSP");
    }

    public static boolean isArtTaxaAcces(String numeArticol) {
        boolean taxa1 = numeArticol != null && numeArticol.toUpperCase().contains("TAXA") && numeArticol.toUpperCase().contains("ACCES");
        boolean taxa2 = numeArticol != null && numeArticol.toUpperCase().contains("EXTRA") && numeArticol.toUpperCase().contains("METRO");

        return taxa1 || taxa2;
    }

    public static void eliminaTaxeTransport(List<ArticolComanda> listArticole) {

        Iterator<ArticolComanda> iterator = listArticole.iterator();
        while (iterator.hasNext()) {

            ArticolComanda articol = iterator.next();

            if (isArtTaxaTransp(articol.getNumeArticol()) || isArtTaxaMetro(articol.getNumeArticol())) {
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

    public static void eliminaCostDescarcareFiliala(CostDescarcare costDescarcare, List<ArticolComanda> listArticole) {

        Iterator<ArticolComanda> iterator = listArticole.iterator();

        for (ArticolDescarcare articolDescarcare : costDescarcare.getArticoleDescarcare()) {

            while (iterator.hasNext()) {

                ArticolComanda articolComanda = iterator.next();

                if (articolDescarcare.getCod().replaceFirst("^0*", "").equals((articolComanda.getCodArticol().replaceFirst("^0*", ""))))
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
        articolComanda.setPretMinim(articolComanda.getPretUnit());
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
        articolComanda.setCantitate50(articolComanda.getCantitate());
        articolComanda.setUm50("BUC");

        return articolComanda;
    }

    private static String getDepozitDescarcare(ArticolComanda articol) {
        if (UtilsComenzi.isDespozitDeteriorate(articol.getDepozit()) || isDepozitExceptie(articol.getDepozit()))
            return articol.getDepozit();
        else {
            if (articol.getDepart().substring(0, 2).equals("11"))
                return "MAV1";
            else
                return articol.getDepart().substring(0, 2) + "V1";
        }
    }

    public static boolean isDepozitExceptie(String depozit) {
        return depozit != null &&
                (depozit.equals("DESC") || depozit.equals("GAR1") || depozit.equals("WOOD") || depozit.equals("MAV2") ||
                        depozit.equals("92V1") || depozit.equals("95V1") || depozit.equals("DSCM") || depozit.equals("MAD1"));
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

    public static void setTonajComanda() {

        if (DateLivrare.getInstance().getDatePoligonLivrare() == null)
            return;

        if (DateLivrare.getInstance().getTipMasina() == null || DateLivrare.getInstance().getTipMasina().trim().isEmpty())
            DateLivrare.getInstance().setTonaj("20");
        else if (DateLivrare.getInstance().getTipMasina().toLowerCase().contains("iveco"))
            DateLivrare.getInstance().setTonaj("3.5");
        else if (DateLivrare.getInstance().getTipMasina().toLowerCase().contains("scurt"))
            DateLivrare.getInstance().setTonaj("10");

    }

    public static double getCantitateCanal50(DateArticolMathaus articolMathaus, ArticolComanda articolComanda) {

        BigDecimal cantLivrata = new BigDecimal(articolMathaus.getQuantity());
        BigDecimal cant50 = new BigDecimal(articolComanda.getCantitate50());
        BigDecimal cantTotal = new BigDecimal(articolComanda.getCantitate());
        BigDecimal resultInter = cantLivrata.multiply(cant50);
        BigDecimal resultFin = resultInter.divide(cantTotal, 2, RoundingMode.HALF_EVEN);

        return resultFin.doubleValue();

    }

    public static String getFilialaSecundara() {
        if (DateLivrare.getInstance().getDatePoligonLivrare() != null && !DateLivrare.getInstance().getDatePoligonLivrare().getFilialaSecundara().trim().isEmpty())
            return DateLivrare.getInstance().getDatePoligonLivrare().getFilialaSecundara();

        return "";
    }

    public static boolean isConditiiDepozitTCLI(ArticolComanda articolComanda, String canal) {

        if (canal.equals("10") && DateLivrare.getInstance().getTipComandaDistrib().equals(TipCmdDistrib.DISPOZITIE_LIVRARE))
            return false;

        if (canal.equals("10") && DateLivrare.getInstance().getTipComandaDistrib().equals(TipCmdDistrib.ARTICOLE_DETERIORATE))
            return false;

        if (canal.equals("20") && DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.DISPOZITIE_LIVRARE))
            return false;

        if (canal.equals("20") && DateLivrare.getInstance().getTipComandaGed().equals(TipCmdGed.ARTICOLE_DETERIORATE))
            return false;

        if (!DateLivrare.getInstance().getTransport().equals("TCLI"))
            return false;

        if (articolComanda.getListStocTCLI() == null || articolComanda.getListStocTCLI().size() == 0)
            return false;

        return true;
    }

    public static List<BeanStocTCLI> genereazaStocUnitLog(ArticolComanda articolComanda) {
        List<BeanStocTCLI> listStocTCLI = new ArrayList<>();

        BeanStocTCLI beanStoc = new BeanStocTCLI();
        beanStoc.setCantitate(articolComanda.getCantitate());
        beanStoc.setDepozit(articolComanda.getListStocTCLI().get(0).getDepozit());
        beanStoc.setUm(articolComanda.getListStocTCLI().get(0).getUm());
        listStocTCLI.add(beanStoc);

        return listStocTCLI;
    }


    public static DateArticolMathaus genereazaStocArticolTCLI(ArticolComanda artCmd, BeanStocTCLI stocTCLI) {

        DateArticolMathaus dateArticol = new DateArticolMathaus();
        dateArticol.setProductCode(artCmd.getCodArticol());
        dateArticol.setQuantity(artCmd.getCantitate());
        dateArticol.setUnit(artCmd.getUm());

        DecimalFormat df = new DecimalFormat("#####0.00");
        DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(dfs);

        double valPozArt = artCmd.getPret();

        if (valPozArt == 0)
            valPozArt = (artCmd.getPretUnitarClient() / artCmd.getMultiplu()) * artCmd.getCantUmb();

        dateArticol.setValPoz(valPozArt);
        dateArticol.setGreutate(artCmd.getGreutateBruta());
        dateArticol.setQuantity50(artCmd.getCantitate50());

        if (stocTCLI != null && stocTCLI.getCantitate() > 0) {
            dateArticol.setQuantity(stocTCLI.getCantitate());

            double valPozTCLI = (stocTCLI.getCantitate() * valPozArt) / artCmd.getCantitate();
            dateArticol.setValPoz(Double.parseDouble(df.format(valPozTCLI)));

            double greutateTCLI = (stocTCLI.getCantitate() * artCmd.getGreutateBruta()) / artCmd.getCantitate();
            dateArticol.setGreutate(Double.parseDouble(df.format(greutateTCLI)));

            dateArticol.setQuantity50(stocTCLI.getCantitate());
        }

        dateArticol.setUnit50(artCmd.getUm50());

        if (UtilsComenzi.isDespozitDeteriorate(artCmd.getDepozit()) || isDepozitExceptie(artCmd.getDepozit()) || UtilsComenzi.isLivrareCustodie() ||
                UtilsComenzi.isArticolCuDepozit(artCmd, stocTCLI))
            dateArticol.setDepozit(artCmd.getDepozit());
        else if (stocTCLI != null && !stocTCLI.getDepozit().trim().isEmpty())
            dateArticol.setDepozit(stocTCLI.getDepozit());
        else
            dateArticol.setDepozit("");

        if (artCmd.getArticolMathaus() != null)
            dateArticol.setTip2(artCmd.getArticolMathaus().getTip2());
        else
            dateArticol.setTip2("");

        if (artCmd.getFilialaSite().equals("BV90"))
            dateArticol.setUlStoc("BV90");
        else if (stocTCLI != null) {
            if (stocTCLI.getDepozit().equals("MAV1"))
                dateArticol.setUlStoc(UtilsGeneral.getUnitLogGed(artCmd.getFilialaSite()));
            else
                dateArticol.setUlStoc(artCmd.getFilialaSite());
        } else if (!DateLivrare.getInstance().getCodFilialaFasonate().trim().isEmpty())
            dateArticol.setUlStoc(DateLivrare.getInstance().getCodFilialaFasonate());

        if (artCmd.getArticolMathaus() != null && artCmd.getArticolMathaus().getTipStoc() != null)
            dateArticol.setTipStoc(artCmd.getArticolMathaus().getTipStoc());


        return dateArticol;

    }

    public static void setTransportTERT(LivrareMathaus livrareMathaus) {

        for (TaxaMasina taxaMasina : livrareMathaus.getTaxeMasini()) {

            if (taxaMasina.getTraty().equals("TERT")) {
                CostTransportMathaus costTransportMathaus = new CostTransportMathaus();
                costTransportMathaus.setCodArtTransp(taxaMasina.getMatnrTransport());
                costTransportMathaus.setNumeCost(taxaMasina.getMaktxTransport());
                costTransportMathaus.setValTransp(String.valueOf(taxaMasina.getTaxaTransport()));
                costTransportMathaus.setFiliala(taxaMasina.getWerks());
                costTransportMathaus.setTipTransp("TERT");
                costTransportMathaus.setDepart(taxaMasina.getSpart());
                livrareMathaus.getCostTransport().add(costTransportMathaus);
            }
        }

    }

    public static List<ArticolPalet> getListPaletiCopy(List<ArticolPalet> listPaleti) {

        List<ArticolPalet> copyList = new ArrayList<>();

        for (ArticolPalet articolPalet : listPaleti) {

            ArticolPalet copyPalet = new ArticolPalet();
            copyPalet.setUmArticol(articolPalet.getUmArticol());
            copyPalet.setCantArticol(articolPalet.getCantArticol());
            copyPalet.setNumePalet(articolPalet.getNumePalet());
            copyPalet.setAdaugat(articolPalet.isAdaugat());
            copyPalet.setCodArticol(articolPalet.getCodArticol());
            copyPalet.setCodPalet(articolPalet.getCodPalet());
            copyPalet.setCantitate(articolPalet.getCantitate());
            copyPalet.setDepart(articolPalet.getDepart());
            copyPalet.setFiliala(articolPalet.getFiliala());
            copyPalet.setFurnizor(articolPalet.getFurnizor());
            copyPalet.setPretUnit(articolPalet.getPretUnit());
            copyPalet.setCantitate(articolPalet.getCantitate());

            copyList.add(copyPalet);
        }


        return copyList;
    }

    public static List<BeanStocTCLI> getStocTCLIDepozit(String cantitate, String depozit, String um) {

        DecimalFormat df = new DecimalFormat("#####0.00");
        DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(dfs);

        BeanStocTCLI beanStocTCLI = new BeanStocTCLI();
        try {
            beanStocTCLI.setCantitate(df.parse(cantitate).doubleValue());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        beanStocTCLI.setDepozit(depozit);
        beanStocTCLI.setUm(um);
        return Arrays.asList(beanStocTCLI);

    }

    public static boolean isComandaVanzareTCLI() {
        return DateLivrare.getInstance().getTransport().equals("TCLI") && DateLivrare.getInstance().getFilialaLivrareTCLI() != null &&
                !DateLivrare.getInstance().getFilialaLivrareTCLI().getUnitLog().trim().isEmpty();
    }


    public static boolean isTaxaTransport(ArticolComanda articolComanda, CostTransportMathaus costTranport, String filiala) {

        if (articolComanda == null)
            return false;

        if (costTranport == null)
            return false;

        if (filiala == null)
            return false;

        return articolComanda.getCodArticol().replaceFirst("^0*", "").equals(costTranport.getCodArtTransp().replaceFirst("^0*", ""))
                && articolComanda.getFilialaSite().equals(filiala) && costTranport.getFiliala().equals(filiala);

    }

    public static boolean isArticolIdentic(ArticolComanda articol1, ArticolComanda articol2) {
        if (articol1 == null || articol2 == null)
            return false;

        return articol1.getCodArticol().replaceFirst("^0*", "").equals(articol2.getCodArticol().replaceFirst("^0*", ""));

    }


    public static boolean isCodArticolServiciuTRAP(ArticolComanda articolComanda) {

        if (articolComanda == null)
            return false;

        if (articolComanda.getCodArticol() == null)
            return false;

        if (articolComanda.getNumeArticol() == null)
            return false;

        boolean isServiciu = articolComanda.getCodArticol().replaceFirst("^0*", "").startsWith("30");

        if (!isServiciu)
            return false;

        if (articolComanda.getNumeArticol().contains(Constants.NUME_SERV_DESC_PALET))
            return true;
        else if (isServiciuGeneral(articolComanda)) ;
        return false;

    }

    public static double getCmpCorectat(String codArticol, LivrareMathaus livrareMathaus) {

        double cmpCorectat = 0;

        for (DateArticolMathaus dateArticol : livrareMathaus.getComandaMathaus().getDeliveryEntryDataList()) {

            String codArticolComanda = codArticol;

            if (codArticol.length() == 8 || !Character.isDigit(codArticol.charAt(0)))
                codArticolComanda = "0000000000" + codArticol;

            if (codArticolComanda.equals(dateArticol.getProductCode())) {
                cmpCorectat = dateArticol.getCmpCorectat();
                break;
            }

        }

        return cmpCorectat;
    }

    public static void setFilialaSite(ArticolComanda articolComanda, LivrareMathaus livrareMathaus) {

        if (livrareMathaus == null)
            return;

        if (articolComanda != null && articolComanda.getFilialaSite().equals("BV90"))
            return;

        if (livrareMathaus.getComandaMathaus().getDeliveryEntryDataList() == null)
            return;


        String codArticolComanda = articolComanda.getCodArticol();

        if (articolComanda.getCodArticol().length() == 8 || !Character.isDigit(codArticolComanda.charAt(0)))
            codArticolComanda = "0000000000" + articolComanda.getCodArticol();

        for (DateArticolMathaus articolMathaus : livrareMathaus.getComandaMathaus().getDeliveryEntryDataList()) {
            if (codArticolComanda.equals(articolMathaus.getProductCode())) {
                articolComanda.setFilialaSite(articolMathaus.getDeliveryWarehouse());
                break;
            }
        }

    }

    public static int getNrPaletiFiliala(LivrareMathaus dateLivrare, String filiala) {

        int nrPaleti = 0;

        for (ArticolPalet articolPalet : dateLivrare.getListPaleti()) {

            for (DateArticolMathaus articolMathaus : dateLivrare.getComandaMathaus().getDeliveryEntryDataList()) {

                if (articolPalet.getCodArticol().replaceFirst("^0*", "").equals(articolMathaus.getProductCode().replaceFirst("^0*", ""))
                        && articolMathaus.getDeliveryWarehouse().equals(filiala)) {
                    nrPaleti += articolPalet.getCantitate();
                }
            }
        }

        return nrPaleti;
    }

    public static int getNrPaletiFilialaDepart(CostDescarcare costDescarcare, String filiala, String depart) {

        int nrPaleti = 0;

        for (ArticolPalet articolPalet : costDescarcare.getArticolePaleti()) {
            if (articolPalet.getFiliala().equals(filiala) && articolPalet.getDepart().equals(depart))
                nrPaleti += articolPalet.getCantitate();
        }

        return nrPaleti;
    }

    public static List<CostTransportMathaus> getCostTransportDepart(List<TaxaTransport> listTaxeTransport, List<ArticolComanda> listArticoleComanda) {

        List<CostTransportMathaus> costTranspDepart = new ArrayList<>();

        for (TaxaTransport taxaTransportFil : listTaxeTransport) {

            EnumTipCamion camionSelect = taxaTransportFil.getSelectedCamion();
            boolean acceptaMacara = taxaTransportFil.isAcceptaMacara();

            for (BeanTaxaCamion taxaCamion : taxaTransportFil.getListTaxe()) {

                if (taxaCamion.getTipCamion().equals(camionSelect) && acceptaMacara == (taxaCamion.getTaxeLivrare().isMacara() || taxaCamion.getTaxeLivrare().isLift())) {

                    List<CostTransportMathaus> taxeTranspFil = getCostTranspFiliala(taxaCamion.getTaxeLivrare().getTaxeDivizii(), taxaTransportFil.getFiliala(), listArticoleComanda);
                    costTranspDepart.addAll(taxeTranspFil);

                }

            }

        }

        return costTranspDepart;

    }

    private static List<CostTransportMathaus> getCostTranspFiliala(List<TaxaMasina> taxeDivizii, String filiala, List<ArticolComanda> listArticoleComanda) {

        ArticolComanda articolTranspFiliala = getTransportFiliala(filiala, listArticoleComanda);

        NumberFormat nf3 = NumberFormat.getInstance();
        nf3.setMinimumFractionDigits(2);
        nf3.setMaximumFractionDigits(2);

        double valTranspExtraTotal = UtilsFormatting.bigDecimalSubstract(articolTranspFiliala.getPret(), articolTranspFiliala.getPretMinim());
        double transpExtraDepart;

        List<CostTransportMathaus> listCostTransportMathaus = new ArrayList<>();

        for (TaxaMasina taxaMasina : taxeDivizii) {

            if (taxaMasina.getTaxaTransport() > 0) {
                CostTransportMathaus costTransportMathaus = new CostTransportMathaus();
                costTransportMathaus.setDepart(taxaMasina.getSpart());
                costTransportMathaus.setFiliala(filiala);
                costTransportMathaus.setTipTransp(taxaMasina.getTraty());
                costTransportMathaus.setCodArtTransp(taxaMasina.getMatnrTransport());
                costTransportMathaus.setNumeCost(taxaMasina.getMaktxTransport());
                transpExtraDepart = taxaMasina.getTaxaTransport() + valTranspExtraTotal * (UtilsFormatting.bigDecimalDivide(taxaMasina.getTaxaTransport(), articolTranspFiliala.getPretMinim()));
                costTransportMathaus.setValTransp(nf3.format(transpExtraDepart));
                listCostTransportMathaus.add(costTransportMathaus);
            }

            if (taxaMasina.getTaxaAcces() > 0) {
                CostTransportMathaus costTransportMathaus = new CostTransportMathaus();
                costTransportMathaus.setDepart(taxaMasina.getSpart());
                costTransportMathaus.setFiliala(filiala);
                costTransportMathaus.setTipTransp(taxaMasina.getTraty());
                costTransportMathaus.setCodArtTransp(taxaMasina.getMatnrAcces());
                costTransportMathaus.setNumeCost(taxaMasina.getMaktxAcces());
                costTransportMathaus.setValTransp(String.valueOf(taxaMasina.getTaxaAcces()));
                listCostTransportMathaus.add(costTransportMathaus);
            }

            if (taxaMasina.getTaxaZona() > 0) {
                CostTransportMathaus costTransportMathaus = new CostTransportMathaus();
                costTransportMathaus.setDepart(taxaMasina.getSpart());
                costTransportMathaus.setFiliala(filiala);
                costTransportMathaus.setTipTransp(taxaMasina.getTraty());
                costTransportMathaus.setCodArtTransp(taxaMasina.getMatnrZona());
                costTransportMathaus.setNumeCost(taxaMasina.getMaktxZona());
                costTransportMathaus.setValTransp(String.valueOf(taxaMasina.getTaxaZona()));
                listCostTransportMathaus.add(costTransportMathaus);
            }

            if (taxaMasina.getTaxaUsor() > 0) {
                CostTransportMathaus costTransportMathaus = new CostTransportMathaus();
                costTransportMathaus.setDepart(taxaMasina.getSpart());
                costTransportMathaus.setFiliala(filiala);
                costTransportMathaus.setTipTransp(taxaMasina.getTraty());
                costTransportMathaus.setCodArtTransp(taxaMasina.getMatnrUsor());
                costTransportMathaus.setNumeCost(taxaMasina.getMaktxUsor());
                costTransportMathaus.setValTransp(String.valueOf(taxaMasina.getTaxaUsor()));
                listCostTransportMathaus.add(costTransportMathaus);
            }

        }

        return listCostTransportMathaus;

    }

    private static ArticolComanda getTransportFiliala(String filiala, List<ArticolComanda> listArticoleComanda) {

        ArticolComanda artTransport = null;

        for (ArticolComanda articolComanda : listArticoleComanda) {
            if (UtilsArticole.isArticolTrasport(articolComanda.getNumeArticol()) && articolComanda.getFilialaSite().equals(filiala))
                artTransport = articolComanda;
        }

        return artTransport;

    }

    public static CostDescarcare getCostDescarcareDivizii(List<TaxaTransport> listTaxeTransport) {

        CostDescarcare costDescarcare = new CostDescarcare();
        List<ArticolDescarcare> listArticoleDescarcare = new ArrayList<>();
        costDescarcare.setArticoleDescarcare(listArticoleDescarcare);

        for (TaxaTransport taxaTransport : listTaxeTransport) {

            if (!taxaTransport.isAcceptaMacara())
                continue;

            EnumTipCamion tipCamionFiliala = taxaTransport.getSelectedCamion();

            for (BeanTaxaCamion taxaCamion : taxaTransport.getListTaxe()) {

                if (taxaCamion.getTipCamion().equals(tipCamionFiliala) && (taxaCamion.getTaxeLivrare().isMacara() || taxaCamion.getTaxeLivrare().isLift())) {

                    for (TaxaMasina taxaMasina : taxaCamion.getTaxeLivrare().getTaxeDivizii()) {
                        ArticolDescarcare articolDescarcare = new ArticolDescarcare();
                        articolDescarcare.setCod(taxaMasina.getMatnrMacara());
                        articolDescarcare.setFiliala(taxaTransport.getFiliala());
                        articolDescarcare.setDepart(taxaMasina.getSpart());
                        articolDescarcare.setCantitate(taxaMasina.getNrPaleti());
                        articolDescarcare.setValoare(taxaMasina.getTaxaMacara());
                        articolDescarcare.setValoareMin(taxaMasina.getTaxaMacara());
                        listArticoleDescarcare.add(articolDescarcare);
                    }
                }
            }
        }

        return costDescarcare;
    }


    public static CostDescarcare getCostDescarcareFiliala(List<TaxaTransport> listTaxeTransport) {

        CostDescarcare costDescarcare = new CostDescarcare();
        List<ArticolDescarcare> listArticoleDescarcare = new ArrayList<>();
        costDescarcare.setArticoleDescarcare(listArticoleDescarcare);

        for (TaxaTransport taxaTransport : listTaxeTransport) {

            if (!taxaTransport.isAcceptaMacara())
                continue;

            EnumTipCamion tipCamionFiliala = taxaTransport.getSelectedCamion();

            for (BeanTaxaCamion taxaCamion : taxaTransport.getListTaxe()) {

                if (taxaCamion.getTipCamion().equals(tipCamionFiliala) && (taxaCamion.getTaxeLivrare().isMacara() || taxaCamion.getTaxeLivrare().isLift())) {


                    ArticolDescarcare articolDescarcare = new ArticolDescarcare();
                    articolDescarcare.setCod(taxaCamion.getTaxeLivrare().getCodTaxaMacara());
                    articolDescarcare.setFiliala(taxaTransport.getFiliala());
                    articolDescarcare.setDepart(taxaCamion.getTaxeLivrare().getDepart());

                    double taxaDescarcare = 0, nrPaleti = 0;
                    for (TaxaMasina taxaMasina : taxaCamion.getTaxeLivrare().getTaxeDivizii()) {

                        if (taxaMasina.getTaxaMacara() > 0)
                            taxaDescarcare = taxaMasina.getTaxaMacara();

                        nrPaleti += taxaMasina.getNrPaleti();
                    }

                    articolDescarcare.setCantitate(nrPaleti);
                    articolDescarcare.setValoare(taxaDescarcare);
                    articolDescarcare.setValoareMin(taxaDescarcare);

                    listArticoleDescarcare.add(articolDescarcare);
                }
            }

        }

        return costDescarcare;
    }


    private static boolean isServiciuGeneral(ArticolComanda articolComanda) {

        if (articolComanda.getSintetic() == null)
            return false;

        String codArticol = articolComanda.getCodArticol().replaceFirst("^0*", "");

        String articoleServicii = "30100021    SERVICII DEBITARE PAL " +
                "30100028    SERVICII APLICARE CANT (19-25)/2MM " +
                "30100029    SERVICII APLICARE CANT (19-23)/0,4MM" +
                "30100060    SERVICII DEBITARE HDF" +
                "30100061    SERVICII DEBITARE BLAT" +
                "30100071    SERVICII DEBITARE PAL - PROMO" +
                "30100072    SERVICII DEBITARE PAL - BUC" +
                "30100073    SERVICII DEBITARE PAL - ML" +
                "30100553    SERVICII DEBITARE OSB 1BUC" +
                "30100840    SERVICII APLICARE CANT (28-45)/2MM" +
                "30101101    SERVICII DUBLARE PAL" +
                "30101102    SERVICII DEBITARE/INDREPTARE PAL DUBLAT" +
                "30101421    SERVICII APLICARE CANT EXTERN(19-25)/2MM" +
                "30101422    SERVICII APLICARE CANT EXTERN 22/0.4MM" +
                "30101423    SERVICII APLICARE CANT EXTERN(28-44)/2MM" +
                "30101463    SERVICII DEBITARE OSB - BUC" +
                "30102430    SERVICII APLICARE CANT 0,4MM - PROMO" +
                "30102431    SERVICII APLICARE CANT (19-25)/2MM-PROMO" +
                "30102432    SERVICII APLICARE CANT (28-45)/2MM-PROMO" +
                "30102433    SERVICII DEBITARE HDF-PROMO" +
                "30102434    SERVICII DEBITARE BLAT-PROMO";

        return articoleServicii.contains(codArticol) || articoleServicii.contains(articolComanda.getNumeArticol().toUpperCase().trim()) ||
                articolComanda.getSintetic().toUpperCase().equals("01_SERVWD");

    }


}
