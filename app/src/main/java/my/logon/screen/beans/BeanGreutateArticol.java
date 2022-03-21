package my.logon.screen.beans;


import my.logon.screen.enums.EnumUnitMas;

public class BeanGreutateArticol {

    private String codArticol;
    private double greutate;
    private EnumUnitMas unitMas;
    private String unitMasCantiate;

    public String getCodArticol() {
        return codArticol;
    }

    public void setCodArticol(String codArticol) {
        this.codArticol = codArticol;
    }

    public double getGreutate() {
        return greutate;
    }

    public void setGreutate(double greutate) {
        this.greutate = greutate;
    }

    public EnumUnitMas getUnitMas() {
        return unitMas;
    }

    public void setUnitMas(EnumUnitMas unitMas) {
        this.unitMas = unitMas;
    }

    public String getUnitMasCantiate() {
        return unitMasCantiate;
    }

    public void setUnitMasCantiate(String unitMasCantiate) {
        this.unitMasCantiate = unitMasCantiate;
    }

    @Override
    public String toString() {
        return "BeanGreutateArticol [codArticol=" + codArticol + ", greutate="
                + greutate + ", unitMas=" + unitMas + ", unitMasCantiate="
                + unitMasCantiate + "]";
    }

}
