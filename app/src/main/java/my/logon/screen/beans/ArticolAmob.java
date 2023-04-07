package my.logon.screen.beans;

public class ArticolAmob {

	private String codArticol;
	private String depozit;
	private double cantitate;
	private String um;
	private double pretUnitar;
	private double procentReducere;
	private String numeArticol;
	private String umVanz;
	private String depart;
	private String tipAB;
	private boolean procesat;

	public String getCodArticol() {
		return codArticol;
	}

	public void setCodArticol(String codArticol) {
		this.codArticol = codArticol;
	}

	public String getDepozit() {
		return depozit;
	}

	public void setDepozit(String depozit) {
		this.depozit = depozit;
	}

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

	public double getPretUnitar() {
		return pretUnitar;
	}

	public void setPretUnitar(double pretUnitar) {
		this.pretUnitar = pretUnitar;
	}

	public double getProcentReducere() {
		return procentReducere;
	}

	public void setProcentReducere(double procentReducere) {
		this.procentReducere = procentReducere;
	}

	public String getNumeArticol() {
		return numeArticol;
	}

	public void setNumeArticol(String numeArticol) {
		this.numeArticol = numeArticol;
	}

	public String getUmVanz() {
		return umVanz;
	}

	public void setUmVanz(String umVanz) {
		this.umVanz = umVanz;
	}

	public String getDepart() {
		return depart;
	}

	public void setDepart(String depart) {
		this.depart = depart;
	}

	public String getTipAB() {
		return tipAB;
	}

	public void setTipAB(String tipAB) {
		this.tipAB = tipAB;
	}

	public boolean isProcesat() {
		return procesat;
	}

	public void setProcesat(boolean procesat) {
		this.procesat = procesat;
	}

	@Override
	public String toString() {
		return "ArticolAmob [codArticol=" + codArticol + ", depozit=" + depozit + ", cantitate=" + cantitate + ", um=" + um + ", pretUnitar=" + pretUnitar
				+ ", procentReducere=" + procentReducere + ", numeArticol=" + numeArticol + ", umVanz=" + umVanz + ", depart=" + depart + ", tipAB=" + tipAB
				+ "]";
	}

}
