package my.logon.screen.beans;

import my.logon.screen.enums.EnumTipCamion;

public class BeanTaxaCamion {

    private EnumTipCamion tipCamion;
    private TaxeLivrare taxeLivrare;

    public EnumTipCamion getTipCamion() {
        return tipCamion;
    }

    public void setTipCamion(EnumTipCamion tipCamion) {
        this.tipCamion = tipCamion;
    }

    public TaxeLivrare getTaxeLivrare() {
        return taxeLivrare;
    }

    public void setTaxeLivrare(TaxeLivrare taxeLivrare) {
        this.taxeLivrare = taxeLivrare;
    }
}
