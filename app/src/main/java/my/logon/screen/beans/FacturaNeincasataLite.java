package my.logon.screen.beans;

public class FacturaNeincasataLite {

	private String nrFactura;
	private String dataEmitere;
	private String anDocument;
	private double restPlata;
	private boolean selectata;
	private int ordineSelectie = -1;
	private double platit;
	private String nrDocument;

	public String getNrFactura() {
		return nrFactura;
	}

	public void setNrFactura(String nrFactura) {
		this.nrFactura = nrFactura;
	}

	public String getDataEmitere() {
		return dataEmitere;
	}

	public void setDataEmitere(String dataEmitere) {
		this.dataEmitere = dataEmitere;
	}

	public double getRestPlata() {
		return restPlata;
	}

	public void setRestPlata(double restPlata) {
		this.restPlata = restPlata;
	}

	public boolean isSelectata() {
		return selectata;
	}

	public void setSelectata(boolean selectata) {
		this.selectata = selectata;
	}

	public int getOrdineSelectie() {
		return ordineSelectie;
	}

	public void setOrdineSelectie(int ordineSelectie) {
		this.ordineSelectie = ordineSelectie;
	}

	public double getPlatit() {
		return platit;
	}

	public void setPlatit(double platit) {
		this.platit = platit;
	}

	public String getAnDocument() {
		return anDocument;
	}

	public void setAnDocument(String anDocument) {
		this.anDocument = anDocument;
	}

	public String getNrDocument() {
		return nrDocument;
	}

	public void setNrDocument(String nrDocument) {
		this.nrDocument = nrDocument;
	}
	
	

}
