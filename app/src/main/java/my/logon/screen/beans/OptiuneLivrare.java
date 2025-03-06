package my.logon.screen.beans;

import my.logon.screen.enums.EnumTipCamion;

public class OptiuneLivrare {

    private String numeOptiune;
    private String valoareOptiune;
    private TaxaTransport taxaTransport;
    private boolean isMacara;
    private EnumTipCamion tipCamion;

    public String getNumeOptiune() {
        return numeOptiune;
    }

    public void setNumeOptiune(String numeOptiune) {
        this.numeOptiune = numeOptiune;
    }

    public String getValoareOptiune() {
        return valoareOptiune;
    }

    public void setValoareOptiune(String valoareOptiune) {
        this.valoareOptiune = valoareOptiune;
    }

    public TaxaTransport getTaxaTransport() {
        return taxaTransport;
    }

    public void setTaxaTransport(TaxaTransport taxaTransport) {
        this.taxaTransport = taxaTransport;
    }

    public boolean isMacara() {
        return isMacara;
    }

    public void setMacara(boolean macara) {
        isMacara = macara;
    }

    public EnumTipCamion getTipCamion() {
        return tipCamion;
    }

    public void setTipCamion(EnumTipCamion tipCamion) {
        this.tipCamion = tipCamion;
    }
}
