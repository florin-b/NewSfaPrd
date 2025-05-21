package my.logon.screen.beans;

import java.util.List;

public class TaxeLivrare {

    private double taxaMacara;
    private boolean macara;
    private boolean lift;
    private String codTaxaMacara;
    private String numeTaxaMacara;
    private int nrPaleti;
    private String codTaxaZona;
    private String numeTaxaZona;
    private double valoareTaxaZona;
    private String codTaxaAcces;
    private String numeTaxaAcces;
    private double valoareTaxaAcces;
    private String codTaxaTransport;
    private String numeTaxaTransport;
    private double valoareTaxaTransport;
    private String depart;
    private List<TaxaMasina> taxeDivizii;

    public double getTaxaMacara() {
        return taxaMacara;
    }

    public void setTaxaMacara(double taxaMacara) {
        this.taxaMacara = taxaMacara;
    }

    public String getCodTaxaZona() {
        return codTaxaZona;
    }

    public void setCodTaxaZona(String codTaxaZona) {
        this.codTaxaZona = codTaxaZona;
    }

    public String getNumeTaxaZona() {
        return numeTaxaZona;
    }

    public void setNumeTaxaZona(String numeTaxaZona) {
        this.numeTaxaZona = numeTaxaZona;
    }

    public double getValoareTaxaZona() {
        return valoareTaxaZona;
    }

    public void setValoareTaxaZona(double valoareTaxaZona) {
        this.valoareTaxaZona = valoareTaxaZona;
    }

    public double getValoareTaxaAcces() {
        return valoareTaxaAcces;
    }

    public void setValoareTaxaAcces(double valoareTaxaAcces) {
        this.valoareTaxaAcces = valoareTaxaAcces;
    }

    public String getCodTaxaTransport() {
        return codTaxaTransport;
    }

    public void setCodTaxaTransport(String codTaxaTransport) {
        this.codTaxaTransport = codTaxaTransport;
    }

    public String getNumeTaxaTransport() {
        return numeTaxaTransport;
    }

    public void setNumeTaxaTransport(String numeTaxaTransport) {
        this.numeTaxaTransport = numeTaxaTransport;
    }

    public double getValoareTaxaTransport() {
        return valoareTaxaTransport;
    }

    public void setValoareTaxaTransport(double valoareTaxaTransport) {
        this.valoareTaxaTransport = valoareTaxaTransport;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getCodTaxaAcces() {
        return codTaxaAcces;
    }

    public void setCodTaxaAcces(String codTaxaAcces) {
        this.codTaxaAcces = codTaxaAcces;
    }

    public String getNumeTaxaAcces() {
        return numeTaxaAcces;
    }

    public void setNumeTaxaAcces(String numeTaxaAcces) {
        this.numeTaxaAcces = numeTaxaAcces;
    }

    public boolean isMacara() {
        return macara;
    }

    public void setMacara(boolean macara) {
        this.macara = macara;
    }

    public boolean isLift() {
        return lift;
    }

    public void setLift(boolean lift) {
        this.lift = lift;
    }

    public String getCodTaxaMacara() {
        return codTaxaMacara;
    }

    public void setCodTaxaMacara(String codTaxaMacara) {
        this.codTaxaMacara = codTaxaMacara;
    }

    public String getNumeTaxaMacara() {
        return numeTaxaMacara;
    }

    public void setNumeTaxaMacara(String numeTaxaMacara) {
        this.numeTaxaMacara = numeTaxaMacara;
    }

    public List<TaxaMasina> getTaxeDivizii() {
        return taxeDivizii;
    }

    public void setTaxeDivizii(List<TaxaMasina> taxeDivizii) {
        this.taxeDivizii = taxeDivizii;
    }

    public int getNrPaleti() {
        return nrPaleti;
    }

    public void setNrPaleti(int nrPaleti) {
        this.nrPaleti = nrPaleti;
    }
}
