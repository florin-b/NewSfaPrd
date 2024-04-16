package my.logon.screen.beans;

public class DatePoligonLivrare {

    private String filialaPrincipala;
    private String filialaSecundara;
    private String tipZona;
    private String limitareTonaj;
    private String nume;
    private boolean isRestrictionat;

    public String getFilialaPrincipala() {
        return filialaPrincipala;
    }

    public void setFilialaPrincipala(String filialaPrincipala) {
        this.filialaPrincipala = filialaPrincipala;
    }

    public String getFilialaSecundara() {
        return filialaSecundara;
    }

    public void setFilialaSecundara(String filialaSecundara) {
        this.filialaSecundara = filialaSecundara;
    }

    public String getTipZona() {
        return tipZona;
    }

    public void setTipZona(String tipZona) {
        this.tipZona = tipZona;
    }

    public String getLimitareTonaj() {
        return limitareTonaj;
    }

    public void setLimitareTonaj(String limitareTonaj) {
        this.limitareTonaj = limitareTonaj;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public boolean isRestrictionat() {
        return isRestrictionat;
    }

    public void setRestrictionat(boolean restrictionat) {
        isRestrictionat = restrictionat;
    }
}
