package my.logon.screen.beans;

import java.util.List;

import my.logon.screen.enums.EnumTipCamion;

public class TaxaTransport {

    private String filiala;
    private List<BeanTaxaCamion> listTaxe;
    private EnumTipCamion selectedCamion;
    private double taxaMacaraAgent;
    private boolean acceptaMacara;

    public String getFiliala() {
        return filiala;
    }

    public void setFiliala(String filiala) {
        this.filiala = filiala;
    }

    public List<BeanTaxaCamion> getListTaxe() {
        return listTaxe;
    }

    public void setListTaxe(List<BeanTaxaCamion> listTaxe) {
        this.listTaxe = listTaxe;
    }

    public EnumTipCamion getSelectedCamion() {
        return selectedCamion;
    }

    public void setSelectedCamion(EnumTipCamion selectedCamion) {
        this.selectedCamion = selectedCamion;
    }

    public double getTaxaMacaraAgent() {
        return taxaMacaraAgent;
    }

    public void setTaxaMacaraAgent(double taxaMacaraAgent) {
        this.taxaMacaraAgent = taxaMacaraAgent;
    }

    public boolean isAcceptaMacara() {
        return acceptaMacara;
    }

    public void setAcceptaMacara(boolean acceptaMacara) {
        this.acceptaMacara = acceptaMacara;
    }
}
