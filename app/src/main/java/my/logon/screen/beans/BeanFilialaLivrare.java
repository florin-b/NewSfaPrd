package my.logon.screen.beans;

import java.util.List;

public class BeanFilialaLivrare {

    private String numeFiliala;
    private String unitLog;
    private List<String> depozite;

    public String getNumeFiliala() {
        return numeFiliala;
    }

    public void setNumeFiliala(String numeFiliala) {
        this.numeFiliala = numeFiliala;
    }

    public String getUnitLog() {
        return unitLog;
    }

    public void setUnitLog(String unitLog) {
        this.unitLog = unitLog;
    }

    public List<String> getDepozite() {
        return depozite;
    }

    public void setDepozite(List<String> depozite) {
        this.depozite = depozite;
    }

    @Override
    public String toString() {
        return numeFiliala;
    }
}
