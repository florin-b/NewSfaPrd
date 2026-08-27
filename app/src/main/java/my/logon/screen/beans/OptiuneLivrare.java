package my.logon.screen.beans;

import my.logon.screen.enums.EnumTipCamion;
import my.logon.screen.enums.EnumTipMacara;

public class OptiuneLivrare {

    private String numeOptiune;
    private String valoareOptiune;
    private TaxaTransport taxaTransport;
    private boolean isMacara;
    private EnumTipCamion tipCamion;
    private EnumTipMacara tipMacara;
    private String defaultTonaj;

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

    public EnumTipMacara getTipMacara() {
        return tipMacara;
    }

    public void setTipMacara(EnumTipMacara tipMacara) {
        this.tipMacara = tipMacara;
    }

    public String getDefaultTonaj() {
        return defaultTonaj;
    }

    public void setDefaultTonaj(String defaultTonaj) {
        this.defaultTonaj = defaultTonaj;
    }
}
