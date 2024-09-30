package my.logon.screen.beans;

import java.util.List;

public class DateClientSap {

    private String cui;
    private String numeCompanie;
    private String emailCompanie;
    private String strada;
    private String numarStrada;
    private String localitate;
    private String judet;
    private String numePersContact;
    private String prenumePersContact;
    private String telPersContact;
    private String codJ;
    private boolean platitorTVA;
    private String filialaAsociata;
    private List<BeanTipClient> listTipClient;
    private String tipClientSelect;
    private String coordonateAdresa;

    public String getCui() {
        return cui;
    }

    public void setCui(String cui) {
        this.cui = cui;
    }

    public String getNumeCompanie() {
        return numeCompanie;
    }

    public void setNumeCompanie(String numeCompanie) {
        this.numeCompanie = numeCompanie;
    }

    public String getEmailCompanie() {
        return emailCompanie;
    }

    public void setEmailCompanie(String emailCompanie) {
        this.emailCompanie = emailCompanie;
    }

    public String getStrada() {
        return strada;
    }

    public void setStrada(String strada) {
        this.strada = strada;
    }

    public String getNumarStrada() {
        return numarStrada;
    }

    public void setNumarStrada(String numarStrada) {
        this.numarStrada = numarStrada;
    }

    public String getLocalitate() {
        return localitate;
    }

    public void setLocalitate(String localitate) {
        this.localitate = localitate;
    }

    public String getJudet() {
        return judet;
    }

    public void setJudet(String judet) {
        this.judet = judet;
    }

    public String getNumePersContact() {
        return numePersContact;
    }

    public void setNumePersContact(String numePersContact) {
        this.numePersContact = numePersContact;
    }

    public String getPrenumePersContact() {
        return prenumePersContact;
    }

    public void setPrenumePersContact(String prenumePersContact) {
        this.prenumePersContact = prenumePersContact;
    }

    public String getTelPersContact() {
        return telPersContact;
    }

    public void setTelPersContact(String telPersContact) {
        this.telPersContact = telPersContact;
    }

    public String getCodJ() {
        return codJ;
    }

    public void setCodJ(String codJ) {
        this.codJ = codJ;
    }

    public boolean getPlatitorTVA() {
        return platitorTVA;
    }

    public void setPlatitorTVA(boolean platitorTVA) {
        this.platitorTVA = platitorTVA;
    }

    public String getFilialaAsociata() {
        return filialaAsociata;
    }

    public void setFilialaAsociata(String filialaAsociata) {
        this.filialaAsociata = filialaAsociata;
    }

    public List<BeanTipClient> getListTipClient() {
        return listTipClient;
    }

    public void setListTipClient(List<BeanTipClient> listTipClient) {
        this.listTipClient = listTipClient;
    }

    public String getTipClientSelect() {
        return tipClientSelect;
    }

    public void setTipClientSelect(String tipClientSelect) {
        this.tipClientSelect = tipClientSelect;
    }

    public String getCoordonateAdresa() {
        return coordonateAdresa;
    }

    public void setCoordonateAdresa(String coordonateAdresa) {
        this.coordonateAdresa = coordonateAdresa;
    }
}
