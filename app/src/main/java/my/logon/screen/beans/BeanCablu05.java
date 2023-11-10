package my.logon.screen.beans;

import java.io.Serializable;

public class BeanCablu05 implements Serializable {
	private String numeBoxa;
	private String codBoxa;
	private String stoc;
	private boolean isSelected;
	private String cantitate;

	public String getNumeBoxa() {
		return numeBoxa;
	}

	public void setNumeBoxa(String numeBoxa) {
		this.numeBoxa = numeBoxa;
	}

	public String getCodBoxa() {
		return codBoxa;
	}

	public void setCodBoxa(String codBoxa) {
		this.codBoxa = codBoxa;
	}

	public String getStoc() {
		return stoc;
	}

	public void setStoc(String stoc) {
		this.stoc = stoc;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public String getCantitate() {
		return cantitate;
	}

	public void setCantitate(String cantitate) {
		this.cantitate = cantitate;
	}

}
