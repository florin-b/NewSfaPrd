package my.logon.screen.beans;


import my.logon.screen.model.ArticolComanda;

public class ArticolComandaAfis extends ArticolComanda {

	private double cmp;
	private double discountAg;
	private double discountSd;
	private double discountDv;
	private String permitSubCmp;
	

	public ArticolComandaAfis() {

	}

	public double getCmp() {
		return cmp;
	}

	public void setCmp(double cmp) {
		this.cmp = cmp;
	}

	public double getDiscountAg() {
		return discountAg;
	}

	public void setDiscountAg(double discountAg) {
		this.discountAg = discountAg;
	}

	public double getDiscountSd() {
		return discountSd;
	}

	public void setDiscountSd(double discountSd) {
		this.discountSd = discountSd;
	}

	public double getDiscountDv() {
		return discountDv;
	}

	public void setDiscountDv(double discountDv) {
		this.discountDv = discountDv;
	}

	public String getPermitSubCmp() {
		return permitSubCmp;
	}

	public void setPermitSubCmp(String permitSubCmp) {
		this.permitSubCmp = permitSubCmp;
	}

	

}
