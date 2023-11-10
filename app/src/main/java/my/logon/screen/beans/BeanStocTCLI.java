package my.logon.screen.beans;

import java.io.Serializable;

public class BeanStocTCLI implements Serializable {

    private double cantitate;
    private String um;
    private String depozit;

    public double getCantitate() {
        return cantitate;
    }

    public void setCantitate(double cantitate) {
        this.cantitate = cantitate;
    }

    public String getUm() {
        return um;
    }

    public void setUm(String um) {
        this.um = um;
    }

    public String getDepozit() {
        return depozit;
    }

    public void setDepozit(String depozit) {
        this.depozit = depozit;
    }
}
