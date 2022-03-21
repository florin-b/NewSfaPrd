package my.logon.screen.beans;

public class SalarizareDetaliiMalus {

	private String numeClient;
	private String codClient;
	private double valoareFactura;
	private double penalizare;

	private String nrFactura;
	private String dataFactura;
	private int tpFact;
	private int tpAgreat;
	private int tpIstoric;
	private double valIncasare;
	private String dataIncasare;
	private int zileIntarziere;
	private double coefPenalizare;

	public String getNumeClient() {
		return numeClient;
	}

	public void setNumeClient(String numeClient) {
		this.numeClient = numeClient;
	}

	public double getValoareFactura() {
		return valoareFactura;
	}

	public void setValoareFactura(double valoareFactura) {
		this.valoareFactura = valoareFactura;
	}

	public double getPenalizare() {
		return penalizare;
	}

	public void setPenalizare(double penalizare) {
		this.penalizare = penalizare;
	}

	public String getCodClient() {
		return codClient;
	}

	public void setCodClient(String codClient) {
		this.codClient = codClient;
	}

	public String getNrFactura() {
		return nrFactura;
	}

	public void setNrFactura(String nrFactura) {
		this.nrFactura = nrFactura;
	}

	public String getDataFactura() {
		return dataFactura;
	}

	public void setDataFactura(String dataFactura) {
		this.dataFactura = dataFactura;
	}

	public int getTpFact() {
		return tpFact;
	}

	public void setTpFact(int tpFact) {
		this.tpFact = tpFact;
	}

	public int getTpAgreat() {
		return tpAgreat;
	}

	public void setTpAgreat(int tpAgreat) {
		this.tpAgreat = tpAgreat;
	}

	public int getTpIstoric() {
		return tpIstoric;
	}

	public void setTpIstoric(int tpIstoric) {
		this.tpIstoric = tpIstoric;
	}

	public double getValIncasare() {
		return valIncasare;
	}

	public void setValIncasare(double valIncasare) {
		this.valIncasare = valIncasare;
	}

	public String getDataIncasare() {
		return dataIncasare;
	}

	public void setDataIncasare(String dataIncasare) {
		this.dataIncasare = dataIncasare;
	}

	public int getZileIntarziere() {
		return zileIntarziere;
	}

	public void setZileIntarziere(int zileIntarziere) {
		this.zileIntarziere = zileIntarziere;
	}

	public double getCoefPenalizare() {
		return coefPenalizare;
	}

	public void setCoefPenalizare(double coefPenalizare) {
		this.coefPenalizare = coefPenalizare;
	}

}
