package my.logon.screen.beans;

import java.util.List;

public class AntetCmdMathaus {

    private String localitate;
    private String codJudet;
    private String codClient;
    private String tipPers;
    private String depart;
    private String codPers;
    private String tipTransp;
    private boolean isCamionDescoperit;
    private boolean isMacara;
    private List<OptiuneCamion> optiuniCamion;
    private double greutateComanda;
    private String tipComandaCamion;
    private boolean isComandaDL;
    private String nrCmdSap;
    private String strada;
    private String codFurnizor;


    public String getLocalitate() {
        return localitate;
    }

    public void setLocalitate(String localitate) {
        this.localitate = localitate;
    }

    public String getCodJudet() {
        return codJudet;
    }

    public void setCodJudet(String codJudet) {
        this.codJudet = codJudet;
    }

    public String getCodClient() {
        return codClient;
    }

    public void setCodClient(String codClient) {
        this.codClient = codClient;
    }

    public String getTipPers() {
        return tipPers;
    }

    public void setTipPers(String tipPers) {
        this.tipPers = tipPers;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getCodPers() {
        return codPers;
    }

    public void setCodPers(String codPers) {
        this.codPers = codPers;
    }

    public String getTipTransp() {
        return tipTransp;
    }

    public void setTipTransp(String tipTransp) {
        this.tipTransp = tipTransp;
    }

    public boolean isCamionDescoperit() {
        return isCamionDescoperit;
    }

    public void setCamionDescoperit(boolean camionDescoperit) {
        isCamionDescoperit = camionDescoperit;
    }

    public boolean isMacara() {
        return isMacara;
    }

    public void setMacara(boolean macara) {
        isMacara = macara;
    }

    public List<OptiuneCamion> getOptiuniCamion() {
        return optiuniCamion;
    }

    public void setOptiuniCamion(List<OptiuneCamion> optiuniCamion) {
        this.optiuniCamion = optiuniCamion;
    }

    public double getGreutateComanda() {
        return greutateComanda;
    }

    public void setGreutateComanda(double greutateComanda) {
        this.greutateComanda = greutateComanda;
    }

    public String getTipComandaCamion() {
        return tipComandaCamion;
    }

    public void setTipComandaCamion(String tipComandaCamion) {
        this.tipComandaCamion = tipComandaCamion;
    }

    public boolean isComandaDL() {
        return isComandaDL;
    }

    public void setComandaDL(boolean comandaDL) {
        isComandaDL = comandaDL;
    }

    public String getNrCmdSap() {
        return nrCmdSap;
    }

    public void setNrCmdSap(String nrCmdSap) {
        this.nrCmdSap = nrCmdSap;
    }

    public String getStrada() {
        return strada;
    }

    public void setStrada(String strada) {
        this.strada = strada;
    }

    public String getCodFurnizor() {
        return codFurnizor;
    }

    public void setCodFurnizor(String codFurnizor) {
        this.codFurnizor = codFurnizor;
    }
}
